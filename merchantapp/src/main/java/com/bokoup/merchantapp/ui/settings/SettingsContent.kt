package com.bokoup.merchantapp.ui.settings

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel

@Composable
@ExperimentalMaterial3Api
fun SettingsContent(
    viewModel: SettingsViewModel = hiltViewModel(),
    modifier: Modifier = Modifier,
    padding: PaddingValues,
) {
    val pubKeyAddress by viewModel.addressConsumer.data.collectAsState(null)
    val mnemonic by viewModel.mnemonicConsumer.data.collectAsState(emptyList())

    var showPassPhrase by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        val mnemonicString =
            "lecture gorilla snap perfect sunny meadow panda mosquito agent turn harsh join"
        viewModel.saveMnemonic(mnemonicString.split(" "))
        viewModel.getMnemonic()
    }

    Row(
        modifier = modifier
            .padding(padding)
            .fillMaxSize()
            .padding(horizontal = 32.dp),
    ) {
        Row(modifier = Modifier.weight(0.5f)) {
            Column(modifier = Modifier.fillMaxWidth()) {
                Row(
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        "Address:",
                        modifier = Modifier.weight(0.2f),
                    )
                    Text(
                        text = pubKeyAddress.toString(),
                        modifier = Modifier.weight(0.7f),
                    )
                    IconButton(
                        onClick = { showPassPhrase = !showPassPhrase },
                        enabled = pubKeyAddress != null,
                        modifier = Modifier.weight(0.1f),
                    ) {
                        if (showPassPhrase) {
                            Icon(Icons.Filled.VisibilityOff, null)
                        } else {
                            Icon(Icons.Filled.Visibility, null)
                        }
                    }
                }
                if (pubKeyAddress != null && showPassPhrase) {
                    Row(horizontalArrangement = Arrangement.SpaceBetween) {
                        Text(
                            "Pass Phrase:",
                            modifier = Modifier.weight(0.2f),
                        )
                        Text(
                            mnemonic?.joinToString(" ") ?: "",
                            softWrap = true,
                            modifier = Modifier.weight(0.7f),
                            textAlign = TextAlign.Left
                        )
                        Spacer(modifier = Modifier.weight(0.1f))
                    }
                }
            }
        }
        Row(modifier = Modifier.weight(0.5f)) {
            Spacer(modifier = Modifier.fillMaxWidth())
        }
    }

}
