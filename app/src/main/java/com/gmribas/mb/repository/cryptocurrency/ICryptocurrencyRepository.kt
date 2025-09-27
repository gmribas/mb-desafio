package com.gmribas.mb.repository.cryptocurrency

import androidx.paging.PagingData
import com.gmribas.mb.repository.dto.CryptocurrencyDTO
import com.gmribas.mb.repository.dto.CryptocurrencyListingDTO
import kotlinx.coroutines.flow.Flow

interface ICryptocurrencyRepository {
    suspend fun getLatestListings(
        start: Int,
        limit: Int,
        convert: String = "USD",
        sort: String = "market_cap",
        sortDir: String = "desc"
    ): CryptocurrencyListingDTO
    
    fun getCryptocurrenciesPagingData(): Flow<PagingData<CryptocurrencyDTO>>
}