package com.example.quoteapp.presentation.search

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.quoteapp.data.model.Quote
import com.example.quoteapp.databinding.ItemQuoteBinding

class QuoteSearchAdapter:RecyclerView.Adapter<QuoteSearchAdapter.SearchQuoteViewHolder>() {

    private var quoteSearchListener:QuoteSearchListener? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SearchQuoteViewHolder {
        return SearchQuoteViewHolder(ItemQuoteBinding.inflate(LayoutInflater.from(parent.context),parent,false))
    }

    override fun getItemCount(): Int {
        return differ.currentList.size
    }

    override fun onBindViewHolder(holder: SearchQuoteViewHolder, position: Int) {
        val quote = differ.currentList[position]
        holder.binding.apply {
            tvQuote.text = quote.content
            tvAuthor.text = quote.author
            lyFav.visibility = View.GONE

            root.setOnClickListener {
                quoteSearchListener?.onQuoteClick(quote)
            }
        }
    }



    private val diffCallback = object :DiffUtil.ItemCallback<Quote>(){
        override fun areItemsTheSame(oldItem: Quote, newItem: Quote): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Quote, newItem: Quote): Boolean {
            return oldItem == newItem
        }

    }

    val differ = AsyncListDiffer(this,diffCallback)

    fun setQuoteSearchListener(quoteSearchListener:QuoteSearchListener){
        this.quoteSearchListener = quoteSearchListener
    }

    class SearchQuoteViewHolder(val binding:ItemQuoteBinding):RecyclerView.ViewHolder(binding.root)

    interface QuoteSearchListener{
        fun onQuoteClick(quote: Quote)
    }

}