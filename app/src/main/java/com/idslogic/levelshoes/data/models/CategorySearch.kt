package com.idslogic.levelshoes.data.models

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class CategorySearch(
    @Expose
    @SerializedName("column_breakpoint")
    var columnBreakpoint: Int?,
    @Expose
    @SerializedName("name")
    var name: String?,
    @Expose
    @SerializedName("menu_item_type")
    var menuItemType: String?,
    @Expose
    @SerializedName("menu_item_link")
    var menuItemLink: String?,
    @Expose
    @SerializedName("id")
    var id: Int?,
    @SerializedName("children_data")
    @Expose
    var childrenData: ArrayList<CategorySearch>?,
    @SerializedName("include_in_menu")
    @Expose
    var includeInMenu: Int?
) : Serializable



