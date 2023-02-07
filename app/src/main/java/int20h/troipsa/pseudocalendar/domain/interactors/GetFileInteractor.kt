package int20h.troipsa.pseudocalendar.domain.interactors

import int20h.troipsa.pseudocalendar.data.local.mapper.FileMapper
import int20h.troipsa.pseudocalendar.data.repository.DataRepository
import int20h.troipsa.pseudocalendar.domain.models.File
import javax.inject.Inject

class GetFileInteractor @Inject constructor(
    private val repository: DataRepository,
) {
    suspend operator fun invoke(fileName: String) : File {
        return FileMapper.mapToDomain(
            repository.getFileByName(fileName)
        )
    }
}