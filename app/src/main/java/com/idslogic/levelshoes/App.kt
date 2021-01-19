package com.idslogic.levelshoes

import android.app.Application
import com.google.android.exoplayer2.database.DatabaseProvider
import com.google.android.exoplayer2.database.ExoDatabaseProvider
import com.google.android.exoplayer2.upstream.cache.LeastRecentlyUsedCacheEvictor
import com.google.android.exoplayer2.upstream.cache.SimpleCache
import com.google.firebase.ktx.Firebase
import com.google.firebase.remoteconfig.FirebaseRemoteConfig
import com.google.firebase.remoteconfig.FirebaseRemoteConfigSettings
import com.idslogic.levelshoes.data.AppCache
import dagger.hilt.android.HiltAndroidApp
import timber.log.Timber
import java.io.File
import javax.inject.Inject

@HiltAndroidApp
class App : Application() {
    @Inject
    lateinit var appCache: AppCache
    lateinit var simpleCache: SimpleCache
    lateinit var firebaseRemoteConfig: FirebaseRemoteConfig
    override fun onCreate() {
        super.onCreate()
        if (BuildConfig.DEBUG) {
            Timber.plant(Timber.DebugTree())
        }
        initSimpleCache()
        initFirebaseRemoteConfig()
    }

    private fun initFirebaseRemoteConfig() {
        firebaseRemoteConfig = FirebaseRemoteConfig.getInstance()
        firebaseRemoteConfig.setConfigSettingsAsync(
            FirebaseRemoteConfigSettings.Builder()
                .setMinimumFetchIntervalInSeconds(5000).build()
        )
    }

    private fun initSimpleCache() {
        val evictor = LeastRecentlyUsedCacheEvictor((100 * 1024 * 1024).toLong())
        val databaseProvider: DatabaseProvider = ExoDatabaseProvider(this)
        simpleCache = SimpleCache(File(cacheDir, "media"), evictor, databaseProvider)
    }
}