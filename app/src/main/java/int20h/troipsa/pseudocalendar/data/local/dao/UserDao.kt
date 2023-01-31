package int20h.troipsa.pseudocalendar.data.local.dao

import androidx.room.Dao
import androidx.room.Query
import int20h.troipsa.pseudocalendar.data.local.base.BaseDao
import int20h.troipsa.pseudocalendar.data.local.entity.UserEntity
import kotlinx.coroutines.flow.Flow

@Dao
abstract class UserDao : BaseDao<UserEntity>() {

    @Query("SELECT * FROM users LIMIT 1")
    abstract fun getCurrentUser() : Flow<UserEntity?>

}
