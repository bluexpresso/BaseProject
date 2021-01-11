package com.idslogic.levelshoes.network

import com.idslogic.levelshoes.data.models.BaseModel
import com.idslogic.levelshoes.data.models.OnboardingResponse
import com.idslogic.levelshoes.data.models.LandingResponse
import com.idslogic.levelshoes.data.models.Product
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
    ): Call<BaseModel<LandingResponse>>

    @GET
    fun getOnboardingData(@Url url: String): Call<OnboardingResponse>

    @POST
    fun getLandingProducts(@Url url: String, @Body request: JsonObject): Call<BaseModel<Product>>
}