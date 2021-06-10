package com.konyekokim.core.mappers

import com.konyekokim.core.data.entities.CurrentWeather
import com.konyekokim.core.network.responses.*
import javax.inject.Inject

class CurrentRespToCurrentWeatherMapper @Inject constructor():
    Mapper<CurrentResp, CurrentWeather> {
    override suspend fun map(from: CurrentResp): CurrentWeather {
        return CurrentWeather(
            id = from.id,
            weather = from.weather,
            main = from.main,
            visibility = from.visibility,
            wind = from.wind,
            clouds = from.clouds,
            dt = from.dt,
            name = from.name,
            cod = from.cod,
            sys = from.sys,
            coord = from.coord
        )
    }
}