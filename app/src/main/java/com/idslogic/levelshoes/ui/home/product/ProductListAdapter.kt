package com.idslogic.levelshoes.ui.home.product

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.idslogic.levelshoes.BuildConfig
import com.idslogic.levelshoes.data.models.BaseModel
import com.idslogic.levelshoes.data.models.Product
import com.idslogic.levelshoes.databinding.ItemProductInListBinding
import com.idslogic.levelshoes.di.GlideApp
import java.util.*

class ProductListAdapter(val currency: String) :
    PagingDataAdapter<BaseModel.Hit<Product>, ProductListAdapter.ViewHolder>(DiffUtils) {

    inner class ViewHolder(val binding: ItemProductInListBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind() {
            getItem(bindingAdapterPosition)?.source?.let { source ->
                binding.name.text = source.name
                binding.brand.text = source.manufacturerName ?: ""
                source.price?.let {
                    binding.price.text =
                        String.format(Locale.getDefault(), source.price!!, currency)
                }
                GlideApp.with(binding.image)
                    .load(BuildConfig.IMAGE_URL.plus(source.image))
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

    object DiffUtils : DiffUtil.ItemCallback<BaseModel.Hit<Product>>() {

        override fun areItemsTheSame(
            oldItem: BaseModel.Hit<Product>,
            newItem: BaseModel.Hit<Product>
        ): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(
            oldItem: BaseModel.Hit<Product>,
            newItem: BaseModel.Hit<Product>
        ): Boolean {
            return oldItem.hashCode() == newItem.hashCode()
        }
    }
}