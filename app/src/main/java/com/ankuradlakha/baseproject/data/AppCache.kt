package com.ankuradlakha.baseproject.data

import android.content.Context
import android.content.SharedPreferences
import javax.inject.Inject

class AppCache @Inject constructor(context: Context) {
    val sharedPreferences: SharedPreferences = context.getSharedPreferences("",Context.MODE_PRIVATE)

    fun getAuthToken(): String {
        return ""
    }
}