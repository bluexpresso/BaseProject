package com.idslogic.levelshoes.utils

import com.google.gson.JsonArray
import com.google.gson.JsonElement
import com.google.gson.JsonObject
import com.idslogic.levelshoes.custom.SortView
import com.idslogic.levelshoes.custom.SortView.Companion.SORT_BY_HIGHEST_PRICE
import com.idslogic.levelshoes.custom.SortView.Companion.SORT_BY_LOWEST_PRICE
import com.idslogic.levelshoes.custom.SortView.Companion.SORT_BY_NEWEST_FIRST
import com.idslogic.levelshoes.data.models.FilterData
import com.idslogic.levelshoes.network.APIUrl
import java.lang.StringBuilder
import java.util.*

class ProductListingRequestBuilder {
    companion object {

        fun buildCategoryBasedProductListingRequest(
            fromPosition: Int,
            category: Int,
            productIds: ArrayList<Long>,
            genderFilter: String = "",
            filterData: FilterData? = null
        ): JsonObject {
            val request = JsonObject()

            val query = JsonObject()
            query.add(
                "function_score", buildFunctionScoreForKlevuCategoryProducts(
                    category,
                    productIds,
                    genderFilter,
                    filterData
                )
            )

            request.add("_source", getSourceArray())
            request.addProperty("size", productIds.size)
            request.addProperty("from", 0)
            request.add("aggs", buildAggsForKlevuCategoryProducts())
            request.add("query", query)
            if (!filterData?.sortBy.isNullOrEmpty() && filterData?.sortBy != SortView.SORT_BY_RELEVANCE) {
                request.add("sort", buildSortByPrice(filterData!!.sortBy!!))
            }
            return request
        }

        private fun buildSortByPrice(sortBy: String): JsonElement? {
            val finalPrice = JsonObject()
            if (sortBy == SORT_BY_HIGHEST_PRICE || sortBy == SORT_BY_LOWEST_PRICE) {
                val order = JsonObject()
                order.addProperty("order", sortBy)
                finalPrice.add("final_price", order)
                return finalPrice
            }
            if (sortBy == SORT_BY_NEWEST_FIRST) {
                val order = JsonObject()
                order.addProperty("order", SORT_BY_HIGHEST_PRICE)
                val scriptChildJson = JsonObject()
                scriptChildJson.addProperty(
                    "script",
                    "if([1870, 2019].contains((int)doc['seasondesc'].value)) return 1; else return 0;"
                )
                scriptChildJson.addProperty("type", "number")
                scriptChildJson.addProperty("order", "desc")
                val scriptParentJson = JsonObject()
                scriptParentJson.add("_script", scriptChildJson)

                val seasonDescJson = JsonObject()
                seasonDescJson.addProperty("seasondesc", "desc")
                return JsonArray().apply {
                    add(scriptParentJson)
                    add(seasonDescJson)
                }
            }
            return null
        }

        private fun buildFunctionScoreForKlevuCategoryProducts(
            category: Int,
            productIds: ArrayList<Long>,
            genderFilters: String? = "",
            filterData: FilterData? = null
        ): JsonObject {
            val functionScore = JsonObject()
            val query = JsonObject()
            val bool = JsonObject()
            val must = JsonArray()

            val matchTypeId = JsonObject()
            val typeId = JsonObject()
            typeId.addProperty("type_id", "configurable")
            matchTypeId.add("match", typeId)
            var matchCategoryId: JsonObject? = null
            if (category > 0) {
                matchCategoryId = JsonObject()
                val categoryId = JsonObject()
                categoryId.addProperty("category_ids", category)
                matchCategoryId.add("match", categoryId)
            }

            var genderFilterObject: JsonObject? = null
            if (!genderFilters.isNullOrEmpty()) {
                genderFilterObject = JsonObject()
                val gender = JsonArray()
                if (genderFilters.contains(",")) {
                    val genderFilterArray = genderFilters.split(",")
                    genderFilterArray.forEach { gndrFilter ->
                        gender.add(gndrFilter)
                    }
                } else {
                    gender.add(genderFilters)
                }
                genderFilterObject.add("terms", JsonObject().apply {
                    add("gender", gender)
                })
            }

            val termsVisibility = JsonObject()
            val visibility = JsonArray()
            visibility.add(2)
            visibility.add(4)
            termsVisibility.add("terms", JsonObject().apply {
                add("visibility", visibility)
            })

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
            if (matchCategoryId != null)
                must.add(matchCategoryId)
            if (genderFilterObject != null)
                must.add(genderFilterObject)
            must.add(termsVisibility)
            must.add(matchStatus)
            must.add(matchIsInStock)
            must.add(range)

            //FILTERS
            //1) Category filters
            if (!filterData?.categories.isNullOrEmpty()) {
                must.add(buildCategoryFilterJson(filterData))
            }
            bool.add("must", must)
            query.add("bool", bool)
            functionScore.add("query", query)
            functionScore.add("script_score", buildScriptJson(productIds))
            return functionScore
        }

        private fun buildCategoryFilterJson(filterData: FilterData?): JsonObject {
            val matchCategoryParent = JsonObject()
            val matchCategoryChild = JsonObject()
            val sbCategories = StringBuilder()
            filterData?.categories?.values?.forEach { catFilterOption ->
                sbCategories.append("${catFilterOption.name},")
            }
            sbCategories.deleteCharAt(sbCategories.lastIndex)
            matchCategoryChild.addProperty(
                "configurable_children.lvl_category_label", sbCategories.toString()
            )
            matchCategoryParent.add("match", matchCategoryChild)
            return matchCategoryParent
        }

        private fun buildScriptJson(productIds: ArrayList<Long>): JsonObject {
            val scriptScoreJson = JsonObject()
            val scriptJson = JsonObject()
            val sortOrderArray = JsonArray()
            productIds.forEach {
                sortOrderArray.add(it)
            }
            val paramsJson = JsonObject()
            paramsJson.add("sortOrder", sortOrderArray)
            scriptJson.add("params", paramsJson)
            scriptJson.addProperty("inline", "params.sortOrder.indexOf((int)doc['id'].value)")
            scriptScoreJson.add("script", scriptJson)
            return scriptScoreJson
        }

        private fun buildAggsForKlevuCategoryProducts(): JsonObject {
            val aggs = JsonObject()
            val maxPrice = JsonObject()
            val minPrice = JsonObject()
            val finalPrice = JsonObject()
            finalPrice.addProperty("field", "final_price")
            maxPrice.add("max", finalPrice)
            minPrice.add("min", finalPrice)
            aggs.add("max_price", maxPrice)
            aggs.add("min_price", minPrice)

            val termsManufacturerFilter = JsonObject()
            termsManufacturerFilter.addProperty("field", "configurable_children.manufacturer")
            termsManufacturerFilter.addProperty("size", 10000)
            aggs.add("manufacturerFilter", JsonObject().apply {
                add("terms", termsManufacturerFilter)
            })

            val termsSize = JsonObject()
            termsSize.addProperty("field", "configurable_children.size")
            termsSize.addProperty("size", 10000)
            aggs.add("sizeFilter", JsonObject().apply {
                add("terms", termsSize)
            })

            val termsGender = JsonObject()
            termsGender.addProperty("field", "configurable_children.gender")
            termsGender.addProperty("size", 10000)
            aggs.add("genderFilter", JsonObject().apply {
                add("terms", termsGender)
            })

            val termsLvlCategory = JsonObject()
            termsLvlCategory.addProperty("field", "configurable_children.lvl_category")
            termsLvlCategory.addProperty("size", 10000)
            aggs.add("lvl_categoryFilter", JsonObject().apply {
                add("terms", termsLvlCategory)
            })

            val termsColor = JsonObject()
            termsColor.addProperty("field", "configurable_children.color")
            termsColor.addProperty("size", 10000)
            aggs.add("colorFilter", JsonObject().apply {
                add("terms", termsColor)
            })

            return aggs
        }

        private fun getSourceArray(): JsonArray {
            val sourceArray = JsonArray()
            productFields.forEach {
                sourceArray.add(it)
            }
            return sourceArray
        }

        fun getCategoryBasedProductsQueryParams(
            category: String?, searchTerm: String,
            languageCode: String = "en", storeCode: String? = "ae",
            gender: String? = null
        ): HashMap<String, String> {
            return linkedMapOf<String, String>().apply {
                if (category != null) {
                    put("category", "KLEVU_PRODUCT $category")
                } else {
                    put("category", "KLEVU_PRODUCT")
                }
                put("isCategoryNavigationRequest", "true")
                put("sortOrder", "rel")
                put("visibility", "search")
                put("paginationStartsFrom", "0")
                put("showOutOfStockProducts", "false")
                put("ticket", APIUrl.getKlevuSkuCode(languageCode, storeCode ?: "ae"))
                put("noOfResults", "1000")
                put("enableMultiSelectFilters", "true")
                put("resultForZero", "1")
                put("enableFilters", "true")
                put("responseType", "json")
//                if (isDesignerCategory(category) || isCollectionsCategory(category)) {
                if (!gender.isNullOrEmpty())
                    put(
                        "applyFilters", when (gender) {
                            GENDER_WOMEN -> "gender:$CATEGORY_WOMEN"
                            GENDER_MEN -> "gender:$CATEGORY_MEN"
                            GENDER_KIDS -> {
                                "gender:${CATEGORY_BABY.toLowerCase(Locale.getDefault())};;gender:${
                                    CATEGORY_GIRL
                                };;gender:${
                                    CATEGORY_BOY
                                };;gender:${
                                    CATEGORY_UNISEX
                                }"
                            }
                            else -> "gender:$gender"
                        }
                    )
//                }
                put("term", searchTerm)
            }
        }

        fun getSearchProductsQueryParams(
            category: String?, searchTerm: String, gender: String,
            languageCode: String = "en", storeCode: String = "ae"
        ): HashMap<String, String> {
            return linkedMapOf<String, String>().apply {
                if (category != null) {
                    put("category", "KLEVU_PRODUCT $category")
                } else {
                    put("category", "KLEVU_PRODUCT")
                }
                put("sortOrder", "rel")
                put("visibility", "search")
                put("paginationStartsFrom", "0")
                put("showOutOfStockProducts", "false")
                put("ticket", APIUrl.getKlevuSkuCode(languageCode, storeCode))
                put("noOfResults", "1000")
                put("enableMultiSelectFilters", "true")
                put("resultForZero", "1")
                put("enableFilters", "true")
                put("fetchMinMaxPrice", "true")
                put("applyResults", "")
                put("klevu_filterLimit", "50")
                put("lsqt", "")
                put("sv", "2219")
                put("responseType", "json")
                put("applyFilters", getGenderFilter(gender, languageCode).first)
                put("term", searchTerm)
            }
        }
    }
}