package int20h.troipsa.pseudocalendar.data.local.entity

import androidx.room.*

@Entity(
    tableName = "event_contact",
    primaryKeys = [
        "event_id",
        "contact_id"
    ]
)
data class EventContactCrossRef(
    @ColumnInfo(name = "event_id")
    val eventId: Int,
    @ColumnInfo(name = "contact_id")
    val contactId: Int
)

data class EventWithContacts(
    @Embedded val event: EventEntity,
    @Relation(
        parentColumn = "event_id",
        entityColumn = "contact_id",
        associateBy = Junction(EventContactCrossRef::class)
    )
    val contacts: List<ContactEntity>
)