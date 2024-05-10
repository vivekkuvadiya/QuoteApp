package com.example.quoteapp.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.quoteapp.data.model.Quote
import com.example.quoteapp.data.model.QuoteRemoteKey

@Database(entities = [Quote::class,QuoteRemoteKey::class], version = 1)
abstract class QuoteDatabase:RoomDatabase() {

    abstract fun getQuoteDao():QuoteDao

    abstract fun getRemoteKeyDao():RemoteKeyDao

}