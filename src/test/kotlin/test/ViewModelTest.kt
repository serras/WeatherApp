package test

import app.cash.turbine.test
import arrow.core.toNonEmptyListOrNull
import domain.FakeLocationTracker
import domain.FakeWeatherRepository
import domain.location.Location
import domain.weather.WeatherData
import io.kotest.matchers.types.shouldBeInstanceOf
import io.kotest.property.Arb
import io.kotest.property.arbitrary.bind
import io.kotest.property.arbitrary.constant
import io.kotest.property.arbitrary.list
import io.kotest.property.checkAll
import kotlinx.coroutines.test.runTest
import presentation.model.WeatherState
import presentation.model.WeatherViewModel
import kotlin.test.Test

class ViewModelTest {
    fun fake(
        location: Arb<Location?>,
        weatherData: Arb<List<WeatherData>>,
        block: suspend (WeatherViewModel) -> Unit
    ) = runTest {
        checkAll(location, weatherData) { location, weatherData ->
            with(FakeLocationTracker(location)) {
                with(FakeWeatherRepository(weatherData.toNonEmptyListOrNull()!!)) {
                    block(WeatherViewModel())
                }
            }
        }
    }

    @Test
    fun `loading works fine`() = fake(
        Arb.bind<Location>(),
        Arb.list(Arb.bind<WeatherData>(), 24..48)
    ) { model ->
        model.state.test {
            awaitItem().shouldBeInstanceOf<WeatherState.Loading>()
            model.loadWeatherInfo()
            awaitItem().shouldBeInstanceOf<WeatherState.Ok>()
        }
    }

    @Test
    fun `errors when location is down`() = fake(
        Arb.constant(null),
        Arb.list(Arb.bind<WeatherData>(), 24..48)
    ) { model ->
        model.state.test {
            awaitItem().shouldBeInstanceOf<WeatherState.Loading>()
            model.loadWeatherInfo()
            awaitItem().shouldBeInstanceOf<WeatherState.Error>()
        }
    }
}
