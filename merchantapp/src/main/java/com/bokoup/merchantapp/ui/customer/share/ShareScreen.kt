package com.bokoup.merchantapp.ui.customer.share

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import com.bokoup.merchantapp.nav.Screen
import com.bokoup.merchantapp.ui.common.AppScreen

@Composable
@ExperimentalMaterial3Api
fun ShareScreen(
    snackbarHostState: SnackbarHostState,
    openDrawer: () -> Unit = {},
) {
    AppScreen(
        snackbarHostState = snackbarHostState,
        openDrawer = openDrawer,
        screen = Screen.Customer,
        content = {
            ShareContent(padding = it)
        }
    )

}