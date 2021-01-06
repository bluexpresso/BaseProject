package com.ankuradlakha.baseproject.utils

import android.content.Context
import com.ankuradlakha.baseproject.R
import com.ankuradlakha.baseproject.data.AppCache
import java.util.*

fun setLocale(context: Context?): Context? {
    try {
        val locale = Locale(getPreferredLanguage(context) ?: LANGUAGE_ENGLISH)
        Locale.setDefault(locale)
        val config = context?.resources?.configuration
        config?.setLocale(locale)
        config?.setLayoutDirection(locale)
        return context?.createConfigurationContext(config!!)
    } catch (ex: Exception) {

    }
    return null
}

fun getPreferredLanguage(context: Context?) =
    context?.getSharedPreferences(context.getString(R.string.app_name), Context.MODE_PRIVATE)
        ?.getString(AppCache.KEY_SELECTED_LANGUAGE, LANGUAGE_ENGLISH)

fun savePreferredLanguage(context: Context?, language: String) {
    context?.getSharedPreferences(context.getString(R.string.app_name), Context.MODE_PRIVATE)
        ?.edit()
        ?.putString(AppCache.KEY_SELECTED_LANGUAGE, language)?.apply()
}