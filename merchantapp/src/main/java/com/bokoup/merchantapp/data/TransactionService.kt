package com.bokoup.merchantapp.data

import com.bokoup.merchantapp.model.AppId
import okhttp3.OkHttpClient
import okhttp3.RequestBody
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Multipart
import retrofit2.http.PUT
import retrofit2.http.Part


class TransactionService {
    private val interceptor = HttpLoggingInterceptor().apply {
        level = HttpLoggingInterceptor.Level.BODY
    }

    private val retrofit: Retrofit = Retrofit.Builder().baseUrl("https://tx.api.bokoup.xyz")
        .client(OkHttpClient.Builder().apply {addInterceptor(interceptor = interceptor)}.build())
        .addConverterFactory(GsonConverterFactory.create())
        .build()
    val service: PromoService = retrofit.create(PromoService::class.java)
}

public interface PromoService {
    @Multipart
    @PUT("promo/create")
    fun createPromo(
        @Part("image") photo: RequestBody,
        @Part("metadata") description: RequestBody
    ): String

    @GET("promo/mint/mintString/message")
    suspend fun mintPromoToken(): AppId
}
