package com.bokoup.customerapp.nav

import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material3.ExperimentalMaterial3Api
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
import kotlinx.coroutines.flow.receiveAsFlow

@Composable
@ExperimentalMaterial3Api
@ExperimentalMaterialApi
fun NavGraph(navController: NavHostController, openDrawer: () -> Unit) {
    val channel = Channel<String>(Channel.BUFFERED)
    var activeKeyPair: KeyPair? by rememberSaveable { mutableStateOf(null) }
    fun setKeyPair(newKeyPair: KeyPair) {
        activeKeyPair = newKeyPair
    }
    val snackbarHostState = remember { SnackbarHostState() }

    LaunchedEffect(channel) {
        channel.receiveAsFlow().collect { message ->
            val result = snackbarHostState.showSnackbar(
                message = message,
                actionLabel = "Dismiss"
            )
            when (result) {
                SnackbarResult.ActionPerformed -> {
                    /* action has been performed */
                }
                SnackbarResult.Dismissed -> {
                    /* dismissed, no action needed */
                }
            }
        }
    }

    NavHost(
        navController = navController,
        startDestination = Screen.Tokens.name
    ) {
        composable(
            route = Screen.Tokens.name
        ) {
            TokensScreen(openDrawer = openDrawer, snackbarHostState = snackbarHostState)
        }
        composable(
            route = Screen.Wallet.name
        ) {
            WalletScreen(openDrawer = openDrawer, snackbarHostState = snackbarHostState,  channel = channel, setKeyPair = { setKeyPair(it) }, activeAddress = activeKeyPair?.publicKey.toString())
        }
        composable(
            route = Screen.Share.name
        ) {
            ShareScreen(openDrawer = openDrawer, snackbarHostState = snackbarHostState, channel = channel, address = activeKeyPair?.publicKey.toString())
        }
        composable(
            route = Screen.Scan.name
        ) {
            ScanScreen(openDrawer = openDrawer, snackbarHostState = snackbarHostState, channel = channel, navigateTo = { navController.navigate(Screen.Tokens.name) } )
        }
        composable(
            route = Screen.Approve.name
        ) {
            ApproveScreen(openDrawer = openDrawer, snackbarHostState = snackbarHostState, address = activeKeyPair?.publicKey.toString() )
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