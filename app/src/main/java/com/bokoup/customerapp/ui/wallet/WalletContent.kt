package com.bokoup.customerapp.ui.wallet

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.bokoup.customerapp.dom.model.Address
import com.bokoup.customerapp.dom.model.Token
import com.dgsd.ksol.model.KeyPair

@Composable
@ExperimentalMaterial3Api
fun WalletContent(
    padding: PaddingValues,
    addresses: List<Address>,
    setKeyPairFromAddress: (String) -> Unit,
    activeAddress: String

) {
    LazyColumn(modifier = Modifier
        .fillMaxSize()
        .padding(padding)) {
        items(items = addresses) {
            AddressItem(address = it, setKeyPairFromAddress = setKeyPairFromAddress, activeAddress = activeAddress)
        }
    }
}

