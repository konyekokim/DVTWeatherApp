package com.konyekokim.core.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.konyekokim.core.data.entities.CurrentWeather

@Dao
interface CurrentWeatherDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun saveCurrentWeather(currentWeather: CurrentWeather)

    @Query("SELECT * FROM current_weather WHERE ids = 1")
    suspend fun getLastSavedCurrentWeather(): CurrentWeather

}