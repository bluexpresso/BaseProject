package com.idslogic.levelshoes.ui.onboarding

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import androidx.appcompat.widget.AppCompatImageView
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.idslogic.levelshoes.BuildConfig
import com.idslogic.levelshoes.R
import com.idslogic.levelshoes.databinding.ActivityOnboardingBinding
import com.idslogic.levelshoes.network.APIUrl
import com.idslogic.levelshoes.network.Status.*
import com.idslogic.levelshoes.ui.BaseActivity
import com.idslogic.levelshoes.ui.MainActivity
import com.google.android.exoplayer2.MediaItem
import com.google.android.exoplayer2.Player
import com.google.android.exoplayer2.SimpleExoPlayer
import com.google.android.exoplayer2.source.ExtractorMediaSource
import com.google.android.exoplayer2.upstream.DefaultHttpDataSourceFactory
import com.google.android.exoplayer2.upstream.cache.CacheDataSourceFactory
import com.google.android.exoplayer2.video.VideoRendererEventListener
import com.idslogic.levelshoes.App
import com.idslogic.levelshoes.di.GlideApp
import com.idslogic.levelshoes.utils.*
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.activity_onboarding.*
import kotlinx.coroutines.launch
import java.util.*

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
                setLanguage()
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
                    initVideo()
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
            setLanguage()
        })
    }

    private fun setLanguage() {
        group_selected_country.visibility = View.VISIBLE
        country_language.text = String.format(
            "%s | %s",
            viewModel.getSelectedCountry().name, Locale.getDefault().displayLanguage
        )
    }

    private fun initVideo() {
        val introVideo = viewModel.onboardingLiveData.value?.data?.intro?.find {
            it.type.equals(TYPE_VIDEO, true)
        } ?: return
        if (introVideo.status.equals(STATUS_ACTIVE, true)) {
            val url = introVideo.url
            val mediaItem = MediaItem.fromUri(Uri.parse(url))
            player = SimpleExoPlayer.Builder(this).build()
            video_view.player = player
            player!!.addListener(object : Player.EventListener {
                override fun onPlaybackStateChanged(state: Int) {
                    super.onPlaybackStateChanged(state)
                    if (state == Player.STATE_READY) {
                    }
                }
            })

            val cache = (application as App).simpleCache

            val cacheDataSourceFactory =
                CacheDataSourceFactory(cache, DefaultHttpDataSourceFactory("ExoplayerDemo"))

            val mediaSource =
                ExtractorMediaSource.Factory(cacheDataSourceFactory).createMediaSource(mediaItem)
            player!!.setMediaSource(mediaSource)
            player!!.playWhenReady = true
            player!!.repeatMode = Player.REPEAT_MODE_ONE
            if (BuildConfig.DEBUG) player!!.volume = 0f
            player!!.prepare()
        } else {
            val introImage = viewModel.onboardingLiveData.value?.data?.intro?.find {
                it.type.equals(TYPE_IMAGE, true)
            }
            video_view.visibility = View.GONE
            bg_image.visibility = View.VISIBLE
            GlideApp.with(bg_image).load(introImage?.url).into(bg_image)
        }
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