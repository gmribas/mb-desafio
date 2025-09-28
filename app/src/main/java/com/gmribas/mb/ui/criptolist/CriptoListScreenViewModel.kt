package com.gmribas.mb.ui.criptolist

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.gmribas.mb.domain.GetCryptocurrenciesUseCase
import com.gmribas.mb.repository.dto.CryptocurrencyDTO
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

@HiltViewModel
class CriptoListScreenViewModel @Inject constructor(
    private val getCryptocurrenciesUseCase: GetCryptocurrenciesUseCase
) : ViewModel() {

    internal val cryptocurrenciesPagingFlow: Flow<PagingData<CryptocurrencyDTO>> = getCryptocurrenciesUseCase()
        .cachedIn(viewModelScope)
}
