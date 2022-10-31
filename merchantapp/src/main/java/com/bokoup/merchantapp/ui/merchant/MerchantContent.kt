package com.bokoup.merchantapp.ui.merchant

import android.app.Activity
import android.app.Activity.RESULT_OK
import android.util.Log
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel

@Composable
@ExperimentalMaterial3Api
fun MerchantContent(
    viewModel: MerchantViewModel = hiltViewModel(),
    modifier: Modifier = Modifier,
    padding: PaddingValues,
    orderId: String,
    promoOwner: String
) {
    val barcodeResult by viewModel.barcodeResult.collectAsState()
    val tokenAccounts by viewModel.tokenAccountConsumer.data.collectAsState()
    val activity = LocalContext.current as Activity
    val customActivityResponse by viewModel.customerActivityResult

    LaunchedEffect(customActivityResponse) {
        Log.d("MerchantContent", customActivityResponse?.result.toString())
        if (customActivityResponse?.result == RESULT_OK) {
            viewModel.approve(activity)
        }
    }

    LaunchedEffect(barcodeResult) {
        if (barcodeResult != null) {
            viewModel.fetchEligibleTokenAccounts(barcodeResult!!, promoOwner, orderId)
        }
    }

    LaunchedEffect(tokenAccounts) {
        if (tokenAccounts != null && barcodeResult != null) {
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
        Spacer(Modifier.height(16.dp))
        if (barcodeResult != null) {
            Button(onClick = {
                viewModel.startCustomActivity(
                    orderId,
                    tokenAccounts!!,
                    barcodeResult!!
                )
            }) {
                Text("start customer activity", style = MaterialTheme.typography.headlineMedium)
            }
            Spacer(Modifier.height(16.dp))
            Button(onClick = { viewModel.endCustomActivity() }) {
                Text("end customer activity", style = MaterialTheme.typography.headlineMedium)
            }
        }
        Spacer(Modifier.height(16.dp))
        Text("barcodeResult: $barcodeResult")
        Spacer(Modifier.height(16.dp))
        Button(onClick = { viewModel.startBarcodeScanner() }) {
            Text("start scanner", style = MaterialTheme.typography.headlineMedium)
        }
        Spacer(Modifier.height(16.dp))
        Button(onClick = { viewModel.stopBarcodeScanner() }) {
            Text("end scanner", style = MaterialTheme.typography.headlineMedium)
        }
        Spacer(Modifier.height(16.dp))
        Button(onClick = { viewModel.approve(activity) }) {
            Text("end merchant activity", style = MaterialTheme.typography.headlineMedium)
        }
    }
}
