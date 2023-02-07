package int20h.troipsa.pseudocalendar.data.local

import android.content.Context
import androidx.room.*
import androidx.room.migration.Migration
import int20h.troipsa.pseudocalendar.data.local.dao.ContactsDao
import int20h.troipsa.pseudocalendar.data.local.dao.EventsDao
import int20h.troipsa.pseudocalendar.data.local.dao.EventsTypeDao
import int20h.troipsa.pseudocalendar.data.local.dao.UserDao
import int20h.troipsa.pseudocalendar.data.local.entity.ContactEntity
import int20h.troipsa.pseudocalendar.data.local.entity.EventEntity
import int20h.troipsa.pseudocalendar.data.local.entity.EventTypeEntity
import int20h.troipsa.pseudocalendar.data.local.entity.UserEntity
import int20h.troipsa.pseudocalendar.data.local.utils.LocalDateTimeConverter

@Database(
    entities = [
        UserEntity::class,
        EventEntity::class,
        EventTypeEntity::class,
        ContactEntity::class,
    ],
    version = 1,
    exportSchema = true
)
@TypeConverters(
    LocalDateTimeConverter::class,
)
abstract class MainDatabase : RoomDatabase() {

    abstract fun userDao(): UserDao

    abstract fun eventsDao(): EventsDao
    abstract fun eventsTypeDao(): EventsTypeDao

    abstract fun contactsDao(): ContactsDao

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
