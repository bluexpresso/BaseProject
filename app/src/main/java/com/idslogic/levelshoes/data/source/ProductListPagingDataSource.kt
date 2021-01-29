package com.idslogic.levelshoes.data.source

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.idslogic.levelshoes.data.AppCache
import com.idslogic.levelshoes.data.models.BaseModel
import com.idslogic.levelshoes.data.models.Product
import com.idslogic.levelshoes.network.API
import com.idslogic.levelshoes.network.APIUrl
import com.idslogic.levelshoes.utils.ProductListingRequestBuilder
import com.idslogic.levelshoes.utils.getStoreCode
import retrofit2.await
import java.lang.Exception

class ProductListPagingDataSource(
    private val api: API,
    private val appCache: AppCache,
    private val category: Int,
    private val productIds: ArrayList<Long>
) :
    PagingSource<Int, BaseModel.Hit<Product>>() {
    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, BaseModel.Hit<Product>> {
        try {
            val currentFromPosition = params.key ?: 0
            val response = api.getProductsAsync(
                APIUrl.getProducts(
                    getStoreCode(
                        appCache.getSelectedCountry().storeCode,
                        appCache.getSelectedLanguage()
                    ),
                ),
                ProductListingRequestBuilder.buildCategoryBasedProductListingRequest(
                    currentFromPosition,
                    category,
                    productIds
                )
            )
            if (response.hits.hits.isNullOrEmpty())
                return LoadResult.Error(Throwable())
            return LoadResult.Page(
                data = response.hits.hits,
                if (currentFromPosition == 0) null else currentFromPosition.minus(20),
                currentFromPosition.plus(20)
            )
        } catch (e: Exception) {
            return LoadResult.Error(e)
        }
    }

    override fun getRefreshKey(state: PagingState<Int, BaseModel.Hit<Product>>): Int? {
        TODO("Not yet implemented")
    }
}