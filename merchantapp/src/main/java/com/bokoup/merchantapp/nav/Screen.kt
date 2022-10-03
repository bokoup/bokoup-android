package com.bokoup.merchantapp.nav

enum class Screen(val title: String) {
    Customer(title = "Customer"),
    Merchant(title = "Merchant"),
    Tender(title = "Tenders");

    var route: String = name
}

