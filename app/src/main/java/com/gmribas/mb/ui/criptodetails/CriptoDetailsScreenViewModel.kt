package com.gmribas.mb.ui.criptodetails

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.gmribas.mb.domain.GetExchangeAssetsUseCase
import com.gmribas.mb.domain.GetCriptoDetailsUseCase
import com.gmribas.mb.domain.UseCaseResult
import com.gmribas.mb.ui.criptodetails.model.CriptoDetailsScreenState
import com.gmribas.mb.ui.exchangedetails.model.ExchangeDetailsScreenEvent
import com.gmribas.mb.ui.exchangedetails.model.ExchangeDetailsScreenState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CriptoDetailsScreenViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val getCriptoDetailsUseCase: GetCriptoDetailsUseCase,
    private val getExchangeAssetsUseCase: GetExchangeAssetsUseCase
) : ViewModel() {

    private val _state = MutableStateFlow<CriptoDetailsScreenState>(CriptoDetailsScreenState.Loading)
    val state: StateFlow<CriptoDetailsScreenState> = _state.asStateFlow()

    private val exchangeId: Int = savedStateHandle.get<Int>("id") ?: 0

    init {
        if (exchangeId > 0) {
            loadExchangeDetails()
        } else {
            _state.value = CriptoDetailsScreenState.Error("Invalid exchange ID")
        }
    }

    fun onEvent(event: ExchangeDetailsScreenEvent) {
        when (event) {
            is ExchangeDetailsScreenEvent.RetryLoad -> {
                if (exchangeId > 0) {
                    loadExchangeDetails()
                }
            }
            is ExchangeDetailsScreenEvent.OpenWebsite -> {
                // Handle in UI layer with Intent
            }
        }
    }

    private fun loadExchangeDetails() {
        viewModelScope.launch {
            _state.value = CriptoDetailsScreenState.Loading
            
            when (val result = getCriptoDetailsUseCase(exchangeId)) {
                is UseCaseResult.Success -> {
                    _state.value = CriptoDetailsScreenState.Success(result.data)
                    // Load assets after exchange details
                    loadExchangeAssets()
                }
                is UseCaseResult.Error -> {
                    _state.value = CriptoDetailsScreenState.Error(
                        result.error.message ?: "Unknown error occurred"
                    )
                }
            }
        }
    }
    
    private fun loadExchangeAssets() {
        viewModelScope.launch {
            // Update state to show assets loading
            val currentState = _state.value
            if (currentState is CriptoDetailsScreenState.Success) {
                _state.value = currentState.copy(assetsLoading = true)
                
                when (val result = getExchangeAssetsUseCase(exchangeId)) {
                    is UseCaseResult.Success -> {
                        _state.value = currentState.copy(
                            assets = result.data,
                            assetsLoading = false
                        )
                    }
                    is UseCaseResult.Error -> {
                        // Keep the exchange details but show empty assets on error
                        _state.value = currentState.copy(
                            assets = emptyList(),
                            assetsLoading = false
                        )
                    }
                }
            }
        }
    }
}