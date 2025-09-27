package com.gmribas.mb.repository.dto

data class ExchangeDetailDTO(
    val id: Int,
    val name: String,
    val symbol: String,
    val slug: String,
    val logo: String?,
    val description: String?,
    val dateAdded: String?,
    val dateLaunched: String?,
    val websiteUrl: String?,
    val category: String?,
    val platform: String?
)
