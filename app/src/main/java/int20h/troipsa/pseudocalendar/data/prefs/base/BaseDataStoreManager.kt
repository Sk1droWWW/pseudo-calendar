package int20h.troipsa.pseudocalendar.data.prefs.base

import android.content.Context
import androidx.annotation.CallSuper
import androidx.datastore.core.DataMigration
import androidx.datastore.preferences.SharedPreferencesMigration
import androidx.datastore.preferences.core.PreferenceDataStoreFactory
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.preferencesDataStoreFile

abstract class BaseDataStoreManager(private val context: Context) {
    protected abstract val fileName: String

    protected val dataStore by lazy {
        PreferenceDataStoreFactory.create(
            migrations = getMigrations(),
            produceFile = { context.preferencesDataStoreFile(fileName) }
        )
    }

    @CallSuper
    open fun getMigrations(): List<DataMigration<Preferences>> {
        return listOf(
            SharedPreferencesMigration(context, fileName),
        )
    }

    suspend fun clearAll() {
        dataStore.edit {
            it.clear()
        }
    }

    suspend fun withTransaction(block: suspend TransactionScope.() -> Unit) {
        dataStore.editValues(block)
    }
}