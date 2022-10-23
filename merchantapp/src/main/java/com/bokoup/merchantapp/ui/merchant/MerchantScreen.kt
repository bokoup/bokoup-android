package com.bokoup.merchantapp.ui.merchant

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import com.bokoup.merchantapp.nav.Screen
import com.bokoup.merchantapp.ui.common.AppScreen

@Composable
@ExperimentalMaterial3Api
fun MerchantScreen(
    snackbarHostState: SnackbarHostState,
    openDrawer: () -> Unit = {},
    orderId: String,
) {
    AppScreen(
        snackbarHostState = snackbarHostState,
        openDrawer = openDrawer,
        screen = Screen.Merchant,
        content = {
            MerchantContent(padding = it, orderId = orderId)
        }
    )

}