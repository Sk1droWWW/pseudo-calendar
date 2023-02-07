package int20h.troipsa.pseudocalendar.data.local.mapper

import int20h.troipsa.pseudocalendar.data.local.entity.EventEntity
import int20h.troipsa.pseudocalendar.data.local.entity.EventTypeEntity
import int20h.troipsa.pseudocalendar.domain.models.Event
import int20h.troipsa.pseudocalendar.domain.models.EventType

object EventMapper {

    fun mapToDomain(
        eventEntity: EventEntity,
        eventType: EventTypeEntity
    ) = Event(
        id = eventEntity.id,
        name = eventEntity.name,
        startTime = eventEntity.startTime,
        epochDay = eventEntity.epochDay,
        endTime = eventEntity.endTime,
        description = eventEntity.description,
        eventType = mapToDomain(eventType),
    )

    fun mapToDomain(eventTypeEntity: EventTypeEntity) = EventType(
        id = eventTypeEntity.id,
        name = eventTypeEntity.name,
        color = eventTypeEntity.color,
        visible = eventTypeEntity.visible,
    )

    fun mapToEntity(event: Event, eventTypeId: Int) = EventEntity(
        id = event.id,
        name = event.name,
        epochDay = event.epochDay,
        startTime = event.startTime,
        endTime = event.endTime,
        description = event.description,
        eventType = eventTypeId,
    )

    fun mapToEntity(eventType: EventType) = EventTypeEntity(
        id = eventType.id,
        name = eventType.name,
        color = eventType.color,
        visible = eventType.visible,
    )

}