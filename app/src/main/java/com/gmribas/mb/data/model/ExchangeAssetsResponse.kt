package com.gmribas.mb.data.model

import com.google.gson.annotations.SerializedName

data class ExchangeAssetsResponse(
    @SerializedName("status")
    val status: AssetStatusResponse?,
    @SerializedName("data")
    val data: List<ExchangeAssetData>?
)

data class ExchangeAssetData(
    @SerializedName("id")
    val id: Int,
    @SerializedName("name")
    val name: String,
    @SerializedName("symbol")
    val symbol: String,
    @SerializedName("slug")
    val slug: String,
    @SerializedName("logo")
    val logo: String?,
    @SerializedName("currency")
    val currency: AssetCurrencyInfo
)

data class AssetCurrencyInfo(
    @SerializedName("name")
    val name: String,
    @SerializedName("price_usd")
    val priceUsd: Double
)

data class AssetStatusResponse(
    @SerializedName("timestamp")
    val timestamp: String?,
    @SerializedName("error_code")
    val errorCode: Int?,
    @SerializedName("error_message")
    val errorMessage: String?,
    @SerializedName("elapsed")
    val elapsed: Int?,
    @SerializedName("credit_count")
    val creditCount: Int?
)