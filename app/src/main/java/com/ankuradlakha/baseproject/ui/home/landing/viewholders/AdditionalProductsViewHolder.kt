package com.ankuradlakha.baseproject.ui.home.landing.viewholders

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.ankuradlakha.baseproject.data.models.BaseModel
import com.ankuradlakha.baseproject.data.models.Content
import com.ankuradlakha.baseproject.data.models.Product
import com.ankuradlakha.baseproject.databinding.ItemAdditionalProductBinding
import com.ankuradlakha.baseproject.databinding.ItemLandingAdditionalProductsViewBinding
import com.ankuradlakha.baseproject.di.GlideApp

class AdditionalProductsViewHolder(val binding: ItemLandingAdditionalProductsViewBinding) :
    RecyclerView.ViewHolder(binding.root) {
    fun bind(content: Content) {
        binding.contentSubtitle.text = content.subTitle ?: ""
        binding.contentTitle.text = content.title ?: ""
        val adapter = AdditionalProductsListAdapter()
        binding.productsList.adapter = adapter
        adapter.setItems(content.productsList)
    }

    class AdditionalProductsListAdapter :
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
                binding.name.text = "Sandal"
                binding.brand.text = "BRAND"
                binding.price.text = "123AED"
                binding.serialNumber.setText("0${adapterPosition + 1}")
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

        override fun getItemCount() = 10

        fun setItems(products: ArrayList<BaseModel.Hit<Product>>?) {
            this.products = products
            notifyDataSetChanged()
        }
    }
}