package com.ankuradlakha.baseproject.data.repositories

import com.ankuradlakha.baseproject.data.AppCache
import com.ankuradlakha.baseproject.data.models.BaseModel
import com.ankuradlakha.baseproject.data.models.VersionInfoResponse
import com.ankuradlakha.baseproject.network.API
import com.ankuradlakha.baseproject.network.APIUrl
import com.ankuradlakha.baseproject.network.Resource
import com.google.gson.JsonObject
import retrofit2.Response
import java.io.IOException

class ConfigurationRepository(private val api: API, private val appCache: AppCache) {
    fun getVersionInfo(buildGetVersionInfoRequest: JsonObject): Resource<Response<BaseModel<VersionInfoResponse>>> {
        return try {
            val response =
                api.getVersionInfo(APIUrl.getVersionInfo(), buildGetVersionInfoRequest).execute()
            if (response.isSuccessful) {
                Resource.success(response, response.code())
            } else {
                Resource.error("", null, response.code())
            }
        } catch (e: IOException) {
            e.printStackTrace()
            Resource.error(e.message, null, 401)
        }
    }

    fun getCurrentVersion(gender: String) =
        appCache.getCurrentVersion(gender)

    fun saveCurrentVersion(gender: String, version: String) {
        appCache.setCurrentVersion(gender, version)
    }
}