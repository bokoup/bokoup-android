package com.bokoup.customerapp.ui.wallet

import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.viewModel
import com.bokoup.customerapp.dom.model.Address
import com.bokoup.customerapp.dom.repo.AddressRepo
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject
import com.dgsd.ksol.keygen.KeyFactory
import com.dgsd.ksol.keygen.MnemonicPhraseLength
import com.dgsd.ksol.model.KeyPair
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.withContext

@HiltViewModel
class WalletViewModel @Inject constructor(private val repo: AddressRepo) : ViewModel() {
    var addresses: List<Address> by mutableStateOf(emptyList<Address>())
    val dispatcher: CoroutineDispatcher = Dispatchers.IO

    fun createAddress(channel: Channel<String>) = viewModelScope.launch {
        val words = KeyFactory.createMnemonic(MnemonicPhraseLength.TWELVE)
        val newKeyPair = KeyFactory.createKeyPairFromMnemonic(words)

        val address = Address(id = newKeyPair.publicKey.toString(), phrase = words.joinToString(), name = "jingus")

        withContext(dispatcher) {
            repo.insertAddress(address)
        }

        channel.trySend("Address ${address.id.slice(0..8)}... created!")
    }

    suspend fun getAddresses() = withContext(dispatcher) {
        repo.getAddresses().collect {
            addresses = it
        }
    }

    fun setKeyPairFromAddress(id: String, setKeyPair: (KeyPair) -> Unit) = viewModelScope.launch {
        repo.getAddress(id).collect { address ->
            val newKeyPair = KeyFactory.createKeyPairFromMnemonic(address.phrase.split(", "))
            setKeyPair(newKeyPair)
        }
    }
}
