package com.bokoup.customerapp.di

import android.content.Context
import androidx.room.Room
import com.apollographql.apollo3.ApolloClient
import com.bokoup.customerapp.data.net.*
import com.bokoup.customerapp.data.repo.AddressRepoImpl
import com.bokoup.customerapp.data.repo.DataRepoImpl
import com.bokoup.customerapp.data.repo.SolanaRepoImpl
import com.bokoup.customerapp.data.repo.TokenRepoImpl
import com.bokoup.customerapp.dom.repo.AddressRepo
import com.bokoup.customerapp.dom.repo.DataRepo
import com.bokoup.customerapp.dom.repo.SolanaRepo
import com.bokoup.customerapp.dom.repo.TokenRepo
import com.bokoup.customerapp.util.QRCodeScanner
import com.bokoup.lib.QRCodeGenerator
import com.bokoup.lib.SystemClipboard
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
class AppModule() {
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
    fun dataRepo(
        dataService: DataService,
        addressDao: AddressDao
    ): DataRepo = DataRepoImpl(
        dataService,
        addressDao
    )

    @Provides
    fun chainDb(
        @ApplicationContext
        context: Context
    ) = Room.databaseBuilder(
        context,
        ChainDb::class.java,
        "token"
    ).allowMainThreadQueries().build()
    // total friggin hack because I am idiot

    @Provides
    fun tokenDao(
        chainDb: ChainDb,
    ) = chainDb.tokenDao()

    @Provides
    fun tokenApi(
    ) = TransactionService()

    @Provides
    fun solanaApi(
    ) = SolanaApi(Cluster.Custom("https://api.devnet.solana.com/", "wss://api.devnet.solana.com/"), OkHttpClient.Builder().apply {
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
        transactionService: TransactionService,
    ): TokenRepo = TokenRepoImpl(
        tokenDao = tokenDao,
        transactionService = transactionService,
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
        solanaRepo: SolanaRepo,
        keyFactory: KeyFactory
    ): AddressRepo = AddressRepoImpl(
        addressDao = addressDao,
        solanaRepo = solanaRepo,
        keyFactory = keyFactory
    )

    @Provides
    fun qRCodeGenerator(
    ) = QRCodeGenerator

    @Provides
    fun keyFactory(
    ) = KeyFactory

    @Provides
    fun qRCodeScanner(
    ) = QRCodeScanner

    @Provides
    fun systemClipboard(
        @ApplicationContext
        context: Context
    ) = SystemClipboard(context)
}