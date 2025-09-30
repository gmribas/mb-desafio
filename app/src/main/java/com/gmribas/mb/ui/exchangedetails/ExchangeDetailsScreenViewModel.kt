package com.gmribas.mb.ui.exchangedetails

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.gmribas.mb.domain.GetExchangeAssetsUseCase
import com.gmribas.mb.domain.GetExchangeDetailsUseCase
import com.gmribas.mb.domain.UseCaseResult
import com.gmribas.mb.ui.exchangedetails.model.ExchangeDetailsScreenEvent
import com.gmribas.mb.ui.exchangedetails.model.ExchangeDetailsScreenState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ExchangeDetailsScreenViewModel @Inject constructor(
    private val savedStateHandle: SavedStateHandle,
    private val getExchangeDetailsUseCase: GetExchangeDetailsUseCase,
    private val getExchangeAssetsUseCase: GetExchangeAssetsUseCase
) : ViewModel() {

    private val _state = MutableStateFlow<ExchangeDetailsScreenState>(ExchangeDetailsScreenState.Loading)
    val state: StateFlow<ExchangeDetailsScreenState> = _state.asStateFlow()

    fun onEvent(event: ExchangeDetailsScreenEvent) {
        when (event) {
            is ExchangeDetailsScreenEvent.LoadExchangeDetails -> {
                val exchangeId: Int = savedStateHandle.get<Int>("id") ?: 0

                if (exchangeId > 0) {
                    loadExchangeDetails(exchangeId)
                } else {
                    _state.value = ExchangeDetailsScreenState.Error("Invalid exchange ID")
                }
            }
        }
    }

    internal fun loadExchangeDetails(exchangeId: Int) {
        viewModelScope.launch {
            _state.value = ExchangeDetailsScreenState.Loading
            
            when (val result = getExchangeDetailsUseCase(exchangeId)) {
                is UseCaseResult.Success -> {
                    _state.value = ExchangeDetailsScreenState.Success(result.data)
                    // Load assets after exchange details
                    loadExchangeAssets(exchangeId)
                }
                is UseCaseResult.Error -> {
                    _state.value = ExchangeDetailsScreenState.Error(
                        result.error.message ?: "Unknown error occurred"
                    )
                }
            }
        }
    }
    
    internal fun loadExchangeAssets(exchangeId: Int) {
        viewModelScope.launch {
            // Update state to show assets loading
            val currentState = _state.value
            if (currentState is ExchangeDetailsScreenState.Success) {
                _state.value = currentState.copy(assetsLoading = true)
                
                when (val result = getExchangeAssetsUseCase(exchangeId)) {
                    is UseCaseResult.Success -> {
                        _state.value = currentState.copy(
                            assets = result.data,
                            assetsLoading = false
                        )
                    }
                    is UseCaseResult.Error -> {
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