package com.gmribas.mb.ui.exchangedetails.model

sealed interface ExchangeDetailsScreenEvent {
    data object RetryLoad : ExchangeDetailsScreenEvent
    data class OpenWebsite(val url: String) : ExchangeDetailsScreenEvent
}
