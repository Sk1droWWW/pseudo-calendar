package int20h.troipsa.pseudocalendar.domain.interactors

import int20h.troipsa.pseudocalendar.data.repository.DataRepository
import javax.inject.Inject


class DeleteEventInteractor @Inject constructor(
    private val dataRepository: DataRepository
) {
    suspend operator fun invoke(eventId: Int) {
        dataRepository.deleteEvent(eventId)
    }
}