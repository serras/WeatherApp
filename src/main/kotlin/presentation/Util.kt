package presentation

import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.loadXmlImageVector
import androidx.compose.ui.res.useResource
import androidx.compose.ui.unit.Density
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toInstant
import kotlinx.datetime.toLocalDateTime
import org.xml.sax.InputSource

@Composable
fun vectorResource(
    resourcePath: String,
    density: Density = LocalDensity.current
): ImageVector =
    useResource(resourcePath) { loadXmlImageVector(InputSource(it), density) }

val LocalDateTime.local: LocalDateTime
    get() = toInstant(TimeZone.UTC).toLocalDateTime(TimeZone.currentSystemDefault())

@Suppress("MagicNumber")
val LocalDateTime.timeAsString: String
    get() = when (local.minute) {
        in 0..9 -> "${local.hour}:0${local.minute}"
        else -> "${local.hour}:${local.minute}"
    }

fun r(path: String) = "drawable/ic_$path.xml"
