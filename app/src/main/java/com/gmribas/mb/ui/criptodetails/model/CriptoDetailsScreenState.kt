package com.gmribas.mb.ui.criptodetails.model

import com.gmribas.mb.repository.dto.ExchangeAssetDTO
import com.gmribas.mb.repository.dto.CriptoDetailDTO

sealed interface CriptoDetailsScreenState {
    data object Loading : CriptoDetailsScreenState
    data class Success(
        val cripto: CriptoDetailDTO,
        val assets: List<ExchangeAssetDTO> = emptyList(),
        val assetsLoading: Boolean = false
    ) : CriptoDetailsScreenState
    data class Error(val message: String) : CriptoDetailsScreenState
}
