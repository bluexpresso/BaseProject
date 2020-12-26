package com.ankuradlakha.baseproject.ui.splash

import android.os.Bundle
import androidx.lifecycle.ViewModelProvider
import com.ankuradlakha.baseproject.ui.BaseActivity
import com.ankuradlakha.baseproject.R
import com.ankuradlakha.baseproject.network.Status.*
import com.ankuradlakha.baseproject.ui.onboarding.OnboardingActivity
import com.ankuradlakha.baseproject.utils.getNoInternetDialog
import com.ankuradlakha.baseproject.utils.isInternetAvailable
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import timber.log.Timber

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
                    Timber.d("LOADING")
                }
                SUCCESS -> {
                    OnboardingActivity.startActivity(this)
                    finish()
                }
                ERROR -> {
                    Timber.d("Error")
                }
            }
        })
    }

    private fun checkVersion() {
        if (isInternetAvailable(this)) {
            GlobalScope.launch {
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