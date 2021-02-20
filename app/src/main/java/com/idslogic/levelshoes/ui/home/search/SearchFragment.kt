package com.idslogic.levelshoes.ui.home.search

import android.animation.Animator
import android.animation.ObjectAnimator
import android.animation.TimeInterpolator
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import android.view.animation.DecelerateInterpolator
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.google.android.material.tabs.TabLayout
import com.google.gson.Gson
import com.idslogic.levelshoes.R
import com.idslogic.levelshoes.data.models.CategorySearch
import com.idslogic.levelshoes.databinding.FragmentSearchBinding
import com.idslogic.levelshoes.network.Status
import com.idslogic.levelshoes.ui.BaseFragment
import com.idslogic.levelshoes.ui.MainViewModel
import com.idslogic.levelshoes.utils.*
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch


@AndroidEntryPoint
class SearchFragment : BaseFragment() {
    private val activityViewModel: MainViewModel by activityViewModels()
    private val viewModel: SearchViewModel by viewModels()
    private val adapter = SearchCategoryAdapter()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val binding = DataBindingUtil.inflate(
            inflater, R.layout.fragment_search, container,
            false
        ) as FragmentSearchBinding
        activityViewModel.searchTerm.value = ""
        initToolbar(binding)
        initTabs(binding)
        initSearch(binding)
        changeStatusBarColor(R.color.secondaryBackground)
        startSlidingAnimation(binding)
        return binding.root
    }

    private fun startSlidingAnimation(binding: FragmentSearchBinding) {
        if (!activityViewModel.disableSearchAnimation) {
            ObjectAnimator.ofFloat(binding.root, "translationY", 2000f, 0f).apply {
                duration = 700
                interpolator = DecelerateInterpolator()
                setAutoCancel(true)
                start()
            }
        }
    }

    private fun endSlidingAnimation(binding: FragmentSearchBinding) {
        ObjectAnimator.ofFloat(binding.root, "translationY", binding.root.translationY * 0.8f)
            .apply {
                duration = 500
                interpolator = DecelerateInterpolator()
                setAutoCancel(true)
                start()
            }.addListener(object : Animator.AnimatorListener {
                override fun onAnimationStart(p0: Animator?) {
                }

                override fun onAnimationEnd(p0: Animator?) {
                    activity?.onBackPressed()
                }

                override fun onAnimationCancel(p0: Animator?) {
                }

                override fun onAnimationRepeat(p0: Animator?) {
                }
            })
    }

    private fun initToolbar(binding: FragmentSearchBinding) {
        binding.toolbar.onLeftIconClick = {
            activity?.onBackPressed()
        }
    }

    private fun initSearch(binding: FragmentSearchBinding) {
        binding.recyclerviewSearch.adapter = adapter
        viewModel.categoryFetcherLiveData.observe(viewLifecycleOwner, {
            when (it.status) {
                Status.LOADING -> {
                }
                Status.SUCCESS -> {
                    val controller =
                        AnimationUtils.loadLayoutAnimation(
                            context,
                            R.anim.search_categories_layout_animation
                        );

                    binding.recyclerviewSearch.layoutAnimation = controller
                    adapter.setItems(viewModel.mapCategories[viewModel.gender])
                    binding.recyclerviewSearch.scheduleLayoutAnimation()
                }
                Status.ERROR -> {
                }
            }
        })
        binding.viewMaskSearch.setOnClickListener {
            val activityViewModel: MainViewModel by activityViewModels()
            activityViewModel.searchCategories = viewModel.mapCategories
            activityViewModel.selectedSearchTab =
                binding.categorySliderTabLayout.selectedTabPosition
            findNavController().navigate(R.id.navigateFromCategoryToSearch)
        }
        adapter.onCategorySelected =
            { category: CategorySearch, categories: ArrayList<CategorySearch>? ->
                if (categories != null && !categories.isNullOrEmpty()) {
                    activityViewModel.disableSearchAnimation = true
                    findNavController().navigate(R.id.navigateToSubCategory, Bundle().apply {
                        putString(ARG_CATEGORIES, Gson().toJson(categories))
                        putString(ARG_CATEGORY, Gson().toJson(category))
                        putString(ARG_GENDER, viewModel.gender)
                    })
                }
            }
    }

    private fun initTabs(binding: FragmentSearchBinding) {
        binding.categorySliderTabLayout.addOnTabSelectedListener(object :
            TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab?) {
                if (tab != null) {
                    formatTabFonts(
                        binding.root.context,
                        binding.categorySliderTabLayout,
                        tab.position,
                        3
                    )
                    loadCategories(
                        when (tab.position) {
                            0 -> {
                                viewModel.gender = GENDER_WOMEN
                                GENDER_WOMEN
                            }
                            1 -> {
                                viewModel.gender = GENDER_MEN
                                GENDER_MEN
                            }
                            2 -> {
                                viewModel.gender = GENDER_KIDS
                                GENDER_KIDS
                            }
                            else -> GENDER_WOMEN
                        }
                    )
                }
            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {
            }

            override fun onTabReselected(tab: TabLayout.Tab?) {
            }
        })
        when (activityViewModel.selectedSearchTab) {
            0 -> viewModel.gender = GENDER_WOMEN
            1 -> viewModel.gender = GENDER_MEN
            2 -> viewModel.gender = GENDER_KIDS
        }
        when (viewModel.gender) {
            GENDER_WOMEN -> {
                binding.categorySliderTabLayout.selectTab(binding.categorySliderTabLayout.getTabAt(0))
                loadCategories(GENDER_WOMEN)
                formatTabFonts(requireContext(), binding.categorySliderTabLayout, 0, 3)
            }
            GENDER_MEN -> {
                binding.categorySliderTabLayout.selectTab(binding.categorySliderTabLayout.getTabAt(1))
                loadCategories(GENDER_MEN)
                formatTabFonts(requireContext(), binding.categorySliderTabLayout, 1, 3)
            }
            GENDER_KIDS -> {
                binding.categorySliderTabLayout.selectTab(binding.categorySliderTabLayout.getTabAt(2))
                loadCategories(GENDER_KIDS)
                formatTabFonts(requireContext(), binding.categorySliderTabLayout, 2, 3)
            }
        }
    }

    fun loadCategories(gender: String) {
        lifecycleScope.launch {
            viewModel.getCategory(gender)
        }
    }
}