package int20h.troipsa.pseudocalendar.domain.models

data class EventType(
    val id: Int,
    val name: String,
    val color: Long,
    val visible: Boolean,
) {
    companion object {
        val default = EventType(
            id = 0,
            name = "Event Type $0",
            color = 0xFF3700B3,
            visible = true,
        )
    }
}
