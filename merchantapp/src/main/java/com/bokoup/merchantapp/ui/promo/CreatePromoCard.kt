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
import com.bokoup.merchantapp.model.CreatePromoArgs
import com.bokoup.merchantapp.model.PromoType
import com.google.accompanist.placeholder.PlaceholderHighlight
import com.google.accompanist.placeholder.material.placeholder
import com.google.accompanist.placeholder.material.shimmer

@Composable
@ExperimentalMaterial3Api
fun CreatePromoCard(
    setCardState: (Boolean) -> Unit,
    createPromo: (createPromoArgs: CreatePromoArgs) -> Unit,
) {


    var name by remember {
        mutableStateOf(TextFieldValue())
    }

    var symbol by remember {
        mutableStateOf(TextFieldValue())
    }

    var description by remember {
        mutableStateOf(TextFieldValue())
    }

    var maxMint by remember {
        mutableStateOf(TextFieldValue())
    }

    var maxBurn by remember {
        mutableStateOf(TextFieldValue())
    }

    var buyXCurrency by remember {
        mutableStateOf(TextFieldValue())
    }

    var getYPercent by remember {
        mutableStateOf(TextFieldValue())
    }

    var productId by remember {
        mutableStateOf(TextFieldValue())
    }

    var buyXProduct by remember {
        mutableStateOf(TextFieldValue())
    }

    var getYProduct by remember {
        mutableStateOf(TextFieldValue())
    }

    var memo by remember {
        mutableStateOf(TextFieldValue())
    }

    var pickedImageUri by remember { mutableStateOf<Uri?>(null) }
    val launcher =
        rememberLauncherForActivityResult(ActivityResultContracts.StartActivityForResult()) {
            println("selected file URI ${it.data?.data}")
            pickedImageUri = it.data?.data
        }

    var promoTypeMap by remember {
        mutableStateOf(
            PromoType.values().associateWith { false })
    }

    fun updateCheckedState(promoType: PromoType) {
        promoTypeMap = promoTypeMap.entries.associate { entry ->

            val selected = if (entry.key != promoType) {
                false
            } else {
                !entry.value
            }

//            var newEntry = entry.value
//            newEntry = selected

            entry.key to selected
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
                        onClick = { setCardState(false) },
                        modifier = Modifier.padding(0.dp)
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
                            Button(
                                onClick = {
                                    val intent = Intent(
                                        Intent.ACTION_OPEN_DOCUMENT,
                                        MediaStore.Images.Media.EXTERNAL_CONTENT_URI
                                    )
                                        .apply {
                                            addCategory(Intent.CATEGORY_OPENABLE)
                                        }
                                    launcher.launch(intent)
                                }
                            ) {
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
                        TextField(
                            value = name,
                            onValueChange = { text -> name = text },
                            label = { Text("Name") },
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 4.dp)
                        )
                        TextField(
                            value = symbol,
                            onValueChange = { text -> symbol = text },
                            label = { Text("Symbol") },
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 4.dp)
                        )
                        TextField(
                            value = description,
                            onValueChange = { text -> description = text },
                            label = { Text("Description") },
                            maxLines = 3,
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(120.dp)
                                .padding(vertical = 4.dp)
                        )
                        TextField(
                            value = memo,
                            onValueChange = { text -> memo = text },
                            label = { Text("Memo") },
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 4.dp)
                        )
                    }
                    Column(modifier = Modifier.weight(0.5f)) {
                        TextField(
                            value = maxMint,
                            onValueChange = { text -> maxMint = text },
                            label = { Text("Max issue") },
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 4.dp),
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
                        )
                        TextField(
                            value = maxBurn,
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
                            promoTypeMap.entries.map { promoType ->
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    modifier = Modifier.padding(horizontal = 12.dp)
                                ) {
                                    Text(promoType.key.title, modifier = Modifier.weight(0.8f))
                                    Checkbox(
                                        checked = promoType.value,
                                        onCheckedChange = { updateCheckedState(promoType.key) },
                                        modifier = Modifier.weight(0.2f)
                                    )
                                }
                                if (promoType.value) {
                                    promoType.key.attributes.map { attribute ->
                                        val keyboardOptions = if (attribute.number) {
                                            KeyboardOptions(keyboardType = KeyboardType.Number)
                                        } else {
                                            KeyboardOptions(keyboardType = KeyboardType.Ascii)
                                        }
                                        TextField(
                                            value = attribute.value,
                                            onValueChange = { text -> attribute.value = text },
                                            label = { Text(attribute.trait_type) },
                                            modifier = Modifier
                                                .fillMaxWidth()
                                                .padding(vertical = 4.dp),
                                            keyboardOptions = keyboardOptions
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
                        .padding(4.dp), horizontalArrangement = Arrangement.Center
                ) {
                    Button(
                        enabled = pickedImageUri != null && name.text.isNotBlank() && symbol.text.isNotBlank() && description.text.isNotBlank(),
                        onClick = {

                            createPromo(
                                CreatePromoArgs(
                                    pickedImageUri!!,
                                    name.text,
                                    symbol.text,
                                    description.text,
                                    maxMint.text,
                                    maxBurn.text,
                                    "bokoup collection",
                                    "bokoup family",
                                    memo.text,
                                    promoTypeMap.entries.first{ it -> it.value}.key
                                )
                            )
                            setCardState(false)
                        }) {
                        Text("Create Promo")
                    }
                }
            }

        }
    }
}
