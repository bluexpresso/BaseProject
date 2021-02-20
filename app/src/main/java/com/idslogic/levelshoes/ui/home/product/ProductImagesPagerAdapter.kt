package com.idslogic.levelshoes.ui.home.product

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.idslogic.levelshoes.data.models.MediaGallery
import com.idslogic.levelshoes.databinding.ItemProductImageBinding
import com.idslogic.levelshoes.di.GlideApp

class ProductImagesPagerAdapter :
    RecyclerView.Adapter<ProductImagesPagerAdapter.ProductImageViewHolder>() {
    private var mediaGallery: ArrayList<MediaGallery?>? = null

    inner class ProductImageViewHolder(private val binding: ItemProductImageBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind() {
            mediaGallery?.get(bindingAdapterPosition)?.let { mediaGallery ->
                GlideApp.with(binding.image)
                    .load(mediaGallery.image)
                    .into(binding.image)
            }
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

    fun setItems(mediaGallery: ArrayList<MediaGallery?>?) {
        this.mediaGallery = mediaGallery
    }
}