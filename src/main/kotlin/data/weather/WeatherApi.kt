package data.weather

import arrow.core.raise.Raise
import arrow.core.raise.catch
import arrow.core.raise.ensure
import domain.weather.WeatherRepository
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.plugins.HttpRequestRetry
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.defaultRequest
import io.ktor.client.request.get
import io.ktor.client.statement.bodyAsText
import io.ktor.http.isSuccess
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json

private const val OPEN_METEO_ARGS =
    "temperature_2m,relativehumidity_2m,weathercode,pressure_msl,windspeed_10m"

class WeatherApi : AutoCloseable {
    private val openMeteo = HttpClient {
        defaultRequest {
            url("https://api.open-meteo.com/v1/")
        }
        install(ContentNegotiation) {
            json(
                Json {
                    ignoreUnknownKeys = true
                }
            )
        }
        install(HttpRequestRetry) {
            retryOnServerErrors(maxRetries = 5)
            exponentialDelay()
        }
    }

    override fun close() {
        openMeteo.close()
    }

    context(Raise<WeatherRepository.Error>)
    suspend fun getWeatherData(
        latitude: Double,
        longitude: Double
    ): WeatherDto {
        val response = catch({
            openMeteo.get("forecast") {
                url {
                    parameters.append("hourly", OPEN_METEO_ARGS)
                    parameters.append("latitude", "$latitude")
                    parameters.append("longitude", "$longitude")
                }
            }
        }) { raise(WeatherRepository.Error.NetworkError(null, it.message.orEmpty())) }
        ensure(response.status.isSuccess()) {
            raise(WeatherRepository.Error.NetworkError(response.status.value, response.bodyAsText()))
        }
        return catch<IllegalArgumentException, _>({
            response.body<WeatherDto>()
        }) { raise(WeatherRepository.Error.SerializationError(it)) }
    }
}
