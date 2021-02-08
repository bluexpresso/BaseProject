package com.idslogic.levelshoes.network

import com.google.gson.JsonObject
import com.idslogic.levelshoes.data.models.*
import kotlinx.coroutines.Deferred
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
    fun getProducts(@Url url: String, @Body request: JsonObject): Call<BaseModel<Product>>

    @POST
    suspend fun getProductsAsync(@Url url: String, @Body request: JsonObject): BaseModel<Product>

    @POST
    fun getCategoryProducts(
        @Url url: String,
        @QueryMap map: HashMap<String, String>,
        @Body requestBody: JsonObject
    ): Call<ListingProductResponse>

    @POST
    fun getAttributes(@Url url: String,@Body request:JsonObject): Call<JsonObject>


    @POST
    fun getCategory(@Url url: String,@Body request: JsonObject):Call<BaseModel<CategorySearch>> //returns check   //@Body request: JsonObject

    @GET
    fun getCategoryDetailFromId(@Url url: String): Call<BaseModel.Hit<CategoryResponse>>

    @GET
    fun getCategoryBasedProductsFromKlevuIdSearch(
        @Url url: String,
        @QueryMap(encoded = false) queryMap: HashMap<String, String>
    ): Call<ListingProductResponse>

}