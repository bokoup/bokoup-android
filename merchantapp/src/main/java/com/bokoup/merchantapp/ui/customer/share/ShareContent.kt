package com.bokoup.merchantapp.ui.customer.share

import android.app.Activity
import android.graphics.Bitmap
import android.widget.Toast
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
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.bokoup.merchantapp.R

@Composable
@ExperimentalMaterial3Api
fun ShareContent(
    viewModel: ShareViewModel = hiltViewModel(),
    modifier: Modifier = Modifier,
    padding: PaddingValues,
) {
    val activity = (LocalContext.current as? Activity)
    val qrCode: Pair<String, Bitmap>? by viewModel.qrCodeConsumer.data.collectAsState()
    val notification: Notification? by viewModel.notification.collectAsState()


    LaunchedEffect(Unit) {
        viewModel.registerNotificationReceiver()
    }

    DisposableEffect(Unit) {
        onDispose {
            viewModel.unregisterNotificationReceiver()
        }
    }

    LaunchedEffect(viewModel.qrCodeConsumer) {
        viewModel.getQrCode()
    }

    LaunchedEffect(notification) {
        if (notification != null && activity != null) {
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
                Image(qrCode!!.second.asImageBitmap(), "jingus", modifier = Modifier.size(300.dp))
                Image(
                    painter = painterResource(R.drawable.logo_1),
                    contentDescription = null,
                    modifier = modifier
                        .size(75.dp)
                        .background(Color.White)
                        .padding(2.dp)
                )
            }
            Text(
                text = "bokoup",
                fontFamily = FontFamily(Font(R.font.poppins_medium)),
                color = MaterialTheme.colorScheme.secondary,
                fontSize = 36.sp
            )
            if (activity != null) {
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.Center) {
                    Button(onClick = { viewModel.cancel(activity) }, colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.error,
                        contentColor = MaterialTheme.colorScheme.surface
                    )) {
                        Text("Cancel", style = MaterialTheme.typography.headlineMedium)
                    }
                    Spacer(Modifier.width(16.dp))
                    Button(onClick = { viewModel.approve(activity) }) {
                        Text("Approve", style = MaterialTheme.typography.headlineMedium)
                    }
                    if (notification != null) {
                        Toast.makeText(LocalContext.current, notification!!.payload, Toast.LENGTH_LONG).show()
                    }
                }
            }
        }
    }
}