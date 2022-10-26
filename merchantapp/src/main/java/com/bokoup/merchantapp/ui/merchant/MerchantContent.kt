package com.bokoup.merchantapp.ui.merchant

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel

@Composable
@ExperimentalMaterial3Api
fun MerchantContent(
    viewModel: MerchantViewModel = hiltViewModel(),
    modifier: Modifier = Modifier,
    padding: PaddingValues,
    orderId: String,
) {
    val barcodeResult by viewModel.barcodeResult.collectAsState()
    val tokenAccounts by viewModel.tokenAccountConsumer.data.collectAsState()

    LaunchedEffect(barcodeResult) {
        if (barcodeResult != null) {
            viewModel.fetchTokenAccounts(barcodeResult!!)
        }
    }

    LaunchedEffect(tokenAccounts) {
        if (tokenAccounts != null && tokenAccounts!!.isNotEmpty() && barcodeResult != null) {
            viewModel.stopBarcodeScanner()
            viewModel.startCustomActivity(orderId, tokenAccounts!!, barcodeResult!!)
        }
    }

    Column(
        modifier = modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text("orderId: $orderId")
        if (barcodeResult != null) {
            Button(onClick = { viewModel.startCustomActivity(orderId, tokenAccounts!!, barcodeResult!!) }) {
                Text("start activity")
            }
            Button(onClick = { viewModel.endCustomActivity() }) {
                Text("end activity")
            }
        }
        Text("barcodeResult: $barcodeResult")
        Button(onClick = { viewModel.startBarcodeScanner() }) {
            Text("start scanner")
        }
        Button(onClick = { viewModel.stopBarcodeScanner() }) {
            Text("end scanner")
        }
    }
}
