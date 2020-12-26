package com.ankuradlakha.baseproject.data.models

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class Language(
    @Expose
    @SerializedName("name")
    var name: String,
    @Expose
    @SerializedName("langcode")
    var lancode: String
)
