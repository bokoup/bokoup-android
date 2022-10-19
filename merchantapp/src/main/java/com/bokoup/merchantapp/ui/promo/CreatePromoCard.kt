package com.bokoup.merchantapp.ui.promo

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp

@Composable
@ExperimentalMaterial3Api
fun CreatePromoCard(
    setCardState: (Boolean) -> Unit,
    createPromo: (
        name: String,
        symbol: String,
        description: String,
        maxMint: Int,
        maxBurn: Int,
        enabled: Boolean,
        collectionName: String,
        collectionFamily: String,
        isMutable: Boolean,
        memo: String?
    ) -> Unit
) {
    var name by remember {
        mutableStateOf(TextFieldValue())
    }

    var symbol by remember {
        mutableStateOf(TextFieldValue())
    }

    var description by remember {
        mutableStateOf(TextFieldValue())
    }

    var maxMint by remember {
        mutableStateOf("")
    }

    var maxBurn by remember {
        mutableStateOf("")
    }

    var isMutable by remember {
        mutableStateOf(true)
    }

    Column(modifier = Modifier.fillMaxSize(), horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.Center) {
        Card {
            Column(
                Modifier
                    .padding(vertical = 8.dp, horizontal = 16.dp)
                    .width(400.dp)
            ) {
                Row(
                    Modifier
                        .fillMaxWidth()
                        .padding(4.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.Bottom
                ) {
                    Text(
                        "Create Tender",
                        style = MaterialTheme.typography.headlineSmall,
                        modifier = Modifier.padding(bottom = 8.dp)
                    )
                    IconButton(
                        onClick = { setCardState(false) },
                        modifier = Modifier.padding(0.dp)
                    ) {
                        Icon(
                            Icons.Filled.Close,
                            "Close",

                            )
                    }
                }
                TextField(
                    value = name,
                    onValueChange = { text -> name = text },
                    label = { Text("Name") },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(4.dp)
                )
                TextField(
                    value = symbol,
                    onValueChange = { text -> symbol = text },
                    label = { Text("Symbol") },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(4.dp)
                )
                TextField(
                    value = maxMint,
                    onValueChange = { text -> maxMint = text },
                    label = { Text("Max Mints") },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(4.dp),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
                )
                Row(
                    Modifier
                        .fillMaxWidth()
                        .padding(4.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text("Is Mutable")
                    Switch(checked = isMutable, onCheckedChange = { isMutable = it })
                }
            }

        }
    }
}