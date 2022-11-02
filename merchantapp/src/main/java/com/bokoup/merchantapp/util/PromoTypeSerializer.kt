package com.bokoup.merchantapp.util

import com.bokoup.merchantapp.model.BasePromo
import com.bokoup.merchantapp.model.PromoType
import com.google.gson.*
import java.lang.reflect.Type

class PromoTypeSerializer : JsonSerializer<PromoType> {
    override fun serialize(
        src: PromoType?,
        typeOfSrc: Type?,
        context: JsonSerializationContext?
    ): JsonElement {
        val metadata = JsonObject()
        val attributes = JsonArray()
        when (src) {
            is BasePromo -> {
                metadata.addProperty("name", src.name)
                metadata.addProperty("symbol", src.symbol)
                metadata.addProperty("description", src.description)
                metadata.add("attributes", attributes)

                val metadataAttributes = metadata.get("attributes").asJsonArray
                metadataAttributes.addAttribute(
                    "promoType",
                    src.javaClass.name.replaceFirstChar { it.lowercase() })

                when (src) {
                    is PromoType.BuyXCurrencyGetYPercent -> {
                        metadataAttributes.addAttribute(
                            "buyXCurrency",
                            src.buyXCurrency
                        )
                        metadataAttributes.addAttribute(
                            "getYPercent",
                            src.getYPercent
                        )
                    }
                    is PromoType.BuyXProductGetYFree -> {
                        metadataAttributes.addAttribute(
                            "productId",
                            src.productId
                        )
                        metadataAttributes.addAttribute(
                            "buyXProduct",
                            src.buyXProduct
                        )
                        metadataAttributes.addAttribute(
                            "getYProduct",
                            src.getYProduct
                        )
                    }
                }

                if (src.maxMint != null) {
                    metadataAttributes.addAttribute("maxMint", src.maxMint!!)
                }
                if (src.maxBurn != null) {
                    metadataAttributes.addAttribute("maxBurn", src.maxBurn!!)
                }
                if (src.collectionName.isNotBlank() || src.collectionFamily.isNotBlank()) {
                    val collectionObj = JsonObject()
                    if (src.collectionName.isNotBlank()) {
                        collectionObj.addProperty("name", src.collectionName)
                    }
                    if (src.collectionFamily.isNotBlank()) {
                        collectionObj.addProperty("family", src.collectionFamily)
                    }
                    metadata.add("collection", collectionObj)
                }
            }
            else -> {
                throw Exception("Promo type does not implement BasePromo")
            }
        }
        return metadata
    }
}


fun JsonArray.addAttribute(key: String, value: String) {
    val attribute = JsonObject()
    attribute.addProperty(
        "trait_type",
        key
    )
    attribute.addProperty(
        "value",
        value
    )
    this.add(attribute)
}

fun JsonArray.addAttribute(key: String, value: Int) {
    val attribute = JsonObject()
    attribute.addProperty(
        "trait_type",
        key
    )
    attribute.addProperty(
        "value",
        value
    )
    this.add(attribute)
}
