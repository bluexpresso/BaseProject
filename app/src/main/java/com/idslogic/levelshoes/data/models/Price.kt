package com.idslogic.levelshoes.data.models

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class Price(
    @Expose
    @SerializedName("min")
    var min:String?,
    @Expose
    @SerializedName("max")
    var max:String?,
    @Expose
    @SerializedName("start")
    var start:String?,
    @Expose
    @SerializedName("end")
    var end:String?,
)
