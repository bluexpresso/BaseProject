package com.idslogic.levelshoes.di

import com.idslogic.levelshoes.data.AppCache
import com.idslogic.levelshoes.data.AppDatabase
import com.idslogic.levelshoes.data.repositories.CategoryRepository
import com.idslogic.levelshoes.data.repositories.ConfigurationRepository
import com.idslogic.levelshoes.data.repositories.OnboardingRepository
import com.idslogic.levelshoes.data.repositories.ProductsRepository
import com.idslogic.levelshoes.network.API
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.scopes.ViewModelScoped

@Module
@InstallIn(ViewModelComponent::class)
object RepositoryModule {
    @Provides
    @ViewModelScoped
    internal fun providesConfigurationRepository(
        api: API,
        appCache: AppCache,
        appDatabase: AppDatabase
    ) =
        ConfigurationRepository(api, appCache, appDatabase)

    @Provides
    @ViewModelScoped
    internal fun providesOnboardingRepository(
        api: API,
        appCache: AppCache,
        appDatabase: AppDatabase
    ) =
        OnboardingRepository(api, appCache, appDatabase)

    @Provides
    @ViewModelScoped
    internal fun providesProductsRepository(
        api: API,
        appCache: AppCache,
        appDatabase: AppDatabase
    ) = ProductsRepository(api, appCache, appDatabase)

    @Provides
    @ViewModelScoped
    internal fun providesCategoryRepository(
        api: API,
        appCache: AppCache,
        appDatabase: AppDatabase
    ) = CategoryRepository(api, appCache)
}