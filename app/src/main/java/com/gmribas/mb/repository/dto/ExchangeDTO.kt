package com.gmribas.mb.repository.dto


data class ExchangeDTO(
    val id: Long?,
    val name: String?,
    val slug: String?,
    val spotVolumeUsd: Double?,
    val logo: String?,
    val dateLaunched: String?,
    val description: String?,
    val makerFee: Double?,
    val takerFee: Double?,
    val urls: UrlsDTO?,
)

data class UrlsDTO(
    val website: String? = null,
    val twitter: String? = null,
    val chat: String? = null,
    val fee: String? = null,
)

data class ExchangeListingDTO(
    val exchanges: List<ExchangeDTO>?,
)