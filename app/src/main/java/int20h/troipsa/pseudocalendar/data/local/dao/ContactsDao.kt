package int20h.troipsa.pseudocalendar.data.local.dao

import androidx.room.Dao
import androidx.room.Query
import int20h.troipsa.pseudocalendar.data.local.base.BaseDao
import int20h.troipsa.pseudocalendar.data.local.entity.ContactEntity
import kotlinx.coroutines.flow.Flow

@Dao
abstract class ContactsDao : BaseDao<ContactEntity>() {

    @Query("SELECT * FROM contacts")
    abstract fun getContacts(): Flow<List<ContactEntity>>
}