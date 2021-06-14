package com.konyekokim.weather.utils

import com.konyekokim.core.data.entities.ForecastWeather
import com.konyekokim.core.network.responses.WeatherData
import com.konyekokim.core.network.responses.WeatherDataGroup
import java.util.*

fun prepareForecastData(response: ForecastWeather?,
                        setUpAdapter: (forecastList: List<List<WeatherData>>?) -> Unit){
    if (response != null) {
        val data0: MutableList<WeatherData> = ArrayList<WeatherData>()
        val data1: MutableList<WeatherData> = ArrayList<WeatherData>()
        val data2: MutableList<WeatherData> = ArrayList<WeatherData>()
        val data3: MutableList<WeatherData> = ArrayList<WeatherData>()
        val data4: MutableList<WeatherData> = ArrayList<WeatherData>()
        val data5: MutableList<WeatherData> = ArrayList<WeatherData>()
        val calendar0 = Calendar.getInstance()
        calendar0[Calendar.HOUR_OF_DAY] = 0
        calendar0[Calendar.MINUTE] = 0
        calendar0[Calendar.SECOND] = 0
        calendar0[Calendar.MILLISECOND] = 0
        val calendar1 = calendar0.clone() as Calendar
        calendar1.add(Calendar.DAY_OF_YEAR, 1)
        val calendar2 = calendar0.clone() as Calendar
        calendar2.add(Calendar.DAY_OF_YEAR, 2)
        val calendar3 = calendar0.clone() as Calendar
        calendar3.add(Calendar.DAY_OF_YEAR, 3)
        val calendar4 = calendar0.clone() as Calendar
        calendar4.add(Calendar.DAY_OF_YEAR, 4)
        val calendar5 = calendar0.clone() as Calendar
        calendar5.add(Calendar.DAY_OF_YEAR, 5)
        for (data in response.list!!) {
            when {
                getCalendarFromDate(data.dt)!!.before(calendar1) -> {
                    data0.add(data)
                }
                getCalendarFromDate(data.dt)!!.before(calendar2) -> {
                    data1.add(data)
                }
                getCalendarFromDate(data.dt)!!.before(calendar3) -> {
                    data2.add(data)
                }
                getCalendarFromDate(data.dt)!!.before(calendar4) -> {
                    data3.add(data)
                }
                getCalendarFromDate(data.dt)!!.before(calendar5) -> {
                    data4.add(data)
                }
                else -> {
                    data5.add(data)
                }
            }
        }
        val dataGroup = WeatherDataGroup(data0)
        if (data1.size > 0) dataGroup.addWeatherData(data1)
        if (data2.size > 0) dataGroup.addWeatherData(data2)
        if (data3.size > 0) dataGroup.addWeatherData(data3)
        if (data4.size > 0) dataGroup.addWeatherData(data4)
        if (data5.size > 0) dataGroup.addWeatherData(data5)
        setUpAdapter(dataGroup.getDataGroup())
    }
}

private fun getCalendarFromDate(date: Long): Calendar? {
    val cal = Calendar.getInstance()
    cal.timeInMillis = date * 1000L
    return cal
}