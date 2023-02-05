package int20h.troipsa.pseudocalendar.domain.interactors

import int20h.troipsa.pseudocalendar.data.local.mapper.EventMapper
import int20h.troipsa.pseudocalendar.data.repository.DataRepository
import int20h.troipsa.pseudocalendar.domain.models.Event
import javax.inject.Inject

class AddEventInteractor @Inject constructor(
    private val eventsRepository: DataRepository,
) {
    suspend operator fun invoke(event: Event) {
        var eventTypeEntity = eventsRepository.getEventTypeByName(event.eventType.name)

        while (eventTypeEntity == null) {
            eventsRepository.addEventType(EventMapper.mapToEntity(event.eventType))
            eventTypeEntity = eventsRepository.getEventTypeByName(event.eventType.name)
        }

        eventsRepository.addEvent(EventMapper.mapToEntity(event, eventTypeEntity.id))
    }

    suspend operator fun invoke(events: List<Event>) {
        events.forEach { event ->
            invoke(event)
        }
    }

}