package com.ankuradlakha.baseproject.ui.onboarding

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.view.animation.Animation
import androidx.appcompat.widget.AppCompatImageView
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.ankuradlakha.baseproject.BuildConfig
import com.ankuradlakha.baseproject.R
import com.ankuradlakha.baseproject.databinding.ActivityOnboardingBinding
import com.ankuradlakha.baseproject.network.APIUrl
import com.ankuradlakha.baseproject.network.Status.*
import com.ankuradlakha.baseproject.ui.BaseActivity
import com.ankuradlakha.baseproject.ui.MainActivity
import com.ankuradlakha.baseproject.utils.LONG_ANIMATION_DURATION
import com.ankuradlakha.baseproject.utils.yAnimate
import com.google.android.exoplayer2.MediaItem
import com.google.android.exoplayer2.Player
import com.google.android.exoplayer2.SimpleExoPlayer
import com.google.android.exoplayer2.video.VideoRendererEventListener
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.activity_onboarding.*
import kotlinx.coroutines.launch

@AndroidEntryPoint
class OnboardingActivity : BaseActivity(), VideoRendererEventListener {
    private var player: SimpleExoPlayer? = null
    lateinit var viewModel: OnboardingViewModel
    lateinit var alphaBackground: AppCompatImageView

    companion object {
        const val LOGO_TRANSLATE_ANIMATION = -200f
        fun startActivity(activity: Activity) {
            activity.startActivity(Intent(activity, OnboardingActivity::class.java))
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = DataBindingUtil.setContentView(this, R.layout.activity_onboarding)
                as ActivityOnboardingBinding
        viewModel = ViewModelProvider(this).get(OnboardingViewModel::class.java)
        alphaBackground = binding.alphaBackground
        initSkipIntro()
        initOnboardingNavigationInteractor()
        initOnboardingData()
        initGenderSelection()
        initNavigation(savedInstanceState)
    }

    private fun crossFadeAlphaBackground(shouldShow: Boolean) {
        if (shouldShow) {
            alphaBackground.apply {
                alpha = 0f
                visibility = View.VISIBLE
                animate().alpha(0.5f)
                    .setDuration(LONG_ANIMATION_DURATION)
                    .setListener(null)
            }
        } else {
            alphaBackground.animate().alpha(0f).setDuration(LONG_ANIMATION_DURATION)
                .setListener(object : AnimatorListenerAdapter() {
                    override fun onAnimationEnd(animation: Animator?) {
                        super.onAnimationEnd(animation)
                        alphaBackground.visibility = View.GONE
                    }
                })
        }
    }

    private fun initNavigation(savedInstanceState: Bundle?) {
        if (savedInstanceState == null) {
            val storeCode = viewModel.getSelectedCountry()?.storeCode
            if (storeCode != null && storeCode.isNotEmpty() &&
                viewModel.getSelectedGender().isNullOrEmpty()
            ) {
                crossFadeAlphaBackground(true)
                swapFragment(ChooseGenderFragment.newInstance(), false)
            } else {
                swapFragment(SkipIntroFragment.newInstance(), false)
            }
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
            logo_image.yAnimate(0f)
            swapFragment(ChooseGenderFragment.newInstance(), true)
            group_selected_country.visibility = View.VISIBLE
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
            logo_image.yAnimate(LOGO_TRANSLATE_ANIMATION)
            swapFragment(ChooseCountryFragment.newInstance(), true)
            crossFadeAlphaBackground(true)
        })
    }

    override fun onBackPressed() {
        when {
            supportFragmentManager.findFragmentById(R.id.fragment_container) is ChooseCountryFragment -> {
                crossFadeAlphaBackground(false)
                logo_image.yAnimate(0f)
                you_are_in.visibility = View.GONE
                country_language.visibility = View.GONE
                super.onBackPressed()
            }
            supportFragmentManager.findFragmentById(R.id.fragment_container) is ChooseGenderFragment -> {
                logo_image.yAnimate(LOGO_TRANSLATE_ANIMATION)
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