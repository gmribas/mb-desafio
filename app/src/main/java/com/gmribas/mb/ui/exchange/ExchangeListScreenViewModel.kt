package com.gmribas.mb.ui.exchange

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.gmribas.mb.domain.GetCryptocurrenciesUseCase
import com.gmribas.mb.repository.dto.CryptocurrencyDTO
import com.gmribas.mb.ui.exchange.model.ExchangeListScreenState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

@HiltViewModel
class ExchangeListScreenViewModel @Inject constructor(
    private val getCryptocurrenciesUseCase: GetCryptocurrenciesUseCase
) : ViewModel() {

    private val cryptocurrenciesPagingFlow: Flow<PagingData<CryptocurrencyDTO>> = getCryptocurrenciesUseCase()
        .cachedIn(viewModelScope)

    private var _state = MutableStateFlow<ExchangeListScreenState>(
        ExchangeListScreenState.ExchangeScreenSuccessState(cryptocurrenciesPagingFlow)
    )
    internal val state: StateFlow<ExchangeListScreenState> = _state.asStateFlow()
}
