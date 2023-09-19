package presentation.model

import domain.weather.WeatherInfo

sealed interface WeatherState {
    data object Loading : WeatherState
    data class Error(val error: String) : WeatherState
    data class Ok(val place: String?, val weatherInfo: WeatherInfo) : WeatherState
}
