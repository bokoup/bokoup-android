package com.bokoup.merchantapp.net

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
    @GET("promo/create")
    suspend fun getAppId(
    ): AppId

    @Multipart
    @Streaming
    @POST("promo/create/{payer}/{groupSeed}/{memo}")
    suspend fun createPromo(
        @Part metadata: MultipartBody.Part,
        @Part image: MultipartBody.Part,
        @Path("payer") payer: String,
        @Path("groupSeed") groupSeed: String,
        @Path("memo") memo: String?
    ): CreatePromoResult

    @POST("promo/{action}/{mintString}/{message}/{memo}")
    suspend fun post(
        @Body accountData: AccountData,
        @Path("action") action: String,
        @Path("mintString") mintString: String,
        @Path("message") message: String,
        @Path("memo") memo: String?
    ): TokenApiResponse
}
data class TokenApiResponse(val transaction: String, val message: String)
data class AccountData(val account: String)

