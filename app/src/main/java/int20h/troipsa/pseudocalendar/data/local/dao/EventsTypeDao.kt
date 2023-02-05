package int20h.troipsa.pseudocalendar.data.local.dao

import androidx.room.Dao
import androidx.room.Query
import int20h.troipsa.pseudocalendar.data.local.base.BaseDao
import int20h.troipsa.pseudocalendar.data.local.entity.EventTypeEntity
import int20h.troipsa.pseudocalendar.data.local.entity.EventWithTypeProjection
import kotlinx.coroutines.flow.Flow


@Dao
abstract class EventsTypeDao : BaseDao<EventTypeEntity>() {

    @Query("SELECT * FROM event_types WHERE name=:name")
    abstract fun getEventTypeByName(name: String) : EventTypeEntity?
}