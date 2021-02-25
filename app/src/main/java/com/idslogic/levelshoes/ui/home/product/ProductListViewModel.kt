package com.idslogic.levelshoes.ui.home.product

import android.app.Application
import androidx.lifecycle.*
import androidx.paging.PagingData
import com.idslogic.levelshoes.R
import com.idslogic.levelshoes.data.models.*
import com.idslogic.levelshoes.data.repositories.ConfigurationRepository
import com.idslogic.levelshoes.data.repositories.ProductsRepository
import com.idslogic.levelshoes.network.Resource
import com.idslogic.levelshoes.utils.*
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.Response
import java.util.*
import javax.inject.Inject

@HiltViewModel
class ProductListViewModel @Inject constructor(
    private val productsRepository: ProductsRepository,
    private val configurationRepository: ConfigurationRepository,
    private val savedStateHandle: SavedStateHandle, application: Application
) : AndroidViewModel(application) {
    private val context = application.applicationContext
    val filtersListLiveData = MutableLiveData<Resource<ArrayList<Filter>>>()
    var genderFilter: String = ""
    var categoryPath: String? = null
    var title: String? = null
    var gender = GENDER_WOMEN
    var productPriceLiveData = MutableLiveData<Pair<Double, Double>>()
    var categoryId: Int = NO_CATEGORY
    val productIdsLiveData = MutableLiveData<ArrayList<ListingProduct>>()
    val loadingLiveData = MutableLiveData<Boolean>()
    var parentCategoryId: Int = NO_CATEGORY
    var filterData: FilterData? = null
//    val productsLiveData = productIdsLiveData.switchMap { ids ->
//        productsRepository.getProducts(
//            categoryId, genderFilter, ids, filterData
//        ).cachedIn(viewModelScope)
//    }

    suspend fun getProductsFromCategory() {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                loadingLiveData.postValue(true)
                filtersListLiveData.postValue(Resource.loading())
                if (!categoryPath.isNullOrEmpty()) {
                    if (categoryId == NO_CATEGORY) {
                        val categoryResponse =
                            productsRepository.getCategoryDetailsFromCategoryId(parentCategoryId)
                        if (!categoryResponse.body()?.source?.childrenData.isNullOrEmpty()) {
                            categoryId =
                                categoryResponse.body()?.source?.childrenData?.find { thisCat ->
                                    thisCat.name.equals(
                                        title,
                                        true
                                    )
                                }?.id ?: NO_CATEGORY
                        }
                    }
                    val productIdsResponse =
                        productsRepository.getCategoryBasedProductsFromKlevu(categoryPath!!, gender)
                    val filters = productIdsResponse.body()?.filters
                    if (!filters.isNullOrEmpty()) {
                        filtersListLiveData.postValue(Resource.success(filters))
                    } else {
                        filtersListLiveData.postValue(Resource.error())
                    }
                    if (productIdsResponse.isSuccessful && productIdsResponse.body()?.result?.isNotEmpty() == true) {
                        productIdsLiveData.postValue(productIdsResponse.body()!!.result!!)
                    } else {
                        loadingLiveData.postValue(false)
                    }
                } else {
                    val categoryResponse = productsRepository.getCategoryDetailsFromCategoryId(
                        categoryId
                    )
                    if (categoryResponse.isSuccessful) {
                        categoryResponse.body()?.source?.let {
                            val categoryName = it.name.decapitalize(Locale.ENGLISH)
                            val sBuilder = StringBuilder()
                            sBuilder.append(
                                "${
                                    when {
                                        it.urlPath.startsWith(CATEGORY_MEN, true) -> {
                                            CATEGORY_MEN
                                        }
                                        it.urlPath.startsWith(CATEGORY_WOMEN, true) -> {
                                            CATEGORY_WOMEN
                                        }
                                        it.urlPath.startsWith(CATEGORY_KIDS, true) -> {
                                            CATEGORY_KIDS
                                        }
                                        it.urlPath.startsWith(CATEGORY_GIRL, true) -> {
                                            CATEGORY_GIRL
                                        }
                                        it.urlPath.startsWith(CATEGORY_BOY, true) -> {
                                            CATEGORY_BOY
                                        }
                                        else -> {
                                            ""
                                        }
                                    }
                                };$categoryName"
                            )
                            val productIdsResponse =
                                productsRepository.getCategoryBasedProductsFromKlevu(sBuilder.toString())
                            val filters = productIdsResponse.body()?.filters
                            if (!filters.isNullOrEmpty()) {
                                filtersListLiveData.postValue(Resource.success(filters))
                            } else {
                                filtersListLiveData.postValue(Resource.error())
                            }
                            if (productIdsResponse.isSuccessful && productIdsResponse.body()?.result?.isNotEmpty() == true) {
                                productIdsLiveData.postValue(productIdsResponse.body()!!.result!!)
                            } else {
                                loadingLiveData.postValue(false)
                            }
                        }
                    } else {
                        loadingLiveData.postValue(false)
                    }
                }

            }
        }
    }

    private fun getPrice(
        filters: ArrayList<Filter>,
        productIdsResponse: Response<ListingProductResponse>
    ) {
        filters.add(
            Filter(
                FILTER_TYPE_PRICE, context.getString(R.string.price),
                "", arrayListOf(
                    FilterOption(
                        1, "false",
                        "min", productIdsResponse.body()?.price?.min ?: "0"
                    ),
                    FilterOption(
                        1, "false",
                        "max", productIdsResponse.body()?.price?.max ?: "20000"
                    )
                )
            )
        )
    }

    suspend fun getProductsList(filterData: FilterData?): MutableLiveData<Resource<List<BaseModel.Hit<Product>>>> {
        val productsResourceLiveData =
            MutableLiveData<Resource<List<BaseModel.Hit<Product>>>>()
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                productsResourceLiveData.postValue(Resource.loading())
                val productsResponse = productsRepository.getProductsFromCategory(
                    categoryId, genderFilter,
                    productIdsLiveData.value!!, filterData
                )
                if (productsResponse.isSuccessful &&
                    !productsResponse.body()?.hits?.hits.isNullOrEmpty()
                ) {
                    val sortedProducts = productsResponse.body()?.hits?.hits?.sortedWith(
                        compareBy(
                            nullsLast(), { it.score })
                    )
                    productsResourceLiveData.postValue(
                        Resource.success(sortedProducts)
                    )
                    productPriceLiveData.postValue(
                        Pair(
                            productsResponse.body()?.aggregations?.minPrice?.value ?: 0.toDouble(),
                            productsResponse.body()?.aggregations?.maxPrice?.value
                                ?: 10000.toDouble()
                        )
                    )
                } else {
                    productsResourceLiveData.postValue(Resource.error())
                }
            }
        }
        return productsResourceLiveData
    }

    fun getProductPagingLiveData(filterData: FilterData? = null): LiveData<PagingData<BaseModel.Hit<Product>>>? {
        productsRepository.initProductsPagerLiveDataSource(
            categoryId, genderFilter, productIdsLiveData.value!!,
            viewModelScope, filterData
        )
        return productsRepository.productsPagerLiveData
    }

    fun getSelectedCurrency() = configurationRepository.getCurrency()

    override fun onCleared() {
        super.onCleared()
        productsRepository.productsPagerLiveData = null
    }

    fun getCategoryName(name: String): String {
        val categoryName = name.decapitalize(Locale.ENGLISH)
        val sBuilder = StringBuilder()
        sBuilder.append(
            "${
                when (gender) {
                    GENDER_MEN -> {
                        CATEGORY_MEN
                    }
                    GENDER_WOMEN -> {
                        CATEGORY_WOMEN
                    }
                    GENDER_KIDS -> {
                        CATEGORY_KIDS
                    }
                    GENDER_BOYS -> {
                        CATEGORY_BOY
                    }
                    GENDER_GIRLS -> {
                        CATEGORY_GIRL
                    }
                    GENDER_UNISEX -> {
                        CATEGORY_UNISEX
                    }
                    else -> {
                        ""
                    }
                }
            };$categoryName"
        )
        return sBuilder.toString()
    }
}