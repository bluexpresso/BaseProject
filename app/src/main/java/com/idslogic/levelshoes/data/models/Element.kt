package com.idslogic.levelshoes.data.models

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class Element(
    @Expose
    @SerializedName("type")
    var type: String,
    @Expose
    @SerializedName("content")
    var content: String?,
    @Expose
    @SerializedName("background-color")
    var backgroundColor: String?,
    @Expose
    @SerializedName("background-image")
    var backgroundImage: String,
    @Expose
    @SerializedName("foreground-color")
    var foregroundColor: String?,
    @Expose
    @SerializedName("link_type")
    var linkType: String,
    @Expose
    @SerializedName("category_id")
    var categoryId: Int
)
