package com.bokoup.merchantapp.ui.tender

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.bokoup.lib.Loading
import com.bokoup.merchantapp.BuildConfig
import kotlinx.coroutines.channels.Channel

@Composable
@ExperimentalMaterial3Api
fun TenderContent(
    viewModel: TenderViewModel = hiltViewModel(),
    modifier: Modifier = Modifier,
    padding: PaddingValues,
    channel: Channel<String>,
    cardState: Boolean,
    setCardState: (Boolean) -> Unit
) {
    val tenders by viewModel.tendersConsumer.data.collectAsState()
    val isLoading by viewModel.isLoadingConsumer.collectAsState(false)
    val error: Throwable? by viewModel.errorConsumer.collectAsState(null)

    LaunchedEffect(error) {
        if (error != null) {
            channel.trySend(error!!.message.toString())
        }
    }

    DisposableEffect(Unit) {
        onDispose { viewModel.disconnect() }
    }

    Loading(isLoading)

    Box(contentAlignment = Alignment.Center) {
        if ((tenders != null) && !isLoading) {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding)
                    .padding(horizontal = 16.dp)
            ) {
                items(tenders!!) {tender ->
                    val enabled = tender.labelKey == BuildConfig.APPLICATION_ID
                    Row(
                        modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(text = tender.label, Modifier.weight(0.3f))
                        Text(text = tender.labelKey, Modifier.weight(0.4f))
                        Box(
                            contentAlignment = Alignment.CenterEnd,
                            modifier = Modifier.weight(0.15f)
                        ) {
                            Checkbox(
                                checked = tender.enabled, onCheckedChange = {
                                    viewModel.setTenderEnabled(
                                        tender.tenderId, !tender.enabled
                                    )
                                }, enabled = enabled
                            )
                        }
                        Box(
                            contentAlignment = Alignment.CenterEnd,
                            modifier = Modifier.weight(0.15f)
                        ) {
                            IconButton(
                                onClick = {
                                    viewModel.deleteTender(tender.tenderId);
                                },
                                enabled = enabled
                            ) {
                                Icon(
                                    Icons.Filled.Delete,
                                    "delete tender",
                                )
                            }
                        }
                    }
                }
            }
        }
        if (cardState) {
            CreateTenderCard(setCardState = setCardState,
                createTender = { label, labelKey, enabled, opensCashDrawer ->
                    viewModel.createTender(
                        label, labelKey, enabled, opensCashDrawer
                    )
                })
        }
    }
}