package int20h.troipsa.pseudocalendar.domain.interactors

import int20h.troipsa.pseudocalendar.data.local.mapper.UserEntityMapper
import int20h.troipsa.pseudocalendar.data.repository.DataRepository
import int20h.troipsa.pseudocalendar.domain.models.User
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class GetUserFlowInteractor @Inject constructor(
    private val dataRepository: DataRepository,
) {
    operator fun invoke(): Flow<User?> {
        return dataRepository.getCurrentUserFlow()
            .filterNotNull()
            .map { user -> UserEntityMapper.mapToDomain(user) }
    }
}
