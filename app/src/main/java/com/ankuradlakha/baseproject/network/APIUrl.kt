package com.ankuradlakha.baseproject.network

import com.ankuradlakha.baseproject.BuildConfig

class APIUrl {
    companion object {
        private const val BASE_URL: String = BuildConfig.REST_ENDPOINT
        fun getVersionInfo() = BASE_URL.plus(getStoreCode()).plus("/cms_block/_search")
        fun getStoreCode() = "vue_storefront_magento_ae_en"
        fun getOnboardingVideoUrl() =
            "https://dfgub4cuqcmv6.cloudfront.net/levelshoes/mobileapp/images/Media/Levelshoes/540x960.mp4"

        fun getOnboardingData(languageCode: String) =
            BASE_URL.plus(languageCode).plus("/onboarding/staging")
    }
}


