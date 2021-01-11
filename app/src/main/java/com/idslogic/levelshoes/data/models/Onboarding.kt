package com.idslogic.levelshoes.data.models

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class Onboarding(
    @Expose
    @SerializedName("status")
    var status: String,
    @Expose
    @SerializedName("url")
    var url: String,
    @Expose
    @SerializedName("type")
    var type: String,
    @Expose
    @SerializedName("updated")
    var updated: String,
)
