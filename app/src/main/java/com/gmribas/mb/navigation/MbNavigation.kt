package com.gmribas.mb.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.gmribas.mb.ui.assets.AssetsListScreen
import com.gmribas.mb.ui.assets.AssetsListScreenViewModel
import com.gmribas.mb.ui.exchange.ExchangeListScreen
import com.gmribas.mb.ui.exchange.ExchangeListScreenViewModel
import com.gmribas.mb.ui.exchangedetails.ExchangeDetailsScreen
import com.gmribas.mb.ui.exchangedetails.ExchangeDetailsScreenViewModel
import com.gmribas.mb.ui.splash.SplashScreen
import com.google.gson.Gson
import java.net.URLEncoder
import java.nio.charset.StandardCharsets

@Composable
fun MbNavigation(
    navController: NavHostController = rememberNavController(),
    onFinish: () -> Unit = {},
    modifier: Modifier = Modifier
) {
    NavHost(
        navController = navController,
        startDestination = Screen.Splash.route,
        modifier = modifier
    ) {
        composable(route = Screen.Splash.route) {
            SplashScreen(
                onSplashFinished = {
                    navController.navigate(Screen.Exchange.route) {
                        popUpTo(Screen.Splash.route) { inclusive = true }
                    }
                }
            )
        }
        
        composable(route = Screen.Exchange.route) {
            val viewModel: ExchangeListScreenViewModel = hiltViewModel()

            ExchangeListScreen(
                exchangesPagingFlow = viewModel.exchangesPagingFlow,
                onItemClick = { exchange ->
                    exchange.id?.let {
                        navController.navigate(Screen.ExchangeDetails.createRoute(exchange.id))
                    }
                },
                onFinish = onFinish
            )
        }
        
        composable(
            route = Screen.ExchangeDetails.route,
            arguments = listOf(navArgument("id") { type = NavType.IntType })
        ) {
            val viewModel: ExchangeDetailsScreenViewModel = hiltViewModel()
            val state by viewModel.state.collectAsState()
            
            ExchangeDetailsScreen(
                state = state,
                onEvent = viewModel::onEvent,
                viewAllAssetsClick = {
                    val assetsJson = Gson().toJson(it)
                    val encoded = URLEncoder.encode(assetsJson, StandardCharsets.UTF_8.toString())
                    navController.navigate(Screen.Assets.createRoute(encoded))
                },
                onBackClick = {
                    navController.popBackStack()
                }
            )
        }

        composable(
            route = Screen.Assets.route,
            arguments = listOf(
                navArgument("assetsJson") { type = NavType.StringType },
            )
        ) {
            val viewModel: AssetsListScreenViewModel = hiltViewModel()
            val state by viewModel.state.collectAsState()

            AssetsListScreen(
                state = state,
                onEvent = viewModel::onEvent,

            ) {
                navController.popBackStack()
            }
        }
    }
}
