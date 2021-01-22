package com.idslogic.levelshoes.network

import com.google.gson.JsonObject
import com.idslogic.levelshoes.data.models.*
import retrofit2.Call
import retrofit2.http.*

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

    @POST
    fun getCategoryProducts(
        @Url url: String,
        @QueryMap map: HashMap<String, String>,
        @Body requestBody: JsonObject
    ): Call<ListingProductResponse>

    @POST
    fun getAttributes(@Url url: String,@Body request:JsonObject): Call<JsonObject>
}