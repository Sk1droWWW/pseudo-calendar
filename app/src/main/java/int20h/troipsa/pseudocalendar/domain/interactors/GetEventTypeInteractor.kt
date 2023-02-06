package int20h.troipsa.pseudocalendar.domain.interactors

import int20h.troipsa.pseudocalendar.data.local.mapper.EventMapper
import int20h.troipsa.pseudocalendar.data.repository.DataRepository
import int20h.troipsa.pseudocalendar.domain.models.EventType
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class GetEventTypeInteractor @Inject constructor(
    val repository: DataRepository
){
    operator fun invoke(id: Int): Flow<EventType> {
        return repository.getEventTypeById(id).map { eventTypeEntity ->
            EventMapper.mapToDomain(eventTypeEntity)
        }
    }
}