package int20h.troipsa.pseudocalendar.data.local.mapper

import int20h.troipsa.pseudocalendar.data.local.entity.ContactEntity
import int20h.troipsa.pseudocalendar.domain.models.Contact

object ContactMapper {
    fun mapToEntity(contact: Contact): ContactEntity {
        return ContactEntity(
            id = contact.id,
            name = contact.name,
        )
    }
    fun mapToDomain(contactEntity: ContactEntity): Contact {
        return Contact(
            id = contactEntity.id,
            name = contactEntity.name,
        )
    }
}