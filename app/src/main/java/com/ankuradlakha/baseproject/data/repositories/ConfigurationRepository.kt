package com.ankuradlakha.baseproject.data.repositories

import com.ankuradlakha.baseproject.data.AppCache
import com.ankuradlakha.baseproject.data.AppDatabase
import com.ankuradlakha.baseproject.data.models.*
import com.ankuradlakha.baseproject.network.API
import com.ankuradlakha.baseproject.network.APIUrl
import com.ankuradlakha.baseproject.network.Resource
import com.ankuradlakha.baseproject.utils.GENDER_KIDS
import com.ankuradlakha.baseproject.utils.GENDER_MEN
import com.ankuradlakha.baseproject.utils.GENDER_WOMEN
import com.ankuradlakha.baseproject.utils.RequestBuilder
import com.google.gson.Gson
import com.google.gson.JsonObject
import com.google.gson.reflect.TypeToken
import retrofit2.Response
import java.io.IOException

class ConfigurationRepository(
    private val api: API,
    private val appCache: AppCache,
    private val appDatabase: AppDatabase
) {
    fun getLandingData(buildGetVersionInfoRequest: JsonObject): Resource<Response<BaseModel<LandingResponse>>> {
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

    fun getSelectedGender() = appCache.getSelectedGender()
    fun isOnboardingCompleted() = appCache.isOnboardingCompleted()
    fun getLandingProducts(productIds: Array<String>): Resource<ArrayList<BaseModel.Hit<Product>>> {
        val response = api.getLandingProducts(
            APIUrl.getLandingProducts(),
            RequestBuilder.buildLandingProductSearchRequest(productIds)
        ).execute()
        return if (response.isSuccessful && !response.body()?.hits?.hits.isNullOrEmpty()) {
            Resource.success(response.body()?.hits?.hits, response.code())
        } else {
            Resource.error()
        }
    }

    fun saveLandingData(map: HashMap<String, ArrayList<Content>>?) {
        if (map != null)
            appDatabase.getLandingDao()
                .insertLandingData(
                    arrayListOf(
                        LandingData(GENDER_WOMEN, Gson().toJson(map[GENDER_WOMEN])),
                        LandingData(GENDER_MEN, Gson().toJson(map[GENDER_MEN])),
                        LandingData(
                            GENDER_KIDS, Gson().toJson(map[GENDER_KIDS])
                        )
                    )
                )
    }

    fun getLandingData(): HashMap<String, ArrayList<Content>> {
        val womenData = appDatabase.getLandingDao().getLandingJson(GENDER_WOMEN)
        val menData = appDatabase.getLandingDao().getLandingJson(GENDER_MEN)
        val kidsData = appDatabase.getLandingDao().getLandingJson(GENDER_KIDS)
        val hashMap = HashMap<String, ArrayList<Content>>()
        if (womenData != null)
            hashMap[GENDER_WOMEN] =
                Gson().fromJson(womenData.content, object : TypeToken<ArrayList<Content>>() {}.type)
        if (menData != null)
            hashMap[GENDER_MEN] =
                Gson().fromJson(menData.content, object : TypeToken<ArrayList<Content>>() {}.type)
        if (kidsData != null)
            hashMap[GENDER_KIDS] =
                Gson().fromJson(kidsData.content, object : TypeToken<ArrayList<Content>>() {}.type)
        return hashMap
    }
}