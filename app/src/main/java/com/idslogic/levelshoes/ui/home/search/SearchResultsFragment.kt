package com.idslogic.levelshoes.ui.home.search

import android.content.Context
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.google.android.material.tabs.TabLayout
import com.idslogic.levelshoes.R
import com.idslogic.levelshoes.databinding.FragmentSearchResultsBinding
import com.idslogic.levelshoes.network.Status.*
import com.idslogic.levelshoes.ui.MainViewModel
import com.idslogic.levelshoes.utils.*
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class SearchResultsFragment : Fragment() {
    private val viewModel: SearchResultsViewModel by viewModels()
    private val recentTrendingAdapter = SearchRecentTrendingAdapter()
    private val activityViewModel: MainViewModel by activityViewModels()
    private lateinit var searchPagerAdapter: SearchItemsPagerAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel.gender = viewModel.getSelectedGender()
        viewModel.categories = activityViewModel.searchCategories
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val binding = DataBindingUtil.inflate(
            inflater, R.layout.fragment_search_results, container,
            false
        ) as FragmentSearchResultsBinding
        viewModel.recentTrendingList.clear()
        binding.searchBox.editText?.requestFocus()
        openSoftInput(requireContext(), binding.searchBox.editText)
        initTrendingItems(binding)
        initTabs(binding)
        initCloseButton(binding)
        initSearch(binding)
        initCategorySearch(binding)
        lifecycleScope.launch {
            viewModel.getTendingItems()
        }
        return binding.root
    }

    private fun initCategorySearch(binding: FragmentSearchResultsBinding) {
        val adapter = SearchResultsCategoryAdapter()
        binding.listCategorySearch.adapter = adapter
        viewModel.categorySearchLiveData.observe(viewLifecycleOwner, {
            when (it.status) {
                SUCCESS -> {
                    binding.listCategorySearch.visibility = View.VISIBLE
                    adapter.setItems(it.data ?: arrayListOf())
                }
                ERROR -> {
                    binding.listCategorySearch.visibility = View.GONE
                }
                else -> {
                    binding.listCategorySearch.visibility = View.GONE
                }

            }
        })
    }

    private fun initCloseButton(binding: FragmentSearchResultsBinding) {
        binding.rightCloseIconResult.setOnClickListener {
            (activity?.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager).hideSoftInputFromWindow(
                view?.windowToken,
                0
            )
            requireActivity().onBackPressed()
        }
    }

    private fun initTrendingItems(binding: FragmentSearchResultsBinding) {
        binding.listRecentTrending.apply {
            adapter = recentTrendingAdapter
            visibility = View.VISIBLE
        }
        viewModel.trendingProductsLiveData.observe(viewLifecycleOwner, {
            when (it.status) {
                LOADING -> {
                }
                SUCCESS -> {
                    showRecentTrendingList(binding)
                }
                ERROR -> {

                }
            }
        })
        recentTrendingAdapter.onRecentItemClick = {
            if (!it.isNullOrBlank())
                binding.searchBox.editText?.setText(it.toString())
        }
        binding.searchBox.editText?.setOnEditorActionListener { textView, actionId, keyEvent ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                val searchText = binding.searchBox.editText?.text.toString()
                if (searchText.isNotEmpty())
                    viewModel.addRecentSearch(searchText.toString())
            }
            return@setOnEditorActionListener false
        }
    }

    private fun showRecentTrendingList(binding: FragmentSearchResultsBinding) {
        binding.listRecentTrending.visibility = View.VISIBLE
        binding.searchItemPager.visibility = View.GONE
        viewModel.getRecentTrendingList()
        recentTrendingAdapter.setItems(viewModel.recentTrendingList)
    }

    private fun getSearchResults(binding: FragmentSearchResultsBinding) {
        activityViewModel.searchTerm.value = binding.searchBox.editText?.text.toString()
    }


    private fun initSearch(binding: FragmentSearchResultsBinding) {
        binding.searchBox.editText?.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if (s?.trim()?.length ?: 0 >= 3) {
                    getSearchResults(binding)
                    getCategorySearchResults(s.toString())
                    binding.listRecentTrending.visibility = View.GONE
                    binding.searchItemPager.visibility = View.VISIBLE
                } else if (s?.trim()?.length ?: 0 <= 2) {
                    initTrendingItems(binding)
                    binding.listCategorySearch.visibility = View.GONE
                }
            }

            override fun afterTextChanged(s: Editable?) {
            }

        })
    }

    private fun getCategorySearchResults(searchTerm: String) {
        lifecycleScope.launch {
            viewModel.getCategoriesSearchResults(searchTerm)
        }
    }


    private fun initTabs(binding: FragmentSearchResultsBinding) {
        binding.categorySliderTabLayout.addOnTabSelectedListener(object :
            TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab?) {
                if (tab != null) {
                    formatTabFonts(
                        requireContext(),
                        binding.categorySliderTabLayout,
                        tab.position,
                        3
                    )
                    viewModel.gender = when (tab.position) {
                        0 -> {
                            binding.searchItemPager.setCurrentItem(0, false)
                            getSearchResults(binding)
                            GENDER_WOMEN
                        }
                        1 -> {
                            binding.searchItemPager.setCurrentItem(1, false)
                            getSearchResults(binding)
                            GENDER_MEN
                        }
                        2 -> {
                            binding.searchItemPager.setCurrentItem(2, false)
                            getSearchResults(binding)
                            GENDER_KIDS
                        }
                        else -> {
                            GENDER_WOMEN
                        }
                    }
                }
            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {
            }

            override fun onTabReselected(tab: TabLayout.Tab?) {
            }
        })
        when (viewModel.gender) {
            GENDER_WOMEN -> {
                binding.categorySliderTabLayout.selectTab(binding.categorySliderTabLayout.getTabAt(0))
            }
            GENDER_MEN -> {
                binding.categorySliderTabLayout.selectTab(binding.categorySliderTabLayout.getTabAt(1))
            }
            GENDER_KIDS -> {
                binding.categorySliderTabLayout.selectTab(binding.categorySliderTabLayout.getTabAt(2))
            }
        }
        searchPagerAdapter = SearchItemsPagerAdapter(this)
        binding.searchItemPager.offscreenPageLimit = 3
        binding.searchItemPager.adapter = searchPagerAdapter
        binding.searchItemPager.isUserInputEnabled = false
    }

    private fun loadCategories(binding: FragmentSearchResultsBinding, gender: String) {

    }

}