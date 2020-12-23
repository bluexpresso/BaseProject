package com.ankuradlakha.baseproject.data.models

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class OnboardingResponse(
    @Expose
    @SerializedName("_source")
    var source: Source
)

data class Source(
    @Expose
    @SerializedName("date_updated")
    var dateUpdated: String,
    @Expose
    @SerializedName("country")
    var countries: ArrayList<Country>,
    @Expose
    @SerializedName("onboarding")
    var onboarding: ArrayList<Onboarding>,
    @Expose
    @SerializedName("language")
    var languages: ArrayList<Language>,
    @Expose
    @SerializedName("categories")
    var categories: Categories,
    @Expose
    @SerializedName("intro")
    var intro: ArrayList<Intro>

)