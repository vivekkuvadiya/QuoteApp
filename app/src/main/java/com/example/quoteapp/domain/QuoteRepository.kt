package com.example.quoteapp.domain

import androidx.lifecycle.LiveData
import androidx.paging.PagingData
import com.example.quoteapp.data.model.Quote
import com.example.quoteapp.data.model.QuoteResponse
import com.example.quoteapp.utils.Resource

interface QuoteRepository {

    fun loadQuotes():LiveData<PagingData<Quote>>

    suspend fun setFavourite(quote: Quote)

    suspend fun searchQuote(queryString: String, page:Int): Resource<QuoteResponse>

}