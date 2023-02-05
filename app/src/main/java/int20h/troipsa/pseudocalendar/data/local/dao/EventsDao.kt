package int20h.troipsa.pseudocalendar.data.local.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Transaction
import int20h.troipsa.pseudocalendar.data.local.base.BaseDao
import int20h.troipsa.pseudocalendar.data.local.entity.EventEntity
import int20h.troipsa.pseudocalendar.data.local.entity.EventWithTypeProjection
import int20h.troipsa.pseudocalendar.data.local.entity.UserEntity
import kotlinx.coroutines.flow.Flow

@Dao
abstract class EventsDao : BaseDao<EventEntity>() {

    @Transaction
    @Query("SELECT * FROM events WHERE epoch_day=:day ORDER BY start_time")
    abstract fun getEventsByDay(day: Long) : Flow<List<EventWithTypeProjection>>

    @Transaction
    @Query("SELECT * FROM events ORDER BY start_time")
    abstract fun getEvents() : Flow<List<EventWithTypeProjection>>


}