package com.bokoup.merchantapp.di

import android.content.Context
import android.content.SharedPreferences
import androidx.room.Room
import com.apollographql.apollo3.ApolloClient
import com.bokoup.lib.QRCodeGenerator
import com.bokoup.merchantapp.data.CloverDb
import com.bokoup.merchantapp.data.TenderDao
import com.bokoup.merchantapp.domain.*
import com.bokoup.merchantapp.net.DataService
import com.bokoup.merchantapp.net.OrderService
import com.bokoup.merchantapp.net.TenderService
import com.bokoup.merchantapp.net.TransactionService
import com.bokoup.merchantapp.ui.customer.NotificationReceiver
import com.bokoup.merchantapp.ui.merchant.BarCodeReceiver
import com.clover.cfp.connector.RemoteDeviceConnector
import com.clover.sdk.util.CloverAccount
import com.clover.sdk.v1.tender.TenderConnector
import com.clover.sdk.v3.order.OrderConnector
import com.clover.sdk.v3.scanner.BarcodeScanner
import com.dgsd.ksol.SolanaApi
import com.dgsd.ksol.core.LocalTransactions
import com.dgsd.ksol.core.model.Cluster
import com.dgsd.ksol.keygen.KeyFactory
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
    fun tenderService(
        tenderConnector: TenderConnector
    ) = TenderService(tenderConnector)


    @Provides
    fun qRCodeGenerator(
    ) = QRCodeGenerator

    @Provides
    fun tenderConnector(
        @ApplicationContext
        context: Context
    ) = TenderConnector(context, CloverAccount.getAccount(context), null)

    @Provides
    fun orderConnector(
        @ApplicationContext
        context: Context
    ) = OrderConnector(context, CloverAccount.getAccount(context), null)

    @Provides
    fun tenderRepository(
        tenderDao: TenderDao,
        tenderService: TenderService,
    ): TenderRepository = TenderRepositoryImpl(
        tenderDao = tenderDao,
        tenderService = tenderService,
    )

    @Provides
    fun notificationReceiver(
        @ApplicationContext
        context: Context
    ) = NotificationReceiver(context)

    @Provides
    fun barCodeReceiver(
        @ApplicationContext
        context: Context
    ) = BarCodeReceiver(context)

    @Provides
    fun barCodeScanner(
        @ApplicationContext
        context: Context
    ) = BarcodeScanner(context)

    @Provides
    fun solanaApi(
    ) = SolanaApi(Cluster.Custom("https://api.devnet.solana.com", "wss://api.devnet.solana.com"), OkHttpClient.Builder().apply {
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
    ): ApolloClient = ApolloClient.Builder()
        .serverUrl("https://data.api.bokoup.dev/v1/graphql")
        .webSocketServerUrl("https://data.api.bokoup.dev/v1/graphql")
        .build()

    @Provides
    fun dataService(
        apolloClient: ApolloClient
    ): DataService = DataService(apolloClient)

    @Provides
    fun orderService(
        connector: OrderConnector
    ) = OrderService(connector)

    @Provides
    fun promoRepo(
        @ApplicationContext
        context: Context,
        dataService: DataService,
        transactionService: TransactionService,
        orderService: OrderService,
        solanaApi: SolanaApi,
        localTransactions: LocalTransactions,
        sharedPref: SharedPreferences
    ): PromoRepo = PromoRepoImpl(
        context,
        dataService,
        transactionService,
        orderService,
        solanaApi,
        localTransactions,
        sharedPref
    )

    @Provides
    fun customerRepo(
        @ApplicationContext
        context: Context,
        dataService: DataService,
        orderService: OrderService,
        txService: TransactionService
    ): CustomerRepo = CustomerRepoImpl(
        context,
        dataService,
        orderService,
        txService
    )

    @Provides
    fun transactionService(
    ) = TransactionService()

    @Provides
    fun remoteDeviceConnector(
        @ApplicationContext
        context: Context
    ) = RemoteDeviceConnector(context, CloverAccount.getAccount(context))

    @Provides
    fun keyFactory(
    ) = KeyFactory

    @Provides
    fun settingsRepo(
        keyFactory: KeyFactory,
        sharedPref: SharedPreferences
    ): SettingsRepo = SettingsRepoImpl(
        keyFactory,
        sharedPref
    )

    @Provides
    fun sharedPref(
        @ApplicationContext
        context: Context
    ): SharedPreferences =
        context.getSharedPreferences("com.bokoup.merchantapp.SETTINGS", Context.MODE_PRIVATE)

}