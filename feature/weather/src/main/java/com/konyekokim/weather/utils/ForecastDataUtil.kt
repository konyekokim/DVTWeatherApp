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
        calendar0[Calendar.HOUR_OF_DAY] = AMOUNT_0
        calendar0[Calendar.MINUTE] = AMOUNT_0
        calendar0[Calendar.SECOND] = AMOUNT_0
        calendar0[Calendar.MILLISECOND] = AMOUNT_0
        val calendar1 = calendar0.clone() as Calendar
        calendar1.add(Calendar.DAY_OF_YEAR, AMOUNT_1)
        val calendar2 = calendar0.clone() as Calendar
        calendar2.add(Calendar.DAY_OF_YEAR, AMOUNT_2)
        val calendar3 = calendar0.clone() as Calendar
        calendar3.add(Calendar.DAY_OF_YEAR, AMOUNT_3)
        val calendar4 = calendar0.clone() as Calendar
        calendar4.add(Calendar.DAY_OF_YEAR, AMOUNT_4)
        val calendar5 = calendar0.clone() as Calendar
        calendar5.add(Calendar.DAY_OF_YEAR, AMOUNT_5)
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
    cal.timeInMillis = date * TIME_IN_MILLIS_MULTIPLIER
    return cal
}

const val TIME_IN_MILLIS_MULTIPLIER = 1000L
const val AMOUNT_0 = 0
const val AMOUNT_1 = 1
const val AMOUNT_2 = 2
const val AMOUNT_3 = 3
const val AMOUNT_4 = 4
const val AMOUNT_5 = 5