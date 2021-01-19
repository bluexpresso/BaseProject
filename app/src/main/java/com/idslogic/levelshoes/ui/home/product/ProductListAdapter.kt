package com.idslogic.levelshoes.ui.home.product

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.idslogic.levelshoes.data.models.ListingProduct
import com.idslogic.levelshoes.data.models.ListingProductResponse
import com.idslogic.levelshoes.databinding.ItemProductInListBinding
import com.idslogic.levelshoes.di.GlideApp
import java.util.*
import kotlin.collections.ArrayList

class ProductListAdapter : RecyclerView.Adapter<ProductListAdapter.ViewHolder>() {
    val products = arrayListOf<ListingProduct>()

    inner class ViewHolder(val binding: ItemProductInListBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind() {
            val product = products[adapterPosition]
            binding.brand.text = product.name
            binding.name.text = product.manufacturer
            product.price?.let {
                binding.price.text =
                    String.format(Locale.getDefault(), product.price!!, product.currency)
            }
            GlideApp.with(binding.image)
                .load("https://raw.githubusercontent.com/bluexpresso/Pashu-Pakshi/gh-pages/g3184115ricgsv-glgz_1.jpg")
                .into(binding.image)
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

    fun setItems(items: ArrayList<ListingProduct>?) {
        this.products.clear()
        items.let {
            this.products.addAll(items!!)
            notifyDataSetChanged()
        }
    }
}