package com.ankuradlakha.baseproject.ui.home.landing

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.ankuradlakha.baseproject.data.models.Product

class ProductPagerItemViewModel(application: Application) : AndroidViewModel(application) {
    lateinit var product: Product
}