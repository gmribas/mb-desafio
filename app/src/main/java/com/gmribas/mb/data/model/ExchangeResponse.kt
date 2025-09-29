package com.gmribas.mb.data.model

import com.google.gson.annotations.SerializedName

data class ExchangeResponse(
    @SerializedName("data") val data: Map<String, ExchangeResponseData>
)

data class ExchangeResponseData(
    @SerializedName("id")
    val id: Long?,
    @SerializedName("name")
    val name: String?,
    @SerializedName("slug")
    val slug: String?,
    @SerializedName("description")
    val description: String?,
    @SerializedName("date_launched")
    val dateLaunched: String?,
    @SerializedName("maker_fee")
    val makerFee: Double?,
    @SerializedName("taker_fee")
    val takerFee: Double?,
    @SerializedName("spot_volume_usd")
    val spotVolumeUsd: Double?,
    @SerializedName("urls")
    val urls: Urls?

)

data class Urls(
    val website: List<String>?,
    val twitter: List<String>?,
    val chat: List<String>?,
    val fee: List<String>?,
)