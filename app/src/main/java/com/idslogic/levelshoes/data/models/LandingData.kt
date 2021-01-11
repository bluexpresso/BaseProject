package com.idslogic.levelshoes.data.models

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.Expose

@Entity
data class LandingData(
    @PrimaryKey
    @Expose
    @ColumnInfo(name = "gender")
    var gender: String,
    @Expose
    @ColumnInfo(name = "content")
    var content: String?
)