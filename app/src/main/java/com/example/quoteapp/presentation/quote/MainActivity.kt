package com.example.quoteapp.presentation.quote

import android.app.Dialog
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.quoteapp.R
import com.example.quoteapp.data.model.Quote
import com.example.quoteapp.databinding.ActivityMainBinding
import com.example.quoteapp.databinding.DialogQuotesBinding
import com.example.quoteapp.presentation.search.SearchActivity
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private val quoteViewModel: QuoteViewModel by viewModels()
    private lateinit var quoteAdapter: QuoteAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        binding = ActivityMainBinding.inflate(layoutInflater)

        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        quoteAdapter = QuoteAdapter()

        binding.rvQuotes.apply {
            layoutManager = LinearLayoutManager(this@MainActivity)
            adapter = quoteAdapter
//            setHasFixedSize(true)
        }

        quoteAdapter.withLoadStateHeaderAndFooter(header = LoadingViewHolder(), footer = LoadingViewHolder())

        quoteViewModel.quotes.observe(  this){
            quoteAdapter.submitData(lifecycle,it)
        }

        quoteAdapter.setListener(object : QuoteAdapter.QuotesListener {
            override fun onFavBtnClick(quote: Quote?) {
                quoteViewModel.onFavClick(quote)
            }

            override fun onQuoteClick(quote: Quote?) {
                quote?.let { showQuotes(it) }
            }

        })

        binding.ivSearch.setOnClickListener {
            Intent(this, SearchActivity::class.java).also {
                startActivity(it)
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
}