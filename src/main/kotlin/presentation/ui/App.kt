package presentation.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import presentation.model.WeatherState
import presentation.ui.theme.DarkBlue
import presentation.ui.theme.DeepBlue
import presentation.ui.theme.WeatherAppTheme

@Suppress("FunctionNaming")
@Composable
fun WeatherWindow(
    state: WeatherState,
    modifier: Modifier = Modifier
): Unit = WeatherAppTheme {
    Column(
        modifier = modifier.fillMaxSize().background(DarkBlue)
    ) {
        App(state)
    }
}

@Suppress("FunctionNaming")
@Composable
fun App(
    state: WeatherState,
    modifier: Modifier = Modifier
): Unit = when (state) {
    is WeatherState.Loading -> {
        Message("Loading...") {
            CircularProgressIndicator(
                modifier = Modifier.width(64.dp),
                color = Color.White
            )
        }
    }
    is WeatherState.Error -> {
        Message(state.error) {
            Text(
                text = "😭",
                color = Color.White,
                fontSize = 50.sp
            )
        }
    }
    is WeatherState.Ok -> Column {
        WeatherCard(state, DeepBlue, modifier = modifier)
        WeatherForecast(state)
    }
}

@Suppress("FunctionNaming")
@Composable
fun Message(
    text: String,
    modifier: Modifier = Modifier,
    inside: @Composable () -> Unit
): Unit = Card(
    backgroundColor = DeepBlue,
    shape = RoundedCornerShape(10.dp),
    modifier = modifier.padding(16.dp)
) {
    Row(
        modifier = Modifier.fillMaxWidth().padding(16.dp),
        horizontalArrangement = Arrangement.SpaceAround
    ) {
        Text(
            text = text,
            color = Color.White,
            fontSize = 50.sp
        )
        inside()
    }
}
