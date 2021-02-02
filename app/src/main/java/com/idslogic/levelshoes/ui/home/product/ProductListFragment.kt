package com.idslogic.levelshoes.ui.home.product

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.idslogic.levelshoes.R
import com.idslogic.levelshoes.databinding.FragmentProductListBinding
import com.idslogic.levelshoes.ui.BaseFragment
import com.idslogic.levelshoes.ui.MainViewModel
import com.idslogic.levelshoes.utils.*
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class ProductListFragment : BaseFragment() {
    val activityViewModel: MainViewModel by activityViewModels()
    lateinit var viewModel: ProductListViewModel
    lateinit var productListAdapter: ProductListAdapter
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val binding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_product_list, container, false)
                    as FragmentProductListBinding
        viewModel = ViewModelProvider(this).get(ProductListViewModel::class.java)
        getArgs()
        initToolbar(binding.toolbar)
        initProducts(binding)
        initCategory(binding)
        return binding.root
    }

    private fun initCategory(binding: FragmentProductListBinding) {
        viewModel.categoryIdLiveData.observe(viewLifecycleOwner, {
            if (it > 0) {
                binding.shimmerLoadingView.visibility = View.VISIBLE
                binding.viewProducts.visibility = View.GONE
                lifecycleScope.launch {
                    viewModel.getProductsFromCategory()
                }
            }
        })
    }

    private fun getArgs() {
        viewModel.gender = arguments?.getString(ARG_GENDER, GENDER_WOMEN) ?: GENDER_WOMEN
        viewModel.categoryIdLiveData.value = arguments?.getInt(ARG_CATEGORY_ID, 0)
        viewModel.title = arguments?.getString(ARG_TITLE, "")
    }

    private fun initProducts(binding: FragmentProductListBinding) {
        productListAdapter = ProductListAdapter(viewModel.getSelectedCurrency())
        binding.viewProducts.adapter = productListAdapter
        viewModel.productIdsLiveData.observe(viewLifecycleOwner, {
            if (!it.isNullOrEmpty()) {
                binding.shimmerLoadingView.visibility = View.GONE
                binding.viewProducts.visibility = View.VISIBLE
                lifecycleScope.launch {
                    viewModel.getProductPagingLiveData(viewModel.categoryIdLiveData.value ?: -1)
                        ?.observe(viewLifecycleOwner, { pagingData ->
                            productListAdapter.submitData(lifecycle, pagingData)
                        })
                }
            }
        })
    }

    private fun initToolbar(toolbar: CustomToolbar) {
        toolbar.setTitle(viewModel.title)
        toolbar.onLeftIconClick = {
            findNavController().navigateUp()
        }
        toolbar.onRightIconClick = {
            findNavController().navigate(R.id.action_to_filters)
        }
    }
}