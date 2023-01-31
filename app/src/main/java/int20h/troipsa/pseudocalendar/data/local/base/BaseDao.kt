package int20h.troipsa.pseudocalendar.data.local.base

import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Update

abstract class BaseDao<T : Any> {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    abstract suspend fun insert(value: T): Long

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    abstract suspend fun insert(values: List<T>): List<Long>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract suspend fun insertOrReplace(value: T): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract suspend fun insertOrReplace(values: List<T>): List<Long>

    @Update
    abstract suspend fun update(value: T)

    @Update
    abstract suspend fun update(values: List<T>)

    protected fun filterNotInsertedRows(ids: List<Long>, values: List<T>): List<T> {
        return ids.mapIndexedNotNull { index, id -> if (id == INVALID_ID) values[index] else null }
    }

    companion object {
        const val INVALID_ID = -1L
    }
}