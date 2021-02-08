package com.idslogic.levelshoes.ui.home.search

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.transition.Hold
import com.idslogic.levelshoes.databinding.ItemRecentTrendingValuesBinding
import com.idslogic.levelshoes.databinding.ItemSearchRecentTrendingHeaderBinding

class SearchRecentTrendingAdapter :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    companion object {
        const val ITEM_VIEW_HEADER = 1
        const val ITEM_VIEW_ROW = 2
    }

    private var itemList: MutableList<SearchRecentTrendingWrapper> = arrayListOf()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return if (viewType == ITEM_VIEW_ROW) {
            RecentSearchViewHolderRow(
                ItemRecentTrendingValuesBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent, false
                )
            )
        } else {
            RecentSearchViewHolderHeader(
                ItemSearchRecentTrendingHeaderBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent, false
                )
            )
        }
    }

    override fun getItemCount() = itemList.size

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder is RecentSearchViewHolderHeader)
            holder.bind()
        else if (holder is RecentSearchViewHolderRow)
            holder.bind()
    }

    fun setItems(items: MutableList<SearchRecentTrendingWrapper>) {
        this.itemList = items
        notifyDataSetChanged()
    }

    override fun getItemViewType(position: Int): Int {
        return if (itemList[position].isHeader) {
            ITEM_VIEW_HEADER
        } else {
            ITEM_VIEW_ROW
        }
    }

    inner class RecentSearchViewHolderRow(private val binding: ItemRecentTrendingValuesBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind() {
            binding.title.text = itemList[bindingAdapterPosition].title
            binding.root.setOnClickListener {
                if (itemList[bindingAdapterPosition].product == null)
                    onRecentItemClick?.invoke(itemList[bindingAdapterPosition].title)
            }
        }
    }

    inner class RecentSearchViewHolderHeader(private val binding: ItemSearchRecentTrendingHeaderBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind() {
            binding.title.text = itemList[bindingAdapterPosition].title
        }
    }

    var onRecentItemClick: ((String?) -> Unit)? = null
}