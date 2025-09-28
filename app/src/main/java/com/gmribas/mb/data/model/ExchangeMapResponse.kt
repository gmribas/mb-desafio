package com.gmribas.mb.data.model

import com.google.gson.annotations.SerializedName

data class ExchangeMapResponse(
    val id: Long,
    val name: String,
    val slug: String,
    @SerializedName("status")
    val status: String,
    @SerializedName("first_historical_data")
    val firstHistoricalData: String,
    @SerializedName("last_historical_data")
    val lastHistoricalData: String,
)

data class ExchangeMapListingResponse(
    val data: List<ExchangeMapResponse>,
)