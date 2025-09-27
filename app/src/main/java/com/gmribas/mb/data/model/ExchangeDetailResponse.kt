package com.gmribas.mb.data.model

import com.google.gson.annotations.SerializedName

data class ExchangeDetailResponse(
    @SerializedName("status")
    val status: ExchangeStatusResponse?,
    @SerializedName("data")
    val data: Map<String, ExchangeInfoData>?
)

data class ExchangeInfoData(
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
    @SerializedName("description")
    val description: String?,
    @SerializedName("date_added")
    val dateAdded: String?,
    @SerializedName("date_launched")
    val dateLaunched: String?,
    @SerializedName("urls")
    val urls: ExchangeUrls?,
    @SerializedName("category")
    val category: String?,
    @SerializedName("platform")
    val platform: PlatformInfo?
)

data class ExchangeUrls(
    @SerializedName("website")
    val website: List<String>?,
    @SerializedName("technical_doc")
    val technicalDoc: List<String>?,
    @SerializedName("twitter")
    val twitter: List<String>?,
    @SerializedName("reddit")
    val reddit: List<String>?,
    @SerializedName("message_board")
    val messageBoard: List<String>?,
    @SerializedName("announcement")
    val announcement: List<String>?,
    @SerializedName("chat")
    val chat: List<String>?,
    @SerializedName("explorer")
    val explorer: List<String>?,
    @SerializedName("source_code")
    val sourceCode: List<String>?
)

data class PlatformInfo(
    @SerializedName("id")
    val id: Int?,
    @SerializedName("name")
    val name: String?,
    @SerializedName("symbol")
    val symbol: String?,
    @SerializedName("slug")
    val slug: String?,
    @SerializedName("token_address")
    val tokenAddress: String?
)

data class ExchangeStatusResponse(
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