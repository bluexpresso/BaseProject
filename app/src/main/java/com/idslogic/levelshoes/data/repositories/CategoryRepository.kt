package com.idslogic.levelshoes.data.repositories

import com.idslogic.levelshoes.data.AppCache
import com.idslogic.levelshoes.data.models.BaseModel
import com.idslogic.levelshoes.data.models.CategorySearch
import com.idslogic.levelshoes.data.models.ListingProductResponse
import com.idslogic.levelshoes.network.API
import com.idslogic.levelshoes.network.APIUrl
import com.idslogic.levelshoes.utils.*
import retrofit2.Call
import retrofit2.Response
import javax.inject.Inject


class CategoryRepository @Inject constructor(
    private val api: API,
    private val appCache: AppCache,
) {

    fun getCategorySearch(gender: String): Response<BaseModel<CategorySearch>> {
        return api.getCategory(
            APIUrl.getCategorySearch(
                getStoreCode(
                    appCache.getSelectedCountry().storeCode,
                    appCache.getSelectedLanguage()
                )
            ),
            RequestBuilder.buildCategorySearchRequest(gender)
        ).execute()

    }

    fun getCategoryBasedTrendingProductsFromKlevu(categoryId: String?): Response<ListingProductResponse> {
        return api.getCategoryBasedProductsFromKlevuIdSearch(
            APIUrl.getCategoryBasedTrendingProductsFromKlevuSearch(),
            ProductListingRequestBuilder.getCategoryBasedProductsQueryParams(
                categoryId, TERM_VAL,
                appCache.getSelectedLanguage(), appCache.getSelectedCountry().storeCode
            )
        ).execute()
    }

    fun getCategoryBasedSearchResultsProductsFromKlevu(
        termval: String,
        gender: String
    ): Call<ListingProductResponse> {
        return api.getCategoryBasedProductsFromKlevuIdSearch(
            APIUrl.getCategoryBasedTrendingProductsFromKlevuSearch(),
            ProductListingRequestBuilder.getSearchProductsQueryParams(
                null,
                termval,
                gender,
                appCache.getSelectedLanguage(),
                appCache.getSelectedCountry().storeCode?: DEFAULT_COUNTRY
            )
        )
    }
}