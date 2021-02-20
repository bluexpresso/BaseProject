package com.idslogic.levelshoes.ui.home.search

import android.app.Application
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.idslogic.levelshoes.R
import com.idslogic.levelshoes.data.models.CategorySearch
import com.idslogic.levelshoes.data.models.ListingProduct
import com.idslogic.levelshoes.data.models.ListingProductResponse
import com.idslogic.levelshoes.data.repositories.CategoryRepository
import com.idslogic.levelshoes.data.repositories.ConfigurationRepository
import com.idslogic.levelshoes.network.Resource
import com.idslogic.levelshoes.utils.GENDER_WOMEN
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.Call
import java.util.*
import javax.inject.Inject
import kotlin.collections.ArrayList

@HiltViewModel
class SearchResultsViewModel @Inject constructor(
    application: Application,
    private val categoryRepository: CategoryRepository,
    private val configurationRepository: ConfigurationRepository

) :
    AndroidViewModel(application) {

    var categories: HashMap<String, LinkedHashMap<CategorySearch, ArrayList<CategorySearch>?>>? =
        null
    private val context = application.applicationContext
    val categorySearchLiveData =
        MutableLiveData<Resource<ArrayList<Pair<CategorySearch, CategorySearch>>>>()
    var gender: String = GENDER_WOMEN
    val recentTrendingList = mutableListOf<SearchRecentTrendingWrapper>()
    val trendingProductsLiveData = MutableLiveData<Resource<ArrayList<ListingProduct>>>()
    suspend fun getTendingItems() {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                val productIdsResponse =
                    categoryRepository.getCategoryBasedTrendingProductsFromKlevu(null)

                if (productIdsResponse.isSuccessful && productIdsResponse.body()?.popularProducts?.isNotEmpty() == true) {
                    val trendingProducts = arrayListOf<ListingProduct>()
                    productIdsResponse.body()?.popularProducts?.forEach { popularProduct ->

                        popularProduct.name?.let { name -> trendingProducts.add(popularProduct) }
                    }
                    trendingProductsLiveData.postValue(Resource.success(trendingProducts))

                } else {
                    trendingProductsLiveData.postValue(Resource.error())
                }
            }
        }
    }

    fun getSelectedCurrency() = configurationRepository.getCurrency()

    fun getRecentSearches() = configurationRepository.getRecentSearches()
    fun addRecentSearch(text: String) {
        configurationRepository.addRecentSearch(text)
    }

    fun getSelectedGender() = configurationRepository.getSelectedGender() ?: GENDER_WOMEN
    fun getRecentTrendingList() {
        recentTrendingList.clear()
        val recentSearches = getRecentSearches()
        if (!recentSearches.isNullOrEmpty()) {
            recentTrendingList.add(
                SearchRecentTrendingWrapper(
                    null,
                    true,
                    context.getString(R.string.recent_items)
                )
            )
            recentSearches.forEach { element ->
                recentTrendingList.add(
                    SearchRecentTrendingWrapper(
                        null,
                        false,
                        element
                    )
                )
            }
        }

        recentTrendingList.add(
            SearchRecentTrendingWrapper(
                null,
                true,
                context.getString(R.string.trending_items)
            )
        )
        trendingProductsLiveData.value?.data?.forEach { trendingItem ->
            recentTrendingList.add(
                SearchRecentTrendingWrapper(
                    trendingItem,
                    false,
                    trendingItem.name
                )
            )
        }
    }
}