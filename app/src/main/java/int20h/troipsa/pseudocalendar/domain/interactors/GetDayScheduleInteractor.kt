package int20h.troipsa.pseudocalendar.domain.interactors

import int20h.troipsa.pseudocalendar.data.local.mapper.EventMapper
import int20h.troipsa.pseudocalendar.data.repository.DataRepository
import int20h.troipsa.pseudocalendar.domain.models.DaySchedule
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class GetDayScheduleInteractor @Inject constructor(
    private val dataRepository: DataRepository
) {
    operator fun invoke(epochDay: Long): Flow<DaySchedule> {
        val eventsProjection = dataRepository.getEventsByDay(epochDay)

        return eventsProjection.map { list ->
            DaySchedule(
                epochDay = epochDay,
                events = list.map { projection ->
                    EventMapper.mapToDomain(
                        eventEntity = projection.event,
                        eventType = projection.eventType
                    )
                },
            )
        }
    }
}
