package com.idslogic.levelshoes.network

import com.idslogic.levelshoes.BuildConfig
import com.idslogic.levelshoes.utils.MAGNETO

class APIUrl {
    companion object {
        private const val BASE_URL: String = BuildConfig.REST_ENDPOINT
        const val klevuBase = "https://lscs.ksearchnet.com/"
        const val klevuCloud = "cloud-search/"
        const val klevuNSearch = "n-search/search"
        const val klevuIDSearch = "n-search/idsearch"
        fun getKlevuSkuCode(languageCode: String, storeCode: String) =
            if (languageCode == "en" && storeCode == "ae")
                "klevu-158358783414411589"
            else if (languageCode == "ar" && storeCode == "ae")
                "klevu-158358841607111589"
            else if (languageCode == "en" && storeCode == "sa")
                "klevu-158358926983111589"
            else if (languageCode == "ar" && storeCode == "sa")
                "klevu-158358934990911589"
            else if (languageCode == "ar" && storeCode == "kw")
                "klevu-158364133980711589"
            else if (languageCode == "en" && storeCode == "kw")
                "klevu-158358942027511589"
            else if (languageCode == "ar" && storeCode == "om")
                "klevu-158374818352011589"
            else if (languageCode == "en" && storeCode == "om")
                "klevu-158364141744611589"
            else if (languageCode == "ar" && storeCode == "bh")
                "klevu-158375199266011589"
            else if (languageCode == "en" && storeCode == "bh")
                "klevu-158374852940611589"
            else
                "klevu-158676873614211589"

        fun getVersionInfo(storeCode: String) =
            BASE_URL.plus(storeCode).plus("/cms_block/_search")

        fun getOnboardingVideoUrl() =
            "https://dfgub4cuqcmv6.cloudfront.net/levelshoes/mobileapp/images/Media/Levelshoes/540x960.mp4"

        fun getOnboardingData(languageCode: String) =
            BASE_URL.plus(languageCode).plus("/onboarding/staging")

        fun getLandingProducts(storeCode: String) =
            BASE_URL.plus(storeCode).plus("/product/_search")

        fun getCategoryProducts() = klevuBase.plus(klevuCloud).plus(klevuNSearch)

        fun getAttributes() = BASE_URL.plus("vue_storefront_magento_ae_en/attribute/_search")
    }
    //https://staginges.levelshoes.com:9202/vue_storefront_magento_ae_en/product/_search
    //https://staginges.levelshoes.com:9202/vue_storefront_magento_ae_en/cms_block/_search
    //https://staginges.levelshoes.com:9202/vue_storefront_magento_ae_en/cms_block/_search

//    https://lvles.levelshoes.com:9202/vue_storefront_magento_ae_en/attribute/_search -> For version checking on onboarding
    //REQUEST ->
/*{
    "query": {
    "bool": {
    "must": [
    {
        "match": {
        "attribute_code": "manufacturer"
    }
    }
    ]
}
},
    "_source": "options"
}*/
}


