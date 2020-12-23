package com.ankuradlakha.baseproject.data.models

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class Country(
    @Expose
    @SerializedName("name")
    var name: String,
    @Expose
    @SerializedName("storecode")
    var storeCode: String,
    @Expose
    @SerializedName("flag")
    var flag: String,
    @Expose
    @SerializedName("currency")
    var currency: String
)
