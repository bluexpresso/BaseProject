package com.idslogic.levelshoes.data.models

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

@Entity(tableName = "attribute")
data class Attribute(
    @PrimaryKey(autoGenerate = true)
    var id: Int,
    @Expose
    @SerializedName("label")
    var label: String,
    @Expose
    @SerializedName("value")
    var value: String,
    @Expose
    @SerializedName("sort_order")
    var sortOrder: Int
)
