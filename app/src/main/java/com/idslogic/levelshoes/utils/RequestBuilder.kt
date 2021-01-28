package com.idslogic.levelshoes.utils

import com.google.gson.Gson
import com.google.gson.JsonArray
import com.google.gson.JsonObject
import com.google.gson.JsonParser
import com.idslogic.levelshoes.network.APIUrl

class RequestBuilder {
    companion object {
        fun buildGetVersionInfoRequest(gender: String): JsonObject {
            val request = JsonObject()
            val identifier = JsonObject()
            identifier.addProperty("identifier", gender)
            request.add("match", identifier)
            return buildRequest(request)
        }

        fun buildKlevuCategoryProductsRequest(
            category: String,
            fromSize: Int,
            size: Int
        ): JsonObject {
            val parentJsonObject = JsonObject()

            val query = JsonObject()
            query.add("function_score", buildFunctionScoreForKlevuCategoryProducts())


            parentJsonObject.add("_source", getSourceArray(productFields))
            parentJsonObject.add("query", query)
            parentJsonObject.addProperty("size", size)
            parentJsonObject.addProperty("from", fromSize)
            parentJsonObject.add("aggs", buildAggsForKlevuCategoryProducts())
            return parentJsonObject
        }

        private fun buildAggsForKlevuCategoryProducts(): JsonObject {
            val aggs = JsonObject()
            val maxPriceParent = JsonObject()
            val minPriceParent = JsonObject()
            val manufacturerFilter = JsonObject()
            val sizeFilter = JsonObject()
            val genderFilter = JsonObject()
            val lvlCategoryFilter = JsonObject()
            val colorFilter = JsonObject()

            val maxPrice = JsonObject()
            val minPrice = JsonObject()
            val finalPrice = JsonObject()
            finalPrice.add("field", finalPrice)
            maxPrice.add("max", finalPrice)
            minPrice.add("min", finalPrice)
            maxPriceParent.add("max_price", maxPrice)
            minPriceParent.add("min_price", minPrice)

            val termsManufacturerFilter = JsonObject()
            termsManufacturerFilter.addProperty("field", "configurable_children.manufacturer")
            termsManufacturerFilter.addProperty("size", 10000)
            manufacturerFilter.add("terms", termsManufacturerFilter)

            val termsSize = JsonObject()
            termsSize.addProperty("field", "configurable_children.size")
            termsSize.addProperty("size", 10000)
            sizeFilter.add("terms", termsSize)

            val termsGender = JsonObject()
            termsSize.addProperty("field", "configurable_children.gender")
            termsSize.addProperty("size", 10000)
            genderFilter.add("terms", termsGender)

            val termsLvlCategory = JsonObject()
            termsSize.addProperty("field", "configurable_children.lvl_category")
            termsSize.addProperty("size", 10000)
            lvlCategoryFilter.add("terms", termsLvlCategory)

            val termsColor = JsonObject()
            termsSize.addProperty("field", "configurable_children.color")
            termsSize.addProperty("size", 10000)
            colorFilter.add("terms", termsColor)

            aggs.add("", maxPriceParent)
            aggs.add("", minPriceParent)
            aggs.add("", manufacturerFilter)
            aggs.add("", genderFilter)
            aggs.add("", colorFilter)
            aggs.add("", sizeFilter)
            aggs.add("", lvlCategoryFilter)
            return aggs
        }

        //category=KLEVU_PRODUCT%20Women;the%20occasion%20wear%20edit&sortOrder=rel&visibility=search
        // &paginationStartsFrom=0&
        // showOutOfStockProducts=false&ticket=klevu-158676873614211589&noOfResults=10000&
        // enableMultiSelectFilters=true&resultForZero=1&enableFilters=true&term=*&responseType=json
        fun getQueryParamsForKlevuCategoryProducts(): HashMap<String, String> {
            val hashMap = hashMapOf<String, String>()
            hashMap["category"] = "KLEVU_PRODUCT Women;the occasion wear edit"
            hashMap["isCategoryNavigationRequest"] = "true"
            hashMap["sortOrder"] = "rel"
            hashMap["visibility"] = "search"
            hashMap["paginationStartsFrom"] = "0"
            hashMap["showOutOfStockProducts"] = "false"
            hashMap["ticket"] = APIUrl.getKlevuSkuCode("", "")
            hashMap["noOfResults"] = "10000"
            hashMap["enableMultiSelectFilters"] = "true"
            hashMap["resultForZero"] = "1"
            hashMap["enableFilters"] = "true"
            hashMap["term"] = "*"
            hashMap["responseType"] = "json"
            return hashMap
        }

        private fun buildFunctionScoreForKlevuCategoryProducts(): JsonObject {
            val functionScore = JsonObject()
            val query = JsonObject()
            val bool = JsonObject()
            val must = JsonArray()

            val matchTypeId = JsonObject()
            val typeId = JsonObject()
            typeId.addProperty("type_id", "configurable")
            matchTypeId.add("match", typeId)

            val terms = JsonObject()
            val visibility = JsonArray()
            visibility.add(2)
            visibility.add(4)
            terms.add("visibility", visibility)

            val matchIsInStock = JsonObject()
            val isInStock = JsonObject()
            isInStock.addProperty("configurable_children.stock.is_in_stock", true)
            matchIsInStock.add("match", isInStock)

            val matchStatus = JsonObject()
            val status = JsonObject()
            status.addProperty("configurable_children.status", 1)
            matchStatus.add("match", status)

            val gte = JsonObject()
            gte.addProperty("gte", 1)
            val configQty = JsonObject()
            configQty.add("configurable_children.stock.qty", gte)
            val range = JsonObject()
            range.add("range", configQty)

            must.add(matchTypeId)
            must.add(terms)
            must.add(matchStatus)
            must.add(matchIsInStock)
            must.add(range)
            bool.add("must", must)
            query.add("bool", bool)
            functionScore.add("query", query)
            return functionScore
        }

        private fun buildRequest(request: JsonObject): JsonObject {
            val json = JsonObject()
            val query = JsonObject()
            val bool = JsonObject()
            val must = JsonArray()
            must.add(request)
            bool.add("must", must)
            query.add("bool", bool)
            json.add("query", query)
            return json
        }

        fun buildLandingProductSearchRequest(
            gender: String,
            productIds: List<String>?
        ): JsonObject {
            val parentJson = JsonObject()
            parentJson.addProperty("size", 5)
            parentJson.addProperty("from", 0)

            val queryParentJson = JsonObject()

            val scoreJson = JsonObject()
            val queryChildJson = JsonObject()
            val scriptScoreJson = JsonObject()

            /*query child json*/
            val boolJson = JsonObject()

            /*bool json*/
            val mustJson = getMustJsonArray(gender)
            val sku = JsonArray()
            productIds?.forEach {
                sku.add(it)
            }
            val terms = JsonObject()
            terms.add("sku", sku)
            val termsParent = JsonObject()
            termsParent.add("terms", terms)
            mustJson.add(termsParent)
            boolJson.add("must", mustJson)
            queryChildJson.add("bool", boolJson)


            /*script_score*/
            val scriptJson = JsonObject()

            val sortOrderArray = JsonArray()
            productIds?.forEach {
                sortOrderArray.add(it)
            }
            val paramsJson = JsonObject()
            paramsJson.add("sortOrder", sortOrderArray)
            scriptJson.add("params", paramsJson)
            scriptJson.addProperty("inline", "params.sortOrder.indexOf((int)doc['id'].value)")
            scriptScoreJson.add("script", scriptJson)

            val sourceArray = getSourceArray(productFields)
            scoreJson.add("query", queryChildJson)
            scoreJson.add("script_score", scriptScoreJson)
            queryParentJson.add("function_score", scoreJson)
            parentJson.add("query", queryParentJson)
            parentJson.add("_source", sourceArray)
            return parentJson
        }

        private fun getSourceArray(fields: Array<String>): JsonArray {
            val sourceArray = JsonArray()
            fields.forEach {
                sourceArray.add(it)
            }
            return sourceArray
        }

        private fun getMustJsonArray(gender: String?): JsonArray {
            val mustJsonArray = JsonArray()
            if (gender != null) {
                val matchConfigurableGenderParent = JsonObject()
                val matchConfigurableGender = JsonObject()
                matchConfigurableGender.addProperty(
                    "configurable_children.gender", when {
                        gender.equals(
                            GENDER_MEN, true
                        ) -> GENDER_ID_MEN
                        gender.equals(GENDER_WOMEN, true) -> GENDER_ID_WOMEN
                        else -> GENDER_ID_KIDS
                    }
                )
//                matchConfigurableGenderParent.add("match", matchConfigurableGender)
                mustJsonArray.add(matchConfigurableGenderParent)
            }
            val matchTypeIdParent = JsonObject()
            val matchTypeId = JsonObject()
            matchTypeId.addProperty("type_id", "configurable")
            matchTypeIdParent.add("match", matchTypeId)
            mustJsonArray.add(matchTypeIdParent)

            val matchConfigurableChildrenParent = JsonObject()
            val matchConfigurableChildren = JsonObject()
            matchConfigurableChildren.addProperty("configurable_children.status", 1)
            matchConfigurableChildrenParent.add("match", matchConfigurableChildren)
            mustJsonArray.add(matchTypeIdParent)
            return mustJsonArray
        }

        fun getAttributeRequestBody() = JsonParser.parseString(
            "{\"_source\":\"options\",\"query\":{\"bool\":{\"must\":[{\"match\":{\"attribute_code\":\"manufacturer\"}}]}}}"
        ).asJsonObject

        fun getCategoryBasedProductsQueryParams(
            category: String
        ): HashMap<String, String> {
            return linkedMapOf<String, String>().apply {
                put("category", "KLEVU_PRODUCT $category")
                put("isCategoryNavigationRequest", "true")
                put("sortOrder", "rel")
                put("visibility", "search")
                put("paginationStartsFrom", "0")
                put("showOutOfStockProducts", "false")
                put("ticket", "klevu-158358783414411589")
                put("noOfResults", "1000")
                put("enableMultiSelectFilters", "true")
                put("resultForZero", "1")
                put("enableFilters", "true")
                put("responseType", "json")
                put("term", "*")
            }
        }
        fun buildCategoryBasedProductsSearchRequest(
            productsFromPosition: Int,
            productIds: List<String>?
        ): JsonObject {
            val parentJson = JsonObject()
            parentJson.addProperty("size", 20)
            parentJson.addProperty("from", productsFromPosition)

            val queryParentJson = JsonObject()

            val scoreJson = JsonObject()
            val queryChildJson = JsonObject()
            val scriptScoreJson = JsonObject()

            /*query child json*/
            val boolJson = JsonObject()

            /*bool json*/
            val mustJson = getMustJsonArray(null)
            boolJson.add("must", mustJson)
            queryChildJson.add("bool", boolJson)


            /*script_score*/
            val scriptJson = JsonObject()

            val sortOrderArray = JsonArray()
            productIds?.forEach {
                sortOrderArray.add(it)
            }
            val paramsJson = JsonObject()
            paramsJson.add("sortOrder", sortOrderArray)
            scriptJson.add("params", paramsJson)
            scriptJson.addProperty("inline", "params.sortOrder.indexOf((int)doc['id'].value)")
            scriptScoreJson.add("script", scriptJson)

            val sourceArray = getSourceArray(productFields)
            scoreJson.add("query", queryChildJson)
            scoreJson.add("script_score", scriptScoreJson)
            queryParentJson.add("function_score", scoreJson)
            parentJson.add("query", queryParentJson)
            parentJson.add("_source", sourceArray)
            return parentJson
        }
    }
}