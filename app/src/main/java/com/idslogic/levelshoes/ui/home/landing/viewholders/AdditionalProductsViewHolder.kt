package com.idslogic.levelshoes.ui.home.landing.viewholders

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.appcompat.widget.AppCompatImageView
import androidx.recyclerview.widget.RecyclerView
import com.idslogic.levelshoes.data.models.BaseModel
import com.idslogic.levelshoes.data.models.Content
import com.idslogic.levelshoes.data.models.Product
import com.idslogic.levelshoes.databinding.ItemAdditionalProductBinding
import com.idslogic.levelshoes.databinding.ItemLandingAdditionalProductsViewBinding
import com.idslogic.levelshoes.di.GlideApp

class AdditionalProductsViewHolder(
    val binding: ItemLandingAdditionalProductsViewBinding,
    val selectedCurrency: String
) :
    RecyclerView.ViewHolder(binding.root) {
    var onProductSelected: ((BaseModel.Hit<Product>,AppCompatImageView) -> Unit)? = null
    fun bind(content: Content, onProductSelected: ((BaseModel.Hit<Product>, AppCompatImageView) -> Unit)?) {
        this.onProductSelected = onProductSelected
        binding.contentSubtitle.text = content.subTitle ?: ""
        binding.contentTitle.text = content.title ?: ""
        val adapter = AdditionalProductsListAdapter()
        binding.productsList.adapter = adapter
        adapter.setItems(content.productsList)
    }

    inner class AdditionalProductsListAdapter :
        RecyclerView.Adapter<AdditionalProductsListAdapter.AdditionalProductsListViewHolder>() {
        var products: ArrayList<BaseModel.Hit<Product>>? = null

        inner class AdditionalProductsListViewHolder(val binding: ItemAdditionalProductBinding) :
            RecyclerView.ViewHolder(
                binding.root
            ) {
            fun bind() {
                GlideApp.with(binding.productImage)
                    .load("https://raw.githubusercontent.com/bluexpresso/Pashu-Pakshi/gh-pages/g3184115ricgsv-glgz_1.jpg")
                    .into(binding.productImage)
                binding.name.text = products!![adapterPosition].source?.name
                binding.brand.text = products!![adapterPosition].source?.name
                binding.price.text =
                    String.format(
                        "%d%s",
                        products!![adapterPosition].source?.finalPrice,
                        selectedCurrency
                    )
                binding.serialNumber.text = "0${adapterPosition + 1}"
                binding.root.setOnClickListener {
                    onProductSelected?.invoke(products!![adapterPosition],binding.productImage)
                }
            }
        }

        override fun onCreateViewHolder(
            parent: ViewGroup,
            viewType: Int
        ): AdditionalProductsListViewHolder {
            return AdditionalProductsListViewHolder(
                ItemAdditionalProductBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                )
            )
        }

        override fun onBindViewHolder(holder: AdditionalProductsListViewHolder, position: Int) {
            holder.bind()
        }

        override fun getItemCount() = products?.size ?: 0

        fun setItems(products: ArrayList<BaseModel.Hit<Product>>?) {
            this.products = products
            notifyDataSetChanged()
        }
    }
}