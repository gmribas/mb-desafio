package com.gmribas.mb.data.model

import com.google.gson.annotations.SerializedName

data class ExchangeListingResponse(
    val data: List<ExchangeResponse>,
)

data class ExchangeResponse(
    val id: Long,
    val name: String,
    val slug: String,
    @SerializedName("num_market_pairs")
    val numMarketPairs: Long,
    val fiats: List<String>,
    @SerializedName("traffic_score")
    val trafficScore: Long,
    val rank: Long,
    @SerializedName("exchange_score")
    val exchangeScore: Any?,
    @SerializedName("liquidity_score")
    val liquidityScore: Double,
    @SerializedName("last_updated")
    val lastUpdated: String,
    val quote: ExchangeQuote,
)

data class ExchangeQuote(
    @SerializedName("USD")
    val usd: QuoteUSD,
)

data class QuoteUSD(
    @SerializedName("volume_24h")
    val volume24h: Double,
    @SerializedName("volume_24h_adjusted")
    val volume24hAdjusted: Double,
    @SerializedName("volume_7d")
    val volume7d: Long,
    @SerializedName("volume_30d")
    val volume30d: Long,
    @SerializedName("percent_change_volume_24h")
    val percentChangeVolume24h: Double,
    @SerializedName("percent_change_volume_7d")
    val percentChangeVolume7d: Double,
    @SerializedName("percent_change_volume_30d")
    val percentChangeVolume30d: Double,
    @SerializedName("effective_liquidity_24h")
    val effectiveLiquidity24h: Double,
    @SerializedName("derivative_volume_usd")
    val derivativeVolumeUsd: Double,
    @SerializedName("spot_volume_usd")
    val spotVolumeUsd: Double,
)