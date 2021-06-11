package com.konyekokim.core.mappers

import com.konyekokim.core.data.entities.ForecastWeather
import com.konyekokim.core.network.responses.City
import com.konyekokim.core.network.responses.ForecastResp
import com.konyekokim.core.network.responses.WeatherData
import javax.inject.Inject

class ForecastRespToForecastWeatherMapper @Inject constructor() :
    Mapper<ForecastResp, ForecastWeather>{

    override suspend fun map(from: ForecastResp): ForecastWeather {
        return ForecastWeather(
            ids = 1,
            name = from.name,
            cod = from.cod,
            country = from.country,
            cnt = from.cnt,
            list = from.list,
            city = from.city
        )
    }
}