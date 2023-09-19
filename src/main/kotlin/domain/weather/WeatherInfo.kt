package domain.weather

import arrow.core.raise.Raise
import kotlinx.datetime.LocalDateTime

data class WeatherInfo(
    val weatherDataPerDay: Map<Int, List<WeatherData>>,
    val currentWeatherData: WeatherData?
)

data class WeatherData(
    val time: LocalDateTime,
    val temperatureCelsius: Double,
    val pressure: Double,
    val windSpeed: Double,
    val humidity: Double,
    val weatherType: WeatherType
) {
    data class Element(
        val value: Double,
        val unit: String,
        val icon: String
    )
    val elements: List<Element> = listOf(
        Element(temperatureCelsius, "C", "sunny"),
        Element(pressure, "hPa", "pressure"),
        Element(humidity, "%", "drop"),
        Element(windSpeed, "km/h", "wind"),
    )
}

interface WeatherRepository {
    sealed interface Error {
        val message: String

        data class NetworkError(
            val statusCode: Int?,
            override val message: String
        ) : Error
        data class SerializationError(
            val error: IllegalArgumentException
        ) : Error {
            override val message: String = error.message ?: error.toString()
        }
    }

    context(Raise<Error>)
    suspend fun getWeatherData(lat: Double, long: Double): WeatherInfo
}
