package int20h.troipsa.pseudocalendar.data.local

import android.content.Context
import androidx.room.*
import androidx.room.migration.Migration
import int20h.troipsa.pseudocalendar.data.local.dao.UserDao
import int20h.troipsa.pseudocalendar.data.local.entity.UserEntity
import int20h.troipsa.pseudocalendar.data.local.utils.LocalDateTimeConverter

@Database(
    entities = [UserEntity::class],
    version = 1,
    exportSchema = true
)
@TypeConverters(
    LocalDateTimeConverter::class,
)
abstract class MainDatabase : RoomDatabase() {

    abstract fun userDao(): UserDao

    companion object {
        private const val DATABASE_NAME = "pseudo_calendar_db"

        fun createInstance(context: Context): MainDatabase {
            val builder =  Room.databaseBuilder(
                context.applicationContext,
                MainDatabase::class.java, DATABASE_NAME
            )

            return builder
                .addMigrations(*migrations)
                .build()
        }
    }

}

private val migrations: Array<Migration>
    get() = arrayOf(
        // for migrations
    )
