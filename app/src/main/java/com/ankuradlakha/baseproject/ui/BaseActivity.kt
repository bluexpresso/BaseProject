package com.ankuradlakha.baseproject.ui

import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.ankuradlakha.baseproject.R
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
open class BaseActivity : AppCompatActivity() {
    fun swapFragment(fragment: Fragment, addTobackStack: Boolean) {
        supportFragmentManager.beginTransaction().replace(R.id.fragment_container, fragment)
            .addToBackStack(null)
            .commit()
    }

}