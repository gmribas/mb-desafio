package com.gmribas.mb.repository.mapper

import com.gmribas.mb.data.model.AssetData
import com.gmribas.mb.repository.dto.CurrencyDTO
import com.gmribas.mb.repository.dto.ExchangeAssetDTO
import javax.inject.Inject

class ExchangeAssetMapper @Inject constructor() {
    
    fun toDTO(data: AssetData): ExchangeAssetDTO {
        return ExchangeAssetDTO(
            id = data.id,
            name = data.name,
            symbol = data.symbol,
            slug = data.slug,
            logo = "https://s2.coinmarketcap.com/static/img/coins/64x64/${data.currency?.cryptoId}.png",
            currency = CurrencyDTO(
                name = data.currency?.name,
                priceUsd = data.currency?.priceUsd
            ),
        )
    }
    
    fun toDTOList(dataList: List<AssetData>): List<ExchangeAssetDTO> {
        return dataList.map { toDTO(it) }
    }
}