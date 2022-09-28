package com.bokoup.customerapp.ui.share

import android.graphics.Bitmap
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.unit.dp

@Composable
@ExperimentalMaterial3Api
fun ShareContent(
    padding: PaddingValues,
    qrCode: Bitmap?,
    address: String?,
    copyToClipboard: (CharSequence) -> Unit
) {
    if (qrCode != null && address != null) {
        Column(
            verticalArrangement = Arrangement.Center,
            modifier = Modifier
                .fillMaxSize()
                .padding(padding),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Button(onClick = { copyToClipboard(address.toString())} ) {
                Text(
                    text = "${address.slice(0..16)}...",
                )
            }
            Spacer(modifier = Modifier.padding(16.dp))
            Image(qrCode.asImageBitmap(), contentDescription = address)
        }

    }
}