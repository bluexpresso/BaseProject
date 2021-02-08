package com.idslogic.levelshoes.data.source

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.google.gson.Gson
import com.idslogic.levelshoes.data.AppCache
import com.idslogic.levelshoes.data.models.BaseModel
import com.idslogic.levelshoes.data.models.ListingProduct
import com.idslogic.levelshoes.data.models.Product
import com.idslogic.levelshoes.network.API
import com.idslogic.levelshoes.network.APIUrl
import com.idslogic.levelshoes.utils.ProductListingRequestBuilder
import com.idslogic.levelshoes.utils.getStoreCode
import retrofit2.await
import timber.log.Timber
import java.lang.Exception

class ProductSearchResultsDataSource(
    private val api: API,
    private val appCache: AppCache,
    private val category: Int,
    private val productIdsList: ArrayList<ListingProduct>
) :
    PagingSource<Int, BaseModel.Hit<Product>>() {
    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, BaseModel.Hit<Product>> {
        try {
            val currentFromPosition = params.key?.plus(1) ?: 0
            val nextPosition = currentFromPosition.plus(20).minus(1)
            val productIds = arrayListOf<Long>()
            for (i in currentFromPosition..nextPosition) {
                if (i < productIdsList.size) {
                    productIds.add(productIdsList[i].itemGroupId?.toLong() ?: 0)
                }
            }
            if (!productIds.isNullOrEmpty()) {
                val response = api.getProductsAsync(
                    APIUrl.getProducts(
                        getStoreCode(
                            appCache.getSelectedCountry().storeCode,
                            appCache.getSelectedLanguage()
                        ),
                    ),
                    ProductListingRequestBuilder.buildCategoryBasedProductListingRequest(
                        0,
                        category,
                        productIds
                    )
                )
                if (response.hits.hits.isNullOrEmpty())
                    return LoadResult.Error(Throwable())
                return LoadResult.Page(
                    data = response.hits.hits.sortedBy {
                        it.score as Double
                    },
                    if (currentFromPosition == 0) null else currentFromPosition,
                    nextPosition
                )
            } else {
                return LoadResult.Error(Throwable())
            }
        } catch (e: Exception) {
            return LoadResult.Error(e)
        }
    }

    override fun getRefreshKey(state: PagingState<Int, BaseModel.Hit<Product>>): Int? {
        TODO("Not yet implemented")
    }
}