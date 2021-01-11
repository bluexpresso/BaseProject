package com.idslogic.levelshoes.ui

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.idslogic.levelshoes.LanguageContextWrapper
import com.idslogic.levelshoes.R
import com.idslogic.levelshoes.data.AppCache
import com.idslogic.levelshoes.utils.getPreferredLanguage
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