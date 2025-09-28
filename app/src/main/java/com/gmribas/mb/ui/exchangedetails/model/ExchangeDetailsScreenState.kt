package com.gmribas.mb.ui.exchangedetails.model

import com.gmribas.mb.repository.dto.ExchangeAssetDTO
import com.gmribas.mb.repository.dto.ExchangeDetailDTO

sealed interface ExchangeDetailsScreenState {
    data object Loading : ExchangeDetailsScreenState
    data class Success(
        val exchange: ExchangeDetailDTO,
        val assets: List<ExchangeAssetDTO> = emptyList(),
        val assetsLoading: Boolean = false
    ) : ExchangeDetailsScreenState
    data class Error(val message: String) : ExchangeDetailsScreenState
}
