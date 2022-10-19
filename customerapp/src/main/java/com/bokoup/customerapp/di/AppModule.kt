package com.bokoup.customerapp.di

import android.content.Context
import androidx.room.Room
import com.bokoup.customerapp.data.net.AddressDao
import com.bokoup.customerapp.data.net.ChainDb
import com.bokoup.customerapp.data.net.TokenApi
import com.bokoup.customerapp.data.net.TokenDao
import com.bokoup.customerapp.data.repo.AddressRepoImpl
import com.bokoup.customerapp.data.repo.SolanaRepoImpl
import com.bokoup.customerapp.data.repo.TokenRepoImpl
import com.bokoup.customerapp.dom.repo.AddressRepo
import com.bokoup.customerapp.dom.repo.SolanaRepo
import com.bokoup.customerapp.dom.repo.TokenRepo
import com.bokoup.customerapp.util.QRCodeScanner
import com.bokoup.lib.QRCodeGenerator
import com.bokoup.lib.SystemClipboard
import com.dgsd.ksol.LocalTransactions
import com.dgsd.ksol.SolanaApi
import com.dgsd.ksol.model.Cluster
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor

@Module
@InstallIn(SingletonComponent::class)
class AppModule() {
    @Provides
    fun chainDb(
        @ApplicationContext
        context: Context
    ) = Room.databaseBuilder(
        context,
        ChainDb::class.java,
        "token"
    ).build()

    @Provides
    fun tokenDao(
        chainDb: ChainDb,
    ) = chainDb.tokenDao()

    @Provides
    fun tokenApi(
    ) = TokenApi()

    @Provides
    fun solanaApi(
    ) = SolanaApi(Cluster.DEVNET, OkHttpClient.Builder().apply {
        addInterceptor(interceptor = HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        })
    }.build())

    @Provides
    fun localTransactions(
    ) = LocalTransactions

    @Provides
    fun tokenRepo(
        tokenDao: TokenDao,
        tokenApi: TokenApi,
    ): TokenRepo = TokenRepoImpl(
        tokenDao = tokenDao,
        tokenApi = tokenApi,
    )

    @Provides
    fun solanaRepo(
        solanaApi: SolanaApi,
        localTransactions: LocalTransactions,
    ): SolanaRepo = SolanaRepoImpl(
        solanaApi = solanaApi,
        localTransactions = localTransactions
    )

    @Provides
    fun addressDao(
        chainDb: ChainDb,
    ) = chainDb.addressDao()

    @Provides
    fun addressRepo(
        addressDao: AddressDao,
        solanaRepo: SolanaRepo
    ): AddressRepo = AddressRepoImpl(
        addressDao = addressDao,
        solanaRepo = solanaRepo
    )

    @Provides
    fun qRCodeGenerator(
    ) = QRCodeGenerator

    @Provides
    fun qRCodeScanner(
    ) = QRCodeScanner

    @Provides
    fun systemClipboard(
        @ApplicationContext
        context: Context
    ) = SystemClipboard(context)
}