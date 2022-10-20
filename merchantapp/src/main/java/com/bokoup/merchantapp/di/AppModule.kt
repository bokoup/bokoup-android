package com.bokoup.merchantapp.di

import android.content.Context
import androidx.room.Room
import com.apollographql.apollo3.ApolloClient
import com.bokoup.lib.QRCodeGenerator
import com.bokoup.merchantapp.data.*
import com.bokoup.merchantapp.ui.customer.share.NotificationReceiver
import com.clover.sdk.util.CloverAccount
import com.clover.sdk.v1.tender.TenderConnector
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

    @Provides
    fun notificationReceiver(
        @ApplicationContext
        context: Context
    ) = NotificationReceiver(context)

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
    fun solanaRepo(
        solanaApi: SolanaApi,
        localTransactions: LocalTransactions,
    ): SolanaRepo = SolanaRepoImpl(
        solanaApi,
        localTransactions
    )

    @Provides
    fun apolloClient(
    ): ApolloClient = ApolloClient.Builder().serverUrl("https://data.api.bokoup.com/v1/graphql").build()

    @Provides
    fun dataService(
        apolloClient: ApolloClient
    ): DataService = DataService(apolloClient)

    @Provides
    fun dataRepo(
        @ApplicationContext
        context: Context,
        dataService: DataService,
        transactionService: TransactionService
    ): DataRepo = DataRepoImpl(
        context,
        dataService,
        transactionService
    )

    @Provides
    fun transactionService(
    ) = TransactionService()

}