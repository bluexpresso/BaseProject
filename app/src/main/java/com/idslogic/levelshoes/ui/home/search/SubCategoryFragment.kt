package com.idslogic.levelshoes.ui.home.search

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
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
import java.lang.StringBuilder
import java.util.*
import kotlin.collections.ArrayList

@AndroidEntryPoint
class SubCategoryFragment : BaseFragment() {
    private val viewModel: SubCategoryViewModel by viewModels()
    private val activityViewModel: MainViewModel by activityViewModels()
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
        readArguments()
        initToolbar(binding)
        initCategories(binding)
        return binding.root
    }

    override fun onStop() {
        super.onStop()
        activityViewModel.disableSearchAnimation = true
    }

    private fun navigateToPLP(parentCategory: CategorySearch?, categorySearch: CategorySearch) {
        findNavController().navigate(R.id.nav_from_search_to_product_list, Bundle().apply {
            val categoryId: Int
            var genderFilter = ""
            val sbCategoryPath = StringBuilder()
            if (isDesignerCategory(parentCategory?.name)) {
                sbCategoryPath.append(DESIGNERS)
                if (!isViewAllCategory(categorySearch.name)) {
                    sbCategoryPath.append(";${categorySearch.name?.toLowerCase(Locale.getDefault())}")
                }
            } else if (isCollectionsCategory(parentCategory?.name)) {
                sbCategoryPath.append(COLLECTIONS)
                if (!isViewAllCategory(categorySearch.name)) {
                    sbCategoryPath.append(";${categorySearch.name?.toLowerCase(Locale.getDefault())}")
                }
            } else if (viewModel.gender == GENDER_KIDS) {
                if (parentCategory?.name == CATEGORY_BABY || parentCategory?.name == CATEGORY_GIRL
                    || parentCategory?.name == CATEGORY_UNISEX || parentCategory?.name == CATEGORY_BOY
                ) {
                    //KIDS->BABY->SNEAKERS
                    sbCategoryPath.append("$CATEGORY_KIDS;")
                        .append(
                            "${
                                parentCategory.name?.toLowerCase(Locale.getDefault())
                                    ?.toLowerCase(Locale.getDefault())
                            }"
                        )
                    if (!isViewAllCategory(categorySearch.name)) {
                        sbCategoryPath.append(
                            ";${
                                categorySearch.name?.toLowerCase(
                                    Locale.getDefault()
                                )
                            }"
                        )
                    }
                } else {
                    sbCategoryPath.append("$CATEGORY_KIDS;")
                        .append(parentCategory?.name?.toLowerCase(Locale.getDefault()))
                    if (!isViewAllCategory(categorySearch.name)) {
                        sbCategoryPath.append(";${categorySearch.name?.toLowerCase(Locale.getDefault())}")
                    }
                }
            } else if (viewModel.gender == GENDER_WOMEN || viewModel.gender == GENDER_MEN) {
                sbCategoryPath.append(if (viewModel.gender == GENDER_WOMEN) CATEGORY_WOMEN else CATEGORY_MEN)
                if (categorySearch.id == null || isViewAllCategory(categorySearch.name))
                    sbCategoryPath.append(";${parentCategory?.name?.toLowerCase(Locale.getDefault())}")
                if (!isViewAllCategory(categorySearch.name)) {
                    sbCategoryPath.append(";${categorySearch.name?.toLowerCase(Locale.getDefault())}")
                }
            }
            if (!categorySearch.menuItemLink.isNullOrEmpty()) {
                categoryId =
                    getCategoryIdFromLink(categorySearch.menuItemLink!!) ?: NO_CATEGORY
                genderFilter = getGenderIdFromLink(categorySearch.menuItemLink!!) ?: ""
            } else {
                categoryId =
                    if (isDesignerCategory(parentCategory?.name) || isCollectionsCategory(
                            parentCategory?.name
                        )
                    ) {
                        when (viewModel.gender) {
                            GENDER_WOMEN -> GENDER_ID_WOMEN_SEARCH
                            GENDER_MEN -> GENDER_ID_MEN_SEARCH
                            else -> categorySearch.id ?: NO_CATEGORY
                        }
                    } else if (!isViewAllCategory(categorySearch.name)) {
                        categorySearch.id ?: NO_CATEGORY
                    } else {
                        parentCategory?.id ?: NO_CATEGORY
                    }
            }
            val parentCategoryId: Int = parentCategory?.id ?: NO_CATEGORY
            putString(
                ARG_GENDER,
                if (parentCategory?.name == CATEGORY_BABY || parentCategory?.name == CATEGORY_GIRL
                    || parentCategory?.name == CATEGORY_UNISEX || parentCategory?.name == CATEGORY_BOY
                ) parentCategory.name ?: "" else viewModel.gender ?: GENDER_WOMEN
            )
            putString(ARG_GENDER_FILTER, genderFilter)
            putString(ARG_TITLE, categorySearch.name)
            putString(ARG_CATEGORY_PATH, sbCategoryPath.toString())
            putInt(ARG_CATEGORY_ID, categoryId)
            putInt(ARG_PARENT_CATEGORY_ID, parentCategoryId)
        })
    }

    private fun initCategories(binding: FragmentSubCategoryBinding) {
        val subCategoryAdapter = SearchCategoryAdapter()
        subCategoryAdapter.setItems(viewModel.categories ?: arrayListOf())
        binding.viewSubCategories.adapter = subCategoryAdapter
        subCategoryAdapter.onCategorySelected =
            { categorySearch: CategorySearch, _: ArrayList<CategorySearch>? ->
                getCategoryToPLPNavigation(
                    findNavController(),
                    R.id.nav_from_search_to_product_list,
                    viewModel.gender,
                    viewModel.category,
                    categorySearch
                )
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