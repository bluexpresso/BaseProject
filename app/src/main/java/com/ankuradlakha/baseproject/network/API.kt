package com.ankuradlakha.baseproject.network

import retrofit2.Call
import retrofit2.http.GET

interface API {
    @GET("animals.json")
    fun getAnimals(): Call<ArrayList<Any>>
}