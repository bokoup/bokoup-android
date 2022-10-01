package com.bokoup.customerapp.ui.wallet

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.bokoup.customerapp.dom.model.Address


@Composable
@ExperimentalMaterial3Api
fun WalletContent(
    padding: PaddingValues,
    addresses: List<Address>?,
    updateActive: (String) -> Unit,
    isLoading: Boolean,
) {

    if (isLoading) {
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            CircularProgressIndicator(
                color = MaterialTheme.colorScheme.tertiary,
                strokeWidth = ButtonDefaults.outlinedButtonBorder.width,
                modifier = Modifier
                    .size(64.dp)
                    .padding(6.dp)
            )
        }
    }
    if (addresses != null && !isLoading) {
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
        ) {
            items(items = addresses) {
                AddressItem(address = it, updateActive = updateActive)
            }
        }
    }
}

