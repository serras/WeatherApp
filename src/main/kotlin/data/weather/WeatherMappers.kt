package data.weather

import arrow.core.zip
import domain.weather.WeatherData
import domain.weather.WeatherInfo
import domain.weather.WeatherType
import kotlinx.datetime.Clock
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime

const val MINUTES_IN_HALF_HOUR: Int = 30
const val HOURS_IN_A_DAY: Int = 24

fun WeatherDto.toWeatherInfo(): WeatherInfo {
    val map = weatherData.toWeatherDataMap()
    val now = Clock.System.now().toLocalDateTime(TimeZone.UTC)
    val currentHour = if (now.minute < MINUTES_IN_HALF_HOUR) now.hour else now.hour + 1
    val current = map[0]?.find { it.time.hour == currentHour }
    return WeatherInfo(map, current)
}

fun WeatherDataDto.toWeatherDataMap(): Map<Int, List<WeatherData>> =
    (0..time.size).zip(time, temperature, humidities, pressures, windSpeeds, weatherCodes) { ix, time, t, h, p, w, c ->
        ix to WeatherData(
            LocalDateTime.parse(time),
            temperatureCelsius = t,
            humidity = h,
            pressure = p,
            windSpeed = w,
            weatherType = WeatherType.fromWMO(c)
        )
    }.groupBy({ it.first / HOURS_IN_A_DAY }) { it.second }
