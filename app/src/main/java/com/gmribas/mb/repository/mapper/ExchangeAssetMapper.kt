package com.gmribas.mb.repository.mapper

import com.gmribas.mb.data.model.ExchangeAssetData
import com.gmribas.mb.repository.dto.ExchangeAssetDTO
import javax.inject.Inject

class ExchangeAssetMapper @Inject constructor() {
    
    fun toDTO(data: ExchangeAssetData): ExchangeAssetDTO {
        return ExchangeAssetDTO(
            id = data.id,
            name = data.name,
            symbol = data.symbol,
            slug = data.slug,
            logo = data.logo,
            currencyName = data.currency.name,
            priceUsd = data.currency.priceUsd
        )
    }
    
    fun toDTOList(dataList: List<ExchangeAssetData>): List<ExchangeAssetDTO> {
        return dataList.map { toDTO(it) }
    }
}