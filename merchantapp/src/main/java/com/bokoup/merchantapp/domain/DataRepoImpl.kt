package com.bokoup.merchantapp.domain

import android.content.ContentResolver
import android.content.Context
import android.net.Uri
import android.provider.OpenableColumns
import android.util.Log
import com.bokoup.lib.resourceFlowOf
import com.bokoup.merchantapp.model.CreatePromoArgs
import com.bokoup.merchantapp.model.PromoWithMetadata
import com.bokoup.merchantapp.model.TokenAccountWithMetadata
import com.bokoup.merchantapp.net.DataService
import com.bokoup.merchantapp.net.OrderService
import com.bokoup.merchantapp.net.TransactionService
import com.google.gson.Gson
import com.google.gson.JsonArray
import com.google.gson.JsonObject
import com.google.gson.JsonParser
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.flowOn
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.toRequestBody

class DataRepoImpl(
    val context: Context,
    private val dataService: DataService,
    private val transactionService: TransactionService,
    private val orderService: OrderService
) : DataRepo {
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
            eligibleBuyXCurrencyPromos.map{it -> it.discount = it.attributes["getYPercent"]?.toLong()!! * order.total / 100; it}
        }

    override val promoSubscriptionFlow = dataService.promoSubscription.toFlow().flowOn(
        Dispatchers.IO
    )
    override val delegateTokenSubscription =
        dataService.getDelegateTokenSubscription().toFlow().flowOn(Dispatchers.IO)

    override fun fetchAppId() =
        resourceFlowOf { transactionService.service.getAppId() }

    override fun createPromo(createPromoArgs: CreatePromoArgs) = resourceFlowOf {

        val (
            uri, name, symbol, description, maxMint, maxBurn, collection, family, memo) = createPromoArgs

        // metadata part
        val metadata = JsonObject()
        metadata.addProperty("name", name)
        metadata.addProperty("symbol", symbol)
        metadata.addProperty("description", description)

        val attributes = JsonArray()
        try {
            val attribute = JsonObject()
            attribute.addProperty("maxMint", maxMint.toInt())
            attributes.add(attribute)
        } catch (e: Exception) {
            Unit
        }

        try {
            val attribute = JsonObject()
            attribute.addProperty("maxBurn", maxBurn.toInt())
            attributes.add(attribute)
        } catch (e: Exception) {
            Unit
        }
        if (attributes.size() > 0) {
            metadata.add("attributes", attributes)
        }

        if (collection.isNotBlank() || family.isNotBlank()) {
            val collectionObj = JsonObject()
            if (collection.isNotBlank()) {
                collectionObj.addProperty("name", collection)
            }
            if (family.isNotBlank()) {
                collectionObj.addProperty("family", family)
            }
            metadata.add("collection", collectionObj)
        }
        val metadataPart =
            MultipartBody.Part.createFormData("metadata", metadata.toString())
        Log.d("jingus", metadata.toString())

        val stream = context.contentResolver.openInputStream(uri)
        val contentType = context.contentResolver.getType(uri) ?: "image/*"
        val contentInfo = context.contentResolver.getFileName(uri)

        val imagePart = MultipartBody.Part.createFormData(
            "image",
            contentInfo!!.first,
            stream!!.readBytes()
                .toRequestBody(contentType.toMediaType(), 0, contentInfo!!.second)
        )

        // memo part
        val memoPart = memo?.let {
            MultipartBody.Part.createFormData("memo", it)
        } ?: null

        val response =
            transactionService.service.createPromo(metadataPart, imagePart, memoPart)
        Log.d("jingus", response.toString())
        response
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