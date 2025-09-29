package com.gmribas.mb.data.model

import com.google.gson.annotations.SerializedName

data class ExchangeMapResponse(
    val id: Long?,
    val name: String?,
    val slug: String?,
    @SerializedName("status")
    val status: String?,
    @SerializedName("first_historical_data")
    val firstHistoricalData: String?,
    @SerializedName("last_historical_data")
    val lastHistoricalData: String?,
    @SerializedName("taker_fee")
    val takerFee: Double?,
    @SerializedName("spot_volume_usd")
    val spotVolumeUsd: Double?
)

data class ExchangeMapListingResponse(
    val data: List<ExchangeMapResponse>,
)