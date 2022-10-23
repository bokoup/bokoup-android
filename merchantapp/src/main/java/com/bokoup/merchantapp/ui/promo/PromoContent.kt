package com.bokoup.merchantapp.ui.tender

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import com.bokoup.lib.Loading
import com.bokoup.merchantapp.ui.promo.CreatePromoCard
import com.bokoup.merchantapp.ui.promo.PromoViewModel
import com.bokoup.merchantapp.ui.promo.QRCodeCard
import kotlinx.coroutines.channels.Channel

//https://developer.android.com/codelabs/jetpack-compose-basics#8

@Composable
@ExperimentalMaterial3Api
fun PromoContent(
    viewModel: PromoViewModel = hiltViewModel(),
    modifier: Modifier = Modifier,
    padding: PaddingValues,
    channel: Channel<String>,
    cardState: Boolean,
    setCardState: (Boolean) -> Unit,
) {
//    val promos by viewModel.promosConsumer.data.collectAsState()
    val isLoading by viewModel.isLoadingConsumer.collectAsState(false)
    val error: Throwable? by viewModel.errorConsumer.collectAsState(null)
    val uriHandler = LocalUriHandler.current
    val promoSubscription by viewModel.promoSubscription.collectAsState(null)
    val qrCode by viewModel.qrCodeConsumer.data.collectAsState()

    var (mintCardState, setMintCardState) =  remember {
        mutableStateOf(false)
    }

    LaunchedEffect(error) {
        if (error != null) {
            channel.trySend(error!!.message.toString())
        }
    }

    LaunchedEffect(promoSubscription) {
        setMintCardState(false)
    }

    Loading(isLoading)

    Box(contentAlignment = Alignment.Center, modifier = Modifier
        .padding(padding)
        ) {
        if ((promoSubscription?.data?.promo != null) && !isLoading) {
            val chunks = promoSubscription!!.data!!.promo.chunked(6)
            Column(modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())) {
                chunks.map {
                    Row(
                        modifier = Modifier
                            .padding(horizontal = 16.dp),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        it.map { promo ->
                            val link =
                                "https://explorer.solana.com/address/" + promo.mintObject?.id + "?cluster=custom&customUrl=http%3A%2F%2F99.91.8.130%3A8899"
                            ElevatedCard(
                                modifier = Modifier
                                    .width(250.dp)
                                    .padding(8.dp)
                                    .clickable {
                                        viewModel.getQrCode(
                                            promo.mintObject?.id!!,
                                            "Approve to receive promo ${promo.metadataObject?.name!!}"
                                        )
                                        setMintCardState(true)
                                    }
                            ) {
                                Column(
                                    modifier.fillMaxWidth(),
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
                                            text = promo.metadataObject!!.name,
                                            style = MaterialTheme.typography.titleMedium
                                        )
                                        TextButton(onClick = { uriHandler.openUri(link) }) {
                                            Text(text = promo.mintObject?.id?.slice(0..8) ?: "")
                                        }
                                    }
                                    AsyncImage(
                                        model = promo.metadataObject?.image,
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
                                            text = promo.metadataObject?.description.toString(),
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
                                            text = "mints:",
                                            style = MaterialTheme.typography.labelMedium
                                        )
                                        Text(
                                            text = promo.mintCount.toString() + " / " + promo.maxMint.toString(),
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
                                            text = "burns:",
                                            style = MaterialTheme.typography.labelMedium
                                        )
                                        Text(
                                            text = promo.burnCount.toString() + " / " + promo.maxBurn.toString(),
                                            style = MaterialTheme.typography.bodyMedium
                                        )
                                    }
                                }
                            }

                        }
                    }
                }
            }


        }
        if (cardState) {
            CreatePromoCard(setCardState = setCardState,
                createPromo = { viewModel.createPromo(it) })
        }
        if (mintCardState && qrCode != null) {
            QRCodeCard(setCardState = setMintCardState,
                qrCode = qrCode!!)
        }
    }
}