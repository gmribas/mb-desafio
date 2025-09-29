package com.gmribas.mb.repository.dto

data class ExchangeAssetDTO(
    val id: Int?,
    val name: String?,
    val symbol: String?,
    val slug: String?,
    val logo: String?,
    val currency: CurrencyDTO?
)

data class CurrencyDTO(
    val name: String?,
    val priceUsd: Double?
)