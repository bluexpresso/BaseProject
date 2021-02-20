//package com.idslogic.levelshoes.di
//
//import android.app.Application
//import android.content.Context
//import androidx.room.Room
//import com.google.gson.FieldNamingPolicy
//import com.google.gson.Gson
//import com.google.gson.GsonBuilder
//import com.idslogic.levelshoes.BuildConfig
//import com.idslogic.levelshoes.R
//import com.idslogic.levelshoes.data.AppCache
//import com.idslogic.levelshoes.data.AppDatabase
//import com.idslogic.levelshoes.data.repositories.CategoryRepository
//import com.idslogic.levelshoes.data.repositories.ConfigurationRepository
//import com.idslogic.levelshoes.data.repositories.OnboardingRepository
//import com.idslogic.levelshoes.data.repositories.ProductsRepository
//import com.idslogic.levelshoes.network.API
//import com.idslogic.levelshoes.utils.isInternetAvailable
//import dagger.Module
//import dagger.Provides
//import dagger.hilt.DefineComponent
//import dagger.hilt.InstallIn
//import dagger.hilt.android.components.ViewModelComponent
//import dagger.hilt.android.qualifiers.ApplicationContext
//import dagger.hilt.components.SingletonComponent
//import okhttp3.Cache
//import okhttp3.Interceptor
//import okhttp3.OkHttpClient
//import okhttp3.logging.HttpLoggingInterceptor
//import retrofit2.Retrofit
//import retrofit2.converter.gson.GsonConverterFactory
//import java.io.IOException
//import java.net.HttpURLConnection
//import java.util.concurrent.TimeUnit
//import javax.inject.Singleton
//
//@Module
//@InstallIn(ViewModelComponent::class)
//@DefineComponent(parent = SingletonComponent::class)
//class ViewModelModule {
//
//    @Provides
//    @Singleton
//    internal fun provideSharedPreferences(context: Context): AppCache =
//        AppCache(context)
//
//    @Provides
//    @Singleton
//    internal fun providesConfigurationRepository(
//        api: API,
//        appCache: AppCache,
//        appDatabase: AppDatabase
//    ) =
//        ConfigurationRepository(api, appCache, appDatabase)
//
//    @Provides
//    @Singleton
//    internal fun providesOnboardingRepository(
//        api: API,
//        appCache: AppCache,
//        appDatabase: AppDatabase
//    ) =
//        OnboardingRepository(api, appCache, appDatabase)
//
//    @Provides
//    @Singleton
//    internal fun providesProductsRepository(
//        api: API,
//        appCache: AppCache,
//        appDatabase: AppDatabase
//    ) = ProductsRepository(api, appCache, appDatabase)
//
//    @Provides
//    @Singleton
//    internal fun providesCategoryRepository(
//        api: API,
//        appCache: AppCache,
//        appDatabase: AppDatabase
//    ) = CategoryRepository(api, appCache)
//
//    class NoInternetException(private val context: Context) : IOException() {
//        override fun getLocalizedMessage(): String {
//            return context.getString(R.string.no_internet_message)
//        }
//    }
//
//}