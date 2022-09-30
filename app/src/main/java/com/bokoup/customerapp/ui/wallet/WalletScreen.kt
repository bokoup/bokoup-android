package com.bokoup.customerapp.ui.wallet


import androidx.compose.material.icons.Icons
import androidx.compose.runtime.getValue
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.hilt.navigation.compose.hiltViewModel
import com.bokoup.customerapp.dom.model.Address
import com.bokoup.customerapp.nav.Screen
import com.bokoup.customerapp.ui.common.AppScreen
import kotlinx.coroutines.channels.Channel

@Composable
@ExperimentalMaterial3Api
fun WalletScreen(
    viewModel: WalletViewModel = hiltViewModel(),
    snackbarHostState: SnackbarHostState,
    openDrawer: () -> Unit,
    channel: Channel<String>
) {
    val addresses: List<Address>? by viewModel.addressesConsumer.data.collectAsState()
    val isLoading: Boolean by viewModel.addressesConsumer.isLoading.collectAsState()
    val error: Throwable? by viewModel.addressesConsumer.error.collectAsState()

    LaunchedEffect(viewModel.addressesConsumer) {
        if (error != null) {
            channel.trySend(error!!.message.toString())
        }
        if (addresses != null && addresses!!.isEmpty()) {
            viewModel.createAddress(true)
        }
    }

    AppScreen(
        snackbarHostState = snackbarHostState,
        openDrawer = openDrawer,
        screen = Screen.Wallet,
        content = {
            WalletContent(
                padding = it,
                addresses = addresses,
                updateActive = { id -> viewModel.updateActive(id) },
                isLoading = isLoading,
            )
        },
        floatingActionButton = {
            FloatingActionButton(onClick = { viewModel.createAddress() }) {
                Icon(Icons.Filled.Add, contentDescription = "Create Address")
            }
        },
    )
}
