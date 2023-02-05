package int20h.troipsa.pseudocalendar.domain.models

import java.time.LocalDateTime
import java.time.YearMonth


data class Event(
    val id: Int = 0,
    val name: String = "Event Sample",
    val description: String = "Description Sample",
    val startTime: LocalDateTime,
    val endTime: LocalDateTime,
    val epochDay: Long = startTime.toLocalDate().toEpochDay(),
    val eventType: EventType,
) {
    companion object {
        private val sampleEventTypes = List(3) {
            EventType(
                id = it,
                name = "Event Type $it",
                color = when (it) {
                    0 -> 0xFF3700B3
                    1 -> 0xFF03DAC5
                    2 -> 0xFFFFC107
                    else -> 0xFF000000
                }
            )
        }

        fun generateEvents(): List<Event> = buildList {
            val currentMonth = YearMonth.now()

            currentMonth.atDay(17).also { date ->
                add(
                    Event(
                        startTime = date.atTime(14, 0),
                        endTime = date.atTime(15, 0),
                        eventType = sampleEventTypes.random()
                    ),
                )
                add(
                    Event(
                        startTime = date.atTime(21, 30),
                        endTime = date.atTime(22, 0),
                        eventType = sampleEventTypes.random()
                    ),
                )
                add(
                    Event(
                        startTime = date.atTime(22, 30),
                        endTime = date.atTime(23, 0),
                        eventType = sampleEventTypes.random()
                    ),
                )
                add(
                    Event(
                        startTime = date.atTime(23, 0),
                        endTime = date.atTime(23, 15),
                        eventType = sampleEventTypes.random()
                    ),
                )
                add(
                    Event(
                        startTime = date.atTime(23, 30),
                        endTime = date.atTime(23, 45),
                        eventType = sampleEventTypes.random()
                    ),
                )
            }

            currentMonth.atDay(22).also { date ->
                add(
                    Event(
                        startTime = date.atTime(13, 30),
                        endTime = date.atTime(13, 45),
                        eventType = sampleEventTypes.random()
                    ),
                )
                add(
                    Event(
                        startTime = date.atTime(17, 30),
                        endTime = date.atTime(18, 5),
                        eventType = sampleEventTypes.random()
                    ),
                )
            }

            currentMonth.atDay(3).also { date ->
                add(
                    Event(
                        startTime = date.atTime(20, 30),
                        endTime = date.atTime(22, 45),
                        eventType = sampleEventTypes.random()
                    ),
                )
            }

            currentMonth.atDay(12).also { date ->
                add(
                    Event(
                        startTime = date.atTime(14, 0),
                        endTime = date.atTime(15, 0),
                        eventType = sampleEventTypes.random()
                    ),
                )
                add(
                    Event(
                        startTime = date.atTime(21, 30),
                        endTime = date.atTime(22, 0),
                        eventType = sampleEventTypes.random()
                    ),
                )
                add(
                    Event(
                        startTime = date.atTime(22, 30),
                        endTime = date.atTime(23, 0),
                        eventType = sampleEventTypes.random()
                    ),
                )
                add(
                    Event(
                        startTime = date.atTime(23, 0),
                        endTime = date.atTime(23, 15),
                        eventType = sampleEventTypes.random()
                    ),
                )
            }

            currentMonth.plusMonths(1).atDay(13).also { date ->

                add(
                    Event(
                        startTime = date.atTime(2, 30),
                        endTime = date.atTime(6, 0),
                        eventType = sampleEventTypes.random()
                    ),
                )
                add(
                    Event(
                        startTime = date.atTime(23, 0),
                        endTime = date.atTime(23, 15),
                        eventType = sampleEventTypes.random()
                    ),
                )
            }

            currentMonth.minusMonths(1).atDay(9).also { date ->
                add(
                    Event(
                        startTime = date.atTime(23, 0),
                        endTime = date.atTime(23, 15),
                        eventType = sampleEventTypes.random()
                    ),
                )
            }
        }
    }
}

