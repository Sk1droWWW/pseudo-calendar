package int20h.troipsa.pseudocalendar.data.local.entity

import androidx.room.Entity

@Entity(tableName = "users")
data class UserEntity(
    val id: Int,
    val name: String,
    val phoneNumber: String,
)