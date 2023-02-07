package int20h.troipsa.pseudocalendar.domain.interactors

import int20h.troipsa.pseudocalendar.data.local.entity.EventContactCrossRef
import int20h.troipsa.pseudocalendar.data.repository.DataRepository
import javax.inject.Inject

class AddEventContactsInteractor @Inject constructor(
    val repository: DataRepository
) {
    suspend operator fun invoke(eventId: Int, contact: Int) {
        repository.addEventContact(EventContactCrossRef(eventId, contact))
    }

}
