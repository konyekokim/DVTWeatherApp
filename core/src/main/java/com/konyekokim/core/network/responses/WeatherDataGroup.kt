package com.konyekokim.core.network.responses

import java.util.*

class WeatherDataGroup {
    private var dataGroup: MutableList<List<WeatherData>>?

    @SafeVarargs
    constructor(vararg args: List<WeatherData>) {
        dataGroup = ArrayList<List<WeatherData>>()
        dataGroup?.addAll(Arrays.asList<List<WeatherData>>(*args))
    }

    constructor(group: List<List<WeatherData>>?) {
        dataGroup = ArrayList<List<WeatherData>>(group)
    }

    fun addWeatherData(data: List<WeatherData>) {
        if (dataGroup == null) dataGroup = ArrayList<List<WeatherData>>()
        dataGroup?.add(data)
    }

    fun getDataGroup(): List<List<WeatherData>>? {
        return dataGroup
    }

    fun setDataGroup(dataGroup: MutableList<List<WeatherData>>) {
        this.dataGroup = dataGroup
    }

    override fun toString(): String {
        return "DataGroup{" +
                "dataGroup=" + dataGroup +
                '}'
    }
}