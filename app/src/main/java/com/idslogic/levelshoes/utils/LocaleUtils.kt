package com.idslogic.levelshoes.utils

import android.content.Context
import com.idslogic.levelshoes.data.AppCache
import java.util.*

fun setLocale(context: Context?): Context? {
    try {
        val language = getPreferredLanguage(context)
        if (language.isEmpty()) return null
        val locale = getLocale(context)
        Locale.setDefault(locale)
        val config = context?.resources?.configuration
        config?.setLocale(locale)
        config?.setLayoutDirection(locale)
        return if (context != null && config != null)
            context.createConfigurationContext(config)
        else null
    } catch (ex: Exception) {

    }
    return null
}

private fun getLocale(context: Context?) =
    Locale(getPreferredLanguage(context))

fun getPreferredLanguage(context: Context?): String {
    var userSelectedLanguage =
        context?.getSharedPreferences(AppCache.SHARED_PREFERENCE, Context.MODE_PRIVATE)
            ?.getString(AppCache.KEY_SELECTED_LANGUAGE, "") ?: ""
    if (userSelectedLanguage.isEmpty()) {
        val systemLanguage = Locale.getDefault().language
        userSelectedLanguage =
            if (systemLanguage == LANGUAGE_ENGLISH) LANGUAGE_ENGLISH else LANGUAGE_ARABIC
        savePreferredLanguage(
            context,
            userSelectedLanguage
        )
    }
    return context?.getSharedPreferences(AppCache.SHARED_PREFERENCE, Context.MODE_PRIVATE)
        ?.getString(AppCache.KEY_SELECTED_LANGUAGE, "") ?: ""
}

fun savePreferredLanguage(context: Context?, language: String) {
    context?.getSharedPreferences(AppCache.SHARED_PREFERENCE, Context.MODE_PRIVATE)
        ?.edit()
        ?.putString(AppCache.KEY_SELECTED_LANGUAGE, language)?.apply()
}