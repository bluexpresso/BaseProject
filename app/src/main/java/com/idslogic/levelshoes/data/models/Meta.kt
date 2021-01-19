package com.idslogic.levelshoes.data.models

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class Meta(
    @Expose
    @SerializedName("storeBaseCurrency")
    var storeBaseCurrency: String?,
    @Expose
    @SerializedName("totalResultsFound")
    var totalResultsFound: Int?,
    @Expose
    @SerializedName("term")
    var term: String?,
)