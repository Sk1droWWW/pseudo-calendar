package int20h.troipsa.pseudocalendar.data.prefs.base

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.MutablePreferences
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.runBlocking

/**
 * Function for create DataStoreValue without mapping.
 * It can be used only for primitive values: Boolean, Int, Long, Float, Double, String, Set<String>
 *
 * sample usage:
 * ```kotlin
 * class PrefsManager {
 *      val dataStore = ...
 *
 *      val authToken = dataStore.value(AUTH_TOKEN_KEY)
 *
 *      companion object {
 *          private val AUTH_TOKEN_KEY = stringPreferencesKey("auth_token")
 *      }
 * }
 *
 * suspend fun sample(prefsManager: PrefsManager) {
 *     val authToken: String? = prefsManager.authToken.get()
 *
 *     prefsManager.autToken.set("new token value")
 *     prefsManager.authToken.set(null) // remove value from datastore
 * }
 * ```
 */
fun <T> DataStore<Preferences>.value(key: Preferences.Key<T>): DataStoreValue<T, T> {
    return DataStoreValue(this, key, DataStoreMapper.noMapper())
}

/**
 * Function for create DataStoreValue with [DataStoreMapper]
 *
 * sample usage:
 * ```kotlin
 * class PrefsManager {
 *      val dataStore = ...
 *
 *      val signUpDateTime = dataStore.value(SIGN_UP_DATE_KEY, DataStoreMapper.localDateTimeToLong())
 *
 *      companion object {
 *          private val SIGN_UP_DATE_KEY = longPreferencesKey("sign_up_date")
 *      }
 * }
 * ```
 */
fun <Store, Read> DataStore<Preferences>.value(
    key: Preferences.Key<Store>,
    mapper: DataStoreMapper<Store, Read>,
): DataStoreValue<Store, Read> {
    return DataStoreValue(this, key, mapper)
}

/**
 * Save value to storage.
 * See [androidx.datastore.preferences.core.edit] for more details
 *
 * If you need update several [DataStoreValue], use [editValues]
 * or [BaseDataStoreManager.withTransaction]
 */

suspend fun <Store, Read> DataStoreValue<Store, Read>.set(value: Read?) {
    dataStore.edit {
        setInternal(it, value)
    }
}

/**
 * Fetch actual value from DataStore with the given [key]
 */
suspend fun <Store, Read> DataStoreValue<Store, Read>.get(): Read? {
    return getInternal(dataStore.data.first())
}

/**
 * Remove actual value from DataStore
 */
suspend fun <Store, Read> DataStoreValue<Store, Read>.remove() {
    dataStore.edit {
        removeInternal(it)
    }
}

/**
 * Class that provide ability to fetch [Store] value from datastore and convert to [Read] value
 *
 * @param dataStore datastore for fetch value
 * @param key datastore key. See [androidx.datastore.preferences.core.intPreferencesKey] and other
 * @param mapper value mapper. See [DataStoreMapper]
 */
class DataStoreValue<Store, Read>(
    internal val dataStore: DataStore<Preferences>,
    private val key: Preferences.Key<Store>,
    private val mapper: DataStoreMapper<Store, Read>,
) {

    /**
     * Emit value on every update
     */
    fun updates(): Flow<Read?> {
        return dataStore.data.map {
            getInternal(it)
        }.distinctUntilChanged()
    }

    /**
     * Blocking call [get]
     * Is not recommended to use this method, because it's blocking thread, better to use asyncronous version: get()
     * Use this method only if using coroutines is not possible. For example in Retrofit interceptor
     */
    fun getBlocking(): Read? {
        return runBlocking {
            get()
        }
    }

    internal fun setInternal(mutablePreferences: MutablePreferences, value: Read?) {
        val inner = value?.let { mapper.toStoredValue(it) }
        if (inner == null) {
            removeInternal(mutablePreferences)
        } else {
            mutablePreferences[key] = inner
        }
    }

    internal fun getInternal(preferences: Preferences): Read? {
        val inner = preferences[key]
        return inner?.let {
            kotlin.runCatching {
                mapper.fromStoredValue(it)
            }.onFailure {
                it.printStackTrace()
            }.getOrNull()
        }
    }

    internal fun removeInternal(mutablePreferences: MutablePreferences) {
        if (mutablePreferences.contains(key)) {
            mutablePreferences.remove(key)
        }
    }
}