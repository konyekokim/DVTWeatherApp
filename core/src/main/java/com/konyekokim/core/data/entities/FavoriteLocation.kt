package com.konyekokim.core.data.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "favorite_locations")
data class FavoriteLocation(
    @PrimaryKey
    val name: String,
    val lat: Double?,
    val lng: Double?
)