package int20h.troipsa.pseudocalendar.data.local.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Transaction
import int20h.troipsa.pseudocalendar.data.local.base.BaseDao
import int20h.troipsa.pseudocalendar.data.local.entity.*
import kotlinx.coroutines.flow.Flow

@Dao
abstract class EventsDao : BaseDao<EventEntity>() {

    @Transaction
    @Query("SELECT * FROM events WHERE epoch_day=:day ORDER BY start_time")
    abstract fun getEventsByDay(day: Long): Flow<List<EventWithTypeProjection>>

    @Transaction
    @Query("SELECT * FROM events ORDER BY start_time")
    abstract fun getEvents() : Flow<List<EventWithTypeProjection>>

    @Transaction
    @Query("SELECT * FROM events WHERE event_id=:eventId")
    abstract fun getEvent(eventId: Int) : Flow<EventWithTypeProjection>

    @Query("DELETE FROM events WHERE event_id=:eventId")
    abstract suspend fun deleteEvent(eventId: Int)

    @Transaction
    @Query("SELECT * FROM events WHERE event_id=:eventId")
    abstract fun getEventWithContacts(eventId: Int): Flow<EventWithContacts>

}

@Dao
abstract class EventsContactsDao : BaseDao<EventContactCrossRef>()