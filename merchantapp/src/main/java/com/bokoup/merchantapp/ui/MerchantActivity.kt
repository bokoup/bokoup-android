package com.bokoup.merchantapp.ui

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.SnackbarHostState
import androidx.core.view.WindowCompat
import com.bokoup.merchantapp.ui.merchant.MerchantScreen
import com.clover.sdk.v1.Intents
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
@ExperimentalMaterial3Api
class MerchantActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        WindowCompat.setDecorFitsSystemWindows(window, true)
//        val bundle = intent.extras
//        if (bundle != null) {
//            for (key in bundle.keySet()) {
//                Log.e("jingus", key + " : " + if (bundle[key] != null) bundle[key] else "NULL")
//            }
//        }
        val orderId = intent.getStringExtra(Intents.EXTRA_ORDER_ID) ?: ""
        setContent {
            MerchantScreen(
                snackbarHostState = SnackbarHostState(),
                orderId = orderId,
            )
        }
    }
}

// https://docs.clover.com/docs/using-customer-facing-platform