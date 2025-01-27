import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import arrow.continuations.SuspendApp
import arrow.fx.coroutines.autoCloseable
import arrow.fx.coroutines.resourceScope
import data.location.LocationTrackerImpl
import data.weather.WeatherApi
import data.weather.WeatherRepositoryImpl
import domain.location.LocationTracker
import domain.weather.WeatherRepository
import presentation.model.WeatherViewModel
import presentation.ui.WeatherWindow
import kotlin.time.Duration.Companion.seconds

suspend fun main() = SuspendApp(timeout = 1.seconds) {
    resourceScope {
        val weather: WeatherRepository = WeatherRepositoryImpl(autoCloseable { WeatherApi() })
        val location: LocationTracker = autoCloseable { LocationTrackerImpl() }
        // the scope for the model is the entire application
        val model = WeatherViewModel(weather, location)
        // this initializes the application loop
        application {
            LaunchedEffect("load") { model.loadWeatherInfo() }
            // create the window for our application
            Window(
                title = "Compose Desktop Weather App",
                onCloseRequest = ::exitApplication
            ) { WeatherWindow(model.state.collectAsState().value) }
        }
    }
}
