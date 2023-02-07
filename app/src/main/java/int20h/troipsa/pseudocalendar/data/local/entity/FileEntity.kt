package int20h.troipsa.pseudocalendar.data.local.entity

@androidx.room.Entity(tableName = "files")
data class FileEntity(
    @androidx.room.PrimaryKey(autoGenerate = true)
    @androidx.room.ColumnInfo(name = "file_id")
    val id: Int = 0,
    @androidx.room.ColumnInfo(name = "name")
    val name: String,
)
