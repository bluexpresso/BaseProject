package com.idslogic.levelshoes.di

import android.app.Application
import android.content.Context
import androidx.room.Room
import com.google.gson.FieldNamingPolicy
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.idslogic.levelshoes.BuildConfig
import com.idslogic.levelshoes.R
import com.idslogic.levelshoes.data.AppCache
import com.idslogic.levelshoes.data.AppDatabase
import com.idslogic.levelshoes.data.repositories.CategoryRepository
import com.idslogic.levelshoes.data.repositories.ConfigurationRepository
import com.idslogic.levelshoes.data.repositories.OnboardingRepository
import com.idslogic.levelshoes.data.repositories.ProductsRepository
import com.idslogic.levelshoes.network.API
import com.idslogic.levelshoes.utils.isInternetAvailable
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.android.scopes.ViewModelScoped
import dagger.hilt.components.SingletonComponent
import okhttp3.Cache
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.io.IOException
import java.net.HttpURLConnection
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModules {
    private const val CACHE_SIZE = 10L * 1024 * 1024   //10MB
    private const val TIMEOUT = 30L    // Seconds
    private const val DATABASE_NAME = "app_database"

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
            if (!isInternetAvailable(context)) {
                throw NoInternetException(context)
            } else {
                val response = it.proceed(builder.build())
                if (response.code() == HttpURLConnection.HTTP_UNAUTHORIZED) {
//                logOutUser(context,appCache)
                }
                response
            }
        }
    }


    
    @Provides
    @Singleton
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

    class NoInternetException(private val context: Context) : IOException() {
        override fun getLocalizedMessage(): String {
            return context.getString(R.string.no_internet_message)
        }
    }

}