package com.idslogic.levelshoes.data.models

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class OnboardingResponse(
    @Expose
    @SerializedName("_source")
    var source: Source?
)

@Entity(tableName = "onboarding_data")
data class Source(
    @PrimaryKey(autoGenerate = true)
    var id: Int,
    @Expose
    @SerializedName("date_updated")
    var dateUpdated: String?,
    @Expose
    @SerializedName("country")
    var countries: ArrayList<Country>?,
    @Expose
    @SerializedName("onboarding")
    var onboarding: ArrayList<Onboarding>?,
    @Expose
    @SerializedName("language")
    var languages: ArrayList<Language>?,
    @Expose
    @SerializedName("categories")
    var categories: Categories?,
    @Expose
    @SerializedName("intro")
    var intro: ArrayList<Intro>?
) {
    constructor() : this(0, null, null, null, null, null, null)
}