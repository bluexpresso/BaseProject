package com.idslogic.levelshoes.ui.home.search

import android.app.Application
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.AndroidViewModel
import com.idslogic.levelshoes.data.models.CategorySearch

class SubCategoryViewModel @ViewModelInject constructor(application: Application) :
    AndroidViewModel(application) {
    var gender: String? = null
    var category: CategorySearch? = null
    var categories: ArrayList<CategorySearch>? = null
}