package com.konyekokim.weather

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import com.konyekokim.core.data.DataState
import com.konyekokim.core.data.Result
import com.konyekokim.core.data.entities.CurrentWeather
import com.konyekokim.core.data.entities.FavoriteLocation
import com.konyekokim.core.data.entities.ForecastWeather
import com.konyekokim.core.network.responses.*
import com.konyekokim.core.repository.WeatherRepository
import com.konyekokim.testcommons.rules.CoroutineTestRule
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.impl.annotations.MockK
import io.mockk.impl.annotations.RelaxedMockK
import io.mockk.verifyOrder
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@ExperimentalCoroutinesApi
class WeatherViewModelTest {

    @get:Rule
    var coroutineTestRule = CoroutineTestRule()

    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    @RelaxedMockK
    lateinit var repository: WeatherRepository

    @RelaxedMockK
    lateinit var viewModel: WeatherViewModel

    @MockK(relaxed = true)
    lateinit var currentStateObserver: Observer<CurrentWeatherViewState>

    @MockK(relaxed = true)
    lateinit var forecastStateObserver: Observer<ForecastViewState>

    @MockK(relaxed = true)
    lateinit var favoriteStateObserver: Observer<FavoriteLocationViewState>

    @MockK(relaxed = true)
    lateinit var currentDataObserver: Observer<CurrentWeather>

    @MockK(relaxed = true)
    lateinit var forecastDataObserver: Observer<ForecastWeather>

    @MockK(relaxed = true)
    lateinit var favoriteDataObserver: Observer<List<FavoriteLocation>>

    var cityName = "Lagos"
    var cityLat = 3.045
    var cityLon = 3.044

    var currentWeather = CurrentWeather(
        id = 1, weather = listOf(Weather( id = 1, main = "String", description = "String", icon = "String" )),
        main = Main(temp = 0.00, pressure = 0.00, humidity = 0.00, tempMax = 0.00, tempMin = 0.00),
        visibility = 1, wind = Wind(speed = 0.00, deg = 0.00),clouds = Clouds(all = 1),
        dt = 1L, name = "Name", cod = 2, sys = Sys(country = "Country", sunrise = 1, sunset = 1),
        coord = Coordinates(lat = 2.00, lon = 2.00)
    )

    var forecastWeather = ForecastWeather(
        name = "Forecast", cod = "2", country = "Country", cnt = 2,
        list = listOf(
            WeatherData(
                id = 1, dt = 1L,
                main = Main(temp = 0.00, pressure = 0.00, humidity = 0.00, tempMax = 0.00, tempMin = 0.00),
                weather = listOf(Weather( id = 1, main = "String", description = "String", icon = "String" )),
                clouds = Clouds(all = 1), wind = Wind(speed = 0.00, deg = 0.00),
                rain = Rain(mm3h = 0.00), dtTxt = "String"
            )
        ),
        city = City(id = 1, name = "String", country = "Country", coord = Coordinates(lat = 2.00, lon = 2.00))
    )

    var favoriteLocations = listOf(FavoriteLocation(
        name = "String", lat = 2.00, lng = 2.00
    ))

    @Before
    fun setUp(){
        MockKAnnotations.init(this)
        viewModel = WeatherViewModel(repository)
    }

    @Test
    fun `state order is proper when current weather by city data state is success`() =
        coroutineTestRule.dispatcher.runBlockingTest {
            coEvery {
                repository.getCurrentWeatherByCity(cityName)
            } returns  Result.Success(currentWeather)

            viewModel.currentByCityState.observeForever(currentStateObserver)

            viewModel.getCurrentWeatherByCity(cityName)

            verifyOrder {
                currentStateObserver.onChanged(CurrentWeatherViewState(DataState.Loading))
                currentStateObserver.onChanged(CurrentWeatherViewState(DataState.Success))
            }
        }

    @Test
    fun `state order is proper when current weather by coordinates data state is success`() =
        coroutineTestRule.dispatcher.runBlockingTest {
            coEvery {
                repository.getCurrentWeatherByCoordinates(cityLat, cityLon)
            } returns  Result.Success(currentWeather)

            viewModel.currentByCoordinatesState.observeForever(currentStateObserver)

            viewModel.getCurrentWeatherByCoordinates(cityLat, cityLon)

            verifyOrder {
                currentStateObserver.onChanged(CurrentWeatherViewState(DataState.Loading))
                currentStateObserver.onChanged(CurrentWeatherViewState(DataState.Success))
            }
        }

    @Test
    fun `state order is proper when forecast by coordinates data state is success`() =
        coroutineTestRule.dispatcher.runBlockingTest {
            coEvery {
                repository.getForecastByCoordinates(cityLat, cityLon)
            } returns  Result.Success(forecastWeather)

            viewModel.forecastByCoordinatesState.observeForever(forecastStateObserver)

            viewModel.getForecastByCoordinates(cityLat, cityLon)

            verifyOrder {
                forecastStateObserver.onChanged(ForecastViewState(DataState.Loading))
                forecastStateObserver.onChanged(ForecastViewState(DataState.Success))
            }
        }

    @Test
    fun `state order is proper when forecast by city data state is success`() =
        coroutineTestRule.dispatcher.runBlockingTest {
            coEvery {
                repository.getForecastByCity(cityName)
            } returns  Result.Success(forecastWeather)

            viewModel.forecastByCityState.observeForever(forecastStateObserver)

            viewModel.getForecastByCity(cityName)

            verifyOrder {
                forecastStateObserver.onChanged(ForecastViewState(DataState.Loading))
                forecastStateObserver.onChanged(ForecastViewState(DataState.Success))
            }
        }

    @Test
    fun `state order is proper when favorite location data state is success`() =
        coroutineTestRule.dispatcher.runBlockingTest {
            coEvery {
                repository.getFavoriteLocations()
            } returns  favoriteLocations

            viewModel.favoriteLocationState.observeForever(favoriteStateObserver)

            viewModel.getFavoriteLocations()

            verifyOrder {
                favoriteStateObserver.onChanged(FavoriteLocationViewState(DataState.Loading))
                favoriteStateObserver.onChanged(FavoriteLocationViewState(DataState.Success))
            }
        }

    @Test
    fun `state order is proper when forecast from db data state is success`() =
        coroutineTestRule.dispatcher.runBlockingTest {
            coEvery {
                repository.getLastSavedForecastWeather()
            } returns  forecastWeather

            viewModel.forecastByCityState.observeForever(forecastStateObserver)

            viewModel.getLastSavedForecastWeather()

            verifyOrder {
                forecastStateObserver.onChanged(ForecastViewState(DataState.Loading))
                forecastStateObserver.onChanged(ForecastViewState(DataState.Success))
            }
        }

    @Test
    fun `state order is proper when current weather from db data state is success`() =
        coroutineTestRule.dispatcher.runBlockingTest {
            coEvery {
                repository.getLastSavedCurrentWeather()
            } returns  currentWeather

            viewModel.currentByCityState.observeForever(currentStateObserver)

            viewModel.getLastSavedCurrentWeather()

            verifyOrder {
                currentStateObserver.onChanged(CurrentWeatherViewState(DataState.Loading))
                currentStateObserver.onChanged(CurrentWeatherViewState(DataState.Success))
            }
        }

    @Test
    fun `get current weather by city properly`() = coroutineTestRule.dispatcher.runBlockingTest {
        coEvery {
            repository.getCurrentWeatherByCity(cityName)
        } returns Result.Success(currentWeather)

        viewModel.currentByCityData.observeForever(currentDataObserver)

        viewModel.getCurrentWeatherByCity(cityName)

        verifyOrder {
            currentDataObserver.onChanged(currentWeather)
        }
    }

    @Test
    fun `get current weather by coordinates properly`() = coroutineTestRule.dispatcher.runBlockingTest {
        coEvery {
            repository.getCurrentWeatherByCoordinates(cityLat, cityLon)
        } returns Result.Success(currentWeather)

        viewModel.currentByCoordinatesData.observeForever(currentDataObserver)

        viewModel.getCurrentWeatherByCoordinates(cityLat, cityLon)

        verifyOrder {
            currentDataObserver.onChanged(currentWeather)
        }
    }

    @Test
    fun `get forecast by city properly`() = coroutineTestRule.dispatcher.runBlockingTest {
        coEvery {
            repository.getForecastByCity(cityName)
        } returns Result.Success(forecastWeather)

        viewModel.forecastByCityData.observeForever(forecastDataObserver)

        viewModel.getForecastByCity(cityName)

        verifyOrder {
            forecastDataObserver.onChanged(forecastWeather)
        }
    }

    @Test
    fun `get forecast by coordinates properly`() = coroutineTestRule.dispatcher.runBlockingTest {
        coEvery {
            repository.getForecastByCoordinates(cityLat, cityLon)
        } returns Result.Success(forecastWeather)

        viewModel.forecastByCoordinatesData.observeForever(forecastDataObserver)

        viewModel.getForecastByCoordinates(cityLat, cityLon)

        verifyOrder {
            forecastDataObserver.onChanged(forecastWeather)
        }
    }

    @Test
    fun `get favorite locations properly`() = coroutineTestRule.dispatcher.runBlockingTest {
        coEvery {
            repository.getFavoriteLocations()
        } returns favoriteLocations

        viewModel.favoriteLocationData.observeForever(favoriteDataObserver)

        viewModel.getFavoriteLocations()

        verifyOrder {
            favoriteDataObserver.onChanged(favoriteLocations)
        }
    }

    @Test
    fun `get last saved current weather properly`() = coroutineTestRule.dispatcher.runBlockingTest {
        coEvery {
            repository.getLastSavedCurrentWeather()
        } returns currentWeather

        viewModel.currentByCityData.observeForever(currentDataObserver)

        viewModel.getLastSavedCurrentWeather()

        verifyOrder {
            currentDataObserver.onChanged(currentWeather)
        }
    }

    @Test
    fun `get last saved forecast weather properly`() = coroutineTestRule.dispatcher.runBlockingTest {
        coEvery {
            repository.getLastSavedForecastWeather()
        } returns forecastWeather

        viewModel.forecastByCityData.observeForever(forecastDataObserver)

        viewModel.getLastSavedForecastWeather()

        verifyOrder {
            forecastDataObserver.onChanged(forecastWeather)
        }
    }

    @Test
    fun `state is error when there is no current weather by city`() = coroutineTestRule.dispatcher.runBlockingTest {
        coEvery {
            repository.getCurrentWeatherByCity(cityName)
        } returns Result.Error(WeatherViewModel.ERROR_MESSAGE)

        viewModel.currentByCityState.observeForever(currentStateObserver)

        viewModel.getCurrentWeatherByCity(cityName)

        verifyOrder {
            currentStateObserver.onChanged(CurrentWeatherViewState(DataState.Loading))
            currentStateObserver.onChanged(
                CurrentWeatherViewState(DataState.Error(WeatherViewModel.ERROR_MESSAGE))
            )
        }
    }

    @Test
    fun `state is error when there is no current weather by coordinates`() = coroutineTestRule.dispatcher.runBlockingTest {
        coEvery {
            repository.getCurrentWeatherByCoordinates(cityLat, cityLon)
        } returns Result.Error(WeatherViewModel.ERROR_MESSAGE)

        viewModel.currentByCoordinatesState.observeForever(currentStateObserver)

        viewModel.getCurrentWeatherByCoordinates(cityLat, cityLon)

        verifyOrder {
            currentStateObserver.onChanged(CurrentWeatherViewState(DataState.Loading))
            currentStateObserver.onChanged(
                CurrentWeatherViewState(DataState.Error(WeatherViewModel.ERROR_MESSAGE))
            )
        }
    }

    @Test
    fun `state is error when there is no forecast by city`() = coroutineTestRule.dispatcher.runBlockingTest {
        coEvery {
            repository.getForecastByCity(cityName)
        } returns Result.Error(WeatherViewModel.ERROR_MESSAGE)

        viewModel.forecastByCityState.observeForever(forecastStateObserver)

        viewModel.getForecastByCity(cityName)

        verifyOrder {
            forecastStateObserver.onChanged(ForecastViewState(DataState.Loading))
            forecastStateObserver.onChanged(
                ForecastViewState(DataState.Error(WeatherViewModel.ERROR_MESSAGE))
            )
        }
    }

    @Test
    fun `state is error when there is no forecast by coordinates`() = coroutineTestRule.dispatcher.runBlockingTest {
        coEvery {
            repository.getForecastByCoordinates(cityLat, cityLon)
        } returns Result.Error(WeatherViewModel.ERROR_MESSAGE)

        viewModel.forecastByCoordinatesState.observeForever(forecastStateObserver)

        viewModel.getForecastByCoordinates(cityLat, cityLon)

        verifyOrder {
            forecastStateObserver.onChanged(ForecastViewState(DataState.Loading))
            forecastStateObserver.onChanged(
                ForecastViewState(DataState.Error(WeatherViewModel.ERROR_MESSAGE))
            )
        }
    }

    @Test
    fun `state is error when there is no favorite locations`() = coroutineTestRule.dispatcher.runBlockingTest {
        coEvery {
            repository.getFavoriteLocations()
        } returns null

        viewModel.favoriteLocationState.observeForever(favoriteStateObserver)

        viewModel.getFavoriteLocations()

        verifyOrder {
            favoriteStateObserver.onChanged(FavoriteLocationViewState(DataState.Loading))
            favoriteStateObserver.onChanged(
                FavoriteLocationViewState(DataState.Error(WeatherViewModel.FAV_LOC_ERROR_MESSAGE))
            )
        }
    }

    @Test
    fun `state is error when there is no last saved current weather`() = coroutineTestRule.dispatcher.runBlockingTest {
        coEvery {
            repository.getLastSavedCurrentWeather()
        } returns null

        viewModel.currentByCityState.observeForever(currentStateObserver)

        viewModel.getLastSavedCurrentWeather()

        verifyOrder {
            currentStateObserver.onChanged(CurrentWeatherViewState(DataState.Loading))
            currentStateObserver.onChanged(
                CurrentWeatherViewState(DataState.Error(WeatherViewModel.ERROR_MESSAGE_FROM_DB))
            )
        }
    }

    @Test
    fun `state is error when there is no last saved forecast weather`() = coroutineTestRule.dispatcher.runBlockingTest {
        coEvery {
            repository.getLastSavedForecastWeather()
        } returns null

        viewModel.forecastByCityState.observeForever(forecastStateObserver)

        viewModel.getLastSavedForecastWeather()

        verifyOrder {
            forecastStateObserver.onChanged(ForecastViewState(DataState.Loading))
            forecastStateObserver.onChanged(
                ForecastViewState(DataState.Error(WeatherViewModel.ERROR_MESSAGE_FROM_DB))
            )
        }
    }
}