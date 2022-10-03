package com.bokoup.customerapp.ui.share

import android.graphics.Bitmap
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel

@Composable
@ExperimentalMaterial3Api
fun ShareContent(
    viewModel: ShareViewModel = hiltViewModel(),
    padding: PaddingValues,
) {
    LaunchedEffect(viewModel.qrCodeConsumer) {
        viewModel.getQrCode()
    }

    val qrCode: Pair<String, Bitmap>? by viewModel.qrCodeConsumer.data.collectAsState()

    if (qrCode != null) {
        Column(
            verticalArrangement = Arrangement.Center,
            modifier = Modifier
                .fillMaxSize()
                .padding(padding),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Image(qrCode!!.second.asImageBitmap(), contentDescription = qrCode!!.first)
            Spacer(modifier = Modifier.padding(16.dp))
            Button(onClick = { viewModel.copyToClipboard(qrCode!!.first)}) {
                Text(
                    text = "${qrCode!!.first.slice(0..16)}...",
                )
            }
        }

    }
}