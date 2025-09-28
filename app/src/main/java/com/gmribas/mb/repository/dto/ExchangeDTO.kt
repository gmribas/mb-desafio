package com.gmribas.mb.repository.dto


data class ExchangeDTO(
    val id: Long,
    val name: String,
    val slug: String,
    val spotVolumeUsd: Double,
    val logo: String,
    val dateLaunched: String
)

data class ExchangeListingDTO(
    val exchanges: List<ExchangeDTO>,
)