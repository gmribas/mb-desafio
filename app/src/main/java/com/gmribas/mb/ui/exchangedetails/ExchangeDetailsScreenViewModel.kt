package com.gmribas.mb.ui.exchangedetails

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
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
    private val getExchangeDetailsUseCase: GetExchangeDetailsUseCase
) : ViewModel() {

    private val _state = MutableStateFlow<ExchangeDetailsScreenState>(ExchangeDetailsScreenState.Loading)
    val state: StateFlow<ExchangeDetailsScreenState> = _state.asStateFlow()

    private var currentExchangeId: Int? = null

    fun onEvent(event: ExchangeDetailsScreenEvent) {
        when (event) {
            is ExchangeDetailsScreenEvent.LoadExchangeDetails -> {
                currentExchangeId = event.exchangeId
                loadExchangeDetails(event.exchangeId)
            }
            is ExchangeDetailsScreenEvent.RetryLoad -> {
                currentExchangeId?.let { loadExchangeDetails(it) }
            }
            is ExchangeDetailsScreenEvent.OpenWebsite -> {
                // Handle in UI layer with Intent
            }
        }
    }

    private fun loadExchangeDetails(exchangeId: Int) {
        viewModelScope.launch {
            _state.value = ExchangeDetailsScreenState.Loading
            
            when (val result = getExchangeDetailsUseCase(exchangeId)) {
                is UseCaseResult.Success -> {
                    _state.value = ExchangeDetailsScreenState.Success(result.data)
                }
                is UseCaseResult.Error -> {
                    _state.value = ExchangeDetailsScreenState.Error(
                        result.error.message ?: "Unknown error occurred"
                    )
                }
            }
        }
    }
}