package com.idslogic.levelshoes.data.models

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class Filter(
    @Expose
    @SerializedName("key")
    var key: String,
    @Expose
    @SerializedName("label")
    var label: String,
    @Expose
    @SerializedName("type")
    var type: String,
    @Expose
    @SerializedName("options")
    var options: ArrayList<FilterOption>,
)

data class FilterOption(
    @Expose
    @SerializedName("count")
    var count: Int,
    @Expose
    @SerializedName("isSelected")
    var isSelected: String,
    @Expose
    @SerializedName("name")
    var name: String,
    @Expose
    @SerializedName("value")
    var value: String
)
