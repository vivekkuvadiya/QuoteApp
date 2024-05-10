package com.example.quoteapp.presentation.search

import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.AbsListView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.widget.doOnTextChanged
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.quoteapp.R
import com.example.quoteapp.data.model.Quote
import com.example.quoteapp.databinding.ActivitySearchBinding
import com.example.quoteapp.databinding.DialogQuotesBinding
import com.example.quoteapp.utils.Resource
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SearchActivity : AppCompatActivity() {

    private lateinit var binding:ActivitySearchBinding
    lateinit var adapter: QuoteSearchAdapter
    private val viewModel: SearchViewModel by viewModels()

    var isLoading = false
    var isLastPage = false
    var isScrolling = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivitySearchBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        adapter = QuoteSearchAdapter()

        binding.rvQuote.apply {
            layoutManager = LinearLayoutManager(this@SearchActivity)
            adapter = this@SearchActivity.adapter
            addOnScrollListener(scrollListenear)
        }

        binding.etSearch.doOnTextChanged { text, start, before, count ->

            if (text.toString().isEmpty()){
                adapter.differ.submitList(mutableListOf())
            }else{
                viewModel.searchQuote(text.toString())
            }

        }

        binding.etSearch.setText(R.string.motivation)

        adapter.setQuoteSearchListener(object : QuoteSearchAdapter.QuoteSearchListener {
            override fun onQuoteClick(quote: Quote) {
                showQuotes(quote)
            }
        })


        viewModel.searchResponse.observe(this){
            when(it){
                is Resource.Success -> {
                    it.data?.let {
                        adapter.differ.submitList(it.quotes)
                        isLastPage = viewModel.page == it.totalPages
                    }
                }
                is Resource.Error -> {
                    Toast.makeText(this, it.message, Toast.LENGTH_SHORT).show()
                }
                is Resource.Loading ->{

                }
            }
        }

    }

    fun showQuotes(quote: Quote){

        val dialogView = DialogQuotesBinding.inflate(LayoutInflater.from(this))

        val dialog = Dialog(this).apply {
            setContentView(dialogView.root)
            window?.setLayout(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
            )
            window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            create()
        }
        dialogView.tvQuote.text = quote.content
        dialogView.tvAuthor.text = "- ${quote.author}"
        dialog.show()
        dialogView.icClose.setOnClickListener {
            dialog.dismiss()
        }

    }

    var scrollListenear = object : RecyclerView.OnScrollListener(){
        override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
            super.onScrolled(recyclerView, dx, dy)
            val layoutManager = recyclerView.layoutManager as LinearLayoutManager
            val firstVisibleItemPosition = layoutManager.findFirstVisibleItemPosition()
            val visibleCount = layoutManager.childCount
            val totalItemCount = layoutManager.itemCount

            val isNotLoadingAndNotLastPage = !isLoading && !isLastPage
            val isAtLastItem = firstVisibleItemPosition + visibleCount >= totalItemCount
            val isNotAtBegining = firstVisibleItemPosition >= 0
            val isTotalMoreThanVisible = totalItemCount >= 10
            val shouldPaging = isNotLoadingAndNotLastPage && isAtLastItem && isNotAtBegining && isTotalMoreThanVisible && isScrolling
            if (shouldPaging){
                viewModel.searchQuote(binding.etSearch.text.toString())
                isScrolling = false
            }
        }

        override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
            super.onScrollStateChanged(recyclerView, newState)
            if (newState == AbsListView.OnScrollListener.SCROLL_STATE_TOUCH_SCROLL){
                isScrolling = true
            }

        }
    }
}