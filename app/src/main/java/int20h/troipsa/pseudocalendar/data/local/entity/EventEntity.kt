package int20h.troipsa.pseudocalendar.data.local.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import java.time.LocalDateTime


@Entity(
    tableName = "events",
    foreignKeys = [
        ForeignKey(
            entity = EventTypeEntity::class,
            parentColumns = ["id"],
            childColumns = ["event_type"],
            onDelete = ForeignKey.NO_ACTION,
        ),
    ],
)
data class EventEntity(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "event_id")
    val id: Int = 0,
    @ColumnInfo(name = "epoch_day")
    val epochDay: Long,
    @ColumnInfo(name = "start_time")
    val startTime: LocalDateTime,
    @ColumnInfo(name = "end_time")
    val endTime: LocalDateTime,
    @ColumnInfo(name = "name")
    val name: String,
    @ColumnInfo(name = "description")
    val description: String,
    @ColumnInfo(name = "event_type")
    val eventType: Int,
)
