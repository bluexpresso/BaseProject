package com.ankuradlakha.baseproject.ui.onboarding

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.ankuradlakha.baseproject.data.models.Country

class ChooseCountryViewModel(application: Application) : AndroidViewModel(application) {
    var country : Country? = null
}