package com.idslogic.levelshoes.data.models

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class Content(
    @Expose
    @SerializedName("box_type")
    var boxType: String,
    @Expose
    @SerializedName("sort_order")
    var sortOrder: Int,
    @Expose
    @SerializedName("title")
    var title: String?,
    @Expose
    @SerializedName("subtitle")
    var subTitle: String?,
    @Expose
    @SerializedName("category_id")
    var categoryId: Int,
    @Expose
    @SerializedName("data")
    var data: ArrayList<ContentData>?,
    @Expose
    @SerializedName("products")
    var productIds: ProductIds?,
    var productsList: ArrayList<BaseModel.Hit<Product>>?
)

data class ContentData(
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
    var elements: ArrayList<Element>,
)

data class ProductIds(
    @Expose
    @SerializedName("primary_vpn")
    var products: ArrayList<String>
)