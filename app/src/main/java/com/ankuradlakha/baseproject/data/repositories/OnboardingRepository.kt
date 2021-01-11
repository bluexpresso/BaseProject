package com.ankuradlakha.baseproject.data.repositories

import com.ankuradlakha.baseproject.data.AppCache
import com.ankuradlakha.baseproject.data.AppDatabase
import com.ankuradlakha.baseproject.data.models.CategoryData
import com.ankuradlakha.baseproject.data.models.Country
import com.ankuradlakha.baseproject.data.models.Source
import com.ankuradlakha.baseproject.network.API
import com.ankuradlakha.baseproject.network.APIUrl

class OnboardingRepository(
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