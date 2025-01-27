package data.weather

import arrow.core.raise.Raise
import domain.weather.WeatherInfo
import domain.weather.WeatherRepository

class WeatherRepositoryImpl(
    private val api: WeatherApi
) : WeatherRepository {
    override suspend fun Raise<WeatherRepository.Error>.getWeatherData(lat: Double, long: Double): WeatherInfo =
        with(api) { getWeatherData(lat, long).toWeatherInfo() }
}
