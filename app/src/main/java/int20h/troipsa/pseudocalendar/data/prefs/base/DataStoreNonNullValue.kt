package int20h.troipsa.pseudocalendar.data.prefs.base

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.MutablePreferences
import androidx.datastore.preferences.core.Preferences
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

/**
 * The same as in [DataStoreValue] but support default value instead null
 *
 * sample usage:
 * ```kotlin
 * class PrefsManager {
 *      val dataStore = ...
 *
 *      val authToken = dataStore.value(AUTH_TOKEN_KEY, "default")
 *
 *      companion object {
 *          private val AUTH_TOKEN_KEY = stringPreferencesKey("auth_token")
 *      }
 * }
 */
fun <T> DataStore<Preferences>.value(
    key: Preferences.Key<T>,
    defaultValue: T,
): DataStoreNonNullValue<T, T> {
    return DataStoreNonNullValue(
        value = DataStoreValue(this, key, DataStoreMapper.noMapper()),
        defaultValue = defaultValue
    )
}

/**
 * The same as in [DataStoreValue] but support default value instead null
 *
 * sample usage:
 * ```kotlin
 * class PrefsManager {
 *      val dataStore = ...
 *
 *      val signUpDateTime = dataStore.value(SIGN_UP_DATE_KEY, LocalDateTime.MIN, DataStoreMapper.localDateTimeToLong())
 *
 *      companion object {
 *          private val SIGN_UP_DATE_KEY = longPreferencesKey("sign_up_date")
 *      }
 * }
 */
fun <Store, Read> DataStore<Preferences>.value(
    key: Preferences.Key<Store>,
    defaultValue: Read,
    mapper: DataStoreMapper<Store, Read>,
): DataStoreNonNullValue<Store, Read> {
    return DataStoreNonNullValue(
        value = DataStoreValue(this, key, mapper),
        defaultValue = defaultValue
    )
}

/**
 * Save value to storage.
 * See [androidx.datastore.preferences.core.edit] for more details
 *
 * If you need save several values, use [editValues]
 */
suspend fun <Store, Read> DataStoreNonNullValue<Store, Read>.set(value: Read) {
    this.value.set(value)
}

/**
 * Fetch actual [value] from DataStore
 * Return [defaultValue] if value is null
 */
suspend fun <Store, Read> DataStoreNonNullValue<Store, Read>.get(): Read {
    return value.get() ?: defaultValue
}

/**
 * Remove actual value from DataStore
 */
suspend fun <Store, Read> DataStoreNonNullValue<Store, Read>.remove() {
    value.remove()
}

/**
 * The same as [DataStoreValue] but return default value instead null
 */
class DataStoreNonNullValue<Store, Read>(
    internal val value: DataStoreValue<Store, Read>,
    internal val defaultValue: Read,
) {

    /**
     * Emit value on every update
     */
    fun updates(): Flow<Read> {
        return value.updates()
            .map { it ?: defaultValue }
    }

    /**
     * Blocking call [get]
     * Is not recommended to use this method, because it's blocking thread, better to use asyncronous version: get()
     * Use this method only if using coroutines is not possible. For example in Retrofit interceptor
     */
    fun getBlocking(): Read {
        return value.getBlocking() ?: defaultValue
    }

    internal fun setInternal(mutablePreferences: MutablePreferences, value: Read) {
        this.value.setInternal(mutablePreferences, value)
    }

    internal fun getInternal(preferences: Preferences): Read {
        return value.getInternal(preferences) ?: defaultValue
    }

    internal fun removeInternal(mutablePreferences: MutablePreferences) {
        value.removeInternal(mutablePreferences)
    }
}