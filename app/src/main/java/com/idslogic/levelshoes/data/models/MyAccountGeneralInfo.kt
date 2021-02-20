package com.idslogic.levelshoes.data.models

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class MyAccountGeneralInfo(
    @Expose
    @SerializedName("name")
    var name: String?,
    @Expose
    @SerializedName("url")
    var storeCode: String?,
) {
}