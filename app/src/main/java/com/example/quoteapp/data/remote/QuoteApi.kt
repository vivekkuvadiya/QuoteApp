package com.example.quoteapp.data.remote

import com.example.quoteapp.data.model.QuoteResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface QuoteApi {

    @GET("quotes")
    suspend fun getQuote(@Query("page") page: Int, @Query("limit") limit: Int = 10):QuoteResponse

    @GET("search/quotes")
    suspend fun searchQuote(@Query("query") query:String, @Query("page") page:Int, @Query("limit") limit: Int = 10):QuoteResponse

}