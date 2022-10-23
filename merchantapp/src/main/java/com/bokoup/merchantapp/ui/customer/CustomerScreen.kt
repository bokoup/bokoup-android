package com.bokoup.merchantapp.ui.customer

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import com.bokoup.merchantapp.nav.Screen
import com.bokoup.merchantapp.ui.common.AppScreen

@Composable
@ExperimentalMaterial3Api
fun CustomerScreen(
    snackbarHostState: SnackbarHostState,
    openDrawer: () -> Unit = {},
    orderId: String,
) {
    AppScreen(
        snackbarHostState = snackbarHostState,
        openDrawer = openDrawer,
        screen = Screen.Customer,
        content = {
            CustomerContent(padding = it, orderId = orderId)
        }
    )

}