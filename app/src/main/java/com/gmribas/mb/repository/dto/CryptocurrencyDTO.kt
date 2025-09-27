package com.gmribas.mb.repository.dto

data class CryptocurrencyDTO(
    val id: Int,
    val name: String,
    val symbol: String,
    val slug: String,
    val rank: Int,
    val price: Double,
    val volume24h: Double,
    val marketCap: Double,
    val percentChange1h: Double,
    val percentChange24h: Double,
    val percentChange7d: Double,
    val circulatingSupply: Double?,
    val totalSupply: Double?,
    val maxSupply: Double?,
    val lastUpdated: String,
    val dateAdded: String
)

data class CryptocurrencyListingDTO(
    val cryptocurrencies: List<CryptocurrencyDTO>,
    val totalCount: Int
)