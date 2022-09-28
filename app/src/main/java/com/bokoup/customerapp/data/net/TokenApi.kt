package com.bokoup.customerapp.data.net

import android.util.Log
import com.dgsd.ksol.model.LocalTransaction
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
        .baseUrl("https://demo-api-6jgxmo2doq-uw.a.run.app/")
        .client(OkHttpClient.Builder().apply {addInterceptor(interceptor = interceptor)}.build())
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    private val tokenService = retrofit.create(TokenService::class.java)

    suspend fun getApiId(mintString: String, promoName: String): TokenApiId = withContext(Dispatchers.Default) {
        val result = tokenService.get(mintString, promoName)
        result
    }

    suspend fun getTokenTransaction(mintString: String, promoName: String, account: String): TokenApiResponse = withContext(Dispatchers.Default) {
        val accountData = AccountData(account = account)
        var result = TokenApiResponse(transaction = "jingus", message = "malingus")
        try {
            result = tokenService.post(accountData, mintString, promoName)
        } catch(e: Throwable) {
            Log.d("jingus", e.toString())

        }
        result
    }
}

interface TokenService {
    @GET("promo/{mintString}/{promoName}")
    suspend fun get(@Path("mintString") mintString: String, @Path("promoName") promoName: String) : TokenApiId

    @POST("promo/{mintString}/{promoName}")
    suspend fun post(@Body accountData: AccountData, @Path("mintString") mintString: String, @Path("promoName") promoName: String) : TokenApiResponse
}

data class TokenApiId(val label: String, val icon: String)

data class TokenApiResponse(val transaction: String, val message: String)

data class AccountData(val account: String)
