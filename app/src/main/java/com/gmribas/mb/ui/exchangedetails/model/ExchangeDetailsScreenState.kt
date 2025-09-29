package com.gmribas.mb.ui.exchangedetails.model

import com.gmribas.mb.repository.dto.ExchangeAssetDTO
import com.gmribas.mb.repository.dto.ExchangeDTO

sealed interface ExchangeDetailsScreenState {
    data object Loading : ExchangeDetailsScreenState
    data class Success(
        val exchange: ExchangeDTO,
        val assets: List<ExchangeAssetDTO> = emptyList(),
        val assetsLoading: Boolean = false
    ) : ExchangeDetailsScreenState
    data class Error(val message: String) : ExchangeDetailsScreenState
}
