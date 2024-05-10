package com.example.quoteapp.data.local

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.quoteapp.data.model.Quote

@Dao
interface QuoteDao {

    @Query("SELECT * FROM quote")
    fun getQuotes():PagingSource<Int,Quote>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addQuotes(quotes:List<Quote>)

    @Query("DELETE FROM quote")
    suspend fun deleteQuotes()

    @Update
    suspend fun setFavourite(quote: Quote)

}