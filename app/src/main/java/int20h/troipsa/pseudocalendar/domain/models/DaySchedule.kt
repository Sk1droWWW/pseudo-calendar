package int20h.troipsa.pseudocalendar.domain.models

data class DaySchedule(
    val epochDay: Long,
    val events: List<Event>
)
