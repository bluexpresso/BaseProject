package com.idslogic.levelshoes.ui.home.search

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.idslogic.levelshoes.R
import com.idslogic.levelshoes.data.models.ListingProduct
import com.idslogic.levelshoes.databinding.ItemProductInSearchBinding
import com.idslogic.levelshoes.di.GlideApp
import com.idslogic.levelshoes.utils.formatPrice

class SearchResultsProductAdapter(val currency: String) :
    RecyclerView.Adapter<SearchResultsProductAdapter.ViewHolder>() {
    var searchedProducts: ArrayList<ListingProduct>? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        // inflate the custom view from xml layout file
        LayoutInflater.from(parent.context)
            .inflate(R.layout.item_product_in_search, parent, false)

        // return the view holder
        return ViewHolder(
            ItemProductInSearchBinding.inflate(
                LayoutInflater.from(parent.context),
                parent, false
            )
        )
    }


    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind()
    }


    override fun getItemCount(): Int {
        return searchedProducts?.size ?: 0
    }

    inner class ViewHolder(val binding: ItemProductInSearchBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind() {
            searchedProducts?.get(bindingAdapterPosition)?.let { listingProduct ->

                binding.name.text = listingProduct.name
                GlideApp.with(binding.image)
                    .load(
                        listingProduct.imageUrl?.replace(
                            "staging-levelshoes-m2.vaimo.net/needtochange",
                            "levelshoes.com"
                        )
                    )
                    .into(binding.image)

                binding.name.text = listingProduct.name
                binding.brand.text = listingProduct.manufacturer ?: ""
                binding.price.text =
                    formatPrice(
                        binding.root.context, currency, 0.0,
                        listingProduct.price?.toDouble()
                    )
            }
        }
    }

    fun setItems(items: ArrayList<ListingProduct>?) {
        this.searchedProducts = items
        notifyDataSetChanged()
    }
}