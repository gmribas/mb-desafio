package com.gmribas.mb.data.model

import com.google.gson.annotations.SerializedName

data class AssetsResponse(
    @SerializedName("status")
    val status: StatusResponse?,
    @SerializedName("data")
    val data: List<AssetData>?
)

data class AssetData(
    @SerializedName("id")
    val id: Int?,
    @SerializedName("name")
    val name: String?,
    @SerializedName("symbol")
    val symbol: String?,
    @SerializedName("slug")
    val slug: String?,
    @SerializedName("logo")
    val logo: String?,
    @SerializedName("currency")
    val currency: Currency?
)

data class Currency(
    @SerializedName("crypto_id")
    val cryptoId: Int?,
    @SerializedName("name")
    val name: String?,
    @SerializedName("price_usd")
    val priceUsd: Double?
)