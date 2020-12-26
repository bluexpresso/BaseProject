package com.ankuradlakha.baseproject.di

import android.app.Application
import android.content.Context
import androidx.room.Room
import com.ankuradlakha.baseproject.BuildConfig
import com.ankuradlakha.baseproject.data.AppCache
import com.ankuradlakha.baseproject.data.AppDatabase
import com.ankuradlakha.baseproject.data.repositories.ConfigurationRepository
import com.ankuradlakha.baseproject.data.repositories.OnboardingRepository
import com.ankuradlakha.baseproject.network.API
import com.google.gson.FieldNamingPolicy
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent
import dagger.hilt.android.qualifiers.ApplicationContext
import okhttp3.Cache
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.net.HttpURLConnection
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@Module
@InstallIn(ApplicationComponent::class)
class AppModules {
    companion object {
        private const val CACHE_SIZE = 10L * 1024 * 1024   //10MB
        private const val TIMEOUT = 30L    // Seconds
        private const val DATABASE_NAME = "app_database"
    }


    @Provides
    @Singleton
    fun provideInterceptor(context: Context, appCache: AppCache): Interceptor {
        return Interceptor {
            val builder = it.request().newBuilder()
            builder.addHeader("Content-Type", "application/json")
            val token = appCache.getAuthToken()
            if (!token.isNullOrBlank()) {
                builder.addHeader("Authorization", String.format("Token %s", token))
            }
            val response = it.proceed(builder.build())
            if (response.code() == HttpURLConnection.HTTP_UNAUTHORIZED) {
//                logOutUser(context,appCache)
            }
            response
        }
    }


    @Singleton
    @Provides
    fun provideAPI(gson: Gson, okHttpClient: OkHttpClient) = Retrofit.Builder()
        .client(okHttpClient)
        .baseUrl(BuildConfig.REST_ENDPOINT)
        .addConverterFactory(GsonConverterFactory.create())
        .build()
        .create(API::class.java)

    @Provides
    @Singleton
    fun provideGson(): Gson {
        val gsonBuilder = GsonBuilder()
        gsonBuilder.setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES)
        gsonBuilder.excludeFieldsWithoutExposeAnnotation()
        return gsonBuilder.create()
    }

    @Provides
    @Singleton
    fun provideOkHttpClient(application: Application, interceptor: Interceptor): OkHttpClient {
        val builder = OkHttpClient.Builder()
        builder.cache(Cache(application.cacheDir, CACHE_SIZE))
            .writeTimeout(TIMEOUT, TimeUnit.SECONDS)
            .readTimeout(TIMEOUT, TimeUnit.SECONDS).connectTimeout(TIMEOUT, TimeUnit.SECONDS)

        builder.interceptors().add(interceptor)

//        if (BuildConfig.DEBUG) {
        val httpLoggingInterceptor = HttpLoggingInterceptor()
        httpLoggingInterceptor.level = HttpLoggingInterceptor.Level.BODY
        builder.networkInterceptors().add(httpLoggingInterceptor)
//        }

        return builder.build()
    }

    @Provides
    @Singleton
    internal fun provideContext(@ApplicationContext context: Context): Context = context

    @Provides
    @Singleton
    internal fun provideSharedPreferences(context: Context): AppCache =
        AppCache(context)

    @Provides
    @Singleton
    internal fun provideAppDatabase(@ApplicationContext context: Context) =
        Room.databaseBuilder(context, AppDatabase::class.java, DATABASE_NAME).build()

    @Provides
    @Singleton
    internal fun providesConfigurationRepository(api: API, appCache: AppCache) =
        ConfigurationRepository(api, appCache)

    @Provides
    @Singleton
    internal fun providesOnboardingRepository(
        api: API,
        appCache: AppCache,
        appDatabase: AppDatabase
    ) =
        OnboardingRepository(api, appCache, appDatabase)

}