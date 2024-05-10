package com.example.quoteapp.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * to calculate next and previous page index
 * it store id, previous and next page according current page index
 */
@Entity
data class QuoteRemoteKey(
    @PrimaryKey(autoGenerate = false)
    val id:String,
    val prevPage:Int?,
    val nextPage:Int?
)
