package int20h.troipsa.pseudocalendar.utils.extension

import int20h.troipsa.pseudocalendar.utils.displayText
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

fun LocalDateTime.formatToDaySchedule(): String {
    return "${this.dayOfMonth} ${this.month.displayText(short = false)} ${this.year}"
}

fun LocalDateTime.formatToEventTime(): String {
    val formatter = DateTimeFormatter.ofPattern("HH:MM")
    return this.format(formatter)
}