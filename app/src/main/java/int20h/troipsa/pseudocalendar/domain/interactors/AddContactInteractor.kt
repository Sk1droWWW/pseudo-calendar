package int20h.troipsa.pseudocalendar.domain.interactors

import int20h.troipsa.pseudocalendar.data.local.mapper.ContactMapper
import int20h.troipsa.pseudocalendar.data.repository.DataRepository
import int20h.troipsa.pseudocalendar.domain.models.Contact
import javax.inject.Inject

class AddContactInteractor @Inject constructor(
    private val repository: DataRepository,
) {
    suspend operator fun invoke(contact: Contact) {
        repository.addContact(ContactMapper.mapToEntity(contact))
    }

}
