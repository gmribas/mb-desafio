package com.gmribas.mb.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import androidx.navigation.NavType
import com.gmribas.mb.ui.exchange.ExchangeListScreen
import com.gmribas.mb.ui.exchange.ExchangeListScreenViewModel
import com.gmribas.mb.ui.exchangedetails.ExchangeDetailsScreen
import com.gmribas.mb.ui.exchangedetails.ExchangeDetailsScreenViewModel
import com.gmribas.mb.ui.splash.SplashScreen

@Composable
fun MbNavigation(
    navController: NavHostController = rememberNavController(),
    onFinish: () -> Unit = {},
    modifier: androidx.compose.ui.Modifier = androidx.compose.ui.Modifier
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
            val state by viewModel.state.collectAsState()
            
            ExchangeListScreen(
                state = state,
                onItemClick = { cryptocurrency ->
                    navController.navigate(Screen.ExchangeDetails.createRoute(cryptocurrency.id))
                },
                onFinish = onFinish
            )
        }
        
        composable(
            route = Screen.ExchangeDetails.route,
            arguments = listOf(navArgument("id") { type = NavType.IntType })
        ) { backStackEntry ->
            val viewModel: ExchangeDetailsScreenViewModel = hiltViewModel()
            val state by viewModel.state.collectAsState()
            val exchangeId = backStackEntry.arguments?.getInt("id") ?: 0
            
            ExchangeDetailsScreen(
                exchangeId = exchangeId,
                state = state,
                onEvent = viewModel::onEvent,
                onBackClick = {
                    navController.popBackStack()
                }
            )
        }
    }
}
