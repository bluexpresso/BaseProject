package com.idslogic.levelshoes.ui.home.search

import android.app.Application
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.idslogic.levelshoes.data.models.CategorySearch
import com.idslogic.levelshoes.data.repositories.CategoryRepository
import com.idslogic.levelshoes.data.repositories.ConfigurationRepository
import com.idslogic.levelshoes.network.Resource
import com.idslogic.levelshoes.utils.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlin.collections.ArrayList

class SearchViewModel @ViewModelInject constructor(
    application: Application,
    private val categoryRepository: CategoryRepository,
    private val configurationRepository: ConfigurationRepository
) : AndroidViewModel(application) {
    var gender: String = GENDER_WOMEN
    val mapCategories =
        hashMapOf<String, LinkedHashMap<CategorySearch, ArrayList<CategorySearch>?>>()
    val categoryFetcherLiveData =
        MutableLiveData<Resource<LinkedHashMap<CategorySearch, ArrayList<CategorySearch>?>>>()

    init {
        gender = configurationRepository.getSelectedGender() ?: GENDER_WOMEN
    }

    suspend fun getCategory(genderValue: String) {
        try {
            if (mapCategories.containsKey(genderValue)) {
                categoryFetcherLiveData.postValue(Resource.success(mapCategories[genderValue]))
            } else {
                viewModelScope.launch {
                    withContext(Dispatchers.IO) {
                        categoryFetcherLiveData.postValue(Resource.loading())
                        for (i in 0..3) {
                            val response = categoryRepository.getCategorySearch(
                                when (i) {
                                    0 -> GENDER_WOMEN
                                    1 -> GENDER_MEN
                                    2 -> GENDER_KIDS
                                    else -> GENDER_WOMEN
                                }
                            )
                            if (response.isSuccessful && !response.body()?.hits?.hits.isNullOrEmpty()) {
                                val list = response.body()?.hits?.hits
                                var lastAddedCategoryToMap: CategorySearch? = null
                                val categoriesHashMap =
                                    LinkedHashMap<CategorySearch, ArrayList<CategorySearch>?>()
                                list?.forEachIndexed { index, hit ->

                                    list[index].source?.let { source ->
                                        if (source.columnBreakpoint == 1 && source.menuItemType == MENU_ITEM_TYPE_DEFAULT) {
                                            source.childrenData?.forEach { sourceChildrenData ->
                                                val categoryTemp = deepCopy(sourceChildrenData)
                                                if (categoryTemp != null)
                                                    categoriesHashMap[source]?.add(categoryTemp)
                                            }
                                            categoriesHashMap[source] = source.childrenData
                                        } else if ((source.columnBreakpoint == 0 &&
                                                    source.menuItemType == MENU_ITEM_TYPE_TITLE)
                                        ) {
                                            categoriesHashMap[source] = null
                                            val categoryTemp = deepCopy(source)
                                            lastAddedCategoryToMap = categoryTemp
                                        } else if ((source.columnBreakpoint == 0 &&
                                                    (source.menuItemType == MENU_ITEM_TYPE_DEFAULT ||
                                                            source.menuItemType == MENU_ITEM_TYPE_LINK) ||
                                                    source.columnBreakpoint == 1 && source.menuItemType == MENU_ITEM_TYPE_LINK)
                                        ) {
                                            var categoriesList =
                                                categoriesHashMap[lastAddedCategoryToMap]
                                            if (categoriesList.isNullOrEmpty()) categoriesList =
                                                arrayListOf()
                                            val categoryTemp = deepCopy(source)
                                            if (categoryTemp != null)
                                                categoriesList.add(categoryTemp)
                                            lastAddedCategoryToMap?.let { lastAddedCat ->
                                                categoriesHashMap[lastAddedCat] = categoriesList
                                            }

                                        }
                                    }
                                }
                                mapCategories[when (i) {
                                    0 -> GENDER_WOMEN
                                    1 -> GENDER_MEN
                                    2 -> GENDER_KIDS
                                    else -> GENDER_WOMEN
                                }] = categoriesHashMap
                            } else {
                                categoryFetcherLiveData.postValue(Resource.error())
                            }
                        }
                        categoryFetcherLiveData.postValue(Resource.success(mapCategories[genderValue]))
                    }
                }
            }
        } catch (e: Exception) {
            categoryFetcherLiveData.postValue(Resource.error())
        }
    }

    fun getSelectedGender() = configurationRepository.getSelectedGender() ?: GENDER_WOMEN
    val context = application.applicationContext
}


