package com.idslogic.levelshoes.utils

import android.util.Log
import com.google.gson.JsonArray
import com.google.gson.JsonObject
import timber.log.Timber

class ProductListingRequestBuilder {
    companion object {

        fun buildCategoryBasedProductListingRequest(
            fromPosition: Int,
            category: Int,
            productIds: ArrayList<Long>
        ): JsonObject {
            val request = JsonObject()

            val query = JsonObject()
            query.add(
                "function_score", buildFunctionScoreForKlevuCategoryProducts(
                    category,
                    productIds
                )
            )

            request.add("_source", getSourceArray())
            request.addProperty("size", 20)
            request.addProperty("from", fromPosition)
            request.add("aggs", buildAggsForKlevuCategoryProducts())
            request.add("query", query)
            return request
        }

        private fun buildFunctionScoreForKlevuCategoryProducts(
            category: Int,
            productIds: ArrayList<Long>
        ): JsonObject {
            val functionScore = JsonObject()
            val query = JsonObject()
            val bool = JsonObject()
            val must = JsonArray()

            val matchTypeId = JsonObject()
            val typeId = JsonObject()
            typeId.addProperty("type_id", "configurable")
            matchTypeId.add("match", typeId)

            val matchCategoryId = JsonObject()
            val categoryId = JsonObject()
            categoryId.addProperty("category_ids", category)
            matchCategoryId.add("match", categoryId)

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
            must.add(matchCategoryId)
            must.add(termsVisibility)
            must.add(matchStatus)
            must.add(matchIsInStock)
            must.add(range)
            bool.add("must", must)
            query.add("bool", bool)
            functionScore.add("query", query)
            functionScore.add("script_score", buildScriptJson(productIds))
            return functionScore
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
            category: String?, searchTerm: String
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
                put("ticket", "klevu-158358783414411589")
                put("noOfResults", "1000")
                put("enableMultiSelectFilters", "true")
                put("resultForZero", "1")
                put("enableFilters", "true")
                put("responseType", "json")
                put("term", searchTerm)
            }
        }
    }
}