package com.idslogic.levelshoes.data.daos

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.idslogic.levelshoes.data.models.Attribute
import com.idslogic.levelshoes.data.models.BaseModel

@Dao
interface ConfigurationDao {
    @Insert
    fun insertAttributes(attributes: ArrayList<Attribute>)

    @Query("select label from attribute where value like :value")
    fun getLabel(value: Long) : String
}