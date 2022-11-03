package com.bokoup.merchantapp.domain

import android.content.ContentResolver
import android.content.Context
import android.net.Uri
import android.provider.OpenableColumns
import android.util.Log
import com.bokoup.lib.resourceFlowOf
import com.bokoup.merchantapp.model.PromoType
import com.bokoup.merchantapp.model.PromoWithMetadata
import com.bokoup.merchantapp.model.TokenAccountWithMetadata
import com.bokoup.merchantapp.model.toJson
import com.bokoup.merchantapp.net.DataService
import com.bokoup.merchantapp.net.OrderService
import com.bokoup.merchantapp.net.TransactionService
import com.dgsd.ksol.SolanaApi
import com.dgsd.ksol.core.LocalTransactions
import com.dgsd.ksol.core.model.KeyPair
import com.google.gson.Gson
import com.google.gson.JsonParser
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.flowOn
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.toRequestBody

class PromoRepoImpl(
    val context: Context,
    private val dataService: DataService,
    private val transactionService: TransactionService,
    private val orderService: OrderService,
    private val solanaApi: SolanaApi,
    private val localTransactions: LocalTransactions,
) : PromoRepo {
    override fun fetchPromos() = resourceFlowOf {
        dataService.fetchPromos().map { p ->
            val attributesMap =
                JsonParser().parse(Gson().toJson(p.metadataObject?.attributes)).asJsonArray.associate { a ->
                    a.asJsonObject["trait_type"].asString to a.asJsonObject["value"].asString
                }
            PromoWithMetadata(
                promo = p,
                image = p.metadataObject?.image.toString(),
                description = p.metadataObject?.description.toString(),
                attributes = attributesMap
            )
        }
    }

    override fun fetchEligibleTokenAccounts(
        tokenOwner: String,
        promoOwner: String,
        orderId: String
    ) =
        resourceFlowOf {
            val order = orderService.getOrder(orderId)
            val tokenAccounts = dataService.fetchTokenAccounts(tokenOwner, promoOwner).map { t ->
                val attributesMap =
                    JsonParser().parse(Gson().toJson(t.mintObject?.promoObject?.metadataObject?.attributes)).asJsonArray.associate { a ->
                        a.asJsonObject["trait_type"].asString to a.asJsonObject["value"].asString
                    }
                TokenAccountWithMetadata(
                    tokenAccount = t,
                    name = t.mintObject?.promoObject?.metadataObject?.name.toString(),
                    image = t.mintObject?.promoObject?.metadataObject?.image.toString(),
                    description = t.mintObject?.promoObject?.metadataObject?.description.toString(),
                    attributes = attributesMap
                )
            }
            Log.d("TokenAccounts", tokenAccounts.toString())

            val eligibleBuyXCurrencyPromos = tokenAccounts.filter {
                try {
                    it.attributes["promoType"] == "buyXCurrencyGetYPercent" && it.attributes["buyXCurrency"]?.toLong()!! <= order.total
                } catch (_: Exception) {
                    false
                }
            }
            eligibleBuyXCurrencyPromos.map { it ->
                it.discount = it.attributes["getYPercent"]?.toLong()!! * order.total / 100; it
            }
        }

    override val promoSubscriptionFlow = dataService.promoSubscription.toFlow().flowOn(
        Dispatchers.IO
    )
    override val delegateTokenSubscription =
        dataService.getDelegateTokenSubscription().toFlow().flowOn(Dispatchers.IO)

    override fun fetchAppId() =
        resourceFlowOf { transactionService.service.getAppId() }

    override fun createPromo(promo: PromoType, uri: Uri, memo: String?, payer: String, groupSeed: String) = resourceFlowOf {
        val metadata = promo.toJson()
        Log.d("createPromo", metadata)

        val metadataPart =
            MultipartBody.Part.createFormData("metadata", metadata)

        val stream = context.contentResolver.openInputStream(uri)
        val contentType = context.contentResolver.getType(uri) ?: "image/*"
        val contentInfo = context.contentResolver.getFileName(uri)

        val imagePart = MultipartBody.Part.createFormData(
            "image",
            contentInfo!!.first,
            stream!!.readBytes()
                .toRequestBody(contentType.toMediaType(), 0, contentInfo!!.second)
        )

        val response =
            transactionService.service.createPromo(metadataPart, imagePart, payer, groupSeed, memo)
        response
    }

    override fun signAndSend(transaction: String, keyPair: KeyPair) = resourceFlowOf {
        val localTransaction = localTransactions.deserializeTransaction(transaction)
        solanaApi.sendTransaction(localTransactions.sign(localTransaction, keyPair))
    }
}

fun ContentResolver.getFileName(uri: Uri): Pair<String?, Int>? {
    return this.query(uri, null, null, null, null)?.use { cursor ->
        if (!cursor.moveToFirst()) return@use null
        val name = cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME)
        val size = cursor.getColumnIndex(OpenableColumns.SIZE)

        cursor.getString(name) to cursor.getInt(size)
    }
}