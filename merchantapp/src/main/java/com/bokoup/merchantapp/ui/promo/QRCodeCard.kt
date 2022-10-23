package com.bokoup.merchantapp.ui.promo

import android.graphics.Bitmap
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.bokoup.merchantapp.R

@Composable
@ExperimentalMaterial3Api
fun QRCodeCard(
    setCardState: (Boolean) -> Unit,
    qrCode: Bitmap
) {
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.Center
    ) {
        Card {
            Column(
                Modifier
                    .padding(PaddingValues(start = 16.dp, end= 16.dp, top = 4.dp, bottom = 80.dp))
                    .width(480.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Row(
                    Modifier
                        .fillMaxWidth()
                        .padding(PaddingValues(start = 4.dp, end= 4.dp, top = 4.dp, bottom = 32.dp)),
                    horizontalArrangement = Arrangement.SpaceBetween,
                ) {
                    Text(
                        "Mint Promo Token",
                        style = MaterialTheme.typography.headlineSmall,
                        modifier = Modifier.padding(bottom = 8.dp)
                    )
                    IconButton(
                        onClick = { setCardState(false) },
                        modifier = Modifier.padding(0.dp)
                    ) {
                        Icon(
                            Icons.Filled.Close,
                            "Close",

                            )
                    }
                }
                Box(contentAlignment = Alignment.Center) {
                    Image(
                        qrCode.asImageBitmap(),
                        "scan to mint promo token",
                        modifier = Modifier.size(300.dp)
                    )
                    Image(
                        painter = painterResource(R.drawable.logo_1),
                        contentDescription = null,
                        modifier = Modifier
                            .size(75.dp)
                            .background(Color.White)
                            .padding(2.dp)
                    )
                }
            }

        }
    }
}