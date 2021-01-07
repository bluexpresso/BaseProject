package com.ankuradlakha.baseproject.ui.home.landing

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.appcompat.widget.AppCompatImageView
import androidx.recyclerview.widget.RecyclerView
import com.ankuradlakha.baseproject.data.models.BaseModel
import com.ankuradlakha.baseproject.data.models.Product
import com.ankuradlakha.baseproject.databinding.ItemProductViewBinding
import com.ankuradlakha.baseproject.di.GlideApp

class LandingProductPagerAdapter(
    private val onProductSelected: ((BaseModel.Hit<Product>, AppCompatImageView) -> Unit)?
) :
    RecyclerView.Adapter<LandingProductPagerAdapter.ViewHolder>() {
    var products: ArrayList<BaseModel.Hit<Product>>? = null
    override fun getItemCount() = products?.size ?: 0


    fun setItems(products: ArrayList<BaseModel.Hit<Product>>?) {
        this.products = products
        notifyDataSetChanged()
    }

    inner class ViewHolder(private val binding: ItemProductViewBinding) :
        RecyclerView.ViewHolder(binding.root) {
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
                    "AED"
                )
            binding.productImage.setOnClickListener {
                onProductSelected?.invoke(products!![adapterPosition],binding.productImage)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            ItemProductViewBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind()
    }
}