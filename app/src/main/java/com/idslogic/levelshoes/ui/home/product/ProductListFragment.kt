package com.idslogic.levelshoes.ui.home.product

import android.graphics.Rect
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.idslogic.levelshoes.R
import com.idslogic.levelshoes.databinding.FragmentProductListBinding
import com.idslogic.levelshoes.network.Status.LOADING
import com.idslogic.levelshoes.network.Status.SUCCESS
import com.idslogic.levelshoes.ui.BaseFragment
import com.idslogic.levelshoes.ui.MainViewModel
import com.idslogic.levelshoes.ui.home.product.filters.FiltersViewModel
import com.idslogic.levelshoes.utils.*
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class ProductListFragment : BaseFragment() {
    val activityViewModel by activityViewModels<MainViewModel>()
    private val filtersViewModel by activityViewModels<FiltersViewModel>()
    val viewModel by viewModels<ProductListViewModel>()

    //    lateinit var productListPagingAdapter: ProductListPagingAdapter
    private lateinit var productsListAdapter: ProductListAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        productsListAdapter = ProductListAdapter(viewModel.getSelectedCurrency())
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val binding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_product_list, container, false)
                    as FragmentProductListBinding
        changeStatusBarColor(R.color.background)
        readArguments()
        initToolbar(binding.toolbar)
        initProducts(binding)
        if (viewModel.productIdsLiveData.value.isNullOrEmpty()) {
            initCategory(binding)
            observeProductIds(binding)
        } else {
            loadProducts(binding)
        }
        return binding.root
    }

    private fun initCategory(binding: FragmentProductListBinding) {
        binding.shimmerLoadingView.visibility = View.GONE
        binding.viewProducts.visibility = View.VISIBLE
        if (isInternetAvailable(requireContext())) {
            binding.shimmerLoadingView.visibility = View.VISIBLE
            binding.viewProducts.visibility = View.GONE
            lifecycleScope.launch {
                viewModel.getProductsFromCategory()
            }
        } else {
            getNoInternetDialog(requireContext()).setPositiveButton(
                R.string.retry
            ) { _, _ ->
                initCategory(binding)
            }.show()
        }
    }

    private fun readArguments() {
        viewModel.gender = arguments?.getString(ARG_GENDER, GENDER_WOMEN) ?: GENDER_WOMEN
        viewModel.categoryId = arguments?.getInt(ARG_CATEGORY_ID, NO_CATEGORY) ?: NO_CATEGORY
        viewModel.title = arguments?.getString(ARG_TITLE, "")
        viewModel.parentCategoryId =
            arguments?.getInt(ARG_PARENT_CATEGORY_ID, NO_CATEGORY) ?: NO_CATEGORY
        viewModel.categoryPath = arguments?.getString(ARG_CATEGORY_PATH, null)
        viewModel.genderFilter = arguments?.getString(ARG_GENDER_FILTER, "") ?: ""
        viewModel.filterData = filtersViewModel.filterData.value?.peekContent()
    }

    private fun initProducts(binding: FragmentProductListBinding) {
//        productListAdapter.onProductClicked = { product, transitionImage ->
//            findNavController().navigate(
//                R.id.action_to_product_details, Bundle().apply {
//                    putString(ARG_PRODUCT, Gson().toJson(product))
//                }, null, FragmentNavigatorExtras(
//                    transitionImage to
//                            transitionImage.transitionName
//                )
//            )
//        }
        binding.viewProducts.adapter = productsListAdapter
        binding.viewProducts.addItemDecoration(object : RecyclerView.ItemDecoration() {
            override fun getItemOffsets(
                outRect: Rect,
                view: View,
                parent: RecyclerView,
                state: RecyclerView.State
            ) {
                if (parent.getChildLayoutPosition(view) % 2 == 0) {
                    outRect.right = 1
                } else {
                    outRect.left = 0
                }
            }
        })
    }

    private fun observeProductIds(binding: FragmentProductListBinding) {
        viewModel.productIdsLiveData.observe(viewLifecycleOwner, {
            if (!it.isNullOrEmpty()) {
                loadProducts(binding)
            }
        })
        viewModel.filtersListLiveData.observe(viewLifecycleOwner, {
            when (it.status) {
                LOADING -> {
                    binding.toolbar.disableRightIcon()
                }
                SUCCESS -> {
                    filtersViewModel.filtersList = it.data
                    binding.toolbar.enableRightIcon()
                }
                else -> {
                    binding.toolbar.enableRightIcon()
                }
            }
        })
        viewModel.productPriceLiveData.observe(viewLifecycleOwner, {
            if (filtersViewModel.filterData.value?.peekContent() == null) {
                filtersViewModel.price = it
            } else {
                filtersViewModel.filteredPrice = it
            }
        })
    }

    private fun loadProducts(binding: FragmentProductListBinding) {
        lifecycleScope.launch {
            viewModel.getProductsList(filtersViewModel.filterData.value?.peekContent())
                .observe(viewLifecycleOwner, { resource ->
                    when (resource.status) {
                        LOADING -> {
                            binding.shimmerLoadingView.visibility = View.VISIBLE
                            binding.viewProducts.visibility = View.GONE
                        }
                        SUCCESS -> {
                            binding.shimmerLoadingView.visibility = View.GONE
                            binding.viewProducts.visibility = View.VISIBLE
                            productsListAdapter.setItems(
                                resource.data ?: arrayListOf()
                            )
                        }
                        else -> {
                            binding.shimmerLoadingView.visibility = View.GONE
                            binding.viewProducts.visibility = View.GONE
                            Toast.makeText(
                                requireContext(), "No products found",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    }
                })
        }
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