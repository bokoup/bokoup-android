package com.bokoup.customerapp.ui.share


import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.hilt.navigation.compose.hiltViewModel
import com.bokoup.customerapp.nav.Screen
import com.bokoup.customerapp.ui.common.AppScreen
import kotlinx.coroutines.channels.Channel

@Composable
@ExperimentalMaterial3Api
fun ShareScreen(
    viewModel: ShareViewModel = hiltViewModel(),
    snackbarHostState: SnackbarHostState,
    openDrawer: () -> Unit,
    address: String,
    channel: Channel<String>,
) {
    LaunchedEffect(Unit) {
        viewModel.getQrCode(address)
    }
    AppScreen(
        snackbarHostState = snackbarHostState,
        openDrawer = openDrawer,
        screen = Screen.Share,
        content = { ShareContent(padding = it, qrCode = viewModel.qRCode, address = address, copyToClipboard = { viewModel.copyToClipboard(address, channel) }) }
    )

}