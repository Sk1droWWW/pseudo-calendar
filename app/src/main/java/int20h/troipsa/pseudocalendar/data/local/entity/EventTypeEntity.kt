package int20h.troipsa.pseudocalendar.data.local.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "event_types")
data class EventTypeEntity(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    val id: Int = 0,
    @ColumnInfo(name = "name")
    val name: String,
    @ColumnInfo(name = "color")
    val color: Long,
    @ColumnInfo(name = "visible")
    val visible: Boolean,
)
