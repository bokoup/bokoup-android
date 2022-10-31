package com.bokoup.customerapp.ui.approve

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bokoup.customerapp.data.net.TokenApiId
import com.bokoup.customerapp.data.net.TokenApiResponse
import com.bokoup.customerapp.dom.model.toKeyPair
import com.bokoup.customerapp.dom.repo.AddressRepo
import com.bokoup.customerapp.dom.repo.SolanaRepo
import com.bokoup.customerapp.dom.repo.TokenRepo
import com.bokoup.lib.ResourceFlowConsumer
import com.bokoup.lib.mapData
import com.dgsd.ksol.core.model.KeyPair
import com.dgsd.ksol.core.model.TransactionSignature
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.merge
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ApproveViewModel @Inject constructor(
    private val tokenRepo: TokenRepo,
    private val addressRepo: AddressRepo,
    private val solanaRepo: SolanaRepo,
) : ViewModel() {
    val keyPairConsumer = ResourceFlowConsumer<KeyPair>(viewModelScope)
    val appIdConsumer = ResourceFlowConsumer<TokenApiId>(viewModelScope)
    val transactionConsumer = ResourceFlowConsumer<TokenApiResponse>(viewModelScope)
    val signatureConsumer = ResourceFlowConsumer<TransactionSignature>(viewModelScope)
    val errorConsumer = merge(
        appIdConsumer.error,
        keyPairConsumer.error,
        transactionConsumer.error,
        signatureConsumer.error
    )

    private val _swipeComplete = MutableStateFlow<Boolean>(false)
    val swipeComplete =_swipeComplete.asStateFlow()

    fun getKeyPair() {
        viewModelScope.launch(Dispatchers.IO) {
            keyPairConsumer.collectFlow(
                addressRepo.getActiveAddress().mapData { address -> address.toKeyPair() })
        }
    }

    fun getAppId(action: String, mintString: String, message: String, memo: String?) {
        viewModelScope.launch(Dispatchers.IO) {
            appIdConsumer.collectFlow(
                tokenRepo.getApiId(action, mintString, message, memo)
            )
        }
    }

    fun getTokenTransaction(action: String, mintString: String, message: String, address: String, memo: String?) {
        Log.d("gin", "action")
        viewModelScope.launch(Dispatchers.IO) {
            transactionConsumer.collectFlow(
                tokenRepo.getTokenTransaction(
                    action,
                    mintString,
                    message,
                    memo,
                    address
                )
            )
        }
    }

    fun signAndSend(transaction: String, keyPair: KeyPair) {
        viewModelScope.launch(Dispatchers.IO) {
            signatureConsumer.collectFlow(solanaRepo.signAndSend(transaction, keyPair))
        }
    }

    fun setSwipeComplete(value: Boolean) {
        _swipeComplete.value = value
    }

    init {
        viewModelScope.launch(Dispatchers.IO) {
            getKeyPair()
        }
    }
}