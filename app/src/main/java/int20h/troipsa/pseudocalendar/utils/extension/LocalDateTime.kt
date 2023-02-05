package int20h.troipsa.pseudocalendar.utils.extension

import int20h.troipsa.pseudocalendar.utils.displayText
import java.time.LocalDateTime

fun LocalDateTime.formatToDaySchedule(): String {
    return "${this.dayOfMonth} ${this.month.displayText(short = false)} ${this.year}"
}