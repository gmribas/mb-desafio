package com.gmribas.mb.ui.exchange.model

import androidx.paging.PagingData
import kotlinx.coroutines.flow.Flow
import com.gmribas.mb.repository.dto.CryptocurrencyDTO

sealed interface ExchangeListScreenState {
    data class ExchangeScreenSuccessState(
        val cryptocurrenciesPagingFlow: Flow<PagingData<CryptocurrencyDTO>>
    ) : ExchangeListScreenState

    data class ExchangeScreenErrorState(val error: String? = null) : ExchangeListScreenState
    data object ExchangeScreenLoadingState : ExchangeListScreenState
    data object ExchangeScreenIdleState : ExchangeListScreenState
}