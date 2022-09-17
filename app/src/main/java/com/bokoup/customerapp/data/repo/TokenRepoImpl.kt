package com.bokoup.customerapp.data.repo

import com.bokoup.customerapp.data.net.TokenDao
import com.bokoup.customerapp.dom.repo.TokenRepo

class TokenRepoImpl(
    private val tokenDao: TokenDao
) : TokenRepo {
    override fun getTokensFromRoom() = tokenDao.getTokens()
}