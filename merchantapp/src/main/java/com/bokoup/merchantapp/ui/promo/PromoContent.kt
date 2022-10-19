package com.bokoup.merchantapp.ui.tender

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
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
import com.bokoup.lib.Loading
import com.bokoup.merchantapp.ui.promo.CreatePromoCard
import com.bokoup.merchantapp.ui.promo.PromoViewModel
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
    setCardState: (Boolean) -> Unit
) {
    val promos by viewModel.promosConsumer.data.collectAsState()
    val isLoading by viewModel.isLoadingConsumer.collectAsState(false)
    val error: Throwable? by viewModel.errorConsumer.collectAsState(null)

    LaunchedEffect(error) {
        if (error != null) {
            channel.trySend(error!!.message.toString())
        }
    }

    Loading(isLoading)

    Box(contentAlignment = Alignment.Center) {
        if ((promos != null) && !isLoading) {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding)
                    .padding(horizontal = 16.dp)
            ) {
                items(promos!!) {promo ->
                    Row(
                        modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(text = promo.metadataObject!!.name, Modifier.weight(0.3f))
                        Text(text = promo.metadataObject!!.symbol, Modifier.weight(0.4f))
                    }
                }
            }
        }
        if (cardState) {
            CreatePromoCard(setCardState = setCardState,
                createPromo = { name,
                    symbol,
                    description,
                    maxMint,
                    maxBurn,
                    enabled,
                    collectionName,
                    collectionFamily,
                    isMutable,
                    memo
                 -> Unit  })
        }
    }
}