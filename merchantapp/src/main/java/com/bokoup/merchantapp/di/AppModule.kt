package com.bokoup.merchantapp.di

import android.content.Context
import androidx.room.Room
import com.bokoup.lib.QRCodeGenerator
import com.bokoup.merchantapp.data.*
import com.clover.sdk.util.CloverAccount
import com.clover.sdk.v1.tender.TenderConnector
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
class AppModule {
    @Provides
    fun cloverDb(
        @ApplicationContext
        context: Context
    ) = Room.databaseBuilder(
        context,
        CloverDb::class.java,
        "tenders"
    ).build()

    @Provides
    fun tenderDao(
        cloverDb: CloverDb
    ) = cloverDb.tenderDao()

    @Provides
    fun cloverService(
        tenderConnector: TenderConnector
    ) = CloverService(tenderConnector)

    @Provides
    fun qRCodeGenerator(
    ) = QRCodeGenerator

    @Provides
    fun tenderConnector(
        @ApplicationContext
        context: Context
    ) = TenderConnector(context, CloverAccount.getAccount(context), null)

    @Provides
    fun tenderRepository(
        tenderDao: TenderDao,
        cloverService: CloverService,
    ): TenderRepository = TenderRepositoryImpl(
        tenderDao = tenderDao,
        tenderService = cloverService,
    )


}