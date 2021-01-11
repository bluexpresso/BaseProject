package com.idslogic.levelshoes.ui.onboarding

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.idslogic.levelshoes.data.models.Country

class ChooseCountryViewModel(application: Application) : AndroidViewModel(application) {
    var country : Country? = null
}