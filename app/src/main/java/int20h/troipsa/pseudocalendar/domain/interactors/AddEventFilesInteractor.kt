package int20h.troipsa.pseudocalendar.domain.interactors

import int20h.troipsa.pseudocalendar.data.local.entity.EventFilesCrossRef
import int20h.troipsa.pseudocalendar.data.repository.DataRepository
import javax.inject.Inject

class AddEventFilesInteractor @Inject constructor(
    private val repository: DataRepository,
) {
    suspend operator fun invoke(eventId: Int, fileId: Int) {
        repository.addEventFile(EventFilesCrossRef(eventId, fileId))
    }
}