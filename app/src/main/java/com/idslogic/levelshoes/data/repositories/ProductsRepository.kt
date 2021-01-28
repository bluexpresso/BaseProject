package com.idslogic.levelshoes.data.repositories

import com.idslogic.levelshoes.data.AppCache
import com.idslogic.levelshoes.data.AppDatabase
import com.idslogic.levelshoes.data.models.BaseModel
import com.idslogic.levelshoes.data.models.CategoryResponse
import com.idslogic.levelshoes.data.models.ListingProductResponse
import com.idslogic.levelshoes.data.models.Product
import com.idslogic.levelshoes.network.API
import com.idslogic.levelshoes.network.APIUrl
import com.idslogic.levelshoes.utils.ProductListingRequestBuilder
import com.idslogic.levelshoes.utils.RequestBuilder
import com.idslogic.levelshoes.utils.getStoreCode
import retrofit2.Call
import retrofit2.Response

class ProductsRepository(
    private val api: API,
    private val appCache: AppCache,
    private val appDatabase: AppDatabase
) {
//    fun getCategoryProduct(): Response<ListingProductResponse> {
//        return api.getCategoryProducts(
//            APIUrl.getCategoryProducts(),
//            RequestBuilder.getQueryParamsForKlevuCategoryProducts(),
//            RequestBuilder.buildKlevuCategoryProductsRequest("", 0, 100)
//        ).execute()
//    }

    fun getCategoryDetailsFromCategoryId(categoryId: Int): Response<BaseModel.Hit<CategoryResponse>> =
        api.getCategoryDetailFromId(
            APIUrl.getCategoryNameFromCategoryId(
                getStoreCode(
                    appCache.getSelectedCountry().storeCode,
                    appCache.getSelectedLanguage()
                ), categoryId
            )
        ).execute()

    fun getCategoryBasedProductsFromKlevu(categoryId: String): Response<ListingProductResponse> {
        return api.getCategoryBasedProductsFromKlevuIdSearch(
            APIUrl.getCategoryBasedProductsFromKlevuIdSearch(),
            RequestBuilder.getCategoryBasedProductsQueryParams(categoryId)
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