package com.idslogic.levelshoes.data.models

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class ListingProductResponse(
    @Expose
    @SerializedName("meta")
    var meta: Meta?,
    @Expose
    @SerializedName("result")
    var result: ArrayList<ListingProduct>?,
    @Expose
    @SerializedName("filters")
    var filters: ArrayList<Filter>?,
    @Expose
    @SerializedName("price")
    var price: Price?,
    @Expose
    @SerializedName("popularProducts")
    val popularProducts: ArrayList<ListingProduct>?,
)