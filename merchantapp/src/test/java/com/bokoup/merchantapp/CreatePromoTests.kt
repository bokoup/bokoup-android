package com.bokoup.merchantapp

import android.net.Uri
import android.util.Log
import com.bokoup.merchantapp.model.PromoType
import com.bokoup.merchantapp.util.PromoTypeSerializer
import com.bokoup.merchantapp.util.addAttribute
import com.google.gson.GsonBuilder
import com.google.gson.JsonArray
import com.google.gson.JsonObject
import com.google.gson.JsonParser
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
@RunWith(RobolectricTestRunner::class)
class CreatePromoTests {
    @Test
    fun promo_buyXCurrencyGetYPercents_serializes_correctly() {
        val promo = PromoType.BuyXCurrencyGetYPercent(
            uri = Uri.parse("test"),
            name = "name",
            symbol = "symbol",
            description = "description",
            collectionName = "collectionName",
            collectionFamily = "collectionFamily",
            maxMint = 10,
            maxBurn = 5,
            memo = "memo",
            buyXCurrency = 100,
            getYPercent = 10
        )

        val gson = GsonBuilder().apply {
            registerTypeAdapter(PromoType::class.java, PromoTypeSerializer())
        }.create()

        val metadataJson = gson.toJson(promo, PromoType::class.java)
        Log.d("CreatePromoTests", metadataJson)

        val metadata = JsonParser().parse(metadataJson).asJsonObject

        listOf("name", "symbol", "description").forEach {
            assert(metadata[it].asString == it)
        }

        val attributes = JsonArray()
        attributes.addAttribute("promoType", promo.javaClass.simpleName.replaceFirstChar { it.lowercase() })
        attributes.addAttribute("buyXCurrency", promo.buyXCurrency)
        attributes.addAttribute("getYPercent", promo.getYPercent)
        attributes.addAttribute("maxMint", promo.maxMint!!)
        attributes.addAttribute("maxBurn", promo.maxBurn!!)

        assert(attributes == metadata["attributes"].asJsonArray) { "attributes arrays not equal" }

        val collectionObject = JsonObject()
        collectionObject.addProperty("name", promo.collectionName)
        collectionObject.addProperty("family", promo.collectionFamily)

        assert(collectionObject == metadata["collection"].asJsonObject) { "collection objects are not equal" }

    }

    @Test
    fun promo_buyXProductGetYFree_serializes_correctly() {
        val promo = PromoType.BuyXProductGetYFree(
            uri = Uri.parse("test"),
            name = "name",
            symbol = "symbol",
            description = "description",
            collectionName = "collectionName",
            collectionFamily = "collectionFamily",
            maxMint = 10,
            maxBurn = 5,
            memo = "memo",
            productId = "productId",
            buyXProduct = 10,
            getYProduct = 1
        )

        val gson = GsonBuilder().apply {
            registerTypeAdapter(PromoType::class.java, PromoTypeSerializer())
        }.create()

        val metadataJson = gson.toJson(promo, PromoType::class.java)
        Log.d("CreatePromoTests", metadataJson)

        val metadata = JsonParser().parse(metadataJson).asJsonObject

        listOf("name", "symbol", "description").forEach {
            assert(metadata[it].asString == it)
        }

        val attributes = JsonArray()
        attributes.addAttribute("promoType", promo.javaClass.simpleName.replaceFirstChar { it.lowercase() })
        attributes.addAttribute("productId", promo.productId)
        attributes.addAttribute("buyXProduct", promo.buyXProduct)
        attributes.addAttribute("getYProduct", promo.getYProduct)
        attributes.addAttribute("maxMint", promo.maxMint!!)
        attributes.addAttribute("maxBurn", promo.maxBurn!!)

        assert(attributes == metadata["attributes"].asJsonArray) { "attributes arrays not equal" }

        val collectionObject = JsonObject()
        collectionObject.addProperty("name", promo.collectionName)
        collectionObject.addProperty("family", promo.collectionFamily)

        assert(collectionObject == metadata["collection"].asJsonObject) { "collection objects are not equal" }
    }
}
