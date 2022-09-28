package com.bokoup.customerapp.di

import android.content.Context
import androidx.room.Room
import com.bokoup.customerapp.data.net.*
import com.bokoup.customerapp.data.repo.AddressRepoImpl
import com.bokoup.customerapp.data.repo.TokenRepoImpl
import com.bokoup.customerapp.dom.repo.AddressRepo
import com.bokoup.customerapp.dom.repo.TokenRepo
import com.bokoup.customerapp.util.QRCodeGenerator
import com.bokoup.customerapp.util.QRCodeScanner
import com.bokoup.customerapp.util.SystemClipboard
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
class AppModule {
    @Provides
    fun provideChainDb(
        @ApplicationContext
        context : Context
    ) = Room.databaseBuilder(
        context,
        ChainDb::class.java,
        "token"
    ).build()

    @Provides
    fun provideTokenDao(
        chainDb: ChainDb,
    ) = chainDb.tokenDao()

    @Provides
    fun provideTokenApi(
    ) = TokenApi()

    @Provides
    fun provideTokenRepo(
        tokenDao: TokenDao,
        tokenApi: TokenApi
    ): TokenRepo = TokenRepoImpl(
        tokenDao = tokenDao,
        tokenApi = tokenApi
    )

    @Provides
    fun provideAddressDao(
        chainDb: ChainDb,
    ) = chainDb.addressDao()

    @Provides
    fun provideAddressRepo(
        adressDao: AddressDao
    ): AddressRepo = AddressRepoImpl(
        addressDao = adressDao
    )

    @Provides
    fun provideQRCodeGenerator(
    ) = QRCodeGenerator

    @Provides
    fun provideQRCodeScanner(
    ) = QRCodeScanner

    @Provides
    fun provideSystemClipboard(
        @ApplicationContext
        context : Context
    ) = SystemClipboard(context)
}