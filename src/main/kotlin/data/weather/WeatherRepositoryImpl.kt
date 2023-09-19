package data.weather

import arrow.core.raise.Raise
import domain.weather.WeatherInfo
import domain.weather.WeatherRepository

class WeatherRepositoryImpl(
    private val api: WeatherApi
) : WeatherRepository {
    context(Raise<WeatherRepository.Error>)
    override suspend fun getWeatherData(lat: Double, long: Double): WeatherInfo =
        api.getWeatherData(lat, long).toWeatherInfo()
}
