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
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.google.gson.Gson
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
    val viewModel by activityViewModels<ProductListViewModel>()
    lateinit var productListAdapter: ProductListAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
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
        initCategory(binding)
        initFilters(binding)
        return binding.root
    }

    private fun initFilters(binding: FragmentProductListBinding) {
        viewModel.filterData.observe(viewLifecycleOwner, EventObserver {
            Toast.makeText(requireContext(), Gson().toJson(it), Toast.LENGTH_SHORT).show()
        })
    }

    private fun initCategory(binding: FragmentProductListBinding) {
        binding.shimmerLoadingView.visibility = View.VISIBLE
        binding.viewProducts.visibility = View.GONE
        if (isInternetAvailable(requireContext())) {
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
    }

    private fun initProducts(binding: FragmentProductListBinding) {
        productListAdapter = ProductListAdapter(viewModel.getSelectedCurrency())
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
        binding.viewProducts.adapter = productListAdapter
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
        viewModel.productIdsLiveData.observe(viewLifecycleOwner, {
            if (!it.isNullOrEmpty()) {
                binding.shimmerLoadingView.visibility = View.GONE
                binding.viewProducts.visibility = View.VISIBLE
                lifecycleScope.launch {
                    viewModel.getProductPagingLiveData()
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