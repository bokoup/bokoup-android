package com.bokoup.customerapp.data.net

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Url

class TransactionService {

    private val interceptor = HttpLoggingInterceptor().apply {
        level = HttpLoggingInterceptor.Level.BODY
    }
    private val retrofit = Retrofit.Builder()
        .baseUrl("https://tx.api.bokoup.dev/")
        .client(OkHttpClient.Builder().apply { addInterceptor(interceptor = interceptor) }.build())
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    val baseUrl = retrofit.baseUrl().toUrl()

    private val tokenService = retrofit.create(TokenService::class.java)

    suspend fun getApiId(
        url: String,
    ): TokenApiId =
        withContext(Dispatchers.Default) {
            val result = tokenService.get(url)
            result
        }

    suspend fun getTokenTransaction(
        url: String,
        account: String
    ): TokenApiResponse = withContext(Dispatchers.Default) {
        val accountData = AccountData(account = account)
         tokenService.post(url, accountData)
    }
}

interface TokenService {
    @GET
    suspend fun get(
        @Url url: String,
    ): TokenApiId

    @POST
    suspend fun post(
        @Url url: String,
        @Body accountData: AccountData,
    ): TokenApiResponse
}

data class TokenApiId(val label: String, val icon: String)

data class TokenApiResponse(val transaction: String, val message: String)

data class AccountData(val account: String)
