package com.ankuradlakha.baseproject.data

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.ankuradlakha.baseproject.data.daos.OnboardingDao
import com.ankuradlakha.baseproject.data.models.*

@Database(
    entities = [Source::class],
    version = 1
)
@TypeConverters(com.ankuradlakha.baseproject.data.TypeConverters::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun getOnboardingDao(): OnboardingDao
}