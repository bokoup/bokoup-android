package com.bokoup.customerapp.nav

import android.util.Log
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.SnackbarResult
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.bokoup.customerapp.ui.approve.ApproveScreen
import com.bokoup.customerapp.ui.scan.ScanScreen
import com.bokoup.customerapp.ui.share.ShareScreen
import com.bokoup.customerapp.ui.tokens.TokensScreen
import com.bokoup.customerapp.ui.wallet.WalletScreen
import com.bokoup.customerapp.util.SystemClipboard
import com.dgsd.ksol.model.KeyPair
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.consumeAsFlow
import kotlinx.coroutines.flow.receiveAsFlow

@Composable
@ExperimentalMaterial3Api
@ExperimentalMaterialApi
fun NavGraph(navController: NavHostController, openDrawer: () -> Unit) {
    val channel = Channel<String>(Channel.CONFLATED)
    val snackbarHostState by remember { mutableStateOf(SnackbarHostState()) }

    LaunchedEffect(channel) {
        channel.consumeAsFlow().collect { message ->
            val result = snackbarHostState.showSnackbar(
                message = message, withDismissAction = true, duration = SnackbarDuration.Indefinite
            )
            when (result) {
                SnackbarResult.ActionPerformed -> {
                    /* action has been performed */
                }
                SnackbarResult.Dismissed -> {
                }
            }
        }
    }

    NavHost(
        navController = navController, startDestination = Screen.Wallet.name
    ) {
        composable(
            route = Screen.Tokens.name
        ) {
            TokensScreen(openDrawer = openDrawer, snackbarHostState = snackbarHostState)
        }
        composable(
            route = Screen.Wallet.name
        ) {
            WalletScreen(
                openDrawer = openDrawer, snackbarHostState = snackbarHostState, channel = channel
            )
        }
        composable(
            route = Screen.Share.name
        ) {
            ShareScreen(openDrawer = openDrawer, snackbarHostState = snackbarHostState)
        }
        composable(
            route = Screen.Scan.name
        ) {
            val route =
            ScanScreen(openDrawer = openDrawer,
                snackbarHostState = snackbarHostState,
                navigateToApprove = { mintString, promoName -> navController.navigate("" +
                        "${Screen.Approve.name}?mintString=${mintString}?promoName=${promoName}") })
        }
        composable(
            route = "${Screen.Approve.name}?mintString={mintString}?promoName={promoName}",
            arguments = listOf(
                navArgument("mintString") { type = NavType.StringType; nullable = true },
                navArgument("promoName") { type = NavType.StringType; nullable = true })
        ) {
            ApproveScreen(
                openDrawer = openDrawer,
                snackbarHostState = snackbarHostState,
                channel = channel,
                mintString = it.arguments?.getString("mintString"),
                promoName = it.arguments?.getString("promoName")
            )
        }
        composable(
            route = "${Screen.TokenDetail}/{tokenId}",
            arguments = listOf(navArgument("tokenId") {
                type = NavType.StringType
            })
        ) { backStackEntry ->
            val tokenId = backStackEntry.arguments?.getString("tokenId") ?: ""
            TODO()
        }
    }
}