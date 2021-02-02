package com.idslogic.levelshoes.ui.home.landing

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.appcompat.widget.AppCompatImageView
import androidx.recyclerview.widget.RecyclerView
import com.idslogic.levelshoes.data.models.BaseModel
import com.idslogic.levelshoes.data.models.Content
import com.idslogic.levelshoes.data.models.Product
import com.idslogic.levelshoes.databinding.ItemProductViewBinding
import com.idslogic.levelshoes.databinding.ItemViewAllCollectionBinding
import com.idslogic.levelshoes.di.GlideApp
import com.idslogic.levelshoes.utils.VIEW_ALL_COLLECTION
import com.idslogic.levelshoes.utils.formatPrice

class LandingProductPagerAdapter(
    private val onViewAllProducts: ((Int, String?) -> Unit)?,
    private val onProductSelected: ((BaseModel.Hit<Product>, AppCompatImageView) -> Unit)?,
    private val selectedCurrency: String
) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    companion object {
        const val VIEW_TYPE_PRODUCT = 1
        const val VIEW_TYPE_VIEW_ALL_COLLECTION = 2
    }

    private var title: String? = null
    private var categoryId: Int = -1
    var products: ArrayList<BaseModel.Hit<Product>>? = null
    override fun getItemCount() = products?.size ?: 0
    override fun getItemViewType(position: Int): Int {
        val name = products!![position].source?.name ?: ""
        if (name.equals(VIEW_ALL_COLLECTION, true)) {
            return VIEW_TYPE_VIEW_ALL_COLLECTION
        }
        return VIEW_TYPE_PRODUCT
    }

    fun setItems(content: Content) {
        this.categoryId = content.categoryId
        this.products = content.productsList
        this.title = content.title
        notifyDataSetChanged()
    }

    inner class ViewHolderViewAllCollection(private val binding: ItemViewAllCollectionBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind() {
            binding.root.setOnClickListener {
                onViewAllProducts?.invoke(categoryId,title)
            }
        }
    }

    inner class ViewHolder(private val binding: ItemProductViewBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind() {
            products!![bindingAdapterPosition].source?.let { product ->
                GlideApp.with(binding.productImage)
                    .load(product.displayableImage)
                    .into(binding.productImage)
                binding.name.text = product.name
                binding.brand.text = product.manufacturerName
                binding.price.text = formatPrice(
                    binding.root.context,
                    selectedCurrency,
                    product.regularPrice,
                    product.finalPrice
                )
            }
            binding.productImage.setOnClickListener {
                onProductSelected?.invoke(products!![bindingAdapterPosition], binding.productImage)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return if (viewType == VIEW_TYPE_PRODUCT) ViewHolder(
            ItemProductViewBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
        else ViewHolderViewAllCollection(
            ItemViewAllCollectionBinding.inflate(
                LayoutInflater.from(
                    parent.context
                ), parent, false
            )
        )
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder is ViewHolder)
            holder.bind()
        else if (holder is ViewHolderViewAllCollection)
            holder.bind()
    }
}