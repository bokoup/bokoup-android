package com.bokoup.customerapp.ui.scan


import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.hilt.navigation.compose.hiltViewModel
import com.bokoup.customerapp.nav.Screen
import com.bokoup.customerapp.ui.common.AppScreen
import kotlinx.coroutines.channels.Channel

@Composable
@ExperimentalMaterial3Api
@ExperimentalMaterialApi
fun ScanScreen(
    viewModel: ScanViewModel = hiltViewModel(),
    snackbarHostState: SnackbarHostState,
    openDrawer: () -> Unit,
    channel: Channel<String>,
    navigateTo: () -> Unit
) {
    AppScreen(
        snackbarHostState = snackbarHostState,
        openDrawer = openDrawer,
        screen = Screen.Scan,
        content = { ScanContent(padding = it, scanner = viewModel.scanner, navigateTo = navigateTo, channel = channel) }
    )

}