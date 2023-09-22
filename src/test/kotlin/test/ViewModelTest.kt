package test

import app.cash.turbine.test
import arrow.core.toNonEmptyListOrNull
import domain.FakeLocationTracker
import domain.FakeWeatherRepository
import domain.location.Location
import domain.weather.WeatherData
import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.types.shouldBeInstanceOf
import io.kotest.property.Arb
import io.kotest.property.arbitrary.bind
import io.kotest.property.arbitrary.list
import io.kotest.property.checkAll
import kotlinx.coroutines.CoroutineScope
import presentation.model.WeatherState
import presentation.model.WeatherViewModel

class ViewModelTest : StringSpec({
    suspend fun CoroutineScope.fake(
        location: Location?,
        weatherData: List<WeatherData>,
        block: suspend (WeatherViewModel) -> Unit
    ) {
        with(FakeLocationTracker(location)) {
            with(FakeWeatherRepository(weatherData.toNonEmptyListOrNull()!!)) {
                block(WeatherViewModel())
            }
        }
    }

    "loading works fine" {
        checkAll(
            Arb.bind<Location>(),
            Arb.list(Arb.bind<WeatherData>(), 24..48)
        ) { location, weatherData ->
            fake(location, weatherData) { model ->
                model.state.test {
                    awaitItem().shouldBeInstanceOf<WeatherState.Loading>()
                    model.loadWeatherInfo()
                    awaitItem().shouldBeInstanceOf<WeatherState.Ok>()
                }
            }
        }
    }

    "errors when location is down" {
        checkAll(
            Arb.list(Arb.bind<WeatherData>(), 24..48)
        ) { weatherData ->
            fake(null, weatherData) { model ->
                model.state.test {
                    awaitItem().shouldBeInstanceOf<WeatherState.Loading>()
                    model.loadWeatherInfo()
                    awaitItem().shouldBeInstanceOf<WeatherState.Error>()
                }
            }
        }
    }
})
