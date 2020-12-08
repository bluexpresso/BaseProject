package com.ankuradlakha.baseproject

import android.app.Application
import android.content.Context
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.ankuradlakha.baseproject.network.API
import com.google.gson.Gson
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import timber.log.Timber

class MainViewModel @ViewModelInject constructor(
    api: API,/*val mainDAO: MainDAO*/
    application: Application
) :
    AndroidViewModel(application) {
    private val context: Context = application.applicationContext
    private val apiServices: API = api
}