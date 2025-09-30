package com.gmribas.mb.ui.exchangedetails.model

sealed interface ExchangeDetailsScreenEvent {
    data object LoadExchangeDetails : ExchangeDetailsScreenEvent
}
