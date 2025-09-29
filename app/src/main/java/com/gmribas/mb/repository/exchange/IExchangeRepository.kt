package com.gmribas.mb.repository.exchange

import androidx.paging.PagingData
import com.gmribas.mb.repository.dto.CriptoDetailDTO
import com.gmribas.mb.repository.dto.ExchangeAssetDTO
import com.gmribas.mb.repository.dto.ExchangeDTO
import com.gmribas.mb.repository.dto.ExchangeListingDTO
import kotlinx.coroutines.flow.Flow

interface IExchangeRepository {

    suspend fun getInfo(
        id: Int
    ): ExchangeDTO

    suspend fun getLatestListings(
        start: Int,
        limit: Int,
        sort: String = "volume_24h",
    ): ExchangeListingDTO

    fun getExchangePagingData(): Flow<PagingData<ExchangeDTO>>
}
