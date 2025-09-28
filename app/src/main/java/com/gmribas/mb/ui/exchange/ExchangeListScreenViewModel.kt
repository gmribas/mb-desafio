package com.gmribas.mb.ui.exchange

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import com.gmribas.mb.domain.GetExchangesUseCase
import com.gmribas.mb.repository.dto.ExchangeDTO

@HiltViewModel
class ExchangeListScreenViewModel @Inject constructor(
    private val getExchangesUseCase: GetExchangesUseCase,
) : ViewModel() {

    internal val exchangesPagingFlow: Flow<PagingData<ExchangeDTO>> = getExchangesUseCase()
        .cachedIn(viewModelScope)
}
