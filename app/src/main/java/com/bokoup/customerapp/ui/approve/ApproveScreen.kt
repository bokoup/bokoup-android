package com.bokoup.customerapp.ui.approve


import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.hilt.navigation.compose.hiltViewModel
import com.bokoup.customerapp.nav.Screen
import com.bokoup.customerapp.ui.common.AppScreen
import com.dgsd.ksol.model.KeyPair
import kotlinx.coroutines.channels.Channel

@Composable
@ExperimentalMaterialApi
@ExperimentalMaterial3Api
fun ApproveScreen(
    viewModel: ApproveViewModel = hiltViewModel(),
    snackbarHostState: SnackbarHostState,
    openDrawer: () -> Unit,
    keyPair: KeyPair?,

    ) {
    if (keyPair != null) {
        LaunchedEffect(key1 = keyPair.publicKey.toString(), Unit) {
            viewModel.getAppId("GeWRS2Det9da6K2xQw4Fd62Kv3qVQx1E3wsjAqk8DGs1", "Promo1")
            viewModel.getTokenTransaction(
                "GeWRS2Det9da6K2xQw4Fd62Kv3qVQx1E3wsjAqk8DGs1",
                "Promo1",
                keyPair.publicKey.toString()
            )
        }
    }

    AppScreen(
        snackbarHostState = snackbarHostState,
        openDrawer = openDrawer,
        screen = Screen.Approve,
        content = {
            ApproveContent(
                padding = it,
                appId = viewModel.appId,
                message = viewModel.message,
                onSwipe = { viewModel.signAndSend(viewModel.localTransaction!!, keyPair!!) },
                transactionSignature = viewModel.transactionSignature
            )
        }
    )

}