package com.bokoup.merchantapp.ui.customer

import android.app.Activity
import android.util.Log
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import com.bokoup.merchantapp.R
import com.bokoup.merchantapp.TokenAccountListQuery
import com.bokoup.merchantapp.model.DelegateMemo
import com.google.gson.Gson

@Composable
@ExperimentalMaterial3Api
fun CustomerContent(
    viewModel: CustomerViewModel = hiltViewModel(),
    modifier: Modifier = Modifier,
    padding: PaddingValues,
    orderId: String,
    tokenAccounts: List<TokenAccountListQuery.TokenAccount>
) {
    val activity = (LocalContext.current as? Activity)
    val qrCode by viewModel.qrCodeConsumer.data.collectAsState()
    val delegateTokenSubscription by viewModel.delegateTokenSubscription.collectAsState(null)
    val timestamp: MutableState<Int?> = remember { mutableStateOf(null) }

    LaunchedEffect(delegateTokenSubscription) {
        val memo = delegateTokenSubscription?.data?.delegatePromoToken?.first()?.memo
        if (memo != null) {
            val memoJson = Gson().toJson(memo)
            val memo = Gson().fromJson(memoJson, DelegateMemo::class.java)
            Log.d("jingus", memoJson)
            if (memo.orderId == orderId && memo.timestamp == timestamp.value) {
                viewModel.approve(activity!!)
            }
        }
    }

    val checkedState =
        remember { mutableStateOf(tokenAccounts.associate { tokenAccount -> tokenAccount.id to false }) }

    fun updateCheckedState(thisId: String) {
        checkedState.value = checkedState.value.keys.associate { id ->
            if (id != thisId) {
                id to false
            } else {
                if (checkedState.value[thisId]!!) {
                    id to false
                } else {
                    id to true
                }
            }
        }
    }

    Column(
        modifier = modifier
            .padding(padding)
            .fillMaxSize()
            .verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        if (qrCode == null) {
            tokenAccounts.map { tokenAccount ->
                Row(
                    modifier = modifier
                        .fillMaxWidth()
                        .padding(horizontal = 32.dp, vertical = 6.dp)
                        .clickable { updateCheckedState(tokenAccount.id) },
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    AsyncImage(
                        model = tokenAccount.mintObject?.promoObject?.metadataObject?.image,
                        modifier = Modifier
                            .weight(0.2f),
                        contentDescription = null
                    )
                    Text(
                        modifier = Modifier
                            .weight(0.6f)
                            .padding(PaddingValues(start = 32.dp)),
                        text = tokenAccount.mintObject?.promoObject?.metadataObject?.description.toString(),
                        style = MaterialTheme.typography.titleLarge
                    )
                    Switch(
                        modifier = Modifier
                            .weight(0.2f),
                        checked = checkedState.value[tokenAccount.id]!!,
                        onCheckedChange = { updateCheckedState(tokenAccount.id) }
                    )
                }
            }
        }
        if (activity != null) {
            Row(
                modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.Center
            ) {
                Button(
                    onClick = { viewModel.cancel(activity) },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.error,
                        contentColor = MaterialTheme.colorScheme.surface
                    )
                ) {
                    Text("Decline", style = MaterialTheme.typography.headlineMedium)
                }
                Spacer(Modifier.width(16.dp))
                Button(
                    onClick = {
                        timestamp.value = (System.currentTimeMillis()/1000).toInt()
                        viewModel.getQrCode(orderId, tokenAccounts, checkedState.value, timestamp.value!! )
                              },
                    enabled = checkedState.value.values.any { it }) {
                    Text("Accept", style = MaterialTheme.typography.headlineMedium)
                }
            }
        }
    }
    AnimatedVisibility(qrCode != null) {
        Column(
            modifier = modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Box(contentAlignment = Alignment.Center) {
                Image(qrCode!!.asImageBitmap(), "bokoup", modifier = Modifier.size(300.dp))
                Image(
                    painter = painterResource(R.drawable.logo_1),
                    contentDescription = null,
                    modifier = modifier
                        .size(75.dp)
                        .background(Color.White)
                        .padding(2.dp)
                )
            }
            if (activity != null) {
                Row(
                    modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.Center
                ) {
                    Button(
                        onClick = { viewModel.cancel(activity) },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = MaterialTheme.colorScheme.error,
                            contentColor = MaterialTheme.colorScheme.surface
                        )
                    ) {
                        Text("Cancel", style = MaterialTheme.typography.headlineMedium)
                    }
//                    Spacer(Modifier.width(16.dp))
//                    Button(onClick = { viewModel.approve(activity) }) {
//                        Text("Approve", style = MaterialTheme.typography.headlineMedium)
//                    }
                }
            }
        }
    }
}