package com.konyekokim.weather

import androidx.annotation.VisibleForTesting
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.konyekokim.core.data.DataState
import com.konyekokim.core.data.Result
import com.konyekokim.core.data.entities.CurrentWeather
import com.konyekokim.core.data.entities.FavoriteLocation
import com.konyekokim.core.data.entities.ForecastWeather
import com.konyekokim.core.repository.WeatherRepository
import kotlinx.coroutines.launch
import javax.inject.Inject

class WeatherViewModel @Inject constructor(
    private val weatherRepository: WeatherRepository
): ViewModel() {

    private val _currentByCityData = MutableLiveData<CurrentWeather>()
    val currentByCityData : LiveData<CurrentWeather>
        get() = _currentByCityData

    private val _currentByCoordinatesData = MutableLiveData<CurrentWeather>()
    val currentByCoordinatesData : LiveData<CurrentWeather>
        get() = _currentByCoordinatesData

    private val _currentByCityState = MutableLiveData<CurrentWeatherViewState>()
    val currentByCityState: LiveData<CurrentWeatherViewState>
        get() = _currentByCityState

    private val _currentByCoordinatesState = MutableLiveData<CurrentWeatherViewState>()
    val currentByCoordinatesState: LiveData<CurrentWeatherViewState>
        get() = _currentByCoordinatesState

    private val _forecastByCityData = MutableLiveData<ForecastWeather>()
    val forecastByCityData : LiveData<ForecastWeather>
        get() = _forecastByCityData

    private val _forecastByCoordinatesData = MutableLiveData<ForecastWeather>()
    val forecastByCoordinatesData : LiveData<ForecastWeather>
        get() = _forecastByCoordinatesData

    private val _forecastByCityState = MutableLiveData<ForecastViewState>()
    val forecastByCityState : LiveData<ForecastViewState>
        get() = _forecastByCityState

    private val _forecastByCoordinatesState = MutableLiveData<ForecastViewState>()
    val forecastByCoordinatesState : LiveData<ForecastViewState>
        get() = _forecastByCoordinatesState

    private val _favoriteLocationState = MutableLiveData<FavoriteLocationViewState>()
    val favoriteLocationState : LiveData<FavoriteLocationViewState>
        get() = _favoriteLocationState

    private val _favoriteLocationData = MutableLiveData<List<FavoriteLocation>>()
    val favoriteLocationData : LiveData<List<FavoriteLocation>>
        get() = _favoriteLocationData

    fun getCurrentWeatherByCity(city: String){
        _currentByCityState.postValue(CurrentWeatherViewState(dataState = DataState.Loading))
        viewModelScope.launch {
            when(val response = weatherRepository.getCurrentWeatherByCity(city)){
                is Result.Success -> {
                    _currentByCityState.postValue(CurrentWeatherViewState(dataState = DataState.Success))
                    _currentByCityData.postValue(response.data)
                }
                is Result.Error -> {
                    _currentByCityState.postValue(
                        CurrentWeatherViewState(dataState = DataState.Error(
                        ERROR_MESSAGE))
                    )
                }
            }
        }
    }

    fun getCurrentWeatherByCoordinates(lat: Double, lng: Double){
        _currentByCoordinatesState.postValue(CurrentWeatherViewState(dataState = DataState.Loading))
        viewModelScope.launch {
            when(val response = weatherRepository.getCurrentWeatherByCoordinates(lat = lat, lng = lng)){
                is Result.Success -> {
                    _currentByCoordinatesState.postValue(CurrentWeatherViewState(dataState = DataState.Success))
                    _currentByCoordinatesData.postValue(response.data)
                }
                is Result.Error -> {
                    _currentByCoordinatesState.postValue(
                        CurrentWeatherViewState(dataState = DataState.Error(
                            ERROR_MESSAGE))
                    )
                }
            }
        }
    }

    fun getForecastByCity(city: String){
        _forecastByCityState.postValue(ForecastViewState(dataState = DataState.Loading))
        viewModelScope.launch {
            when(val response = weatherRepository.getForecastByCity(city)){
                is Result.Success -> {
                    _forecastByCityState.postValue(ForecastViewState(dataState = DataState.Success))
                    _forecastByCityData.postValue(response.data)
                }
                is Result.Error -> {
                    _forecastByCityState.postValue(
                        ForecastViewState(dataState = DataState.Error(
                            ERROR_MESSAGE))
                    )
                }
            }
        }
    }

    fun getForecastByCoordinates(lat: Double, lng: Double){
        _forecastByCoordinatesState.postValue(ForecastViewState(dataState = DataState.Loading))
        viewModelScope.launch {
            when(val response = weatherRepository.getForecastByCoordinates(lat = lat, lng = lng)){
                is Result.Success -> {
                    _forecastByCoordinatesState.postValue(ForecastViewState(dataState = DataState.Success))
                    _forecastByCoordinatesData.postValue(response.data)
                }
                is Result.Error -> {
                    _forecastByCoordinatesState.postValue(
                        ForecastViewState(dataState = DataState.Error(
                            ERROR_MESSAGE))
                    )
                }
            }
        }
    }

    fun saveFavoriteLocation(favoriteLocation: FavoriteLocation){
        viewModelScope.launch {
            weatherRepository.saveFavoriteLocation(favoriteLocation)
        }
    }

    fun getFavoriteLocations(){
        _favoriteLocationState.postValue(FavoriteLocationViewState(dataState = DataState.Loading))
        viewModelScope.launch {
            val favoriteLocations = weatherRepository.getFavoriteLocations()
            if(!favoriteLocations.isNullOrEmpty()){
                _favoriteLocationState.postValue(FavoriteLocationViewState(dataState = DataState.Success))
                _favoriteLocationData.postValue(favoriteLocations)
            } else {
                _favoriteLocationState.postValue(
                    FavoriteLocationViewState(dataState = DataState.Error(
                        FAV_LOC_ERROR_MESSAGE))
                )
            }
        }
    }

    fun deleteFavoriteLocation(cityName: String){
        viewModelScope.launch {
            weatherRepository.deleteFavoriteLocation(cityName)
        }
    }

    fun getLastSavedCurrentWeather(){
        _currentByCityState.postValue(CurrentWeatherViewState(dataState = DataState.Loading))
        viewModelScope.launch{
            val currentWeather = weatherRepository.getLastSavedCurrentWeather()
            if(currentWeather != null){
                _currentByCityState.postValue(CurrentWeatherViewState(dataState = DataState.Success))
                _currentByCityData.postValue(currentWeather)
            } else {
                _currentByCityState.postValue(
                    CurrentWeatherViewState(dataState = DataState.Error(
                        ERROR_MESSAGE_FROM_DB))
                )
            }
        }
    }

    fun getLastSavedForecastWeather(){
        _forecastByCityState.postValue(ForecastViewState(dataState = DataState.Loading))
        viewModelScope.launch {
            val forecast = weatherRepository.getLastSavedForecastWeather()
            if(forecast != null) {
                _forecastByCityState.postValue(ForecastViewState(dataState = DataState.Success))
                _forecastByCityData.postValue(forecast)
            } else {
                _forecastByCityState.postValue(
                    ForecastViewState(dataState = DataState.Error(
                        ERROR_MESSAGE_FROM_DB))
                )
            }
        }
    }

    companion object {
        @VisibleForTesting(otherwise = VisibleForTesting.PRIVATE)
        const val ERROR_MESSAGE = "Error occurred during fetching weather information"
        const val FAV_LOC_ERROR_MESSAGE = "Error occurred fetching favorite Locations"
        const val ERROR_MESSAGE_FROM_DB = "No weather data saved"
    }

}