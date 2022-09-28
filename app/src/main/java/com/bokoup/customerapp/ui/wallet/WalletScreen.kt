package com.bokoup.customerapp.ui.wallet


import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.hilt.navigation.compose.hiltViewModel
import com.bokoup.customerapp.nav.Screen
import com.bokoup.customerapp.ui.common.AppScreen
import com.dgsd.ksol.model.KeyPair
import kotlinx.coroutines.channels.Channel

@Composable
@ExperimentalMaterial3Api
fun WalletScreen(
    viewModel: WalletViewModel = hiltViewModel(),
    snackbarHostState: SnackbarHostState,
    channel: Channel<String>,
    openDrawer: () -> Unit,
    setKeyPair: (KeyPair) -> Unit,
    activeAddress: String
) {
    LaunchedEffect(Unit) {
        viewModel.getAddresses()
    }

    AppScreen(
        snackbarHostState = snackbarHostState,
        openDrawer = openDrawer,
        screen = Screen.Wallet,
        content = {
            WalletContent(
                padding = it,
                addresses = viewModel.addresses,
                setKeyPairFromAddress = { viewModel.setKeyPairFromAddress(it, setKeyPair) },
                activeAddress = activeAddress
            )
        },
        floatingActionButton = {
            FloatingActionButton(onClick = { viewModel.createAddress(channel) }) {
                Icon(Icons.Filled.Add, contentDescription = "Create Address")
            }
        },
    )
}
