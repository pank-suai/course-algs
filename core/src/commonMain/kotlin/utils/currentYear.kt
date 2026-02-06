package utils

import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import kotlin.time.Clock

fun currentYear(): Int  = Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault()).year