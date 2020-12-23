package com.ankuradlakha.baseproject.data.repositories

import com.ankuradlakha.baseproject.data.AppCache
import com.ankuradlakha.baseproject.network.API
import com.ankuradlakha.baseproject.network.APIUrl

class OnboardingRepository(private val api: API, private val appCache: AppCache) {
    fun getOnboardingData() =
        api.getOnboardingData(APIUrl.getOnboardingData(appCache.getSelectedLanguage()))
}