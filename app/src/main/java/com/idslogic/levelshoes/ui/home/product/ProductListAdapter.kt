package com.idslogic.levelshoes.ui.home.product

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.idslogic.levelshoes.BuildConfig
import com.idslogic.levelshoes.data.models.BaseModel
import com.idslogic.levelshoes.data.models.ListingProduct
import com.idslogic.levelshoes.data.models.ListingProductResponse
import com.idslogic.levelshoes.data.models.Product
import com.idslogic.levelshoes.databinding.ItemProductInListBinding
import com.idslogic.levelshoes.di.GlideApp
import java.util.*
import kotlin.collections.ArrayList

class ProductListAdapter(val currency: String) :
    RecyclerView.Adapter<ProductListAdapter.ViewHolder>() {
    val products = arrayListOf<BaseModel.Hit<Product>>()

    inner class ViewHolder(val binding: ItemProductInListBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind() {
            val productSource = products[adapterPosition].source
            productSource?.let { product ->
                binding.brand.text = product.name
                binding.name.text = product.manufacturerName ?: ""
                product.price?.let {
                    binding.price.text =
                        String.format(Locale.getDefault(), product.price!!, currency)
                }
                GlideApp.with(binding.image)
                    .load(BuildConfig.IMAGE_URL.plus(product.image))
                    .into(binding.image)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            ItemProductInListBinding.inflate(
                LayoutInflater.from(parent.context),
                parent, false
            )
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind()
    }

    override fun getItemCount() = products.size

    fun setItems(items: ArrayList<BaseModel.Hit<Product>>?) {
        this.products.clear()
        items.let {
            this.products.addAll(items!!)
            notifyDataSetChanged()
        }
    }
}