package com.idslogic.levelshoes.data.models

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class TrendingProducts(
    @Expose
    @SerializedName("name")
    val name : String?,

)



