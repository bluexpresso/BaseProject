package com.idslogic.levelshoes.ui.home.search

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EdgeEffect
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.idslogic.levelshoes.R
import com.idslogic.levelshoes.databinding.FragmentSearchItemBinding
import com.idslogic.levelshoes.network.Status
import com.idslogic.levelshoes.ui.BaseFragment
import com.idslogic.levelshoes.ui.MainViewModel
import com.idslogic.levelshoes.utils.*
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import java.util.*

@AndroidEntryPoint
class SearchItemFragment : BaseFragment() {
    val viewModel: SearchItemViewModel by viewModels()
    private val activityViewModel: MainViewModel by activityViewModels()
    private lateinit var adapter: SearchResultsProductAdapter

    companion object {
        fun newInstance(gender: String): SearchItemFragment {
            return SearchItemFragment().apply {
                arguments =
                    Bundle().apply {
                        putString(ARG_GENDER, gender)
                    }
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val binding = DataBindingUtil.inflate(
            inflater, R.layout.fragment_search_item,
            container, false
        ) as FragmentSearchItemBinding
        initSearch(binding)
        initCategorySearch(binding)
        return binding.root
    }

    private fun initCategorySearch(binding: FragmentSearchItemBinding) {
        val categoriesAdapter = SearchResultsCategoryAdapter()
        binding.listCategorySearch.adapter = categoriesAdapter
        viewModel.categorySearchLiveData.observe(viewLifecycleOwner, {
            when (it.status) {
                Status.SUCCESS -> {
                    binding.listCategorySearch.visibility = View.VISIBLE
                    categoriesAdapter.setItems(it.data ?: arrayListOf())
                    categoriesAdapter.onCategoryItemClick = { catSearch ->
                        activityViewModel.clearSearchFocusLiveData.value = true
                        getCategoryToPLPNavigation(
                            findNavController(),
                            R.id.nav_from_search_results_to_product_list,
                            viewModel.gender,
                            catSearch.first,
                            catSearch.second
                        )
                    }
                }
                Status.ERROR -> {
                    binding.listCategorySearch.visibility = View.GONE
                }
                else -> {
                    binding.listCategorySearch.visibility = View.GONE
                }
            }
        })
    }

    private fun initSearch(binding: FragmentSearchItemBinding) {
        binding.listSearchResults.adapter = adapter
        createEdgeEffect(binding)
        viewModel.productSearchResultsLiveData.observe(viewLifecycleOwner, {
            when (it.status) {
                Status.SUCCESS -> {
                    if (it.data?.result.isNullOrEmpty()) showNoResults(binding)
                    else showSearchResults(binding)
                    binding.titleSearchResults.visibility = View.VISIBLE
                    binding.productCount.visibility = View.VISIBLE
                    binding.productCount.text =
                        String.format(
                            Locale.ENGLISH, getString(R.string.number_of_products),
                            it.data?.result?.size ?: 0
                        )
                    adapter.setItems(it.data?.result)
                }
                Status.ERROR -> {
                    showNoResults(binding)
                }
                else -> {
                }
            }
        })
        activityViewModel.searchTerm.observe(viewLifecycleOwner, {
            if (it.isNotEmpty()) {
                loadSearchResults(it)
                loadCategoriesSearchResults(it)
            }
        })
    }

    private fun loadCategoriesSearchResults(searchTerm: String) {
        lifecycleScope.launch {
            viewModel.getCategoriesSearchResults(searchTerm)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel.gender = arguments?.getString(ARG_GENDER, GENDER_WOMEN) ?: GENDER_WOMEN
        viewModel.categories = activityViewModel.searchCategories
        adapter = SearchResultsProductAdapter(viewModel.getSelectedCurrency())
    }

    private fun loadSearchResults(searchTerm: String) {
        lifecycleScope.launch {
            viewModel.getProductsFromCategory(searchTerm, viewModel.gender)
        }
    }

    private fun showSearchResults(binding: FragmentSearchItemBinding) {
        binding.listSearchResults.visibility = View.VISIBLE
        binding.noResults.visibility = View.GONE
    }

    private fun showNoResults(binding: FragmentSearchItemBinding) {
        binding.listSearchResults.visibility = View.GONE
        adapter.clear()
        binding.productCount.text =
            String.format(
                Locale.getDefault(), getString(R.string.number_of_products), 0
            )
        binding.noResults.visibility = View.VISIBLE
    }

    private fun createEdgeEffect(binding: FragmentSearchItemBinding) {
        binding.listSearchResults.edgeEffectFactory = object : RecyclerView.EdgeEffectFactory() {
            override fun createEdgeEffect(recyclerView: RecyclerView, direction: Int): EdgeEffect {
                return object : EdgeEffect(recyclerView.context) {

                    override fun onPull(deltaDistance: Float) {
                        super.onPull(deltaDistance)
                        handlePull(deltaDistance)
                    }

                    override fun onPull(deltaDistance: Float, displacement: Float) {
                        super.onPull(deltaDistance, displacement)
                        handlePull(deltaDistance)
                    }

                    private fun handlePull(deltaDistance: Float) {
                        // This is called on every touch event while the list is scrolled with a finger.
                        // We simply update the view properties without animation.
                        val sign = if (direction == DIRECTION_BOTTOM) -1 else 1
                        val rotationDelta = sign * deltaDistance * OVERSCROLL_ROTATION_MAGNITUDE
                        val translationYDelta =
                            sign * recyclerView.width * deltaDistance * OVERSCROLL_TRANSLATION_MAGNITUDE
                        recyclerView.forEachVisibleHolder { holder: SearchResultsProductAdapter.ViewHolder ->
//                            holder.rotation.cancel()
                            holder.translationY.cancel()
//                            holder.itemView.rotation += rotationDelta
                            holder.itemView.translationY += translationYDelta
                        }
                    }

                    override fun onRelease() {
                        super.onRelease()
                        // The finger is lifted. This is when we should start the animations to bring
                        // the view property values back to their resting states.
                        recyclerView.forEachVisibleHolder { holder: SearchResultsProductAdapter.ViewHolder ->
//                            holder.rotation.start()
                            holder.translationY.start()
                        }
                    }

                    override fun onAbsorb(velocity: Int) {
                        super.onAbsorb(velocity)
                        val sign = if (direction == DIRECTION_BOTTOM) -1 else 1
                        // The list has reached the edge on fling.
                        val translationVelocity = sign * velocity * FLING_TRANSLATION_MAGNITUDE
                        recyclerView.forEachVisibleHolder { holder: SearchResultsProductAdapter.ViewHolder ->
                            holder.translationY
                                .setStartVelocity(translationVelocity)
                                .start()
                        }
                    }
                }
            }
        }
    }
}