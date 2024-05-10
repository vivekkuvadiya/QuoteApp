package com.example.quoteapp.data.model

import com.google.gson.annotations.SerializedName

data class QuoteResponse(
    @SerializedName("count")
    val count:Int,
    @SerializedName("totalCount")
    val totalCount:Int,
    @SerializedName("page")
    val page:Int,
    @SerializedName("totalPages")
    val totalPages:Int,
    @SerializedName("lastItemIndex")
    val lastItemIndex:Int,
    @SerializedName("results")
    val quotes:MutableList<Quote>

)
