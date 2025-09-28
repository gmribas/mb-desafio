package com.gmribas.mb.navigation

sealed class Screen(val route: String) {
    object Splash : Screen("splash")
    object Exchange : Screen("exchange")
    object ExchangeDetails : Screen("exchange_details/{id}") {
        fun createRoute(id: Long) = "exchange_details/$id"
    }
    object CriptoList : Screen("cripto_list")
    object CriptoDetails : Screen("cripto_details/{id}") {
        fun createRoute(id: Int) = "cripto_details/$id"
    }
}
