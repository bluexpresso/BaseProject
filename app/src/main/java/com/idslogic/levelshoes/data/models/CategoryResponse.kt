package com.idslogic.levelshoes.data.models

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class CategoryResponse(
    @Expose
    @SerializedName("_index")
    var parentId: Int,
    @Expose
    @SerializedName("id")
    var id: Int,
    @Expose
    @SerializedName("name")
    var name: String,
    @Expose
    @SerializedName("url_path")
    var urlPath: String,
    @Expose
    @SerializedName("children_data")
    var childrenData: ArrayList<CategoryResponse>
)