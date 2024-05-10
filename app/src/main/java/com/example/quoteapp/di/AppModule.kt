package com.example.quoteapp.di

import android.content.Context
import androidx.room.Room
import com.example.quoteapp.data.local.QuoteDatabase
import com.example.quoteapp.data.remote.QuoteApi
import com.example.quoteapp.data.repository.QuoteRepositoryImpl
import com.example.quoteapp.domain.QuoteRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
object AppModule {

    @Singleton
    @Provides
    fun provideQuoteApi():QuoteApi{
        return Retrofit.Builder()
            .baseUrl("https://api.quotable.io/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(QuoteApi::class.java)
    }

    @Singleton
    @Provides
    fun provideDatabase(@ApplicationContext context: Context) =
        Room.databaseBuilder(context,QuoteDatabase::class.java,"quoteDb").build()

    @Singleton
    @Provides
    fun provideQuotesRepository(quoteApi: QuoteApi, quoteDatabase: QuoteDatabase):QuoteRepository = QuoteRepositoryImpl(quoteApi, quoteDatabase)
}