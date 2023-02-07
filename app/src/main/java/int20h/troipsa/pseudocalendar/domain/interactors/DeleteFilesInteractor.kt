package int20h.troipsa.pseudocalendar.domain.interactors

import int20h.troipsa.pseudocalendar.data.repository.DataRepository
import javax.inject.Inject


class DeleteEventFilesInteractor @Inject constructor(
    private val repository: DataRepository,
) {
    suspend operator fun invoke(eventId: Int) {
        repository.deleteEventFiles(eventId)
    }
}

class DeleteFilesInteractor @Inject constructor(
    private val repository: DataRepository,
) {
    suspend operator fun invoke(fileName: String) {
        repository.deleteFileByName(fileName)
    }
}