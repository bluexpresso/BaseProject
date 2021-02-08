package com.idslogic.levelshoes.ui.home.search

import android.os.Bundle
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.transition.Fade
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.idslogic.levelshoes.R
import com.idslogic.levelshoes.data.models.CategorySearch
import com.idslogic.levelshoes.databinding.FragmentSubCategoryBinding
import com.idslogic.levelshoes.ui.BaseFragment
import com.idslogic.levelshoes.ui.MainViewModel
import com.idslogic.levelshoes.utils.*
import dagger.hilt.android.AndroidEntryPoint
import java.util.*
import kotlin.collections.ArrayList

@AndroidEntryPoint
class SubCategoryFragment : BaseFragment() {
    private val viewModel: SubCategoryViewModel by viewModels()
    private val mainViewModel: MainViewModel by activityViewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        exitTransition = Fade(Fade.MODE_OUT)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val binding = DataBindingUtil.inflate(
            inflater, R.layout.fragment_sub_category, container,
            false
        ) as FragmentSubCategoryBinding
        changeStatusBarColor(R.color.secondaryBackground)
        mainViewModel.disableSearchAnimation = true
        readArguments()
        initToolbar(binding)
        initCategories(binding)
        return binding.root
    }

    private fun initCategories(binding: FragmentSubCategoryBinding) {
        val subCategoryAdapter = SearchCategoryAdapter()
        subCategoryAdapter.setItems(viewModel.categories ?: arrayListOf())
        binding.viewSubCategories.adapter = subCategoryAdapter
        subCategoryAdapter.onCategorySelected =
            { categorySearch: CategorySearch, _: ArrayList<CategorySearch>? ->
                findNavController().navigate(R.id.nav_from_search_to_product_list, Bundle().apply {
                    var gender: String? = ""
                    var categoryPath = ""
                    var categoryId = NO_CATEGORY
                    var parentCategoryId = NO_CATEGORY
                    //create gender logic here
                    //then append the category path
                    //applyFilters -> leave gender blank
                    if (isViewAllCategory(categorySearch.name)) {
                        categoryPath = getCategoryPathForSearch(
                            viewModel.gender,
                            viewModel.category?.name,
                            null
                        )

                        categoryId = viewModel.category?.id ?: NO_CATEGORY
                        parentCategoryId = viewModel.category?.id ?: NO_CATEGORY
                    } else if (isDesignerCategory(viewModel.category?.name)) {
                        categoryPath = getCategoryPathForSearch(
                            DESIGNERS,
                            categorySearch.name,
                            null
                        )
                        categoryId = categorySearch.id ?: NO_CATEGORY
                    } else if (isCollectionsCategory(categorySearch.name)) {
                        categoryPath = getCategoryPathForSearch(
                            COLLECTIONS,
                            categorySearch.name,
                            null
                        )
                        categoryId = categorySearch.id ?: NO_CATEGORY
                    } else {
                        if (viewModel.gender == GENDER_KIDS) {
                            categoryPath = getCategoryPathForSearch(
                                "$CATEGORY_KIDS;${
                                    viewModel.category?.name?.toLowerCase(Locale.getDefault())
                                }",
                                categorySearch.name,
                                null
                            )
                            gender =
                                if (viewModel.category?.name == CATEGORY_BABY ||
                                    viewModel.category?.name === CATEGORY_BOY ||
                                    viewModel.category?.name == CATEGORY_GIRL ||
                                    viewModel.category?.name == CATEGORY_UNISEX
                                ) {
                                    viewModel.category?.name
                                } else {
                                    viewModel.gender
                                }
                        } else {
                            categoryPath = getCategoryPathForSearch(
                                viewModel.gender,
                                categorySearch.name,
                                null
                            )
                        }
                        categoryId = categorySearch.id ?: NO_CATEGORY
                    }
                    putString(ARG_GENDER, gender ?: GENDER_WOMEN)
                    putString(ARG_TITLE, categorySearch.name)
                    putString(ARG_CATEGORY_PATH, categoryPath)
                    putInt(ARG_CATEGORY_ID, categoryId)
                    putInt(ARG_PARENT_CATEGORY_ID, parentCategoryId)
                })
            }
    }

    private fun readArguments() {
        viewModel.categories = Gson().fromJson(
            arguments?.getString(ARG_CATEGORIES, ""),
            object : TypeToken<ArrayList<CategorySearch>>() {}.type
        )
        viewModel.category =
            Gson().fromJson(arguments?.getString(ARG_CATEGORY, ""), CategorySearch::class.java)
        viewModel.gender = arguments?.getString(ARG_GENDER, GENDER_WOMEN)
    }

    private fun initToolbar(binding: FragmentSubCategoryBinding) {
        binding.toolbar.setTitle(viewModel.category?.name)
        binding.toolbar.onLeftIconClick = {
            findNavController().navigateUp()
        }
    }
}