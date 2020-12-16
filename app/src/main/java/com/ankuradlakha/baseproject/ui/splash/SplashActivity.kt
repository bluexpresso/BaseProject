package com.ankuradlakha.baseproject.ui.splash

import android.os.Bundle
import androidx.lifecycle.ViewModelProvider
import com.ankuradlakha.baseproject.BaseActivity
import com.ankuradlakha.baseproject.network.Status.*
import com.ankuradlakha.baseproject.ui.onboarding.OnboardingActivity
import com.ankuradlakha.baseproject.utils.GENDER_WOMEN
import com.ankuradlakha.baseproject.utils.RequestBuilder
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import timber.log.Timber

@AndroidEntryPoint
class SplashActivity : BaseActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val viewModel = ViewModelProvider(this).get(SplashViewModel::class.java)
        GlobalScope.launch {
            viewModel.getVersionInfo()
        }
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
}