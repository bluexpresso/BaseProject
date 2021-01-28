package com.idslogic.levelshoes.network

import com.idslogic.levelshoes.BuildConfig
import com.idslogic.levelshoes.utils.MAGNETO

class APIUrl {
    companion object {
        private const val BASE_URL: String = BuildConfig.REST_ENDPOINT
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
//        func getCheckoutPublicKey() -> String{
//            let language = UserDefaults.standard.value(forKey:string.language)as? String ?? "en"
//            let storeCode = UserDefaults.standard.value(forKey: "storecode") as? String!
//
//            var CheckoutPublicKey = "pk_test_1f7e40f9-8974-45b0-ac33-a48c3405561d"
//            if (language == "en" && storeCode == "ae"){
//
//                CheckoutPublicKey = "pk_test_1f7e40f9-8974-45b0-ac33-a48c3405561d"
//
//            }else if(language == "ar" && storeCode == "ae"){
//
//                CheckoutPublicKey = "pk_test_ab0b80eb-41d2-4c6e-bde9-fc0c400a0e9a"
//            }else if(language == "en" && storeCode == "sa"){
//
//                CheckoutPublicKey = "pk_test_ceb56135-f0f4-468f-8ef3-2272313a42f0"
//            }else if(language == "ar" && storeCode == "sa"){
//
//                CheckoutPublicKey = "pk_test_91d7cd29-496e-42b3-aae6-a72bad20a1b2"
//            }else if(language == "ar" && storeCode == "kw"){
//
//                CheckoutPublicKey = "pk_test_e96bf720-f024-4928-ba8a-2a1b0c193b6f"
//            }else if(language == "en" && storeCode == "kw"){
//
//                CheckoutPublicKey = "pk_test_4bf680ff-873d-4fdb-a40e-f3279a2e0fc4"
//
//            }else if(language == "en" && storeCode == "om"){
//
//                CheckoutPublicKey = "pk_test_e8b39dd6-14c7-4ac7-b577-5cfb35bc10f4"
//
//            }else if(language == "ar" && storeCode == "om"){
//
//                CheckoutPublicKey = "pk_test_5f215130-b49d-4987-a667-f742dd3e60b9"
//
//            }else if(language == "en" && storeCode == "bh"){
//
//                CheckoutPublicKey = "pk_test_4422afc1-b773-4e50-901b-053587230f07"
//
//            }else if(language == "ar" && storeCode == "bh"){
//
//                CheckoutPublicKey = "pk_test_4f465478-4369-42e1-aa22-f4a9b14c687a"
//            }
//            return CheckoutPublicKey
//        }

        fun getVersionInfo(storeCode: String) =
            BASE_URL.plus(storeCode).plus("/cms_block/_search")

        fun getOnboardingVideoUrl() =
            "https://dfgub4cuqcmv6.cloudfront.net/levelshoes/mobileapp/images/Media/Levelshoes/540x960.mp4"

        fun getOnboardingData(languageCode: String) =
            BASE_URL.plus(languageCode).plus("/onboarding/staging")

        fun getProducts(storeCode: String) =
            BASE_URL.plus(storeCode).plus("/product/_search")

        fun getCategoryProducts() = BuildConfig.KLEVU_BASE.plus(klevuCloud).plus(klevuIDSearch)

        fun getCategoryNameFromCategoryId(storeCode: String, categoryId: Int) =
            BASE_URL.plus(storeCode).plus("/category/$categoryId")

        fun getCategoryBasedProductsFromKlevuIdSearch() =
            BuildConfig.KLEVU_BASE.plus(klevuCloud).plus(klevuIDSearch)

        fun getAttributes() = BASE_URL.plus("vue_storefront_magento_ae_en/attribute/_search")
    }
    //KLEVU_PRODUCTS_FROM_CATEGORY
    //?category=KLEVU_PRODUCT Women;shoe&isCategoryNavigationRequest=true&sortOrder=rel&visibility=search&paginationStartsFrom=0&showOutOfStockProducts=false&ticket=klevu-158358783414411589&noOfResults=10000&enableMultiSelectFilters=true&resultForZero=1&enableFilters=true&term=*&responseType=json
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


