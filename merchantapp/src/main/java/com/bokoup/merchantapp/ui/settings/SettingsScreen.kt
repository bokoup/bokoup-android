package com.bokoup.merchantapp.ui.settings

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import com.bokoup.merchantapp.nav.Screen
import com.bokoup.merchantapp.ui.common.AppScreen

@Composable
@ExperimentalMaterial3Api
fun SettingsScreen(
    snackbarHostState: SnackbarHostState,
    openDrawer: () -> Unit = {},
) {
    AppScreen(
        snackbarHostState = snackbarHostState,
        openDrawer = openDrawer,
        screen = Screen.Settings,
        content = {
            SettingsContent(padding = it)
        }
    )

}