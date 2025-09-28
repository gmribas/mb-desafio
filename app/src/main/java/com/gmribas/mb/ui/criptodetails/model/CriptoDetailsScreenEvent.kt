package com.gmribas.mb.ui.criptodetails.model

sealed interface CriptoDetailsScreenEvent {
    data object RetryLoad : CriptoDetailsScreenEvent
    data class OpenWebsite(val url: String) : CriptoDetailsScreenEvent
}
