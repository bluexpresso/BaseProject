package com.idslogic.levelshoes.ui.home.search

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.dynamicanimation.animation.SpringAnimation
import androidx.dynamicanimation.animation.SpringForce
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

    override fun getItemCount() = searchedProducts?.size ?: 0
    inner class ViewHolder(val binding: ItemProductInSearchBinding) :
        RecyclerView.ViewHolder(binding.root) {
        val translationY: SpringAnimation =
            SpringAnimation(binding.root, SpringAnimation.TRANSLATION_Y)
                .setSpring(
                    SpringForce()
                        .setFinalPosition(0f)
                        .setDampingRatio(SpringForce.DAMPING_RATIO_MEDIUM_BOUNCY)
                        .setStiffness(SpringForce.STIFFNESS_LOW)
                )

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
                if (!listingProduct.price.isNullOrEmpty())
                    binding.price.text =
                        formatPrice(
                            binding.root.context, currency, 0.0,
                            listingProduct.startPrice?.toDouble()
                        )
            }
        }
    }

    fun setItems(items: ArrayList<ListingProduct>?) {
        this.searchedProducts = items
        notifyDataSetChanged()
    }

    fun clear() {
        searchedProducts?.clear()
        notifyDataSetChanged()
    }
}