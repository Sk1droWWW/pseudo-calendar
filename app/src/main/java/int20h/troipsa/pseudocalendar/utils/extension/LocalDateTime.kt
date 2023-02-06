package int20h.troipsa.pseudocalendar.utils.extension

import int20h.troipsa.pseudocalendar.utils.displayText
import java.time.LocalDateTime
import java.time.LocalTime

fun LocalDateTime.formatToDaySchedule(): String {
    return "${this.dayOfMonth} ${this.month.displayText(short = false)} ${this.year}"
}

fun LocalDateTime.formatToEventTime(): String {
    return "${this.hour}:${this.minute.toString().padStart(2, '0')}"
}

fun LocalDateTime.atTime(time: LocalTime): LocalDateTime {
    return this.withHour(time.hour).withMinute(time.minute)
}