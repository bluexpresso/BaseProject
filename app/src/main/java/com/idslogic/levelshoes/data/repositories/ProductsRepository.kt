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
import retrofit2.Response
import javax.inject.Inject

class ProductsRepository @Inject constructor(
    private val api: API,
    private val appCache: AppCache,
    private val appDatabase: AppDatabase
) {
    var productsPagerLiveData: LiveData<PagingData<BaseModel.Hit<Product>>>? = null

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

    fun initProductsPagerLiveDataSource(
        categoryId: Int,genderFilter : String = "",
        productIds: ArrayList<ListingProduct>,
        scope: CoroutineScope
    ) {
        if (productsPagerLiveData == null)
            productsPagerLiveData = Pager(PagingConfig(pageSize = 20)) {
                ProductListPagingDataSource(api, appCache, categoryId, productIds,genderFilter)
            }.liveData.cachedIn(scope)
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