package com.gmribas.mb.data.datasource.cryptocurrency

import com.gmribas.mb.data.model.CryptocurrencyListingResponse

interface ICryptocurrencyDataSource {
    suspend fun getLatestListings(
        start: Int,
        limit: Int,
        convert: String = "USD",
        sort: String = "market_cap",
        sortDir: String = "desc"
    ): CryptocurrencyListingResponse
}