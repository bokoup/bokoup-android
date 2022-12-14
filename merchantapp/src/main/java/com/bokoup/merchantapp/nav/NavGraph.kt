package com.bokoup.merchantapp.nav

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.SnackbarResult
import androidx.compose.runtime.*
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.bokoup.merchantapp.model.Screen
import com.bokoup.merchantapp.ui.promo.PromoScreen
import com.bokoup.merchantapp.ui.settings.SettingsScreen
import com.bokoup.merchantapp.ui.tender.TenderScreen
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.consumeAsFlow

@Composable
@ExperimentalMaterial3Api
fun NavGraph(navController: NavHostController, startDestination: String, openDrawer: () -> Unit) {
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
        navController = navController, startDestination = startDestination
    ) {
        composable(
            route = Screen.Tender.route,
        ) {
            TenderScreen(snackbarHostState = snackbarHostState, channel = channel, openDrawer = openDrawer)
        }
        composable(
            route = Screen.Promo.route,
        ) {
            PromoScreen(snackbarHostState = snackbarHostState, channel = channel, openDrawer = openDrawer)
        }
        composable(
            route = Screen.Settings.route,
        ) {
            SettingsScreen(snackbarHostState = snackbarHostState, openDrawer = openDrawer)
        }
    }
}

// https://stackoverflow.com/questions/72316892/navigating-to-a-composable-using-a-deeplink-with-jetpack-compose
// https://developer.android.com/jetpack/compose/navigation#deeplinks
// https://developer.android.com/guide/components/broadcasts#receiving-broadcasts
// https://clover.github.io/clover-android-sdk/constant-values.html#com.clover.sdk.v1.Intents.ACTION_ACTIVE_REGISTER_ORDER
// https://docs.clover.com/docs/app-notifications