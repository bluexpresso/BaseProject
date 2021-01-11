package com.idslogic.levelshoes.data.models

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class Intro(
    @Expose
    @SerializedName("updated")
    var updated: String,
    @Expose
    @SerializedName("status")
    var status: String,
    @Expose
    @SerializedName("type")
    var type: String,
    @Expose
    @SerializedName("url")
    var url: String,
    @Expose
    @SerializedName("foreground-color")
    var foregroundColor: String,
    @Expose
    @SerializedName("logo-url")
    var logoUrl: String
)
