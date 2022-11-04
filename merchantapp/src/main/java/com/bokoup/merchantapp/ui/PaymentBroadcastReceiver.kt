package com.bokoup.merchantapp.ui

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log
import com.bokoup.merchantapp.domain.SettingsRepo
import com.bokoup.merchantapp.model.BurmDelegatedMemo
import com.bokoup.merchantapp.net.AccountData
import com.bokoup.merchantapp.net.DataService
import com.bokoup.merchantapp.net.TransactionService
import com.clover.sdk.v1.Intents
import com.dgsd.ksol.SolanaApi
import com.dgsd.ksol.core.LocalTransactions
import com.google.gson.Gson
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch
import javax.inject.Inject


@AndroidEntryPoint
class PaymentBroadcastReceiver : BroadcastReceiver() {
    @Inject
    lateinit var dataService: DataService

    @Inject
    lateinit var settingsRepo: SettingsRepo

    @Inject
    lateinit var txService: TransactionService

    @Inject
    lateinit var localTransactions: LocalTransactions

    @Inject
    lateinit var solanaApi: SolanaApi

    private val scope = CoroutineScope(SupervisorJob())

    override fun onReceive(context: Context, intent: Intent) {
        val pendingResult: PendingResult = goAsync()
        scope.launch(Dispatchers.IO) {
            try {
                val orderId = intent.getStringExtra(Intents.EXTRA_CLOVER_ORDER_ID)
                val paymentId = intent.getStringExtra(Intents.EXTRA_CLOVER_PAYMENT_ID)
                Log.d(TAG, "orderId: ${orderId.toString()}, paymentId: ${paymentId.toString()}")
                checkNotNull(orderId) { "orderId was null" }
                checkNotNull(paymentId) { "orderId was null" }
                val delegate = settingsRepo.getPublicKeyString()
                checkNotNull(delegate) { "PublicKeyString was null" }
                Log.d(TAG, "delegate: $delegate")
                val delegatedToken = dataService.fetchDelegatedToken(orderId, delegate)
                Log.d(TAG, delegatedToken.toString())
                if (delegatedToken != null) {
                    val keyPair = settingsRepo.getKeyPair();
                    checkNotNull(keyPair) { "PublicKeyString was null" }
                    val memo = BurmDelegatedMemo(
                        orderId = orderId,
                        paymentId = paymentId,
                        delegateSignature = delegatedToken.signature
                    )
                    val tx = txService.service.burnDelegated(
                        AccountData(delegate),
                        delegatedToken.tokenAccount,
                        "burning delegated token",
                        Gson().toJson(memo)
                    )

                    val localTransaction = localTransactions.deserializeTransaction(tx.transaction)
                    solanaApi.sendTransaction(localTransactions.sign(localTransaction, keyPair))
                }

            } finally {
                pendingResult.finish()
            }

        }

    }

    companion object {
        private const val TAG = "PaymentBroadcast"
    }
}