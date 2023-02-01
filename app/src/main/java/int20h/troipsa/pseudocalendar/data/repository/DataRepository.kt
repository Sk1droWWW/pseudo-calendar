package int20h.troipsa.pseudocalendar.data.repository

import int20h.troipsa.pseudocalendar.data.local.MainDatabase
import int20h.troipsa.pseudocalendar.data.local.entity.UserEntity
import int20h.troipsa.pseudocalendar.data.local.mapper.UserEntityMapper
import int20h.troipsa.pseudocalendar.data.network.ApiService
import int20h.troipsa.pseudocalendar.data.network.models.UserApiModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class DataRepository @Inject constructor(
    private val apiService: ApiService,
    private val mainDatabase: MainDatabase,
) {

    fun getCurrentUserFlow(): Flow<UserEntity?> {
        // TODO: remove stub data
//        return mainDatabase.userDao().getCurrentUser()
        return flow {
            emit(
                UserEntity(
                    id = 0,
                    name = "Stub User",
                    phoneNumber = "32423 32423 234"
                )
            )
        }
    }

    suspend fun syncUser() {
        val userApiModel = fetchUserFromApi()
        val userEntity = UserEntityMapper.mapToEntity(userApiModel)
        saveUserToDb(userEntity)
    }

    private suspend fun fetchUserFromApi(): UserApiModel {
        return apiService.getUser()
    }

    private suspend fun saveUserToDb(user: UserEntity) {
        mainDatabase.userDao().insertOrReplace(user)
    }
}