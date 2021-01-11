package com.idslogic.levelshoes.ui.home.myaccount

import android.app.Application
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.AndroidViewModel
import com.idslogic.levelshoes.data.AppCache

class MyAccountViewModel @ViewModelInject constructor(
    private val appCache: AppCache,
    application: Application
) :
    AndroidViewModel(application) {
    fun getSelectedLanguage() = appCache.getSelectedLanguage()
    fun setSelectedLanguage(language: String) {
        appCache.setSelectedLanguage(language)
    }

}