package com.ankuradlakha.baseproject.ui.home.product

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.ankuradlakha.baseproject.R
import com.ankuradlakha.baseproject.data.models.BaseModel
import com.ankuradlakha.baseproject.data.models.Product
import com.ankuradlakha.baseproject.databinding.FragmentProductDetailsBinding
import com.google.android.material.tabs.TabLayoutMediator
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import dagger.hilt.android.AndroidEntryPoint
import java.util.concurrent.TimeUnit

@AndroidEntryPoint
class ProductDetailsFragment : Fragment() {
    private lateinit var viewModel: ProductDetailsViewModel

    companion object {
        const val ARG_PRODUCT = "arg_product"
        fun newInstance() = ProductDetailsFragment()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProvider(this).get(ProductDetailsViewModel::class.java)
        viewModel.product = Gson().fromJson(
            arguments?.getString(ARG_PRODUCT, ""),
            object : TypeToken<BaseModel.Hit<Product>>() {}.type
        )
        sharedElementEnterTransition = androidx.transition.TransitionInflater.from(context)
            .inflateTransition(R.transition.shared_image)
        sharedElementReturnTransition = androidx.transition.TransitionInflater.from(context)
            .inflateTransition(R.transition.shared_image)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val binding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_product_details, container, false)
                    as FragmentProductDetailsBinding
        initImages(binding)
        initProductDetails(binding)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        postponeEnterTransition(100, TimeUnit.MILLISECONDS)
        activity?.window?.apply {
            clearFlags(
                WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS or
                        WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS
            )
        }
    }

    private fun initProductDetails(binding: FragmentProductDetailsBinding) {
        binding.name.text = viewModel.product.source?.name
        binding.price.text = String.format(
            "%d%s",
            viewModel.product.source?.finalPrice,
            "AED"
        )
        binding.brand.text = viewModel.product.source?.name
        binding.editorialNotes.setHeader(getString(R.string.editorial_notes))
        binding.editorialNotes.setBody(
            "Lorem Ipsum is simply dummy text of the printing and typesetting industry. Lorem Ipsum has been the industry's standard dummy text ever since the 1500s, when an unknown printer took a galley of type and scrambled it to make a type specimen book. It has survived not only five centuries, but also the leap into electronic typesetting, remaining essentially unchanged. It was popularised in the 1960s with the release of Letraset sheets containing Lorem Ipsum passages, and more recently with desktop publishing software like Aldus PageMaker including versions of Lorem Ipsum"
        )
        binding.aboutProduct.setHeader(getString(R.string.about_the_product))
        binding.aboutProduct.setBody(
            "Lorem Ipsum is simply dummy text of the printing and typesetting industry. Lorem Ipsum has been the industry's standard dummy text ever since the 1500s, when an unknown printer took a galley of type and scrambled it to make a type specimen book. It has survived not only five centuries, but also the leap into electronic typesetting, remaining essentially unchanged. It was popularised in the 1960s with the release of Letraset sheets containing Lorem Ipsum passages, and more recently with desktop publishing software like Aldus PageMaker including versions of Lorem Ipsum"
        )
        binding.shippingReturn.setHeader(getString(R.string.shipping_and_returns))
        binding.shippingReturn.setBody(
            "Lorem Ipsum is simply dummy text of the printing and typesetting industry. Lorem Ipsum has been the industry's standard dummy text ever since the 1500s, when an unknown printer took a galley of type and scrambled it to make a type specimen book. It has survived not only five centuries, but also the leap into electronic typesetting, remaining essentially unchanged. It was popularised in the 1960s with the release of Letraset sheets containing Lorem Ipsum passages, and more recently with desktop publishing software like Aldus PageMaker including versions of Lorem Ipsum"
        )
    }

    private fun initImages(binding: FragmentProductDetailsBinding) {
        val adapter = ProductImagesPagerAdapter()
        binding.images.adapter = adapter
        TabLayoutMediator(
            binding.tabs,
            binding.images
        ) { _, _ ->

        }.attach()
    }
}