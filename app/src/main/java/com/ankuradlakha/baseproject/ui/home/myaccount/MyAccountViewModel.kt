package com.ankuradlakha.baseproject.ui.home.myaccount

import android.app.Application
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.AndroidViewModel
import com.ankuradlakha.baseproject.data.AppCache

class MyAccountViewModel @ViewModelInject constructor(
    private val appCache: AppCache,
    application: Application
) :
    AndroidViewModel(application) {
    fun getSelectedLanguage() = appCache.getSelectedLanguage()

}