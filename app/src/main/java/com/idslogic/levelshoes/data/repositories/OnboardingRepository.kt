package com.idslogic.levelshoes.data.repositories

import com.idslogic.levelshoes.data.AppCache
import com.idslogic.levelshoes.data.AppDatabase
import com.idslogic.levelshoes.data.models.CategoryData
import com.idslogic.levelshoes.data.models.Country
import com.idslogic.levelshoes.data.models.Source
import com.idslogic.levelshoes.network.API
import com.idslogic.levelshoes.network.APIUrl
import javax.inject.Inject

class OnboardingRepository @Inject constructor(
    private val api: API,
    private val appCache: AppCache,
    private val appDatabase: AppDatabase
) {
    fun getOnboardingData() =
        api.getOnboardingData(APIUrl.getOnboardingData(appCache.getSelectedLanguage()))

    fun saveCountries(countries: ArrayList<Country>) {

    }

    fun saveCategories(categories: ArrayList<CategoryData>) {

    }

    fun getOnboardingDataFromCache() = appDatabase.getOnboardingDao().getOnboardingData()


    fun saveOnboardingData(body: Source?) {
        appDatabase.getOnboardingDao().insertOnbaordingData(body)
    }

    fun setSelectedCountry(country: Country?) {
        appCache.setSelectedCountry(country)
    }

    fun getSelectedCountry() = appCache.getSelectedCountry()
    fun saveSelectedGender(value: String?) {
        appCache.setSelectedGender(value)
    }

    fun getSelectedGender() = appCache.getSelectedGender()

    fun setOnboardingCompleted(isOnboardingCompleted: Boolean) {
        appCache.setOnboardingCompleted(isOnboardingCompleted)
    }
}