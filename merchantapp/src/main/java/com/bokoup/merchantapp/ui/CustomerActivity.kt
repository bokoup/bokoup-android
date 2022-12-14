package com.bokoup.merchantapp.ui

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.SnackbarHostState
import androidx.core.view.WindowCompat
import com.bokoup.merchantapp.model.CustomerPayload
import com.bokoup.merchantapp.ui.customer.CustomerScreen
import com.bokoup.merchantapp.ui.theme.AppTheme
import com.clover.cfp.activity.helper.CloverCFPActivityHelper
import com.clover.cfp.activity.helper.CloverCFPCommsHelper
import com.google.gson.Gson
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
@ExperimentalMaterial3Api
class CustomerActivity : ComponentActivity(), CloverCFPCommsHelper.MessageListener {
    lateinit var activityHelper: CloverCFPActivityHelper
    lateinit var commsHelper: CloverCFPCommsHelper
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        WindowCompat.setDecorFitsSystemWindows(window, true)

        activityHelper = CloverCFPActivityHelper(this)
        commsHelper = CloverCFPCommsHelper(this, intent, this);

        Log.d("CustomerActivity", activityHelper.initialPayload)
        val customerPayload = Gson().fromJson(activityHelper.initialPayload, CustomerPayload::class.java)
        setContent {
            AppTheme {
                CustomerScreen(
                    snackbarHostState = SnackbarHostState(),
                    customerPayload = customerPayload,
                    sendMessage = { it -> commsHelper.sendMessage(it) }
                )
            }
        }
    }
    override fun onDestroy() {
        super.onDestroy()
        commsHelper.dispose()
    }
    override fun onMessage(payload: String) {
        if(payload == "FINISH") {
            activityHelper.setResultAndFinish(RESULT_OK, payload)
        }
    }
}

// https://docs.clover.com/docs/using-customer-facing-platform