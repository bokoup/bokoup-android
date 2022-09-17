package com.bokoup.customerapp.nav

import androidx.annotation.StringRes
import com.bokoup.customerapp.R


enum class Screen(@StringRes val title: Int) {
    Tokens(title = R.string.tokens),
    TokenDetail(title = R.string.token_details),
    Settings(title = R.string.settings)
}

