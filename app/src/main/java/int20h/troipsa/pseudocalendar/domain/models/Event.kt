package int20h.troipsa.pseudocalendar.domain.models

import androidx.annotation.ColorRes
import int20h.troipsa.pseudocalendar.R
import java.time.LocalDateTime
import java.time.YearMonth
import java.time.format.DateTimeFormatter


data class Event(
    val startTime: LocalDateTime,
    @ColorRes val color: Int,
    val name: String = "Event Sample",
    val eventType: EventType = EventType(
        0,
        "Default",
        R.color.purple_700,
    ),
    val endTime: LocalDateTime = LocalDateTime.now()
)

fun generateEvents(): List<Event> = buildList {
    val currentMonth = YearMonth.now()

    currentMonth.atDay(17).also { date ->
        add(
            Event(
                date.atTime(14, 0),
                R.color.blue_800,
            ),
        )
        add(
            Event(
                date.atTime(21, 30),
                R.color.red_800,
            ),
        )
        add(
            Event(
                date.atTime(22, 30),
                R.color.red_800,
            ),
        )
        add(
            Event(
                date.atTime(22, 30),
                R.color.red_800,
            ),
        )
        add(
            Event(
                date.atTime(22, 30),
                R.color.red_800,
            ),
        )
    }

    currentMonth.atDay(22).also { date ->
        add(
            Event(
                date.atTime(13, 20),
                R.color.brown_700,
            ),
        )
        add(
            Event(
                date.atTime(17, 40),
                R.color.blue_grey_700,
            ),
        )
    }

    currentMonth.atDay(3).also { date ->
        add(
            Event(
                date.atTime(20, 0),
                R.color.teal_700,
            ),
        )
    }

    currentMonth.atDay(12).also { date ->
        add(
            Event(
                date.atTime(18, 15),
                R.color.cyan_700,
            ),
        )
        add(
            Event(
                date.atTime(18, 15),
                R.color.cyan_700,
            ),
        )
        add(
            Event(
                date.atTime(18, 15),
                R.color.cyan_700,
            ),
        )
        add(
            Event(
                date.atTime(18, 15),
                R.color.cyan_700,
            ),
        )
    }

    currentMonth.plusMonths(1).atDay(13).also { date ->
        add(
            Event(
                date.atTime(7, 30),
                R.color.pink_700,
            ),
        )
        add(
            Event(
                date.atTime(10, 50),
                R.color.green_700,
            ),
        )
    }

    currentMonth.minusMonths(1).atDay(9).also { date ->
        add(
            Event(
                date.atTime(20, 15),
                R.color.orange_800,
            ),
        )
    }
}

val flightDateTimeFormatter: DateTimeFormatter =
    DateTimeFormatter.ofPattern("EEE'\n'dd MMM'\n'HH:mm")
