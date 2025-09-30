package com.gmribas.mb.navigation

sealed class Screen(val route: String) {
    object Splash : Screen("splash")
    object Exchange : Screen("exchange")
    object ExchangeDetails : Screen("exchange_details/{id}") {
        fun createRoute(id: Long) = "exchange_details/$id"
    }
    object Assets : Screen("exchange_details/{assetsJson}") {
        fun createRoute(assetsJson: String) = "exchange_details/$assetsJson"
    }
}
