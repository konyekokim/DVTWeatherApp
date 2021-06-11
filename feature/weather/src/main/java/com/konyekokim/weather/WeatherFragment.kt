package com.konyekokim.weather

import android.content.Context
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.konyekokim.commons.extensions.*
import com.konyekokim.commons.ui.getDateString
import com.konyekokim.core.data.DataState
import com.konyekokim.core.data.entities.CurrentWeather
import com.konyekokim.core.data.entities.ForecastWeather
import com.konyekokim.core.di.provider.CoreComponentProvider
import com.konyekokim.weather.adapter.ForecastAdapter
import com.konyekokim.weather.databinding.FragmentWeatherBinding
import com.konyekokim.weather.di.WeatherModule
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
    }

    private fun fetchWeatherData(){

    }

    private fun onCurrentWeatherViewDataChanged(currentWeather: CurrentWeather){
        binding.todaysDate.text = getDateString(currentWeather.dt)
        binding.location.text = (currentWeather.name + " , " + currentWeather.sys.country)
        binding.temperature.text = currentWeather.main.temp.toString().appendTempSign()
        binding.weatherType.text = currentWeather.weather[0].main
        binding.minTemp.text = currentWeather.main.tempMin.toString().appendTempSign()
        binding.maxTemp.text = currentWeather.main.tempMax.toString().appendTempSign()
        binding.currentTemp.text = currentWeather.main.temp.toString().appendTempSign()
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
        forecastAdapter.submitList(forecastWeather.list)
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
            layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
            adapter = forecastAdapter
        }
    }

    private fun setUpDependencyInjection(){
       /* DaggerWeatherComponent
            .builder()
            .coreComponent((appContext as CoreComponentProvider).provideCoreComponent())
            .weatherModule(WeatherModule(this))
            .build()
            .inject(this)*/
    }
}