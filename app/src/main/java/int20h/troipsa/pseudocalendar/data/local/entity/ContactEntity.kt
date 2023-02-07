package int20h.troipsa.pseudocalendar.data.local.entity

import androidx.room.*

@Entity(tableName = "contacts")
data class ContactEntity(
    @PrimaryKey
    @ColumnInfo(name = "contact_id")
    val id: Int,
    @ColumnInfo(name = "name")
    val name: String
)
