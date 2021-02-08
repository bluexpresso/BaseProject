package com.idslogic.levelshoes.ui.home.search

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.idslogic.levelshoes.R
import com.idslogic.levelshoes.data.models.CategorySearch
import com.idslogic.levelshoes.databinding.ItemRecentTrendingValuesBinding
import java.util.*
import kotlin.collections.ArrayList

class SearchResultsCategoryAdapter :
    RecyclerView.Adapter<SearchResultsCategoryAdapter.ViewHolder>() {
    private var listItems = arrayListOf<Pair<CategorySearch, CategorySearch>>()

    inner class ViewHolder(private val binding: ItemRecentTrendingValuesBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind() {
            binding.title.text = String.format(
                Locale.getDefault(),
                binding.root.context.getString(R.string.category_search_result_placeholder),
                listItems[bindingAdapterPosition].first.name,
                listItems[bindingAdapterPosition].second.name
            )
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder =
        ViewHolder(
            ItemRecentTrendingValuesBinding.inflate(
                LayoutInflater.from(parent.context),
                parent, false
            )
        )

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind()
    }

    fun setItems(items: ArrayList<Pair<CategorySearch, CategorySearch>>) {
        this.listItems = items
        notifyDataSetChanged()
    }

    override fun getItemCount() = listItems.size

}