package com.ankuradlakha.baseproject.ui

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.ankuradlakha.baseproject.R
import com.ankuradlakha.baseproject.utils.setLocale
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
open class BaseActivity : AppCompatActivity() {
    fun swapFragment(fragment: Fragment, addTobackStack: Boolean) {
        supportFragmentManager.beginTransaction().replace(R.id.fragment_container, fragment)
            .addToBackStack(null)
            .commit()
    }

    override fun attachBaseContext(newBase: Context?) {
        super.attachBaseContext(setLocale(newBase))
    }

}