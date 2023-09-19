package presentation.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.width
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import domain.weather.WeatherData
import presentation.r
import presentation.timeAsString

@Suppress("FunctionNaming")
@Composable
fun HourlyWeatherDisplay(
    data: WeatherData,
    modifier: Modifier = Modifier,
    textColor: Color = Color.White
): Unit = Column(
    modifier = modifier,
    horizontalAlignment = Alignment.CenterHorizontally,
    verticalArrangement = Arrangement.SpaceBetween
) {
    Text(
        text = data.time.timeAsString,
        color = Color.LightGray
    )
    Image(
        painter = painterResource(r(data.weatherType.icon.iconName)),
        contentDescription = data.weatherType.weatherDesc,
        modifier = Modifier.width(40.dp)
    )
    Text(
        text = "${data.temperatureCelsius} C",
        color = textColor,
        fontWeight = FontWeight.Bold
    )
}
