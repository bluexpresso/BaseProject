package com.ankuradlakha.baseproject.data.daos

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.ankuradlakha.baseproject.data.models.Source

@Dao
interface OnboardingDao {
    @Insert
    fun insertOnbaordingData(onboardingData: Source?)

    @Query("select * from onboarding_data")
    fun getOnboardingData(): Source

}