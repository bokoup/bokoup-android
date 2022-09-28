package com.bokoup.customerapp.ui.common

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.RowScope
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import com.bokoup.customerapp.nav.Screen
import com.bokoup.customerapp.ui.AppTopBar
import kotlinx.coroutines.channels.Channel

@Composable
@ExperimentalMaterial3Api
fun AppScreen(
    snackbarHostState: SnackbarHostState,
    openDrawer: () -> Unit,
    screen: Screen,
    topBarActions: @Composable (RowScope.() -> Unit) = {},
    content: @Composable (PaddingValues) -> Unit = {},
    floatingActionButton: @Composable (() -> Unit) = {}

    ) {

    Scaffold(
        snackbarHost = { SnackbarHost(hostState = snackbarHostState) },
        topBar = {
            AppTopBar(openDrawer = openDrawer, screen = screen, actions = topBarActions )
        },
        content = { padding ->
            content(padding)
        },
        floatingActionButton = floatingActionButton,
    )
}