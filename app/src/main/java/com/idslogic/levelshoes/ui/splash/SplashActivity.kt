package com.idslogic.levelshoes.ui.splash

import android.os.Bundle
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.idslogic.levelshoes.R
import com.idslogic.levelshoes.network.Status.*
import com.idslogic.levelshoes.ui.BaseActivity
import com.idslogic.levelshoes.ui.MainActivity
import com.idslogic.levelshoes.ui.onboarding.OnboardingActivity
import com.idslogic.levelshoes.utils.*
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import timber.log.Timber
import java.util.*

@AndroidEntryPoint
class SplashActivity : BaseActivity() {
    lateinit var viewModel: SplashViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProvider(this).get(SplashViewModel::class.java)
        checkVersion()
        viewModel.versionInfoLiveData.observe(this@SplashActivity, {
            when (it.status) {
                LOADING -> {
                }
                SUCCESS -> {
                    if (viewModel.isOnboardingDone()) {
                        MainActivity.startActivity(this)
                        finishAffinity()
                    } else {
                        OnboardingActivity.startActivity(this)
                        finish()
                        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
                    }
                }
                ERROR -> {
                    Timber.d("Error")
                }
            }
        })
    }

    private fun checkVersion() {
        if (isInternetAvailable(this)) {
            lifecycleScope.launch {
                viewModel.getVersionInfo()
            }
        } else {
            getNoInternetDialog(this).setPositiveButton(
                R.string.retry
            ) { _, _ ->
                checkVersion()
            }.show()
        }
    }
}