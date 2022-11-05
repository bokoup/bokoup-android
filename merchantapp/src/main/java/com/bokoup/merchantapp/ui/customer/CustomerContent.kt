package com.bokoup.merchantapp.ui.customer

import android.app.Activity
import android.util.Log
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.scaleIn
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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import com.bokoup.merchantapp.R
import com.bokoup.merchantapp.model.DelegateMemo
import com.bokoup.merchantapp.model.TokenAccountWithMetadata
import com.clover.sdk.v3.order.Discount
import com.google.gson.Gson
import kotlinx.coroutines.delay
import java.text.DecimalFormat

@OptIn(ExperimentalAnimationApi::class)
@Composable
@ExperimentalMaterial3Api
fun CustomerContent(
    viewModel: CustomerViewModel = hiltViewModel(),
    modifier: Modifier = Modifier,
    padding: PaddingValues,
    orderId: String,
    tokenAccounts: List<TokenAccountWithMetadata>,
    delegateString: String,
    sendMessage: (String) -> Unit
) {
    val activity = (LocalContext.current as? Activity)
    val qrCode by viewModel.qrCodeConsumer.data.collectAsState()
    val delegateTokenSubscription by viewModel.delegateTokenSubscription.collectAsState(null)
    val timestamp: MutableState<Int?> = remember { mutableStateOf(null) }
    val order by viewModel.orderConsumer.data.collectAsState(null)

    var tokenAccountsMap by remember { mutableStateOf(tokenAccounts.associateBy { tokenAccount -> tokenAccount.tokenAccount.id }) }

    LaunchedEffect(delegateTokenSubscription?.data?.delegatePromoToken) {

        val trans = try {
            delegateTokenSubscription?.data?.delegatePromoToken?.first()
        } catch (_: Exception) {
            null
        }
        Log.d("trans", trans.toString())
        if (trans != null) {
            val memoJson = Gson().toJson(trans.memo)
            val memo = Gson().fromJson(memoJson, DelegateMemo::class.java)
            val tokenAccount = tokenAccountsMap[trans.tokenAccountObject?.id]
            if (memo.orderId == orderId && memo.timestamp == timestamp.value && tokenAccount != null) {
                val discount = Discount()
                Log.d("discount", discount.toString())
                discount.name = tokenAccount.name
                discount.percentage = tokenAccount.attributes["getYPercent"]?.toLong()
                viewModel.addDiscount(orderId, discount)
            }
        }
    }

    // order gets returned when discount is applied
    LaunchedEffect(order) {
        if (order != null) {
            Log.d("CustomerContent", order.toString())
            viewModel.approve(activity!!)
        }

    }

    LaunchedEffect(tokenAccounts) {
        if (tokenAccounts.isEmpty()) {
            delay(2000)
            viewModel.approve(activity!!)
        }
    }


    // Ensures only one promo can be selected.
    fun updateCheckedState(thisId: String) {
        tokenAccountsMap = tokenAccountsMap.entries.associate { entry ->

            val selected = if (entry.key != thisId) {
                false
            } else {
                !entry.value.selected
            }

            val newEntry = entry.value.copy()
            newEntry.selected = selected

            entry.key to newEntry
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
            if (tokenAccounts.isEmpty()) {
                Text(
                    text = "You don't appear to have any eligible offers",
                    style = MaterialTheme.typography.headlineLarge
                )
            } else {
                Row(
                ) {
                    tokenAccountsMap.values.map { tokenAccount ->
                        Card(
                            modifier = Modifier
                                .width(300.dp)
                                .padding(32.dp)
                                .clickable {
                                    timestamp.value = (System.currentTimeMillis() / 1000).toInt()
                                    viewModel.getQrCode(
                                        orderId,
                                        tokenAccount,
                                        delegateString,
                                        timestamp.value!!
                                    )
                                }
                        ) {
                            Column(
                                modifier = modifier
                                    .fillMaxHeight()
                                    .padding(horizontal = 12.dp, vertical = 6.dp),
                                verticalArrangement = Arrangement.Top,
                            ) {
                                AsyncImage(
                                    model = tokenAccount.image,
                                    modifier = Modifier.padding(16.dp),
                                    contentDescription = null
                                )
                                Text(
                                    modifier = Modifier
                                        .padding(PaddingValues(start = 32.dp)),
                                    text = tokenAccount.description,
                                    style = MaterialTheme.typography.bodyLarge
                                )
                                Divider(thickness = 1.dp, modifier = Modifier.padding(vertical=12.dp))
                                Text(
                                    textAlign = TextAlign.End,
                                    text = "Tap to save $${
                                        DecimalFormat("#,###.##").format(
                                            tokenAccount.discount.toDouble() / 100
                                        )
                                    }!",
                                    style = MaterialTheme.typography.bodyLarge
                                )
                            }

                        }
                    }
                }
            }
        }
        if (activity != null && tokenAccounts.isNotEmpty()) {
            Row(
                modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.Center
            ) {
                Button(
                    onClick = { viewModel.cancel(activity) }, colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.error,
                        contentColor = MaterialTheme.colorScheme.surface
                    )
                ) {
                    Text("No Thanks!", style = MaterialTheme.typography.headlineMedium)
                }
            }
        }
    }
    AnimatedVisibility(
        qrCode != null,
        enter = scaleIn()
    ) {
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
                }
            }
        }
    }
}