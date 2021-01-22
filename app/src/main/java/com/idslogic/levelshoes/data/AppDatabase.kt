package com.idslogic.levelshoes.data

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.idslogic.levelshoes.data.daos.ConfigurationDao
import com.idslogic.levelshoes.data.daos.LandingDao
import com.idslogic.levelshoes.data.daos.OnboardingDao
import com.idslogic.levelshoes.data.models.*

@Database(
    entities = [Source::class, LandingData::class,Attribute::class],
    version = 1
)
@TypeConverters(com.idslogic.levelshoes.data.TypeConverters::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun getOnboardingDao(): OnboardingDao
    abstract fun getLandingDao(): LandingDao
    abstract fun getConfigurationDao(): ConfigurationDao
}