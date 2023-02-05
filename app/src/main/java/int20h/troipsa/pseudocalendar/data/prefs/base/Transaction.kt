package int20h.troipsa.pseudocalendar.data.prefs.base

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.MutablePreferences
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit

/**
 * Edit several [DataStoreValue] transactionally
 *
 * This method is the same as DataStore.edit
 * but also allows change [DataStoreValue] in transaction
 * See: [androidx.datastore.preferences.core.edit] for more details
 *
 * ```kotlin
 *  val firstStoredValue = dataStore.value(key = FIRST_KEY)
 *  val secondStoredValue = dataStore.value(key = SECOND_KEY)
 *
 *  val getFirstValueOutsideTransaction = suspend {
 *      firstStoredValue.get()
 *  }
 *
 *  dataStore.editValues {
 *      firstStoredValue.set(10)
 *      secondStoredValue.set(15)
 *
 *      // print 10, because [get] in transaction scope can see changes
 *      println(firstStoredValue.get())
 *
 *      // print null, because changes write to disk after finish transaction
 *      println(getFirstValueOutsideTransaction())
 *
 *      // update value by key. The same usage as [androidx.datastore.preferences.core.edit]
 *      mutablePreferences[ANOTHER_KEY] = "some value"
 *  }
 * ```
 */
suspend fun DataStore<Preferences>.editValues(block: suspend TransactionScope.() -> Unit) {
    edit {
        val scope = TransactionScope(it)
        scope.block()
    }
}

class TransactionScope internal constructor(private val mutablePreferences: MutablePreferences) {

    fun <Store, Read> DataStoreValue<Store, Read>.set(value: Read?) {
        setInternal(mutablePreferences, value)
    }

    fun <Store, Read> DataStoreValue<Store, Read>.get(): Read? {
        return getInternal(mutablePreferences)
    }

    fun <Store, Read> DataStoreNonNullValue<Store, Read>.set(value: Read) {
        setInternal(mutablePreferences, value)
    }

    fun <Store, Read> DataStoreNonNullValue<Store, Read>.get(): Read {
        return getInternal(mutablePreferences)
    }

    fun <Store, Read> DataStoreValue<Store, Read>.remove() {
        removeInternal(mutablePreferences)
    }

    fun <Store, Read> DataStoreNonNullValue<Store, Read>.remove() {
        removeInternal(mutablePreferences)
    }

}