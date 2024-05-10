package com.example.quoteapp.presentation.quote

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.quoteapp.R
import com.example.quoteapp.data.model.Quote
import com.example.quoteapp.databinding.ItemQuoteBinding

class QuoteAdapter:PagingDataAdapter<Quote, QuoteAdapter.QuoteViewHolder>(DiffUtil) {

    var quotesListener: QuotesListener? = null

    override fun onBindViewHolder(holder: QuoteViewHolder, position: Int) {
        holder.binding.apply {

            val quote = getItem(position)

            tvQuote.text = quote?.content
            tvAuthor.text = quote?.author
            if (quote?.isFavourite == true){
                icFav.setImageResource(R.drawable.ic_fav_filed)
            }else{
                icFav.setImageResource(R.drawable.ic_fav_unfiled)
            }
            lyFav.setOnClickListener {
                quotesListener?.onFavBtnClick(quote)
            }
            root.setOnClickListener {
                quotesListener?.onQuoteClick(quote)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): QuoteViewHolder {
        return QuoteViewHolder(ItemQuoteBinding.inflate(LayoutInflater.from(parent.context),parent,false))
    }

    fun setListener(quotesListener: QuotesListener){
        this.quotesListener = quotesListener
    }


    class QuoteViewHolder(val binding:ItemQuoteBinding):RecyclerView.ViewHolder(binding.root){

    }

    companion object{
        val DiffUtil = object :DiffUtil.ItemCallback<Quote>(){
            override fun areItemsTheSame(oldItem: Quote, newItem: Quote): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(oldItem: Quote, newItem: Quote): Boolean {
                return oldItem == newItem
            }

        }
    }

    interface QuotesListener{
        fun onFavBtnClick(quote: Quote?)
        fun onQuoteClick(quote: Quote?)
    }


}