package presentation.ui

import androidx.compose.foundation.HorizontalScrollbar
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.rememberScrollbarAdapter
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import presentation.model.WeatherState

@Suppress("FunctionNaming")
@Composable
fun WeatherForecast(
    state: WeatherState.Ok,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier.padding(horizontal = 16.dp)
    ) {
        val today = state.weatherInfo.weatherDataPerDay.getValue(0)
        val listState: LazyListState = rememberLazyListState()
        LazyRow(
            state = listState,
            modifier = Modifier.fillMaxWidth()
        ) {
            items(today, key = { it.time }) {
                HourlyWeatherDisplay(it, modifier = Modifier.height(100.dp).padding(horizontal = 16.dp))
            }
        }
        HorizontalScrollbar(
            modifier = Modifier.fillMaxWidth().padding(vertical = 12.dp, horizontal = 8.dp),
            adapter = rememberScrollbarAdapter(listState)
        )
    }
}
