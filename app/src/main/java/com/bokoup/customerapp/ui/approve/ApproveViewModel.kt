package com.bokoup.customerapp.ui.approve

import android.graphics.Bitmap
import androidx.compose.runtime.setValue
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bokoup.customerapp.data.net.TokenApiId
import com.bokoup.customerapp.data.net.TokenApiResponse
import com.bokoup.customerapp.data.repo.TokenRepoImpl
import com.bokoup.customerapp.dom.repo.TokenRepo
import com.bokoup.customerapp.util.QRCodeGenerator
import com.bokoup.customerapp.util.SystemClipboard
import com.dgsd.ksol.LocalTransactions
import com.dgsd.ksol.model.KeyPair
import com.dgsd.ksol.model.LocalTransaction
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class ApproveViewModel @Inject constructor(
    private val tokenRepo: TokenRepo,
) : ViewModel() {
    var appId: TokenApiId? by mutableStateOf(null)
    var tokenApiResponse: TokenApiResponse? by mutableStateOf(null)
    var localTransaction: LocalTransaction? by mutableStateOf(null)

    suspend fun getAppId(mintString: String, promoName: String) = withContext(Dispatchers.IO) {
        appId = tokenRepo.getApiId(mintString, promoName)
    }

    suspend fun getTokenTransaction(mintString: String, promoName: String, address: String) = withContext(Dispatchers.IO) {
        val result = tokenRepo.getTokenTransaction(mintString, promoName, address)
        tokenApiResponse = result
        localTransaction = deserializeTransaction(result.transaction)
    }

    fun deserializeTransaction(base64EncodedTransaction: String): LocalTransaction {
        return LocalTransactions.deserializeTransaction(base64EncodedTransaction)
    }

    fun signTransaction(localTransaction: LocalTransaction, keyPair: KeyPair): LocalTransaction {
        return LocalTransactions.sign(localTransaction, keyPair)
    }
}