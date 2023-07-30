package com.ankuradlakha.baseproject

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.ankuradlakha.baseproject.network.API
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    api: API,/*val mainDAO: MainDAO*/
    application: Application
) :
    AndroidViewModel(application) {
    private val apiServices: API = api
}