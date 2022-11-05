package com.bokoup.merchantapp.ui.promo

import android.content.Intent
import android.net.Uri
import android.provider.MediaStore
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.bokoup.merchantapp.model.PromoType
import com.google.accompanist.placeholder.PlaceholderHighlight
import com.google.accompanist.placeholder.material.placeholder
import com.google.accompanist.placeholder.material.shimmer

@Composable
@ExperimentalMaterial3Api
fun CreatePromoCard(
    setCardState: (Boolean) -> Unit,
    createPromo: (promo: PromoType, uri: Uri, memo: String?, payer: String, groupSeed: String) -> Unit,
) {
    var name by remember {
        mutableStateOf(TextFieldValue("Test Promo"))
    }

    var symbol by remember {
        mutableStateOf(TextFieldValue("BTP"))
    }

    var description by remember {
        mutableStateOf(TextFieldValue("This test promo gives you 10 percent off if you spend more than $1.00."))
    }

    var maxMint by remember {
        mutableStateOf(TextFieldValue("10"))
    }

    var maxBurn by remember {
        mutableStateOf(TextFieldValue("5"))
    }

    var buyXCurrency by remember {
        mutableStateOf(TextFieldValue("100"))
    }

    var getYPercent by remember {
        mutableStateOf(TextFieldValue("10"))
    }

    var productId by remember {
        mutableStateOf(TextFieldValue("productId"))
    }

    var buyXProduct by remember {
        mutableStateOf(TextFieldValue("3"))
    }

    var getYProduct by remember {
        mutableStateOf(TextFieldValue("1"))
    }

    var memo by remember {
        mutableStateOf(TextFieldValue("Created test promo for demo."))
    }

    var pickedImageUri by remember { mutableStateOf<Uri?>(null) }
    val launcher =
        rememberLauncherForActivityResult(ActivityResultContracts.StartActivityForResult()) {
            println("selected file URI ${it.data?.data}")
            pickedImageUri = it.data?.data
        }

    var promoTypes by remember {
        mutableStateOf(PromoType::class.sealedSubclasses.map { it -> it.java.simpleName }
            .associateWith { false })
    }

    var selectedPromoType by remember { mutableStateOf("") }

    fun updateCheckedState(promoType: String) {
        selectedPromoType = if (promoType == selectedPromoType) {
            ""
        } else {
            promoType
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Card {
            Column(
                Modifier
                    .padding(vertical = 4.dp, horizontal = 16.dp)
                    .width(800.dp)
            ) {
                Row(
                    Modifier
                        .fillMaxWidth()
                        .padding(4.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.Bottom
                ) {
                    Text(
                        "Create Promo",
                        style = MaterialTheme.typography.headlineSmall,
                        modifier = Modifier.padding(bottom = 8.dp)
                    )
                    IconButton(
                        onClick = { setCardState(false) }, modifier = Modifier.padding(0.dp)
                    ) {
                        Icon(
                            Icons.Filled.Close,
                            "Close",

                            )
                    }
                }
                Row() {
                    Column(modifier = Modifier.weight(0.5f)) {
                        Row(
                            Modifier
                                .fillMaxWidth()
                                .padding(vertical = 4.dp)
                                .height(200.dp),
                            horizontalArrangement = Arrangement.Center,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Button(onClick = {
                                val intent = Intent(
                                    Intent.ACTION_OPEN_DOCUMENT,
                                    MediaStore.Images.Media.EXTERNAL_CONTENT_URI
                                ).apply {
                                        addCategory(Intent.CATEGORY_OPENABLE)
                                    }
                                launcher.launch(intent)
                            }) {
                                Text("Select Image")
                            }
                            AsyncImage(
                                model = pickedImageUri,
                                modifier = Modifier
                                    .padding(16.dp)
                                    .placeholder(
                                        visible = pickedImageUri == null,
                                        highlight = PlaceholderHighlight.shimmer()
                                    ),
                                contentDescription = null
                            )

                        }
                        TextField(value = name,
                            onValueChange = { text -> name = text },
                            label = { Text("Name") },
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 4.dp)
                        )
                        TextField(value = symbol,
                            onValueChange = { text -> symbol = text },
                            label = { Text("Symbol") },
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 4.dp)
                        )
                        TextField(value = description,
                            onValueChange = { text -> description = text },
                            label = { Text("Description") },
                            maxLines = 3,
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(120.dp)
                                .padding(vertical = 4.dp)
                        )
                        TextField(value = memo,
                            onValueChange = { text -> memo = text },
                            label = { Text("Memo") },
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 4.dp)
                        )
                    }
                    Column(modifier = Modifier.weight(0.5f)) {
                        TextField(value = maxMint,
                            onValueChange = { text -> maxMint = text },
                            label = { Text("Max issue") },
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 4.dp),
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
                        )
                        TextField(value = maxBurn,
                            onValueChange = { text -> maxBurn = text },
                            label = { Text("Max redeem") },
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 4.dp),
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
                        )
                        Column(modifier = Modifier.padding(PaddingValues(start = 16.dp))) {
                            Text(
                                "Type", fontSize = 16.sp, modifier = Modifier.padding(
                                    PaddingValues(top = 18.dp)
                                )
                            )
                            PromoType::class.sealedSubclasses.map { it -> it.java.simpleName }
                                .map { promoType ->
                                    Row(
                                        verticalAlignment = Alignment.CenterVertically,
                                        horizontalArrangement = Arrangement.SpaceBetween,
                                        modifier = Modifier.padding(horizontal = 12.dp)
                                    ) {
                                        Text(promoType, modifier = Modifier.weight(0.8f))
                                        Checkbox(
                                            checked = promoType == selectedPromoType,
                                            onCheckedChange = { updateCheckedState(promoType) },
                                            modifier = Modifier.weight(0.2f)
                                        )
                                    }

                                }
                            if (selectedPromoType.isNotBlank()) {
                                when (selectedPromoType) {
                                    "BuyXCurrencyGetYPercent" -> {
                                        TextField(
                                            value = buyXCurrency,
                                            onValueChange = { text -> buyXCurrency = text },
                                            label = { Text("buyXCurrency") },
                                            modifier = Modifier
                                                .fillMaxWidth()
                                                .padding(vertical = 4.dp),
                                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
                                        )
                                        TextField(
                                            value = getYPercent,
                                            onValueChange = { text -> getYPercent = text },
                                            label = { Text("getYPercent") },
                                            modifier = Modifier
                                                .fillMaxWidth()
                                                .padding(vertical = 4.dp),
                                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
                                        )
                                    }
                                    "BuyXProductGetYFree" -> {
                                        TextField(
                                            value = productId,
                                            onValueChange = { text -> productId = text },
                                            label = { Text("productId") },
                                            modifier = Modifier
                                                .fillMaxWidth()
                                                .padding(vertical = 4.dp),
                                        )
                                        TextField(
                                            value = buyXProduct,
                                            onValueChange = { text -> buyXProduct = text },
                                            label = { Text("buyXProduct") },
                                            modifier = Modifier
                                                .fillMaxWidth()
                                                .padding(vertical = 4.dp),
                                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
                                        )
                                        TextField(
                                            value = getYProduct,
                                            onValueChange = { text -> getYProduct = text },
                                            label = { Text("getYProduct") },
                                            modifier = Modifier
                                                .fillMaxWidth()
                                                .padding(vertical = 4.dp),
                                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
                Row(
                    Modifier
                        .fillMaxWidth()
                        .padding(4.dp),
                    horizontalArrangement = Arrangement.Center
                ) {
                    Button(enabled = (pickedImageUri != null) && name.text.isNotBlank() && symbol.text.isNotBlank() && description.text.isNotBlank(),
                        onClick = {
                            val promo = when (selectedPromoType) {
                                "BuyXCurrencyGetYPercent" -> {
                                    PromoType.BuyXCurrencyGetYPercent(
                                        name = name.text,
                                        symbol = symbol.text,
                                        description = description.text,
                                        collectionName = "bokoup test store",
                                        collectionFamily = "bokoup test",
                                        maxMint = maxMint.text.toIntOrNull(),
                                        maxBurn = maxBurn.text.toIntOrNull(),
                                        buyXCurrency = buyXCurrency.text.toInt(),
                                        getYPercent = getYPercent.text.toInt()
                                    )
                                }
                                "BuyXProductGetYFree" -> {
                                    PromoType.BuyXProductGetYFree(
                                        name = name.text,
                                        symbol = symbol.text,
                                        description = description.text,
                                        collectionName = "bokoup test store",
                                        collectionFamily = "bokoup test",
                                        maxMint = maxMint.text.toIntOrNull(),
                                        maxBurn = maxBurn.text.toIntOrNull(),
                                        productId = productId.text,
                                        buyXProduct = buyXProduct.text.toInt(),
                                        getYProduct = getYProduct.text.toInt()
                                    )
                                }
                                else -> {
                                    throw Exception("Selected promo type does not exist")
                                }
                            }
                            createPromo(promo, pickedImageUri!!, memo.text, "_payer", "_gropSeed")
                            setCardState(false)
                        }) {
                        Text("Create Promo")
                    }
                }
            }

        }
    }
}

