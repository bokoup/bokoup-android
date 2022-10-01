package com.bokoup.customerapp.ui.wallet

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.viewModel
import com.bokoup.customerapp.dom.model.Address
import com.bokoup.customerapp.dom.repo.AddressRepo
import com.bokoup.customerapp.util.ResourceFlowConsumer
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import com.dgsd.ksol.keygen.KeyFactory
import com.dgsd.ksol.keygen.MnemonicPhraseLength
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.merge

@HiltViewModel
class WalletViewModel @Inject constructor(private val repo: AddressRepo) : ViewModel() {
    private val dispatcher: CoroutineDispatcher = Dispatchers.IO
    val insertAddressConsumer = ResourceFlowConsumer<Unit>(viewModelScope)
    val addressesConsumer = ResourceFlowConsumer<List<Address>>(viewModelScope)
    val errorConsumer = merge(
        addressesConsumer.error,
        insertAddressConsumer.error,
    )

    init {
        getAddresses()
    }

    fun insertAddress() = viewModelScope.launch(dispatcher) {
        insertAddressConsumer.collectFlow(
            repo.insertAddress()
        )
    }

    fun getAddresses() = addressesConsumer.collectFlow(
        repo.getAddresses()
    )

    fun updateActive(id: String) = viewModelScope.launch(dispatcher) {
        repo.updateActive(id)
        getAddresses()
    }


}
