package com.bokoup.customerapp.ui.approve

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bokoup.customerapp.data.net.TokenApiId
import com.bokoup.customerapp.data.net.TokenApiResponse
import com.bokoup.customerapp.dom.model.toKeyPair
import com.bokoup.customerapp.dom.repo.AddressRepo
import com.bokoup.customerapp.dom.repo.TokenRepo
import com.bokoup.customerapp.util.ResourceFlowConsumer
import com.bokoup.customerapp.util.mapData
import com.bokoup.customerapp.util.stateFlowOf
import com.dgsd.ksol.model.KeyPair
import com.dgsd.ksol.model.LocalTransaction
import com.dgsd.ksol.model.TransactionSignature
import com.dgsd.ksol.model.TransactionSignatureStatus
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.merge
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.math.sign

@HiltViewModel
class ApproveViewModel @Inject constructor(
    private val tokenRepo: TokenRepo,
    private val addressRepo: AddressRepo,
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

    suspend fun getKeyPair() {
        viewModelScope.launch(Dispatchers.IO) {
            keyPairConsumer.collectFlow(
                addressRepo.getActiveAddress().mapData { address -> address.toKeyPair() })
        }
    }

    fun getAppId(mintString: String, promoName: String) {
        viewModelScope.launch(Dispatchers.IO) {
            appIdConsumer.collectFlow(
                tokenRepo.getApiId(mintString, promoName)
            )
        }
    }

    fun getTokenTransaction(mintString: String, promoName: String, address: String) {
        viewModelScope.launch(Dispatchers.IO) {
            transactionConsumer.collectFlow(
                tokenRepo.getTokenTransaction(
                    mintString,
                    promoName,
                    address
                )
            )
        }
    }

    fun signAndSend(transaction: String, keyPair: KeyPair) {
        viewModelScope.launch(Dispatchers.IO) {
            signatureConsumer.collectFlow(tokenRepo.signAndSend(transaction, keyPair))
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