package int20h.troipsa.pseudocalendar.data.local.entity

import androidx.room.*

@Entity(
    tableName = "event_files",
    primaryKeys = [
        "event_id",
        "file_id"
    ]
)
data class EventFilesCrossRef(
    @ColumnInfo(name = "event_id")
    val eventId: Int,
    @ColumnInfo(name = "file_id")
    val fileId: Int
)

data class EventWithFiles(
    @Embedded val event: EventEntity,
    @Relation(
        parentColumn = "event_id",
        entityColumn = "file_id",
        associateBy = Junction(EventFilesCrossRef::class)
    )
    val files: List<FileEntity>
)