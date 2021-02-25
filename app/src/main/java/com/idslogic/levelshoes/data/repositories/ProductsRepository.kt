package com.idslogic.levelshoes.data.repositories

import androidx.lifecycle.LiveData
import androidx.paging.*
import com.idslogic.levelshoes.data.AppCache
import com.idslogic.levelshoes.data.AppDatabase
import com.idslogic.levelshoes.data.models.*
import com.idslogic.levelshoes.data.source.ProductListPagingDataSource
import com.idslogic.levelshoes.network.API
import com.idslogic.levelshoes.network.APIUrl
import com.idslogic.levelshoes.utils.ProductListingRequestBuilder
import com.idslogic.levelshoes.utils.getStoreCode
import kotlinx.coroutines.CoroutineScope
import retrofit2.Call
import retrofit2.Response
import javax.inject.Inject

class ProductsRepository @Inject constructor(
    private val api: API,
    private val appCache: AppCache,
    private val appDatabase: AppDatabase
) {
    var productsPagerLiveData: LiveData<PagingData<BaseModel.Hit<Product>>>? = null
    var pagingSource: ProductListPagingDataSource? = null
    fun getCategoryDetailsFromCategoryId(categoryId: Int): Response<BaseModel.Hit<CategoryResponse>> =
        api.getCategoryDetailFromId(
            APIUrl.getCategoryNameFromCategoryId(
                getStoreCode(
                    appCache.getSelectedCountry().storeCode,
                    appCache.getSelectedLanguage()
                ), categoryId
            )
        ).execute()

    fun getCategoryBasedProductsFromKlevu(
        categoryId: String,
        gender: String? = null
    ): Response<ListingProductResponse> {
        return api.getCategoryBasedProductsFromKlevuIdSearch(
            APIUrl.getCategoryBasedProductsFromKlevuIdSearch(),
            ProductListingRequestBuilder.getCategoryBasedProductsQueryParams(
                categoryId,
                "*",
                gender = gender
            )
        ).execute()
    }

    fun getProducts(
        categoryId: Int, genderFilter: String = "",
        productIds: ArrayList<ListingProduct>,
        filterData: FilterData? = null,
    ): LiveData<PagingData<BaseModel.Hit<Product>>> {
        return Pager(PagingConfig(pageSize = 20), pagingSourceFactory = {
            ProductListPagingDataSource(
                api, appCache, categoryId, productIds, genderFilter,
                filterData
            )
        }
        ).liveData
    }

    fun initProductsPagerLiveDataSource(
        categoryId: Int, genderFilter: String = "",
        productIds: ArrayList<ListingProduct>,
        scope: CoroutineScope,
        filterData: FilterData? = null,
    ) {
        pagingSource =
            ProductListPagingDataSource(
                api, appCache, categoryId, productIds, genderFilter,
                filterData
            )
        productsPagerLiveData =
            Pager(
                PagingConfig(
                    pageSize = 20,
                    enablePlaceholders = false
                )
            ) { pagingSource!! }.liveData.cachedIn(scope)
    }

    suspend fun getProductsFromCategory(
        categoryId: Int, genderFilter: String = "",
        products: ArrayList<ListingProduct>,
        filterData: FilterData? = null,
    ): Response<BaseModel<Product>> {
        val productIds = arrayListOf<Long>()
        products.forEach { listingProduct ->
            productIds.add(listingProduct.itemGroupId?.toLong() ?: 0)
        }
        return api.getProducts(
            APIUrl.getProducts(
                getStoreCode(
                    appCache.getSelectedCountry().storeCode,
                    appCache.getSelectedLanguage()
                ),
            ),
            ProductListingRequestBuilder.buildCategoryBasedProductListingRequest(
                0,
                categoryId,
                productIds,
                genderFilter,
                filterData
            )
        ).execute()
    }

    fun getProducts(
        fromPosition: Int = 0,
        category: Int,
        productIds: ArrayList<Long>
    ): Response<BaseModel<Product>> {
        return api.getProducts(
            APIUrl.getProducts(
                getStoreCode(
                    appCache.getSelectedCountry().storeCode,
                    appCache.getSelectedLanguage()
                ),
            ),
            ProductListingRequestBuilder.buildCategoryBasedProductListingRequest(
                fromPosition,
                category,
                productIds
            )
        ).execute()
    }

}