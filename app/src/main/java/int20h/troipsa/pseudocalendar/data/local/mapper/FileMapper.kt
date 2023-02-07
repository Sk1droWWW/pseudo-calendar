package int20h.troipsa.pseudocalendar.data.local.mapper

import int20h.troipsa.pseudocalendar.data.local.entity.FileEntity
import int20h.troipsa.pseudocalendar.domain.models.File

object FileMapper {
    fun mapToDomain(entity: FileEntity): File {
        return File(
            id = entity.id,
            name = entity.name,
        )
    }

    fun mapToEntity(domain: File): FileEntity {
        return FileEntity(
            id = domain.id,
            name = domain.name,
        )
    }
}