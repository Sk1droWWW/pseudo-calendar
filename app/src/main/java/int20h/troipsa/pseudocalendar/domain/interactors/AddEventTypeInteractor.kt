package int20h.troipsa.pseudocalendar.domain.interactors

import int20h.troipsa.pseudocalendar.data.local.mapper.EventMapper
import int20h.troipsa.pseudocalendar.data.repository.DataRepository
import int20h.troipsa.pseudocalendar.domain.models.EventType
import javax.inject.Inject

class AddEventTypeInteractor @Inject constructor(
    val dataRepository: DataRepository
){
    suspend operator fun invoke(eventType: EventType) {
        dataRepository.addEventType(EventMapper.mapToEntity(eventType))
    }

    suspend operator fun invoke(eventTypeList: List<EventType>) {
        eventTypeList.forEach { eventType ->
            dataRepository.addEventType(
                EventMapper.mapToEntity(eventType)
            )
        }
    }
}