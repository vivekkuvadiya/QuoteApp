package com.example.quoteapp.presentation.search

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.quoteapp.data.model.QuoteResponse
import com.example.quoteapp.domain.QuoteRepository
import com.example.quoteapp.utils.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SearchViewModel @Inject constructor(private val repository: QuoteRepository) :ViewModel() {

    private val _searchResponse = MutableLiveData<Resource<QuoteResponse>>()
    val searchResponse:LiveData<Resource<QuoteResponse>> = _searchResponse

    var quoteResponse:QuoteResponse? = null

    var page = 1

    fun searchQuote(query:String){
        viewModelScope.launch(Dispatchers.IO) {
            _searchResponse.postValue(Resource.Loading())
            val quoteResponse = repository.searchQuote(query, page)

            when(quoteResponse){
                is Resource.Success -> {
                    quoteResponse.data?.let {
                        if (it.totalCount == 0){
                            page = 1
                        }else{
                            page++
                            if (quoteResponse == null){
                                this@SearchViewModel.quoteResponse = it
                                _searchResponse.postValue(Resource.Success(it))
                            }else{
                                val oldQuote = this@SearchViewModel.quoteResponse?.quotes
                                val newQuote = it.quotes
                                oldQuote?.addAll(newQuote)
                                _searchResponse.postValue(Resource.Success(this@SearchViewModel.quoteResponse ?: it))

                            }
                        }
                    }
                }
                is Resource.Error -> {
                    _searchResponse.postValue(Resource.Error(quoteResponse.message))
                }
                is Resource.Loading -> {

                }
            }
        }
    }

}