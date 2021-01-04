package com.ankuradlakha.baseproject.data

import androidx.room.TypeConverter
import com.ankuradlakha.baseproject.data.models.*
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class TypeConverters {
    @TypeConverter
    fun listToJson(value: ArrayList<Country>?) = Gson().toJson(value)

    @TypeConverter
    fun jsonToList(value: String?): ArrayList<Country>? {
        return Gson().fromJson(value, object : TypeToken<ArrayList<Country>>() {
        }.type)
    }

    @TypeConverter
    fun onboardingToJson(value: ArrayList<Onboarding>?) = Gson().toJson(value)

    @TypeConverter
    fun jsonToOnboardingList(value: String?): ArrayList<Onboarding>? {
        return Gson().fromJson(value, object : TypeToken<ArrayList<Onboarding>>() {
        }.type)
    }

    @TypeConverter
    fun languageToJson(value: ArrayList<Language>?) = Gson().toJson(value)

    @TypeConverter
    fun jsonToLanguageList(value: String?): ArrayList<Language>? {
        return Gson().fromJson(value, object : TypeToken<ArrayList<Language>>() {
        }.type)
    }

    @TypeConverter
    fun categoriesToJson(value: Categories?) = Gson().toJson(value)

    @TypeConverter
    fun jsonToCategories(value: String?): Categories? {
        return Gson().fromJson(value, Categories::class.java)
    }

    @TypeConverter
    fun categoryDataToJson(value: ArrayList<CategoryData>?) = Gson().toJson(value)

    @TypeConverter
    fun jsonToCategoryDataList(value: String?): ArrayList<CategoryData>? {
        return Gson().fromJson(value, object : TypeToken<ArrayList<CategoryData>>() {
        }.type)
    }

    @TypeConverter
    fun namesToJson(value: ArrayList<Name>?) = Gson().toJson(value)

    @TypeConverter
    fun jsonToNamesList(value: String?): ArrayList<Name>? {
        return Gson().fromJson(value, object : TypeToken<ArrayList<Name>>() {
        }.type)
    }

    @TypeConverter
    fun introsToJson(value: ArrayList<Intro>?) = Gson().toJson(value)

    @TypeConverter
    fun jsonToIntrosList(value: String?): ArrayList<Intro>? {
        return Gson().fromJson(value, object : TypeToken<ArrayList<Intro>>() {
        }.type)
    }
}