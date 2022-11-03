package com.bokoup.merchantapp.ui.settings

import androidx.compose.foundation.layout.*
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
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
    val groupSeed by viewModel.groupSeedConsumer.data.collectAsState(null)

    LaunchedEffect(Unit) {
        val bytesString =
            "168,126,25,63,142,86,163,8,155,171,196,77,52,125,37,59,241,58,198,10,151,83,43,91,248,207,36,69,64,243,47,156,30,161,142,168,81,205,247,3,3,41,165,243,48,141,203,61,201,254,10,176,50,111,191,58,228,29,202,4,34,210,44,106"
        viewModel.saveKeyPairString(bytesString)

        val groupSeed = "FqVhBMr1T6pLCr4Ka5LNJNpSag8tgoK6fgx5bxfipySJ"
        viewModel.saveGroupSeed(groupSeed)
        viewModel.getGroupSeed()
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
                        "Pubkey:",
                        modifier = Modifier.weight(0.2f),
                    )
                    Text(
                        text = pubKeyAddress.toString(),
                        modifier = Modifier.weight(0.8f),
                    )
                }
                Row(
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        "Group Seed:",
                        modifier = Modifier.weight(0.2f),
                    )
                    Text(
                        text = groupSeed.toString(),
                        modifier = Modifier.weight(0.7f),
                    )
                    Spacer(modifier = Modifier.weight(0.1f))
                }
            }
        }
        Row(modifier = Modifier.weight(0.5f)) {
            Spacer(modifier = Modifier.fillMaxWidth())
        }
    }

}
