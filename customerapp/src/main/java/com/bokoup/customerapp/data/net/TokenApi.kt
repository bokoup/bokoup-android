package com.bokoup.customerapp.data.net

import android.util.Log
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

class TokenApi {

    private val interceptor = HttpLoggingInterceptor().apply {
        level = HttpLoggingInterceptor.Level.BODY
    }
    private val retrofit = Retrofit.Builder()
        .baseUrl("http://99.91.8.130:8080/")
        .client(OkHttpClient.Builder().apply { addInterceptor(interceptor = interceptor) }.build())
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    val baseUrl = retrofit.baseUrl().toUrl()

    private val tokenService = retrofit.create(TokenService::class.java)

    suspend fun getApiId(
        action: String,
        mintString: String,
        message: String,
        memo: String?,
    ): TokenApiId =
        withContext(Dispatchers.Default) {
            val result = tokenService.get(action, mintString, message, memo)
            result
        }

    suspend fun getTokenTransaction(
        action: String,
        mintString: String,
        message: String,
        memo: String?,
        account: String
    ): TokenApiResponse = withContext(Dispatchers.Default) {
        val accountData = AccountData(account = account)
        var result = TokenApiResponse(transaction = "j", message = "k")
        try {
            result = tokenService.post(accountData, action, mintString, message, memo)
        } catch (e: Throwable) {
            Log.d("jingus", e.toString())

        }
        result
    }
}

interface TokenService {
    @GET("promo/{action}/{mintString}/{message}/{memo}")
    suspend fun get(
        @Path("action") action: String,
        @Path("mintString") mintString: String,
        @Path("message") message: String,
        @Path("memo") memo: String?
    ): TokenApiId

    @POST("promo/{action}/{mintString}/{message}/{memo}")
    suspend fun post(
        @Body accountData: AccountData,
        @Path("action") action: String,
        @Path("mintString") mintString: String,
        @Path("message") message: String,
        @Path("memo") memo: String?
    ): TokenApiResponse
}

data class TokenApiId(val label: String, val icon: String)

data class TokenApiResponse(val transaction: String, val message: String)

data class AccountData(val account: String)
