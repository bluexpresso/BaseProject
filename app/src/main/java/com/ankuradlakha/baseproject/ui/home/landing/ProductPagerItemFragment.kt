package com.ankuradlakha.baseproject.ui.home.landing

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.ankuradlakha.baseproject.R
import com.ankuradlakha.baseproject.data.models.Product
import com.ankuradlakha.baseproject.databinding.ItemProductViewBinding
import com.ankuradlakha.baseproject.di.GlideApp
import com.google.gson.Gson

class ProductPagerItemFragment : Fragment() {
    lateinit var viewModel: ProductPagerItemViewModel

    companion object {
        const val ARG_PRODUCT = "arg_product"
        fun newInstance(product: Product?) = ProductPagerItemFragment().apply {
            arguments = Bundle().apply { putString(ARG_PRODUCT, Gson().toJson(product)) }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val binding =
            DataBindingUtil.inflate(inflater, R.layout.item_product_view, container, false)
                    as ItemProductViewBinding
        viewModel = ViewModelProvider(this).get(ProductPagerItemViewModel::class.java)
        viewModel.product =
            Gson().fromJson(arguments?.getString(ARG_PRODUCT, ""), Product::class.java)
        initProduct(binding)
        return binding.root
    }

    private fun initProduct(binding: ItemProductViewBinding) {
        GlideApp.with(binding.productImage)
            .load("https://raw.githubusercontent.com/bluexpresso/Pashu-Pakshi/gh-pages/g3184115ricgsv-glgz_1.jpg")
            .into(binding.productImage)
        binding.name.text = "N/A"
        binding.brand.text = "Brand"
        binding.price.text = "123AED"
    }
}