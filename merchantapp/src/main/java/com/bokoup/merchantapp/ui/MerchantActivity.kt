package com.bokoup.merchantapp.ui

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.SnackbarHostState
import androidx.core.view.WindowCompat
import com.bokoup.merchantapp.ui.merchant.MerchantScreen
import com.bokoup.merchantapp.ui.theme.AppTheme
import com.clover.sdk.v1.Intents
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
@ExperimentalMaterial3Api
class MerchantActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        WindowCompat.setDecorFitsSystemWindows(window, true)
        val orderId = intent.getStringExtra(Intents.EXTRA_ORDER_ID) ?: ""
        Log.d("orderId", orderId)
        setContent {
            AppTheme{
                MerchantScreen(
                    snackbarHostState = SnackbarHostState(),
                    orderId = orderId,
                )
            }
        }
    }
}

// https://docs.clover.com/docs/using-customer-facing-platform