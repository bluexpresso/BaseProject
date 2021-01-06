package com.ankuradlakha.baseproject.ui.onboarding

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.ankuradlakha.baseproject.ui.BaseActivity
import com.ankuradlakha.baseproject.BuildConfig
import com.ankuradlakha.baseproject.ui.MainActivity
import com.ankuradlakha.baseproject.R
import com.ankuradlakha.baseproject.data.models.Country
import com.ankuradlakha.baseproject.databinding.ActivityOnboardingBinding
import com.ankuradlakha.baseproject.network.APIUrl
import com.ankuradlakha.baseproject.network.Status.*
import com.google.android.exoplayer2.MediaItem
import com.google.android.exoplayer2.Player
import com.google.android.exoplayer2.SimpleExoPlayer
import com.google.android.exoplayer2.video.VideoRendererEventListener
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.activity_onboarding.*
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

@AndroidEntryPoint
class OnboardingActivity : BaseActivity(), VideoRendererEventListener {
    private var player: SimpleExoPlayer? = null
    lateinit var viewModel: OnboardingViewModel

    companion object {
        fun startActivity(activity: Activity) {
            activity.startActivity(Intent(activity, OnboardingActivity::class.java))
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = DataBindingUtil.setContentView(this, R.layout.activity_onboarding)
                as ActivityOnboardingBinding
        viewModel = ViewModelProvider(this).get(OnboardingViewModel::class.java)
        initSkipIntro()
        initOnboardingNavigationInteractor()
        initOnboardingData()
        initGenderSelection()
        if (savedInstanceState == null) {
            swapFragment(SkipIntroFragment.newInstance(), false)
        }
    }

    private fun initGenderSelection() {
        viewModel.selectedGender.observe(this, {
            viewModel.saveSelectedGender()
            MainActivity.startActivity(this)
            finishAffinity()
        })
    }

    private fun initOnboardingData() {
        viewModel.onboardingLiveData.observe(this, {
            when (it.status) {
                LOADING -> {
                }
                SUCCESS -> {
                    if (!it.data?.countries.isNullOrEmpty()) {
                        viewModel.countriesListLiveData.value =
                            it.data!!.countries ?: arrayListOf()
                    }
                }
                ERROR -> {
                }
            }
        })
        lifecycleScope.launch {
            viewModel.getOnboardingData()
        }
    }

    override fun onResume() {
        super.onResume()
        initVideo()
    }

    override fun onPause() {
        super.onPause()
        player?.stop()
        player?.release()
    }

    private fun initOnboardingNavigationInteractor() {
        viewModel.onboardingNavigationInteractor.observe(this, {
            swapFragment(ChooseGenderFragment.newInstance(), true)
            you_are_in.visibility = View.VISIBLE
            country_language.visibility = View.VISIBLE
            country_language.text = String.format(
                "%s|%s",
                viewModel.getSelectedCountry().name, viewModel.getSelectedCountry().storeCode
            )
        })
    }

    private fun initVideo() {
        val mediaItem: MediaItem = if (BuildConfig.DEBUG) {
            MediaItem.fromUri(
                Uri.parse(
                    "android.resource://" + packageName + "/" +
                            R.raw.onboarding_video
                )
            )
        } else {
            MediaItem.fromUri(Uri.parse(APIUrl.getOnboardingVideoUrl()))
        }
        player = SimpleExoPlayer.Builder(this).build()
        video_view.player = player
        player!!.setMediaItem(mediaItem)
        player!!.playWhenReady = true
        player!!.repeatMode = Player.REPEAT_MODE_ONE
        player!!.volume = 0f
        player!!.prepare()
    }

    private fun initSkipIntro() {
        viewModel.skipIntroLiveData.observe(this, {
            swapFragment(ChooseCountryFragment.newInstance(), true)
        })
    }

    override fun onBackPressed() {
        when {
            supportFragmentManager.findFragmentById(R.id.fragment_container) is ChooseCountryFragment -> {
                you_are_in.visibility = View.GONE
                country_language.visibility = View.GONE
                super.onBackPressed()
            }
            supportFragmentManager.findFragmentById(R.id.fragment_container) is SkipIntroFragment -> {
                finish()
            }
            else -> {
                super.onBackPressed()
            }
        }
    }
}