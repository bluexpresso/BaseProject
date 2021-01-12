package com.idslogic.levelshoes

import android.app.Application
import com.google.android.exoplayer2.database.DatabaseProvider
import com.google.android.exoplayer2.database.ExoDatabaseProvider
import com.google.android.exoplayer2.upstream.cache.LeastRecentlyUsedCacheEvictor
import com.google.android.exoplayer2.upstream.cache.SimpleCache
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
    override fun onCreate() {
        super.onCreate()
        if (BuildConfig.DEBUG) {
            Timber.plant(Timber.DebugTree())
        }
        initSimpleCache()
    }

    private fun initSimpleCache() {
        val evictor = LeastRecentlyUsedCacheEvictor((100 * 1024 * 1024).toLong())
        val databaseProvider: DatabaseProvider = ExoDatabaseProvider(this)
        simpleCache = SimpleCache(File(cacheDir, "media"), evictor, databaseProvider)
    }
}