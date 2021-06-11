package com.konyekokim.weather

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.konyekokim.commons.extensions.appContext
import com.konyekokim.commons.extensions.appendTempSign
import com.konyekokim.commons.extensions.observe
import com.konyekokim.commons.extensions.showSnackbar
import com.konyekokim.commons.ui.getDateString
import com.konyekokim.core.data.DataState
import com.konyekokim.core.data.entities.CurrentWeather
import com.konyekokim.core.data.entities.ForecastWeather
import com.konyekokim.core.di.provider.CoreComponentProvider
import com.konyekokim.core.network.responses.WeatherData
import com.konyekokim.core.network.responses.WeatherDataGroup
import com.konyekokim.weather.adapter.ForecastAdapter
import com.konyekokim.weather.databinding.FragmentWeatherBinding
import com.konyekokim.weather.di.DaggerWeatherComponent
import com.konyekokim.weather.di.WeatherModule
import java.util.*
import javax.inject.Inject

class WeatherFragment : Fragment(R.layout.fragment_weather) {

    @Inject
    lateinit var viewModel: WeatherViewModel

    private lateinit var binding: FragmentWeatherBinding

    private lateinit var forecastAdapter: ForecastAdapter

    override fun onAttach(context: Context) {
        super.onAttach(context)
        setUpDependencyInjection()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentWeatherBinding.bind(view)
        setUpForecastRecyclerView()
        observe(viewModel.currentByCityState, ::onCurrentWeatherViewStateChanged)
        observe(viewModel.currentByCoordinatesState, ::onCurrentWeatherViewStateChanged)
        observe(viewModel.forecastByCityState, ::onForecastViewStateChanged)
        observe(viewModel.forecastByCoordinatesState, ::onForecastViewStateChanged)
        observe(viewModel.currentByCityData, ::onCurrentWeatherViewDataChanged)
        observe(viewModel.currentByCoordinatesData, ::onCurrentWeatherViewDataChanged)
        observe(viewModel.forecastByCityData, ::onForecastViewDataChanged)
        observe(viewModel.forecastByCoordinatesData, ::onForecastViewDataChanged)
        //test fetch
        viewModel.getCurrentWeatherByCity("Lagos")
        viewModel.getForecastByCity("Lagos")
    }

    private fun fetchWeatherData(){

    }

    private fun onCurrentWeatherViewDataChanged(currentWeather: CurrentWeather){
        when{
            currentWeather.weather!![0].main.contains("clouds", true) -> {
                binding.weatherScrollView.setBackgroundResource(R.color.color_cloudy)
                binding.weatherThemeImg.setImageResource(R.drawable.forest_cloudy)
            }
            currentWeather.weather!![0].main.contains("clear", true) -> {
                binding.weatherScrollView.setBackgroundResource(R.color.color_sunny)
                binding.weatherThemeImg.setImageResource(R.drawable.forest_sunny)
            }
            currentWeather.weather!![0].main.contains("rain", true) -> {
                binding.weatherScrollView.setBackgroundResource(R.color.color_rainy)
                binding.weatherThemeImg.setImageResource(R.drawable.forest_rainy)
            }
        }
        binding.todaysDate.text = getDateString(currentWeather.dt ?: 1L)
        binding.location.text = (currentWeather.name + " , " + currentWeather.sys?.country)
        binding.temperature.text = currentWeather.main?.temp.toString().appendTempSign()
        binding.weatherType.text = currentWeather.weather!![0].main
        binding.minTemp.text = currentWeather.main?.tempMin.toString().appendTempSign()
        binding.maxTemp.text = currentWeather.main?.tempMax.toString().appendTempSign()
        binding.currentTemp.text = currentWeather.main?.temp.toString().appendTempSign()
    }

    private fun onCurrentWeatherViewStateChanged(currentWeatherViewState: CurrentWeatherViewState){
        when(val dataState = currentWeatherViewState.dataState){
            is DataState.Error -> {
                showSnackbar(dataState.message)
            }
            else -> {
                //do nothing
            }
        }
    }

    private fun onForecastViewDataChanged(forecastWeather: ForecastWeather){
        prepareForecastData(forecastWeather)
        //forecastAdapter.submitList(forecastWeather.list)
    }

    private fun prepareForecastData(response: ForecastWeather){
        if (response != null) {
            Log.e("Forecast", response.toString())
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
                if (getCalendarFromDate(data.dt)!!.before(calendar1)) {
                    data0.add(data)
                } else if (getCalendarFromDate(data.dt)!!.before(calendar2)) {
                    data1.add(data)
                } else if (getCalendarFromDate(data.dt)!!.before(calendar3)) {
                    data2.add(data)
                } else if (getCalendarFromDate(data.dt)!!.before(calendar4)) {
                    data3.add(data)
                } else if (getCalendarFromDate(data.dt)!!.before(calendar5)) {
                    data4.add(data)
                } else {
                    data5.add(data)
                }
            }
            val dataGroup = WeatherDataGroup(data0)
            if (data1.size > 0) dataGroup.addWeatherData(data1)
            if (data2.size > 0) dataGroup.addWeatherData(data2)
            if (data3.size > 0) dataGroup.addWeatherData(data3)
            if (data4.size > 0) dataGroup.addWeatherData(data4)
            if (data5.size > 0) dataGroup.addWeatherData(data5)
            forecastAdapter.submitList(dataGroup.getDataGroup())
        }
    }

    private fun getCalendarFromDate(date: Long): Calendar? {
        val cal = Calendar.getInstance()
        cal.timeInMillis = date * 1000L
        return cal
    }

    private fun onForecastViewStateChanged(forecastViewState: ForecastViewState){
        when(val dataState = forecastViewState.dataState){
            is DataState.Error -> {
                showSnackbar(dataState.message)
            }
            else -> {
                //do nothing
            }
        }
    }

    private fun setUpForecastRecyclerView(){
        forecastAdapter = ForecastAdapter()
        with(binding.forecastList){
            layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
            adapter = forecastAdapter
        }
    }

    private fun setUpDependencyInjection(){
        DaggerWeatherComponent
            .builder()
            .coreComponent((appContext as CoreComponentProvider).provideCoreComponent())
            .weatherModule(WeatherModule(this))
            .build()
            .inject(this)
    }
}