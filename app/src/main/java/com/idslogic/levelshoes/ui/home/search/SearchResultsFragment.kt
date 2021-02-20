package com.idslogic.levelshoes.ui.home.search

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.widget.EditText
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.google.android.material.tabs.TabLayout
import com.idslogic.levelshoes.R
import com.idslogic.levelshoes.databinding.FragmentSearchResultsBinding
import com.idslogic.levelshoes.network.Status.*
import com.idslogic.levelshoes.ui.BaseFragment
import com.idslogic.levelshoes.ui.MainViewModel
import com.idslogic.levelshoes.utils.*
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class SearchResultsFragment : BaseFragment() {
    private val viewModel: SearchResultsViewModel by viewModels()
    private val recentTrendingAdapter = SearchRecentTrendingAdapter()
    private val activityViewModel: MainViewModel by activityViewModels()
    private var searchEditText: EditText? = null
    private lateinit var searchPagerAdapter: SearchItemsPagerAdapter
    private var isSearchEnabled = false
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel.gender = when (activityViewModel.selectedSearchTab) {
            0 -> GENDER_WOMEN
            1 -> GENDER_MEN
            2 -> GENDER_KIDS
            else -> viewModel.getSelectedGender()
        }
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
        changeStatusBarColor(R.color.secondaryBackground)
        isSearchEnabled = !activityViewModel.searchTerm.value.isNullOrEmpty()
        viewModel.recentTrendingList.clear()
        Handler(Looper.myLooper()!!).postDelayed(
            {
                showSoftKeyboard(binding.searchBox.editText)
            }, 500
        )
        if (!isSearchEnabled) {
            initTrendingItems(binding)
            lifecycleScope.launch {
                viewModel.getTendingItems()
            }
        }
        initTabs(binding)
        initCloseButton(binding)
        initSearch(binding)
        activityViewModel.clearSearchFocusLiveData.observe(viewLifecycleOwner, {
            hideSoftKeyboard(binding.searchBox.editText)
        })
        return binding.root
    }

    private fun initCloseButton(binding: FragmentSearchResultsBinding) {
        binding.rightCloseIconResult.setOnClickListener {
            hideSoftKeyboard(binding.searchBox.editText)
            activity?.onBackPressed()
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
            if (!it.isNullOrBlank()) {
                binding.searchBox.editText?.setText(it.toString())
                binding.searchBox.editText?.setSelection(binding.searchBox.editText?.length() ?: 0)
            }
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
        searchEditText = binding.searchBox.editText
        binding.searchBox.editText?.setSelection(binding.searchBox.editText?.text?.length ?: 0)
        binding.searchBox.editText?.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if (s?.trim()?.length ?: 0 >= 3) {
                    getSearchResults(binding)
//                    getCategorySearchResults(viewModel.gender, binding)
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
        if (!activityViewModel.searchTerm.value.isNullOrEmpty()) {
            binding.searchBox.editText?.setText(activityViewModel.searchTerm.value ?: "")
            binding.searchBox.editText?.setSelection(
                activityViewModel.searchTerm.value?.length ?: 0
            )
        }
    }

    private fun initTabs(binding: FragmentSearchResultsBinding) {
        binding.searchItemPager.offscreenPageLimit = 3
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
                            hideSoftKeyboard(binding.searchBox.editText)
                            binding.searchItemPager.setCurrentItem(0, true)
                            getSearchResults(binding)
                            activityViewModel.selectedSearchTab = 0
                            GENDER_WOMEN
                        }
                        1 -> {
                            hideSoftKeyboard(binding.searchBox.editText)
                            binding.searchItemPager.setCurrentItem(1, true)
                            getSearchResults(binding)
                            activityViewModel.selectedSearchTab = 1
                            GENDER_MEN
                        }
                        2 -> {
                            hideSoftKeyboard(binding.searchBox.editText)
                            binding.searchItemPager.setCurrentItem(2, true)
                            getSearchResults(binding)
                            activityViewModel.selectedSearchTab = 2
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
                formatTabFonts(requireContext(), binding.categorySliderTabLayout, 0, 3)
            }
            GENDER_MEN -> {
                binding.categorySliderTabLayout.selectTab(binding.categorySliderTabLayout.getTabAt(1))
                formatTabFonts(requireContext(), binding.categorySliderTabLayout, 1, 3)
            }
            GENDER_KIDS -> {
                binding.categorySliderTabLayout.selectTab(binding.categorySliderTabLayout.getTabAt(2))
                formatTabFonts(requireContext(), binding.categorySliderTabLayout, 2, 3)
            }
        }
        searchPagerAdapter = SearchItemsPagerAdapter(this)
        binding.searchItemPager.offscreenPageLimit = 3
        binding.searchItemPager.adapter = searchPagerAdapter
        binding.searchItemPager.isUserInputEnabled = false
    }
}