package com.gmribas.mb.data.model

import com.google.gson.annotations.SerializedName

data class CriptoDetailResponse(
    @SerializedName("status")
    val status: StatusResponse?,
    @SerializedName("data")
    val data: Map<String, CriptoInfoData>?
)

data class CriptoInfoData(
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
    val urls: CriptoUrls?,
    @SerializedName("category")
    val category: String?,
    @SerializedName("platform")
    val platform: PlatformInfo?
)

data class CriptoUrls(
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