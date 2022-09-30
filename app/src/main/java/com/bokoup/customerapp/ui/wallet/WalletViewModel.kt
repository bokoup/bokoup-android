package com.bokoup.customerapp.ui.wallet

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bokoup.customerapp.dom.model.Address
import com.bokoup.customerapp.dom.repo.AddressRepo
import com.bokoup.customerapp.util.ResourceFlowConsumer
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import com.dgsd.ksol.keygen.KeyFactory
import com.dgsd.ksol.keygen.MnemonicPhraseLength
import kotlinx.coroutines.*

@HiltViewModel
class WalletViewModel @Inject constructor(private val repo: AddressRepo) : ViewModel() {
    val dispatcher: CoroutineDispatcher = Dispatchers.IO
    var addressesConsumer = ResourceFlowConsumer<List<Address>>(viewModelScope)

    init {
        getAddresses()
    }

    fun createAddress(active: Boolean? = null) = viewModelScope.launch {
        val words = KeyFactory.createMnemonic(MnemonicPhraseLength.TWELVE)
        val newKeyPair = KeyFactory.createKeyPairFromMnemonic(words)

        val address = Address(
            id = newKeyPair.publicKey.toString(),
            phrase = words.joinToString(),
            active = active
        )

        withContext(dispatcher) {
            repo.insertAddress(address)
        }
        getAddresses()
    }

    fun getAddresses() = addressesConsumer.collectFlow(
        repo.getAddresses()
    )

    fun updateActive(id: String) = viewModelScope.launch(dispatcher) {
        repo.updateActive(id)
        getAddresses()
    }


}
