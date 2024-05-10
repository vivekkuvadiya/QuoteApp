package com.example.quoteapp.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName

/**
 * use to store quote and persistent when no internet
 * isFavourite is store as liked or dislike quote
 */
@Entity
data class Quote(
    @PrimaryKey(autoGenerate = false)
    @SerializedName("_id")
    val id:String,
    @SerializedName("author")
    val author:String,
    @SerializedName("content")
    val content:String,
    val isFavourite:Boolean
)
