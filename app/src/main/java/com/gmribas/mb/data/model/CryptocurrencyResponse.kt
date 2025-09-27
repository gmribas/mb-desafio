package com.gmribas.mb.data.model

import com.google.gson.annotations.SerializedName

data class CryptocurrencyListingResponse(
    @SerializedName("status")
    val status: Status,
    @SerializedName("data")
    val data: List<Cryptocurrency>
)

data class Status(
    @SerializedName("timestamp")
    val timestamp: String,
    @SerializedName("error_code")
    val errorCode: Int,
    @SerializedName("error_message")
    val errorMessage: String?,
    @SerializedName("elapsed")
    val elapsed: Int,
    @SerializedName("credit_count")
    val creditCount: Int,
    @SerializedName("notice")
    val notice: String?,
    @SerializedName("total_count")
    val totalCount: Int
)

data class Cryptocurrency(
    @SerializedName("id")
    val id: Int,
    @SerializedName("name")
    val name: String,
    @SerializedName("symbol")
    val symbol: String,
    @SerializedName("slug")
    val slug: String,
    @SerializedName("num_market_pairs")
    val numMarketPairs: Int,
    @SerializedName("date_added")
    val dateAdded: String,
    @SerializedName("tags")
    val tags: List<String>,
    @SerializedName("max_supply")
    val maxSupply: Double?,
    @SerializedName("circulating_supply")
    val circulatingSupply: Double?,
    @SerializedName("total_supply")
    val totalSupply: Double?,
    @SerializedName("platform")
    val platform: Platform?,
    @SerializedName("cmc_rank")
    val cmcRank: Int,
    @SerializedName("self_reported_circulating_supply")
    val selfReportedCirculatingSupply: Double?,
    @SerializedName("self_reported_market_cap")
    val selfReportedMarketCap: Double?,
    @SerializedName("tvl_ratio")
    val tvlRatio: Double?,
    @SerializedName("last_updated")
    val lastUpdated: String,
    @SerializedName("quote")
    val quote: Map<String, Quote>
)

data class Platform(
    @SerializedName("id")
    val id: Int,
    @SerializedName("name")
    val name: String,
    @SerializedName("symbol")
    val symbol: String,
    @SerializedName("slug")
    val slug: String,
    @SerializedName("token_address")
    val tokenAddress: String
)

data class Quote(
    @SerializedName("price")
    val price: Double,
    @SerializedName("volume_24h")
    val volume24h: Double,
    @SerializedName("volume_change_24h")
    val volumeChange24h: Double,
    @SerializedName("percent_change_1h")
    val percentChange1h: Double,
    @SerializedName("percent_change_24h")
    val percentChange24h: Double,
    @SerializedName("percent_change_7d")
    val percentChange7d: Double,
    @SerializedName("percent_change_30d")
    val percentChange30d: Double,
    @SerializedName("percent_change_60d")
    val percentChange60d: Double,
    @SerializedName("percent_change_90d")
    val percentChange90d: Double,
    @SerializedName("market_cap")
    val marketCap: Double,
    @SerializedName("market_cap_dominance")
    val marketCapDominance: Double,
    @SerializedName("fully_diluted_market_cap")
    val fullyDilutedMarketCap: Double,
    @SerializedName("tvl")
    val tvl: Double?,
    @SerializedName("last_updated")
    val lastUpdated: String
)