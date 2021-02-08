package com.idslogic.levelshoes.ui.home.search

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.idslogic.levelshoes.data.models.CategorySearch
import com.idslogic.levelshoes.databinding.ItemCategorySearchBinding
import com.idslogic.levelshoes.databinding.ItemSearchLinkBinding
import com.idslogic.levelshoes.utils.MENU_ITEM_TYPE_LINK
import com.idslogic.levelshoes.utils.underlined

class SearchCategoryAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    companion object {
        const val TYPE_BODY = 1
        const val TYPE_LINK = 2
    }

    private var mapCategories: LinkedHashMap<CategorySearch, ArrayList<CategorySearch>?>? = null
    private var categories: List<CategorySearch> = arrayListOf()

    override fun getItemViewType(position: Int): Int {
        if (position == categories.size.minus(1) && (categories[position].menuItemType ==
                    MENU_ITEM_TYPE_LINK || categories[position].menuItemType == null)
        )
            return TYPE_LINK
        return TYPE_BODY
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            TYPE_LINK -> ItemLinkViewHolder(
                ItemSearchLinkBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                )
            )
            else -> ViewHolder(
                ItemCategorySearchBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                )
            )
        }
    }


    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder is ViewHolder)
            holder.bind()
        else if (holder is ItemLinkViewHolder)
            holder.bind()
    }


    override fun getItemCount() = categories.size

    fun setItems(mapCategories: LinkedHashMap<CategorySearch, ArrayList<CategorySearch>?>?) {
        val oldListSize = this.categories.size
        this.categories = mapCategories?.keys?.toList() ?: arrayListOf()
        this.mapCategories = mapCategories
        if (oldListSize == 0) notifyDataSetChanged()
        else notifyItemRangeChanged(0, oldListSize)
    }

    fun setItems(categories: ArrayList<CategorySearch>) {
        this.categories = categories
        notifyDataSetChanged()
    }

    inner class ViewHolder(private val binding: ItemCategorySearchBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind() {
            binding.categorySearch = categories[bindingAdapterPosition]
            binding.arrowNext.visibility =
                if (mapCategories.isNullOrEmpty()) View.GONE else View.VISIBLE

            binding.root.setOnClickListener {
                onCategorySelected?.invoke(
                    categories[bindingAdapterPosition],
                    mapCategories?.get(categories[bindingAdapterPosition])
                )
            }
        }
    }

    inner class ItemLinkViewHolder(private val binding: ItemSearchLinkBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind() {
            binding.linkText.text = categories[bindingAdapterPosition].name?.underlined()
            binding.linkText.setOnClickListener {
                onCategorySelected?.invoke(categories[bindingAdapterPosition], null)
            }
        }
    }

    var onCategorySelected: ((CategorySearch, ArrayList<CategorySearch>?) -> Unit)? = null
}