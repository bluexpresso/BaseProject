package com.ankuradlakha.baseproject.network

import com.ankuradlakha.baseproject.data.models.BaseModel
import com.ankuradlakha.baseproject.data.models.VersionInfoResponse
import com.google.gson.JsonObject
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Url

interface API {
    @POST
    fun getVersionInfo(
        @Url url: String,
        @Body request: JsonObject
    ): Call<BaseModel<VersionInfoResponse>>
}