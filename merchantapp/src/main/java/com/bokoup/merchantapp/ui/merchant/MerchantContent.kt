package com.bokoup.merchantapp.ui.merchant

import android.app.Activity
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.navigation.compose.hiltViewModel

@Composable
@ExperimentalMaterial3Api
fun MerchantContent(
    viewModel: MerchantViewModel = hiltViewModel(),
    modifier: Modifier = Modifier,
    padding: PaddingValues,
    orderId: String,
) {
    val activity = (LocalContext.current as? Activity)
    Column(
        modifier = modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text("orderId: $orderId")
        Button(onClick = {viewModel.startCustomActivity()}) {
            Text("start activity")
        }
        Button(onClick = {viewModel.endCustomActivity()}) {
            Text("end activity")
        }
    }
}
