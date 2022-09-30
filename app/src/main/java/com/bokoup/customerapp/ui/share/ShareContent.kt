package com.bokoup.customerapp.ui.share

import android.graphics.Bitmap
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
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
            Image(qrCode.asImageBitmap(), contentDescription = address)
            Spacer(modifier = Modifier.padding(16.dp))
            Button(onClick = { copyToClipboard(address.toString())}) {
                Text(
                    text = "${address.slice(0..16)}...",
                )
            }


        }

    }
}