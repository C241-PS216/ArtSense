package com.harish.artsense.Api.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.harish.artsense.Api.Response.HistoryResponseItem
import com.harish.artsense.databinding.HistoryLayoutBinding

class HistoryAdapter : PagingDataAdapter<HistoryResponseItem, HistoryAdapter.HistoryViewHolder>(DIFF_CALLBACK) {

    private var onItemClickCallback: OnItemClickCallBack? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HistoryViewHolder {
        val binding = HistoryLayoutBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return HistoryViewHolder(binding)
    }

    override fun onBindViewHolder(holder: HistoryViewHolder, position: Int) {
        val item = getItem(position)
        holder.bind(item!!, holder.itemView.context)
    }
    inner class HistoryViewHolder(private val binding: HistoryLayoutBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(history: HistoryResponseItem, context: Context) {
            binding.root.setOnClickListener {
                onItemClickCallback?.onItemClicked(history)

//                val intent = Intent(context, DetailActivity::class.java)
//                intent.putExtra(EXTRA_DETAIL, story)
//                context.startActivity(intent)
//                val intent = Intent(context, DetailActivity::class.java).apply {
//                }
//                context.startActivity(intent)
            }
            binding.tvResult.text = history.history!!.result
            binding.tvHistory.text = history.id
            val gambar = history.history.gambar
            Glide.with(itemView)
                .load(gambar)
                .transition(DrawableTransitionOptions.withCrossFade())
                .override(600, 400)
                .centerCrop()
                .into(binding.ivHistory)
        }
    }

    companion object {
        val DIFF_CALLBACK = object : DiffUtil.ItemCallback<HistoryResponseItem>() {
            override fun areItemsTheSame(oldItem: HistoryResponseItem, newItem: HistoryResponseItem): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(oldItem: HistoryResponseItem, newItem: HistoryResponseItem): Boolean {
                return oldItem.history!!.gambar == newItem.history!!.gambar && oldItem.id == newItem.id && oldItem.history.result == newItem.history.result
            }
        }
    }

    interface OnItemClickCallBack {
        fun onItemClicked(data: HistoryResponseItem)
    }
}