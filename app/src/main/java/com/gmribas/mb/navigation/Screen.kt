package com.gmribas.mb.navigation

sealed class Screen(val route: String) {
    object Splash : Screen("splash")
    object Exchange : Screen("exchange")
    object ExchangeDetails : Screen("exchange_details/{id}") {
        fun createRoute(id: Int) = "exchange_details/$id"
    }
}
