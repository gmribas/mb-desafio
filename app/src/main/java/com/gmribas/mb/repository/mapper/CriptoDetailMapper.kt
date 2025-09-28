package com.gmribas.mb.repository.mapper

import com.gmribas.mb.data.model.CriptoInfoData
import com.gmribas.mb.repository.dto.CriptoDetailDTO
import javax.inject.Inject

class CriptoDetailMapper @Inject constructor() {
    
    fun toDTO(exchangeInfo: CriptoInfoData): CriptoDetailDTO {
        return CriptoDetailDTO(
            id = exchangeInfo.id,
            name = exchangeInfo.name,
            symbol = exchangeInfo.symbol,
            slug = exchangeInfo.slug,
            logo = "https://s2.coinmarketcap.com/static/img/coins/64x64/${exchangeInfo.id}.png",
            description = exchangeInfo.description,
            dateAdded = exchangeInfo.dateAdded,
            dateLaunched = exchangeInfo.dateLaunched,
            websiteUrl = exchangeInfo.urls?.website?.firstOrNull(),
            category = exchangeInfo.category,
            platform = exchangeInfo.platform?.name
        )
    }
}