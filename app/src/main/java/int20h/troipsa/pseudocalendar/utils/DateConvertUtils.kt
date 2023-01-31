package int20h.troipsa.pseudocalendar.utils

import java.time.*

object DateConvertUtils {

    fun convertToLocalDateTime(time: Long): LocalDateTime {
        return Instant.ofEpochMilli(time).atZone(ZoneId.systemDefault()).toLocalDateTime()
    }

    fun convertToTimestamp(dateTime: LocalDateTime): Long {
        val zoneOffset = ZoneId.systemDefault().rules.getOffset(dateTime)
        return dateTime.toInstant(zoneOffset).toEpochMilli()
    }
}