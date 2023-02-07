package int20h.troipsa.pseudocalendar.domain.interactors

import int20h.troipsa.pseudocalendar.data.local.mapper.ContactMapper
import int20h.troipsa.pseudocalendar.data.repository.DataRepository
import int20h.troipsa.pseudocalendar.domain.models.Contact
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class GetEventContactsInteractor @Inject constructor(
    private val repository: DataRepository,
) {
    operator fun invoke(eventId: Int): Flow<List<Contact>> {
        return repository.getEventContacts(eventId).map { eventWithContacts ->
            eventWithContacts.contacts.map { contact ->
                ContactMapper.mapToDomain(contact)
            }
        }
    }
}
