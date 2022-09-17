package com.bokoup.customerapp.nav

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.navArgument
import com.bokoup.customerapp.ui.AppDrawer
import com.bokoup.customerapp.ui.tokens.TokensScreen

@Composable
@ExperimentalMaterial3Api
fun NavGraph(navController: NavHostController, openDrawer: () -> Unit) {
    NavHost(
        navController = navController,
        startDestination = Screen.Tokens.name
    ) {
        composable(
            route = Screen.Tokens.name
        ) {
            TokensScreen(openDrawer = openDrawer)
        }
        composable(
            route = Screen.Settings.name
        ) {
            TODO()
        }
        composable(
            route = "${Screen.TokenDetail}/{tokenId}",
            arguments = listOf(
                navArgument("tokenId") {
                    type = NavType.StringType
                }
            )
        ) { backStackEntry ->
            val tokenId = backStackEntry.arguments?.getString("tokenId") ?: ""
            TODO()
        }
    }
}