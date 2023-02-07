package int20h.troipsa.pseudocalendar.data.local.dao

import androidx.room.Dao
import androidx.room.Query
import int20h.troipsa.pseudocalendar.data.local.base.BaseDao
import int20h.troipsa.pseudocalendar.data.local.entity.*

@Dao
abstract class FilesDao : BaseDao<FileEntity>() {
    @Query("DELETE FROM files WHERE file_id=:fileId")
    abstract suspend fun deleteFile(fileId: Int)

    @Query("DELETE FROM files WHERE name=:name")
    abstract suspend fun deleteFileByName(name: String)

    @Query("SELECT * FROM files WHERE name=:name")
    abstract suspend fun getFileByName(name: String) : FileEntity

}