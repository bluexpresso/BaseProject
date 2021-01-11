package com.idslogic.levelshoes.utils

import com.google.gson.JsonArray
import com.google.gson.JsonObject

class RequestBuilder {
    companion object {
        fun buildGetVersionInfoRequest(gender: String): JsonObject {
            val request = JsonObject()
            val identifier = JsonObject()
            identifier.addProperty("identifier", gender)
            request.add("match", identifier)
            return buildRequest(request)
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

        fun buildLandingProductSearchRequest(productIds: Array<String>?): JsonObject {
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
            val mustJson = getMustJsonArray(GENDER_WOMEN)
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

            val sourceArray = JsonArray()
            productFields.forEach {
                sourceArray.add(it)
            }
            scoreJson.add("query", queryChildJson)
            scoreJson.add("script_score", scriptScoreJson)
            queryParentJson.add("function_score", scoreJson)
            parentJson.add("query", queryParentJson)
            parentJson.add("_source", sourceArray)
            return parentJson
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
                matchConfigurableGenderParent.add("match", matchConfigurableGender)
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
    }
}