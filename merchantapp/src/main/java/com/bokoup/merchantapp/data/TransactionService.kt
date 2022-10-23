package com.bokoup.merchantapp.data

import com.bokoup.merchantapp.model.AppId
import com.bokoup.merchantapp.model.CreatePromoResult
import okhttp3.MultipartBody
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.*


class TransactionService {
    private val interceptor = HttpLoggingInterceptor().apply {
        level = HttpLoggingInterceptor.Level.HEADERS
    }

    private val retrofit: Retrofit = Retrofit.Builder().baseUrl("http://99.91.8.130:8080")
        .client(OkHttpClient.Builder().apply {addInterceptor(interceptor = interceptor)}.build())
        .addConverterFactory(GsonConverterFactory.create())
        .build()
    val service: PromoService = retrofit.create(PromoService::class.java)
}

public interface PromoService {
    @Multipart
    @Streaming
    @POST("promo/create")
    suspend fun createPromo(
        @Part metadata: MultipartBody.Part,
        @Part image: MultipartBody.Part,
        @Part memo: MultipartBody.Part?
    ): CreatePromoResult

    @GET("promo/mint/mintString/message")
    suspend fun mintPromoToken(): AppId
}
