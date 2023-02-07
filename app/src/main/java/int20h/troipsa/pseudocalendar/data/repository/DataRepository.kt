package int20h.troipsa.pseudocalendar.data.repository

import int20h.troipsa.pseudocalendar.data.local.MainDatabase
import int20h.troipsa.pseudocalendar.data.local.entity.*
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

    fun getEventsByDay(epochDay: Long): Flow<List<EventWithTypeProjection>> {
        return mainDatabase.eventsDao().getEventsByDay(epochDay)
    }

    fun getEvent(eventId: Int): Flow<EventWithTypeProjection> {
        return mainDatabase.eventsDao().getEvent(eventId)
    }

    fun getEventContacts(eventId: Int): Flow<EventWithContacts> {
        return mainDatabase.eventsDao().getEventWithContacts(eventId)
    }

    suspend fun addEventContact(eventContact: EventContactCrossRef) {
        mainDatabase.eventsContactsDao().insertOrReplace(eventContact)
    }

    fun getEvents(): Flow<List<EventWithTypeProjection>> {
        return mainDatabase.eventsDao().getEvents()
    }

    suspend fun addEventType(eventType: EventTypeEntity) {
        mainDatabase.eventsTypeDao().insertOrReplace(eventType)
    }

    fun getEventTypes(): Flow<List<EventTypeEntity>> {
        return mainDatabase.eventsTypeDao().getEventTypes()
    }

    fun getEventTypeByName(name: String): EventTypeEntity? {
        return mainDatabase.eventsTypeDao().getEventTypeByName(name)
    }

    fun getEventTypeById(id: Int): Flow<EventTypeEntity> {
        return mainDatabase.eventsTypeDao().getEventTypeById(id)
    }

    suspend fun addEvent(event: EventEntity) {
        mainDatabase.eventsDao().insertOrReplace(event)
    }

    suspend fun deleteEvent(eventId: Int) {
        mainDatabase.eventsDao().deleteEvent(eventId)
    }

    suspend fun addContact(contact: ContactEntity) {
        mainDatabase.contactsDao().insertOrReplace(contact)
    }

    fun getContacts(): Flow<List<ContactEntity>> {
        return mainDatabase.contactsDao().getContacts()
    }

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
