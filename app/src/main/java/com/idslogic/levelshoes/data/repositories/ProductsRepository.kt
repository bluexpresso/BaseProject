package com.idslogic.levelshoes.data.repositories

import com.idslogic.levelshoes.data.AppCache
import com.idslogic.levelshoes.data.AppDatabase
import com.idslogic.levelshoes.data.models.ListingProductResponse
import com.idslogic.levelshoes.network.API
import com.idslogic.levelshoes.network.APIUrl
import com.idslogic.levelshoes.utils.RequestBuilder
import retrofit2.Response

class ProductsRepository(
    private val api: API,
    private val appCache: AppCache,
    private val appDatabase: AppDatabase
) {
    fun getCategoryProduct(): Response<ListingProductResponse> {
        return api.getCategoryProducts(
            APIUrl.getCategoryProducts(),
            RequestBuilder.getQueryParamsForKlevuCategoryProducts(),
            RequestBuilder.buildKlevuCategoryProductsRequest("", 0, 100)
        ).execute()
    }

}