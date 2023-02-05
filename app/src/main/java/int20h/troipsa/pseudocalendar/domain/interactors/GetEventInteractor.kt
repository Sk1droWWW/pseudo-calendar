package int20h.troipsa.pseudocalendar.domain.interactors

import int20h.troipsa.pseudocalendar.data.local.mapper.EventMapper
import int20h.troipsa.pseudocalendar.data.repository.DataRepository
import int20h.troipsa.pseudocalendar.domain.models.Event
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class GetEventInteractor @Inject constructor(
    private val dataRepository: DataRepository
) {
    operator fun invoke(eventId: Int): Flow<Event> {
        return dataRepository.getEvent(eventId).map { projection ->
            EventMapper.mapToDomain(
                eventEntity = projection.event,
                eventType = projection.eventType
            )
        }
    }
}