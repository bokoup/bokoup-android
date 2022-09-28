package com.bokoup.customerapp.ui.wallet

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import com.bokoup.customerapp.dom.model.Address


@Composable
fun AddressItem(address: Address, setKeyPairFromAddress: (String) -> Unit, activeAddress: String, modifier: Modifier = Modifier) {
    val modifier = if (address.id == activeAddress) {
        modifier
            .padding(12.dp)
            .background(color = MaterialTheme.colorScheme.secondaryContainer, RoundedCornerShape(50))
            .border(ButtonDefaults.outlinedButtonBorder, RoundedCornerShape(50))
            .clip(RoundedCornerShape(50))
            .fillMaxSize().clickable(
                onClick = { setKeyPairFromAddress(address.id) },
            )
    } else {
        modifier
            .padding(12.dp)
            .border(ButtonDefaults.outlinedButtonBorder, RoundedCornerShape(50))
            .clip(RoundedCornerShape(50))
            .fillMaxSize().clickable(
                onClick = { setKeyPairFromAddress(address.id) },
            )
    }

    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween,
    ) {
        Text(text = "${address.id.slice(0..27)}...", modifier = Modifier.padding(20.dp))
        if (address.id == activeAddress) {
            Icon(Icons.Filled.Check, contentDescription = "Active", modifier = Modifier.padding(horizontal = 16.dp))
        }
    }
}