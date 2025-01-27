package presentation.model

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import arrow.core.raise.either
import arrow.core.raise.ensureNotNull
import arrow.core.raise.withError
import domain.location.LocationTracker
import domain.weather.WeatherRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class WeatherViewModel(
    private val weather: WeatherRepository,
    private val locationTracker: LocationTracker
) : ViewModel() {

    private val _state: MutableStateFlow<WeatherState> = MutableStateFlow(WeatherState.Loading)
    val state: StateFlow<WeatherState> = _state

    fun loadWeatherInfo() = viewModelScope.launch(Dispatchers.IO) {
        _state.value = WeatherState.Loading
        val result = either<String, _> {
            val location =
                ensureNotNull(locationTracker.getCurrentLocation()) { "Couldn't retrieve location" }
            val weather = withError({ it.message }) {
                with(weather) { getWeatherData(location.latitude, location.longitude) }
            }
            WeatherState.Ok(location.name, weather)
        }
        _state.value = result.fold(
            ifLeft = { WeatherState.Error(it) },
            ifRight = { it }
        )
    }
}
