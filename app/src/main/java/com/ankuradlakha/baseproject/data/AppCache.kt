package com.ankuradlakha.baseproject.data

import android.content.Context
import android.content.SharedPreferences
import com.ankuradlakha.baseproject.R
import com.ankuradlakha.baseproject.data.models.Country
import com.ankuradlakha.baseproject.utils.GENDER_KIDS
import com.ankuradlakha.baseproject.utils.GENDER_MEN
import com.ankuradlakha.baseproject.utils.GENDER_WOMEN
import com.ankuradlakha.baseproject.utils.LANGUAGE_ENGLISH
import com.google.gson.Gson
import javax.inject.Inject

class AppCache @Inject constructor(context: Context) {
    companion object {
        const val KEY_VERSION_MEN = "key_version_men"
        const val KEY_VERSION_WOMEN = "key_version_women"
        const val KEY_VERSION_KIDS = "key_version_kids"
        const val KEY_AUTH_TOKEN = "key_auth_token"
        const val KEY_SELECTED_LANGUAGE = "key_selected_language"
        const val KEY_COUNTRY = "key_country"
        const val KEY_GENDER = "key_gender"
        const val KEY_ONBOARDING_COMPLETED = "key_onboarding_completed"
    }

    private val sharedPreferences: SharedPreferences =
        context.getSharedPreferences(context.getString(R.string.app_name), Context.MODE_PRIVATE)

    fun getAuthToken(): String {
        return ""
    }

    fun setAuthToken(authToken: String) {
        sharedPreferences.edit().putString(KEY_AUTH_TOKEN, authToken).apply()
    }

    fun getCurrentVersion(gender: String): String {
        if (gender == GENDER_MEN) {
            return sharedPreferences.getString(KEY_VERSION_MEN, "0") ?: "0"
        }
        if (gender == GENDER_WOMEN) {
            return sharedPreferences.getString(KEY_VERSION_WOMEN, "0") ?: "0"
        }
        if (gender == GENDER_KIDS) {
            return sharedPreferences.getString(KEY_VERSION_KIDS, "0") ?: "0"
        }
        return "0"
    }

    fun setCurrentVersion(gender: String, currentVersion: String) {
        if (gender == GENDER_MEN) {
            sharedPreferences.edit().putString(KEY_VERSION_MEN, currentVersion).apply()
        }
        if (gender == GENDER_WOMEN) {
            sharedPreferences.edit().putString(KEY_VERSION_WOMEN, currentVersion).apply()
        }
        if (gender == GENDER_KIDS) {
            sharedPreferences.edit().putString(KEY_VERSION_KIDS, currentVersion).apply()
        }
    }

    fun setSelectedLanguage(languageCode: String) {
        sharedPreferences.edit().putString(KEY_SELECTED_LANGUAGE, languageCode).apply()
    }

    fun getSelectedLanguage() =
        sharedPreferences.getString(KEY_SELECTED_LANGUAGE, LANGUAGE_ENGLISH) ?: LANGUAGE_ENGLISH

    fun setSelectedCountry(country: Country?) {
        sharedPreferences.edit().putString(KEY_COUNTRY, Gson().toJson(country)).apply()
    }

    fun getSelectedCountry() =
        Gson().fromJson(sharedPreferences.getString(KEY_COUNTRY, ""), Country::class.java)

    fun setSelectedGender(value: String?) {
        sharedPreferences.edit().putString(KEY_GENDER, value).apply()
    }

    fun getSelectedGender() = sharedPreferences.getString(KEY_GENDER, GENDER_WOMEN)
    fun setOnboardingCompleted(onboardingCompleted: Boolean) {
        sharedPreferences.edit().putBoolean(KEY_ONBOARDING_COMPLETED, onboardingCompleted).apply()
    }

    fun isOnboardingCompleted() = sharedPreferences.getBoolean(KEY_ONBOARDING_COMPLETED, false)
}