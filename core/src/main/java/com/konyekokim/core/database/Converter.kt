package com.konyekokim.core.database

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.konyekokim.core.data.entities.FavoriteLocation
import com.konyekokim.core.network.responses.*

class Converter {
    companion object {
        val gson = Gson()
        @TypeConverter
        @JvmStatic
        fun fromCityData(value: City): String{
            return gson.toJson(value)
        }

        @TypeConverter
        @JvmStatic
        fun toCityData(value: String): City{
            return gson.fromJson(value, City::class.java)
        }

        @TypeConverter
        @JvmStatic
        fun fromWindData(value: Wind): String{
            return gson.toJson(value)
        }

        @TypeConverter
        @JvmStatic
        fun toWindData(value: String): Wind{
            return gson.fromJson(value, Wind::class.java)
        }

        @TypeConverter
        @JvmStatic
        fun fromCloudsData(value: Clouds): String{
            return gson.toJson(value)
        }

        @TypeConverter
        @JvmStatic
        fun toCloudsData(value: String): Clouds{
            return gson.fromJson(value, Clouds::class.java)
        }

        @TypeConverter
        @JvmStatic
        fun fromMainData(value: Main): String{
            return gson.toJson(value)
        }

        @TypeConverter
        @JvmStatic
        fun toMainData(value: String): Main {
            return gson.fromJson(value, Main::class.java)
        }

        @TypeConverter
        @JvmStatic
        fun fromRainData(value: Rain): String{
            return gson.toJson(value)
        }

        @TypeConverter
        @JvmStatic
        fun toRainData(value: String): Rain{
            return gson.fromJson(value, Rain::class.java)
        }

        @TypeConverter
        @JvmStatic
        fun fromSysData(value: Sys): String{
            return gson.toJson(value)
        }

        @TypeConverter
        @JvmStatic
        fun toSysData(value: String): Sys{
            return gson.fromJson(value, Sys::class.java)
        }

        @TypeConverter
        @JvmStatic
        fun fromCoordinatesData(value: Coordinates):String{
            return gson.toJson(value)
        }

        @TypeConverter
        @JvmStatic
        fun toCoordinatesData(value: String): Coordinates{
            return gson.fromJson(value, Coordinates::class.java)
        }

        @TypeConverter
        @JvmStatic
        fun fromWeather(value: Weather): String{
            return gson.toJson(value)
        }

        @TypeConverter
        @JvmStatic
        fun toWeather(value: String): Weather{
            return gson.fromJson(value, Weather::class.java)
        }

        @TypeConverter
        @JvmStatic
        fun fromWeatherList(value: List<Weather>): String{
            return gson.toJson(value)
        }

        @TypeConverter
        @JvmStatic
        fun toWeatherList(value: String): List<Weather>{
            return gson.fromJson(value, Array<Weather>::class.java).asList()
        }

        @TypeConverter
        @JvmStatic
        fun fromWeatherData(value: WeatherData): String{
            return gson.toJson(value)
        }

        @TypeConverter
        @JvmStatic
        fun toWeatherData(value: String): WeatherData{
            return gson.fromJson(value, WeatherData::class.java)
        }

        @TypeConverter
        @JvmStatic
        fun fromWeatherDataList(value: List<WeatherData>): String{
            return gson.toJson(value)
        }

        @TypeConverter
        @JvmStatic
        fun toWeatherDataList(value: String): List<WeatherData>{
            return gson.fromJson(value, Array<WeatherData>::class.java).asList()
        }
    }
}