package com.ankuradlakha.baseproject.data.models

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class LandingElementResponse(
    @Expose
    @SerializedName("data")
    var landingElement: ArrayList<LandingElement>
)

data class LandingElement(
    @Expose
    @SerializedName("box_type")
    var boxType: String,
    @Expose
    @SerializedName("sort_order")
    var sortOrder: Int,
    @Expose
    @SerializedName("data")
    var data: ArrayList<Data>,
)

data class Data(
    @Expose
    @SerializedName("type")
    var type: String,
    @Expose
    @SerializedName("url")
    var url: String,
    @Expose
    @SerializedName("status")
    var status: Int,
    @Expose
    @SerializedName("sort_order")
    var sortOrder: Int,
    @Expose
    @SerializedName("elements")
    var elements: ArrayList<Element>
)