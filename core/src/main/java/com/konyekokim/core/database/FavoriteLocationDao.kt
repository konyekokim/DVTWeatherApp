package com.konyekokim.core.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.konyekokim.core.data.entities.FavoriteLocation

@Dao
interface FavoriteLocationDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun saveLocation(favoriteLocation: FavoriteLocation)

    @Query("SELECT * FROM favorite_locations")
    suspend fun getFavoriteLocations(): List<FavoriteLocation>

}