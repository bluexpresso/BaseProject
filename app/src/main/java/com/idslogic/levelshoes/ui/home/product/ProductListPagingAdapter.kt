package com.idslogic.levelshoes.ui.home.product

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.AppCompatImageView
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.chip.Chip
import com.idslogic.levelshoes.BuildConfig
import com.idslogic.levelshoes.R
import com.idslogic.levelshoes.data.models.BaseModel
import com.idslogic.levelshoes.data.models.Product
import com.idslogic.levelshoes.databinding.ItemProductInListBinding
import com.idslogic.levelshoes.di.GlideApp
import com.idslogic.levelshoes.utils.formatPrice

class ProductListPagingAdapter(val currency: String) :
    PagingDataAdapter<BaseModel.Hit<Product>, ProductListPagingAdapter.ViewHolder>(DiffUtils) {

    inner class ViewHolder(val binding: ItemProductInListBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind() {
            getItem(bindingAdapterPosition)?.source?.let { source ->
                binding.name.text = source.name
                binding.brand.text = source.manufacturerName ?: ""
                binding.price.text = formatPrice(
                    binding.root.context,
                    currency,
                    source.regularPrice,
                    source.finalPrice
                )

                val badges = source.badgeName?.split(",")
                if (badges.isNullOrEmpty()) {
                    binding.viewTags.visibility = View.GONE
                } else {
                    badges.forEach { badge ->
                        if (badge.isNotEmpty()) {
                            binding.viewTags.visibility = View.VISIBLE
                            val chip = Chip(binding.viewTags.context)
                            chip.setTextAppearance(R.style.TextLabelChip)
                            chip.text = badge
                            binding.viewTags.addView(chip)
                        }
                    }
                }
                GlideApp.with(binding.image)
                    .load(BuildConfig.IMAGE_URL_LIVE.plus(source.image))
                    .into(binding.image)
                binding.cardContent.setOnClickListener {
                    onProductClicked?.invoke(source, binding.image)
                }
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

    object DiffUtils : DiffUtil.ItemCallback<BaseModel.Hit<Product>>() {

        override fun areItemsTheSame(
            oldItem: BaseModel.Hit<Product>,
            newItem: BaseModel.Hit<Product>
        ): Boolean {
            return oldItem.source?.id == newItem.source?.id
        }

        override fun areContentsTheSame(
            oldItem: BaseModel.Hit<Product>,
            newItem: BaseModel.Hit<Product>
        ): Boolean {
            return oldItem.source == newItem.source
        }
    }

    var onProductClicked: ((Product, AppCompatImageView) -> Unit)? = null
}