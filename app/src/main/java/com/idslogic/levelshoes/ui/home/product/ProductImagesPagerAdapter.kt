package com.idslogic.levelshoes.ui.home.product

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.idslogic.levelshoes.databinding.ItemProductImageBinding
import com.idslogic.levelshoes.di.GlideApp

class ProductImagesPagerAdapter :
    RecyclerView.Adapter<ProductImagesPagerAdapter.ProductImageViewHolder>() {
    inner class ProductImageViewHolder(private val binding: ItemProductImageBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind() {
            GlideApp.with(binding.image)
                .load("https://raw.githubusercontent.com/bluexpresso/Pashu-Pakshi/gh-pages/g3184115ricgsv-glgz_1.jpg")
                .into(binding.image)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductImageViewHolder {
        return ProductImageViewHolder(
            ItemProductImageBinding.inflate(
                LayoutInflater.from(parent.context),
                parent, false
            )
        )
    }

    override fun onBindViewHolder(holder: ProductImageViewHolder, position: Int) {
        holder.bind()
    }

    override fun getItemCount() = 4
}