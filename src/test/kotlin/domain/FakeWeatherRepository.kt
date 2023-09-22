package domain

import arrow.core.NonEmptyList
import arrow.core.raise.Raise
import domain.weather.WeatherData
import domain.weather.WeatherInfo
import domain.weather.WeatherRepository
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toInstant
import kotlinx.datetime.toLocalDateTime
import kotlin.time.Duration.Companion.minutes

data class FakeWeatherRepository(
    val data: NonEmptyList<WeatherData>
) : WeatherRepository {
    context(Raise<WeatherRepository.Error>) override suspend fun getWeatherData(
        lat: Double,
        long: Double,
    ): WeatherInfo {
        val current = data.first()
        var currentTime: LocalDateTime = current.time
        val updatedData = data.map {
            currentTime = (currentTime.toInstant(TimeZone.UTC) + 60.minutes).toLocalDateTime(TimeZone.UTC)
            it.copy(time = currentTime)
        }
        val weatherMap = updatedData.chunked(24).withIndex().associate { it.index to it.value }
        return WeatherInfo(weatherMap, current)
    }
}
