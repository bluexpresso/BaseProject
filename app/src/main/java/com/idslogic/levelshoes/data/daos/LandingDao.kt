package com.idslogic.levelshoes.data.daos

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.idslogic.levelshoes.data.models.LandingData

@Dao
interface LandingDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertLandingData(landingData: ArrayList<LandingData>)

    @Query("select * from LandingData where gender = :gender")
    fun getLandingJson(gender: String): LandingData?
}