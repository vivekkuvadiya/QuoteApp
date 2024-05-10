package com.example.quoteapp.data.paging

import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import androidx.room.withTransaction
import com.example.quoteapp.data.local.QuoteDatabase
import com.example.quoteapp.data.model.Quote
import com.example.quoteapp.data.model.QuoteRemoteKey
import com.example.quoteapp.data.remote.QuoteApi

@OptIn(ExperimentalPagingApi::class)
class QuoteRemoteMediator(
    private val quoteApi: QuoteApi,
    private val quoteDatabase: QuoteDatabase,
) : RemoteMediator<Int, Quote>() {

    val quoteDao = quoteDatabase.getQuoteDao()
    val quoteRemoteKeyDao = quoteDatabase.getRemoteKeyDao()


    override suspend fun load(loadType: LoadType, state: PagingState<Int, Quote>): MediatorResult {
        return try {
            val currentPage = when(loadType){
                LoadType.REFRESH -> {
                    /**
                     * finding closest page from recently access item
                     */
                    val key = state.anchorPosition?.let {
                        state.closestItemToPosition(it)?.id?.let {
                            quoteRemoteKeyDao.getRemoteKey(it)
                        }
                    }
                    /**
                     * if we are loading data first time then it pass 1 page otherwise closed page
                     */
                    key?.nextPage?.minus(1)?:1
                }
                LoadType.PREPEND -> {
                    /**
                     * state when it scroll up
                     * finding remoteKey of first item from first page of remote database to find
                     *
                     */
                    val key = state.pages.firstOrNull { it.data.isNotEmpty()}?.data?.firstOrNull()?.let {
                        quoteRemoteKeyDao.getRemoteKey(it.id)
                    }

                    /**
                     * from remote key it get previous page index
                     */
                    val prevPage = key?.prevPage?:return MediatorResult.Success(key != null)
                    prevPage
                }
                LoadType.APPEND -> {
                    /**
                     * state when it scroll down
                     * finding remoteKey of last item from last page of remote database
                     */
                    val key = state.pages.lastOrNull{ it.data.isNotEmpty()}?.data?.lastOrNull()?.let {
                        quoteRemoteKeyDao.getRemoteKey(it.id)
                    }

                    /**
                     * from key it get last page index
                     */
                    val nextPage = key?.nextPage?: return MediatorResult.Success(key != null)
                    nextPage
                }
            }

            val quoteResponse = quoteApi.getQuote(currentPage)

            val endOfPagination = currentPage == quoteResponse.totalPages

            /**
             * finding next page and previous page according current page
             * to store remote key in database
             */
            val prevPage = if (currentPage == 1) null else currentPage-1
            val nextPage = if (endOfPagination) null else currentPage+1

            quoteDatabase.withTransaction{
                quoteDao.addQuotes(quoteResponse.quotes)
                /**
                 * adding appropriate remote key to database
                 */
                val keys = quoteResponse.quotes.map { QuoteRemoteKey(it.id,prevPage, nextPage) }
                quoteRemoteKeyDao.addAllRemoteKey(keys)
            }

            MediatorResult.Success(endOfPagination)

        }catch (e:Exception){
            MediatorResult.Error(e)
        }
    }


}