package int20h.troipsa.pseudocalendar.utils

import java.time.*
import java.util.concurrent.TimeUnit

object DateConvertUtils {

    fun convertToLocalDate(time: Long): LocalDate {
        return Instant.ofEpochMilli(time).atZone(ZoneId.of("UTC")).toLocalDate()
    }

    fun convertToTimestamp(date: LocalDate): Long {
        return date.atStartOfDay(ZoneOffset.UTC).toInstant().toEpochMilli()
    }

    fun convertToLocalDateTime(time: Long): LocalDateTime {
        return Instant.ofEpochMilli(time).atZone(ZoneId.systemDefault()).toLocalDateTime()
    }

    fun convertToTimestamp(dateTime: LocalDateTime): Long {
        val zoneOffset = ZoneId.systemDefault().rules.getOffset(dateTime)
        return dateTime.toInstant(zoneOffset).toEpochMilli()
    }

    fun convertMillisToSeconds(millis: Long): Long {
        return TimeUnit.MILLISECONDS.toSeconds(millis)
    }
}