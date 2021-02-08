package com.idslogic.levelshoes.ui.home.search

import android.graphics.BlendMode
import android.graphics.Color
import android.graphics.Paint
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EdgeEffect
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.RecyclerView
import com.idslogic.levelshoes.R
import com.idslogic.levelshoes.databinding.FragmentSearchItemBinding
import com.idslogic.levelshoes.network.Status
import com.idslogic.levelshoes.ui.BaseFragment
import com.idslogic.levelshoes.ui.MainViewModel
import com.idslogic.levelshoes.utils.ARG_GENDER
import com.idslogic.levelshoes.utils.EdgeEffectHelper
import com.idslogic.levelshoes.utils.GENDER_WOMEN
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import java.util.*

@AndroidEntryPoint
class SearchItemFragment : BaseFragment() {
    val viewModel: SearchItemViewModel by viewModels()
    private val activityViewModel: MainViewModel by activityViewModels()

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
        return binding.root
    }

    private fun initSearch(binding: FragmentSearchItemBinding) {
        val adapter = SearchResultsProductAdapter(viewModel.getSelectedCurrency())
        binding.listSearchResults.adapter = adapter

        viewModel.productSearchResultsLiveData.observe(viewLifecycleOwner, {
            when (it.status) {
                Status.LOADING -> {
                    binding.progressBar.visibility = View.VISIBLE
                }
                Status.SUCCESS -> {
                    binding.progressBar.visibility = View.GONE
                    if (it.data?.result.isNullOrEmpty()) showNoResults(binding)
                    else showSearchResults(binding)
                    binding.productCount.text =
                        String.format(
                            Locale.getDefault(), getString(R.string.number_of_products),
                            it.data?.result?.size ?: 0
                        )
                    adapter.setItems(it.data?.result)
                }
                Status.ERROR -> {
                    binding.progressBar.visibility = View.GONE
                    showNoResults(binding)
                }
            }
        })
        activityViewModel.searchTerm.observe(viewLifecycleOwner, {
            if (it.isNotEmpty()) {
                loadSearchResults(it)
            }
        })
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel.gender = arguments?.getString(ARG_GENDER, GENDER_WOMEN) ?: GENDER_WOMEN
    }

    private fun loadSearchResults(searchTerm: String) {
        lifecycleScope.launch {
            viewModel.getProductsFromCategory(searchTerm, viewModel.gender)
        }
    }

    private fun showSearchResults(binding: FragmentSearchItemBinding) {
        binding.listSearchResults.visibility = View.VISIBLE
        binding.noResults.root.visibility = View.GONE
    }

    private fun showNoResults(binding: FragmentSearchItemBinding) {
        binding.listSearchResults.visibility = View.GONE
        binding.noResults.root.visibility = View.VISIBLE
    }
}