package com.bokoup.customerapp.ui.share


import android.graphics.Bitmap
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import com.bokoup.customerapp.dom.model.Address
import com.bokoup.customerapp.nav.Screen
import com.bokoup.customerapp.ui.common.AppScreen
import kotlinx.coroutines.channels.Channel

@Composable
@ExperimentalMaterial3Api
fun ShareScreen(
    viewModel: ShareViewModel = hiltViewModel(),
    snackbarHostState: SnackbarHostState,
    openDrawer: () -> Unit,
) {
    LaunchedEffect(viewModel.qrCodeConsumer) {
        viewModel.getQrCode()
    }

    val qrCode: Pair<String, Bitmap>? by viewModel.qrCodeConsumer.data.collectAsState()

    AppScreen(
        snackbarHostState = snackbarHostState,
        openDrawer = openDrawer,
        screen = Screen.Share,
        content = {
            if (qrCode != null) {
                ShareContent(
                    padding = it,
                    qrCode = qrCode!!.second,
                    address = qrCode!!.first,
                    copyToClipboard = { viewModel.copyToClipboard(qrCode!!.first) })
            }
        }
    )

}