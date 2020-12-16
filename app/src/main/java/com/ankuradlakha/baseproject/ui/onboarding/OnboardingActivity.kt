package com.ankuradlakha.baseproject.ui.onboarding

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.databinding.DataBindingUtil
import com.ankuradlakha.baseproject.BaseActivity
import com.ankuradlakha.baseproject.R
import com.ankuradlakha.baseproject.databinding.ActivityOnboardingBinding
import com.google.android.material.button.MaterialButton
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class OnboardingActivity : BaseActivity() {
    lateinit var skipIntro: MaterialButton

    companion object {
        fun startActivity(activity: Activity) {
            activity.startActivity(Intent(activity, OnboardingActivity::class.java))
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = DataBindingUtil.setContentView(this, R.layout.activity_onboarding)
                as ActivityOnboardingBinding
        initSkipIntro(binding)
    }

    private fun initSkipIntro(binding: ActivityOnboardingBinding) {
        skipIntro = binding.skipIntro
        skipIntro.setOnClickListener {
            skipIntro.visibility = View.GONE
            swapFragment(ChooseCountryFragment.newInstance(), true)
        }
    }

    override fun onBackPressed() {
        if (supportFragmentManager.findFragmentById(R.id.fragment_container) is ChooseCountryFragment) {
            skipIntro.visibility = View.VISIBLE
        }
        super.onBackPressed()
    }
}