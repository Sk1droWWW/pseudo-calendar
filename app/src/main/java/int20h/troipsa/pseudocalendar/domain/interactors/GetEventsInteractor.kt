package int20h.troipsa.pseudocalendar.domain.interactors

import int20h.troipsa.pseudocalendar.data.local.mapper.EventMapper
import int20h.troipsa.pseudocalendar.data.repository.DataRepository
import int20h.troipsa.pseudocalendar.domain.models.Event
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import java.time.LocalDate
import javax.inject.Inject

class GetEventsInteractor @Inject constructor(
    private val dataRepository: DataRepository
) {

    operator fun invoke(): Flow<Map<LocalDate, List<Event>>> {
        return dataRepository.getEvents().map { list ->
            list.map { projection ->
                EventMapper.mapToDomain(
                    eventEntity = projection.event,
                    eventType = projection.eventType
                )
            }.groupBy { item -> item.startTime.toLocalDate() }
        }
    }

}
