package com.bokoup.customerapp.ui.tokens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage

@Composable
@ExperimentalMaterial3Api
fun TokensContent(
    padding: PaddingValues,
    viewModel: TokensViewModel = hiltViewModel()
) {
    val activeAddress by viewModel.addressConsumer.data.collectAsState()

    val uriHandler = LocalUriHandler.current

    val tokenAccounts by viewModel.tokenAccountSubscription.collectAsState(null)

    LaunchedEffect(tokenAccounts) {
        viewModel.getActiveAdress()
    }

    if (tokenAccounts?.data?.tokenAccount != null) {
        val chunks = tokenAccounts!!.data!!.tokenAccount.chunked(2)
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            tokenAccounts!!.data!!.tokenAccount.filter { it.owner == activeAddress && it.amount as Int > 0 }
                .map { tokenAccount ->
                val link =
                    "https://explorer.solana.com/address/" + tokenAccount.mintObject?.id + "?cluster=custom&customUrl=http%3A%2F%2F99.91.8.130%3A8899"
                ElevatedCard(
                    modifier = Modifier
                        .width(284.dp)
                        .padding(8.dp)
                ) {
                    Column(
                        modifier = Modifier.fillMaxWidth(),
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(
                                    PaddingValues(
                                        start = 16.dp,
                                        end = 16.dp,
                                        top = 8.dp
                                    )
                                ),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = tokenAccount.mintObject?.promoObject?.metadataObject!!.name,
                                style = MaterialTheme.typography.titleMedium
                            )
                            TextButton(onClick = { uriHandler.openUri(link) }) {
                                Text(text = tokenAccount.mintObject?.id?.slice(0..8) ?: "")
                            }
                        }
                        AsyncImage(
                            model = tokenAccount.mintObject?.promoObject?.metadataObject?.image,
                            modifier = Modifier
                                .padding(6.dp)
                                .width(324.dp),
                            contentDescription = null
                        )
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 16.dp, vertical = 6.dp),
                            horizontalArrangement = Arrangement.Start
                        ) {
                            Text(
                                text = tokenAccount.mintObject?.promoObject?.metadataObject?.description.toString(),
                                style = MaterialTheme.typography.bodySmall
                            )
                        }
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 16.dp, vertical = 4.dp),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Divider(thickness = 1.dp)
                        }
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 16.dp, vertical = 2.dp),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(
                                text = "amount:",
                                style = MaterialTheme.typography.labelMedium
                            )
                            Text(
                                text = tokenAccount.amount.toString(),
                                style = MaterialTheme.typography.bodyMedium
                            )
                        }
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 16.dp, vertical = 2.dp),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(
                                text = "delegated amount:",
                                style = MaterialTheme.typography.labelMedium
                            )
                            Text(
                                text = tokenAccount.delegatedAmount.toString(),
                                style = MaterialTheme.typography.bodyMedium
                            )
                        }
                    }
                }

            }
        }
    }
}

// https://www.kodeco.com/34398400-lazy-layouts-in-jetpack-compose


