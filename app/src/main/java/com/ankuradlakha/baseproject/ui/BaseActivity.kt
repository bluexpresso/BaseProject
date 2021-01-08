package com.ankuradlakha.baseproject.ui

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.ankuradlakha.baseproject.R
import com.ankuradlakha.baseproject.utils.setLocale
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
open class BaseActivity : AppCompatActivity() {
    fun swapFragment(fragment: Fragment, customAnimation: Boolean) {
        val transaction = supportFragmentManager.beginTransaction()
        if (customAnimation) {
            transaction.setCustomAnimations(
                R.animator.fragment_slide_up,
                R.animator.fragment_slide_down
            )
        }
        transaction.replace(R.id.fragment_container, fragment)
            .addToBackStack(null)
            .commit()
    }

    override fun attachBaseContext(newBase: Context?) {
        super.attachBaseContext(setLocale(newBase))
    }

}