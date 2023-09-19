package presentation.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import presentation.model.WeatherState
import presentation.r
import presentation.timeAsString
import presentation.vectorResource
import kotlin.math.roundToInt

@Suppress("FunctionNaming", "LongMethod")
@Composable
fun WeatherCard(
    state: WeatherState.Ok,
    backgroundColor: Color,
    modifier: Modifier = Modifier
) {
    val data = state.weatherInfo.currentWeatherData!!
    Card(
        backgroundColor = backgroundColor,
        shape = RoundedCornerShape(10.dp),
        modifier = modifier.padding(16.dp)
    ) {
        Column(
            modifier = Modifier.fillMaxWidth().padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "${state.place ?: "Today"}, ${data.time.timeAsString}",
                modifier = Modifier.align(Alignment.End),
                color = Color.White
            )
            Spacer(modifier = Modifier.height(16.dp))
            Image(
                painter = painterResource(r(data.weatherType.icon.iconName)),
                contentDescription = data.weatherType.weatherDesc,
                modifier = Modifier.width(200.dp)
            )
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = "${data.temperatureCelsius} C",
                fontSize = 50.sp,
                color = Color.White
            )
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = data.weatherType.weatherDesc,
                fontSize = 20.sp,
                color = Color.White
            )
            Spacer(modifier = Modifier.height(32.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceAround
            ) {
                // the first one is the temperature
                data.elements.drop(1).map {
                    WeatherDataDisplay(
                        value = it.value.roundToInt(),
                        unit = it.unit,
                        icon = vectorResource(r(it.icon)),
                        tint = Color.White,
                        textStyle = TextStyle(color = Color.White)
                    )
                }
            }
        }
    }
}
