package com.example.quoteapp.data.repository

import androidx.lifecycle.LiveData
import androidx.paging.ExperimentalPagingApi
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.liveData
import com.example.quoteapp.data.local.QuoteDatabase
import com.example.quoteapp.data.model.Quote
import com.example.quoteapp.data.model.QuoteResponse
import com.example.quoteapp.data.paging.QuoteRemoteMediator
import com.example.quoteapp.data.remote.QuoteApi
import com.example.quoteapp.domain.QuoteRepository
import com.example.quoteapp.utils.Resource
import javax.inject.Inject

class QuoteRepositoryImpl @Inject constructor(
    private val quoteApi: QuoteApi,
    private val quoteDatabase: QuoteDatabase
):QuoteRepository {

    @OptIn(ExperimentalPagingApi::class)
    override fun loadQuotes(): LiveData<PagingData<Quote>> {
       return Pager(config = PagingConfig(10,100), remoteMediator = QuoteRemoteMediator(quoteApi, quoteDatabase),  pagingSourceFactory = {quoteDatabase.getQuoteDao().getQuotes()}).liveData
    }

    override suspend fun setFavourite(quote: Quote) {
       quoteDatabase.getQuoteDao().setFavourite(quote)
    }

    override suspend fun searchQuote(queryString: String,page:Int): Resource<QuoteResponse> {
        return try {
            val response = quoteApi.searchQuote(queryString,page)
            Resource.Success(response)
        }catch (e:Exception){
            Resource.Error(e.message)
        }
    }

}