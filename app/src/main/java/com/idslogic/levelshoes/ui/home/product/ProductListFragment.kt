package com.idslogic.levelshoes.ui.home.product

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.idslogic.levelshoes.R
import com.idslogic.levelshoes.databinding.FragmentProductListBinding
import com.idslogic.levelshoes.network.Status
import com.idslogic.levelshoes.network.Status.*
import com.idslogic.levelshoes.ui.BaseFragment
import com.idslogic.levelshoes.ui.MainViewModel
import com.idslogic.levelshoes.utils.CustomToolbar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class ProductListFragment : BaseFragment() {
    val activityViewModel: MainViewModel by activityViewModels()
    lateinit var viewModel: ProductListViewModel
    val productListAdapter = ProductListAdapter()
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val binding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_product_list, container, false)
                    as FragmentProductListBinding
        viewModel = ViewModelProvider(this).get(ProductListViewModel::class.java)
        initToolbar(binding.toolbar)
        initProducts(binding)
        return binding.root
    }

    private fun initProducts(binding: FragmentProductListBinding) {
        binding.viewProducts.adapter = productListAdapter
        viewModel.productsList.observe(viewLifecycleOwner, {
            when (it.status) {
                LOADING -> {
                    binding.progressBar.visibility = View.VISIBLE
                }
                SUCCESS -> {
                    binding.progressBar.visibility = View.GONE
                    productListAdapter.setItems(it.data?.result)
                    binding.viewProducts.layoutAnimation = AnimationUtils.loadLayoutAnimation(
                        requireContext(),
                        R.anim.product_list_animation
                    )
                    binding.viewProducts.scheduleLayoutAnimation()
                }
                ERROR -> {
                    binding.progressBar.visibility = View.GONE
                }

            }
        })
        lifecycleScope.launch {
            viewModel.getCategoryProducts("")
        }
    }

    private fun initToolbar(toolbar: CustomToolbar) {
        toolbar.onLeftIconClick = {
            findNavController().navigateUp()
        }
        toolbar.onRightIconClick = {
            findNavController().navigate(R.id.action_to_filters)
        }
    }
}