package presentation

import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toInstant
import kotlinx.datetime.toLocalDateTime
import kotlin.time.ExperimentalTime

@OptIn(ExperimentalTime::class)
val LocalDateTime.local: LocalDateTime
    get() = toInstant(TimeZone.UTC).toLocalDateTime(TimeZone.currentSystemDefault())

@Suppress("MagicNumber")
val LocalDateTime.timeAsString: String
    get() = when (local.minute) {
        in 0..9 -> "${local.hour}:0${local.minute}"
        else -> "${local.hour}:${local.minute}"
    }
