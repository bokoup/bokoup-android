package com.bokoup.customerapp.di

import android.content.Context
import androidx.room.Room
import com.bokoup.customerapp.data.net.ChainDb
import com.bokoup.customerapp.data.net.TokenDao
import com.bokoup.customerapp.data.repo.TokenRepoImpl
import com.bokoup.customerapp.dom.repo.TokenRepo
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
    fun provideTokenRepo(
        tokenDao: TokenDao
    ): TokenRepo = TokenRepoImpl(
        tokenDao = tokenDao
    )
}