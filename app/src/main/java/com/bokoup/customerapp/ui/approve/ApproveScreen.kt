package com.bokoup.customerapp.ui.approve


import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.hilt.navigation.compose.hiltViewModel
import com.bokoup.customerapp.data.net.TokenApiId
import com.bokoup.customerapp.data.net.TokenApiResponse
import com.bokoup.customerapp.dom.model.Address
import com.bokoup.customerapp.nav.Screen
import com.bokoup.customerapp.ui.common.AppScreen
import com.dgsd.ksol.model.KeyPair
import com.dgsd.ksol.model.LocalTransaction
import com.dgsd.ksol.model.TransactionSignature
import com.dgsd.ksol.model.TransactionSignatureStatus
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.merge

@Composable
@ExperimentalMaterialApi
@ExperimentalMaterial3Api
fun ApproveScreen(
    viewModel: ApproveViewModel = hiltViewModel(),
    snackbarHostState: SnackbarHostState,
    openDrawer: () -> Unit,
    channel: Channel<String>,
) {

    val keyPair: KeyPair? by viewModel.keyPairConsumer.data.collectAsState()
    val appId: TokenApiId? by viewModel.appIdConsumer.data.collectAsState()
    val transaction: TokenApiResponse? by viewModel.transactionConsumer.data.collectAsState()
    val signature: TransactionSignature? by viewModel.signatureConsumer.data.collectAsState()
    val error: Throwable? by viewModel.errorConsumer.collectAsState(null)
    val swipeComplete: Boolean by viewModel.swipeComplete.collectAsState()

    LaunchedEffect(error) {
        if (error != null) {
            channel.trySend(error!!.message.toString())
        }
    }

    if (keyPair != null) {
        LaunchedEffect(key1 = keyPair, Unit) {
            viewModel.getAppId("GeWRS2Det9da6K2xQw4Fd62Kv3qVQx1E3wsjAqk8DGs1", "Promo1")
            viewModel.getTokenTransaction(
                "GeWRS2Det9da6K2xQw4Fd62Kv3qVQx1E3wsjAqk8DGs1",
                "Promo1",
                keyPair!!.publicKey.toString()
            )
        }
    }

    AppScreen(
        snackbarHostState = snackbarHostState,
        openDrawer = openDrawer,
        screen = Screen.Approve,
        content = {
            if (transaction != null && keyPair != null) {
                ApproveContent(
                    padding = it,
                    appId = appId,
                    message = transaction!!.message,
                    onSwipe = { viewModel.signAndSend(transaction!!.transaction, keyPair!!) },
                    swipeComplete = swipeComplete,
                    setSwipeComplete = { value -> viewModel.setSwipeComplete(value) },
                    isComplete = signature != null || error != null,
                    signature = signature
                )
            }

        }
    )

}