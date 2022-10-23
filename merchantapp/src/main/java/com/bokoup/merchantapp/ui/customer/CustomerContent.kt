package com.bokoup.merchantapp.ui.customer

import android.app.Activity
import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.bokoup.merchantapp.R

@Composable
@ExperimentalMaterial3Api
fun CustomerContent(
    viewModel: CustomerViewModel = hiltViewModel(),
    modifier: Modifier = Modifier,
    padding: PaddingValues,
    orderId: String,
) {
    val activity = (LocalContext.current as? Activity)
    val qrCode by viewModel.qrCodeConsumer.data.collectAsState()
//    val notification: Notification? by viewModel.notification.collectAsState()

    val delegateTokenTransaction by viewModel.delegateTokenSubscription.collectAsState(null)
    Log.d("jingus", orderId)

    LaunchedEffect(Unit) {
        viewModel.registerNFCReceiver()
    }

    DisposableEffect(Unit) {
        onDispose {
            viewModel.unregisterNFCReceiver()
        }
    }

    LaunchedEffect(viewModel.qrCodeConsumer) {
        viewModel.getQrCode(orderId)
    }

    LaunchedEffect(delegateTokenTransaction) {
        if (delegateTokenTransaction?.data?.delegatePromoToken?.first()?.reference == orderId && activity != null) {
            viewModel.approve(activity)
        }
    }


    if (qrCode != null) {
        Column(
            modifier = modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Box(contentAlignment = Alignment.Center) {
                Image(qrCode!!.asImageBitmap(), "bokoup", modifier = Modifier.size(300.dp))
                Image(
                    painter = painterResource(R.drawable.logo_1),
                    contentDescription = null,
                    modifier = modifier
                        .size(75.dp)
                        .background(Color.White)
                        .padding(2.dp)
                )
            }
            if (activity != null) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.Center
                ) {
                    Button(
                        onClick = { viewModel.cancel(activity) },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = MaterialTheme.colorScheme.error,
                            contentColor = MaterialTheme.colorScheme.surface
                        )
                    ) {
                        Text("Cancel", style = MaterialTheme.typography.headlineMedium)
                    }
//                    Spacer(Modifier.width(16.dp))
//                    Button(onClick = { viewModel.approve(activity) }) {
//                        Text("Approve", style = MaterialTheme.typography.headlineMedium)
//                    }
                }
            }
        }
    }
}