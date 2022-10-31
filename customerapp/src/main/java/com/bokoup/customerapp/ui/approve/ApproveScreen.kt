package com.bokoup.customerapp.ui.approve


import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import com.bokoup.customerapp.data.net.TokenApiId
import com.bokoup.customerapp.data.net.TokenApiResponse
import com.bokoup.customerapp.nav.Screen
import com.bokoup.customerapp.ui.common.AppScreen
import com.dgsd.ksol.core.model.KeyPair
import com.dgsd.ksol.core.model.TransactionSignature
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.delay

@Composable
@ExperimentalMaterialApi
@ExperimentalMaterial3Api
fun ApproveScreen(
    viewModel: ApproveViewModel = hiltViewModel(),
    snackbarHostState: SnackbarHostState,
    openDrawer: () -> Unit,
    channel: Channel<String>,
    action: String?,
    mintString: String?,
    message: String?,
    memo: String?,
    navigateToTokens: () -> Unit
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

    LaunchedEffect(key1 = keyPair) {
        if (keyPair != null && action != null && mintString != null && message != null) {
            viewModel.getAppId(action, mintString, message, memo)
            viewModel.getTokenTransaction(
                action, mintString, message,
                keyPair!!.publicKey.toString(), memo
            )
        }
    }

    LaunchedEffect(signature) {
        if (signature != null) {
            delay(1000)
                navigateToTokens()
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