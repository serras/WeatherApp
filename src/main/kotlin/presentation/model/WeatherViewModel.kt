package presentation.model

import arrow.core.raise.either
import arrow.core.raise.ensureNotNull
import arrow.core.raise.withError
import domain.location.LocationTracker
import domain.weather.WeatherRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

// lifecycle-aware == scope-aware == CoroutineScope as dependency
context(WeatherRepository, LocationTracker, CoroutineScope)
class WeatherViewModel {

    private val _state: MutableStateFlow<WeatherState> = MutableStateFlow(WeatherState.Loading)
    val state: StateFlow<WeatherState> = _state

    fun loadWeatherInfo() {
        launch(Dispatchers.IO) {
            _state.value = WeatherState.Loading
            val result = either<String, _> {
                val location =
                    ensureNotNull(getCurrentLocation()) { "Couldn't retrieve location" }
                val weather =
                    withError({ it.message }) { getWeatherData(location.latitude, location.longitude) }
                WeatherState.Ok(location.name, weather)
            }
            _state.value = result.fold(
                ifLeft = { WeatherState.Error(it) },
                ifRight = { it }
            )
        }
    }
}
