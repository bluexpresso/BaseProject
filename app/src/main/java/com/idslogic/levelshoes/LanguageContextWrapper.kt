package com.idslogic.levelshoes

import android.annotation.TargetApi
import android.content.Context
import android.content.ContextWrapper
import android.os.Build
import java.util.*

class LanguageContextWrapper(base: Context?) : ContextWrapper(base) {
    companion object {
        fun wrap(context: Context?, language: String): ContextWrapper? {
            return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                updateResources(context, language)
            } else updateResourcesLegacy(context, language)
        }

        @TargetApi(Build.VERSION_CODES.N)
        private fun updateResources(context: Context?, language: String): ContextWrapper? {
            val locale = Locale(language)
            Locale.setDefault(locale)
            val configuration = context?.resources?.configuration
            configuration?.setLocale(locale)
            configuration?.setLayoutDirection(locale)
            return if (configuration == null)
                null
            else LanguageContextWrapper(context.createConfigurationContext(configuration))
        }

        private fun updateResourcesLegacy(context: Context?, language: String): ContextWrapper {
            val locale = Locale(language)
            Locale.setDefault(locale)
            val resources = context?.resources
            val configuration = resources?.configuration
            configuration?.locale = locale
            configuration?.setLayoutDirection(locale)
            resources?.updateConfiguration(configuration, resources.displayMetrics)
            return LanguageContextWrapper(context)
        }
    }
}