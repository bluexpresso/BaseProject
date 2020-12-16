package com.ankuradlakha.baseproject.data.models

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class VersionInfoResponse(
    @Expose
    @SerializedName("title")
    var title: String,

    @Expose
    @SerializedName("identifier")
    var identifier: String,

    @Expose
    @SerializedName("content")
    var content: String,

    @Expose
    @SerializedName("version_no")
    var versionNo: String,

    @Expose
    @SerializedName("store_view_id")
    var storeViewId: String,

    @Expose
    @SerializedName("id")
    var id: Int = 0,

    @Expose
    @SerializedName("active")
    var active: Boolean = false,

    @Expose
    @SerializedName("tsk")
    var tsk: Long = 0
)