package int20h.troipsa.pseudocalendar.domain.interactors

import int20h.troipsa.pseudocalendar.data.local.entity.UserEntity
import int20h.troipsa.pseudocalendar.data.repository.DataRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.filterNotNull
import javax.inject.Inject

class GetUserFlowInteractor @Inject constructor(
    private val dataRepository: DataRepository,
) {
    operator fun invoke(): Flow<UserEntity?> {
        return dataRepository.getCurrentUserFlow().filterNotNull()
    }
}
