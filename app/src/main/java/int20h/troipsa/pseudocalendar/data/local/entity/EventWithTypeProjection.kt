package int20h.troipsa.pseudocalendar.data.local.entity

import androidx.room.Embedded
import androidx.room.Relation


class EventWithTypeProjection {
    @Embedded
    lateinit var event: EventEntity

    @Relation(
        entity = EventTypeEntity::class,
        parentColumn = "event_type",
        entityColumn = "id",
    )
    lateinit var eventType: EventTypeEntity
}