package com.example.quoteapp.presentation.quote

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.cachedIn
import com.example.quoteapp.data.model.Quote
import com.example.quoteapp.domain.QuoteRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class QuoteViewModel @Inject constructor(private val quoteRepository: QuoteRepository):ViewModel() {
    fun onFavClick(quote: Quote?) {
        quote?.let {
            viewModelScope.launch(Dispatchers.IO) {
                quoteRepository.setFavourite(it.copy(isFavourite = !it.isFavourite))
            }
        }


    }

    val quotes = quoteRepository.loadQuotes().cachedIn(viewModelScope)

}
