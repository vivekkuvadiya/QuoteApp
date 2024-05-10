package com.example.quoteapp.presentation.quote

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.paging.LoadState
import androidx.paging.LoadStateAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.quoteapp.databinding.ItemLoadingBinding

class LoadingViewHolder:LoadStateAdapter<LoadingViewHolder.LoadingView>() {


    override fun onBindViewHolder(holder: LoadingView, loadState: LoadState) {
         holder.onBind(loadState)
    }

    override fun onCreateViewHolder(parent: ViewGroup, loadState: LoadState): LoadingView {
        return LoadingView(ItemLoadingBinding.inflate(LayoutInflater.from(parent.context),parent,false))
    }

    class LoadingView(val binding:ItemLoadingBinding):RecyclerView.ViewHolder(binding.root){

        fun onBind(loadState: LoadState){

            binding.root.isVisible = loadState is LoadState.Loading

        }
    }


}