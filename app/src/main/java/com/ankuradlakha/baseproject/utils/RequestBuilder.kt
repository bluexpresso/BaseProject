package com.ankuradlakha.baseproject.utils

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
    }
}