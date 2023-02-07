package int20h.troipsa.pseudocalendar.domain.interactors

import int20h.troipsa.pseudocalendar.data.local.mapper.FileMapper
import int20h.troipsa.pseudocalendar.data.repository.DataRepository
import int20h.troipsa.pseudocalendar.domain.models.File
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class GetEventFilesInteractor @Inject constructor(
    private val repository: DataRepository,
) {
    operator fun invoke(eventId: Int): Flow<List<File>> {
        return repository.getEventFiles(eventId).map { eventWithFiles ->
            eventWithFiles.files.map { file ->
                FileMapper.mapToDomain(file)
            }
        }
    }
}