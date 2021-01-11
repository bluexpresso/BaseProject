package com.ankuradlakha.baseproject.ui

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.ankuradlakha.baseproject.LanguageContextWrapper
import com.ankuradlakha.baseproject.R
import com.ankuradlakha.baseproject.data.AppCache
import com.ankuradlakha.baseproject.utils.getPreferredLanguage
import com.ankuradlakha.baseproject.utils.setLocale
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
open class BaseActivity : AppCompatActivity() {
    @Inject
    lateinit var appCache: AppCache
    fun swapFragment(fragment: Fragment, addToBackStack: Boolean) {
        val transaction = supportFragmentManager.beginTransaction()
        if (addToBackStack)
            transaction.addToBackStack(null)
        transaction.replace(R.id.fragment_container, fragment)
            .commit()
    }

    override fun attachBaseContext(newBase: Context?) {
        super.attachBaseContext(
            LanguageContextWrapper.wrap(
                newBase,
                getPreferredLanguage(newBase)
            )
        )
    }

}