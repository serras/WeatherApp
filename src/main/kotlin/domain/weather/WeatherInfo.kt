@file:Suppress("WildcardImport", "NoWildcardImports")

package domain.weather

import arrow.core.raise.Raise
import com.serranofp.weatherapp.generated.resources.*
import kotlinx.datetime.LocalDateTime
import org.jetbrains.compose.resources.DrawableResource

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
        val icon: DrawableResource
    )
    val elements: List<Element> = listOf(
        Element(temperatureCelsius, "C", Res.drawable.ic_sunny),
        Element(pressure, "hPa", Res.drawable.ic_pressure),
        Element(humidity, "%", Res.drawable.ic_drop),
        Element(windSpeed, "km/h", Res.drawable.ic_wind),
    )
}

interface WeatherRepository : AutoCloseable {
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

    suspend fun Raise<Error>.getWeatherData(lat: Double, long: Double): WeatherInfo
}
