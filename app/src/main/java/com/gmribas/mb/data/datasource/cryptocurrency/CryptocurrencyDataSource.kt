package com.gmribas.mb.data.datasource.cryptocurrency

import com.gmribas.mb.data.api.CoinMarketCapApi
import com.gmribas.mb.data.model.CryptocurrencyListingResponse

class CryptocurrencyDataSource(
    private val api: CoinMarketCapApi
) : ICryptocurrencyDataSource {
    
    companion object {
        const val PAGE_SIZE = 20
    }

    override suspend fun getLatestListings(
        start: Int,
        limit: Int,
        convert: String,
        sort: String,
        sortDir: String
    ): CryptocurrencyListingResponse {
        return api.getLatestListings(
            start = start,
            limit = limit,
            convert = convert,
            sort = sort,
            sortDir = sortDir
        )
    }
}