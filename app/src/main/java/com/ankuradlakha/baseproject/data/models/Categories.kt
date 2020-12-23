package com.ankuradlakha.baseproject.data.models

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class Categories(
    @Expose
    @SerializedName("data")
    var data: ArrayList<CategoryData>,
    @Expose
    @SerializedName("names")
    var names: ArrayList<Name>
)

data class CategoryData(
    @Expose
    @SerializedName("status")
    var status: String,
    @Expose
    @SerializedName("image_url")
    var imageUrl: String,
    @Expose
    @SerializedName("type")
    var type: String,
    @Expose
    @SerializedName("updated")
    var udpated: String,
    @Expose
    @SerializedName("lable-color")
    var labelColor: String,
    @Expose
    @SerializedName("logo-url")
    var logoUrl: String
)

data class Name(
    @Expose
    @SerializedName("name")
    var name: String,
    @Expose
    @SerializedName("url")
    var url: String
)
