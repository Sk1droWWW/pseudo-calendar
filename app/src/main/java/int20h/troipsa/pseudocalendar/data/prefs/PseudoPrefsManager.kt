package int20h.troipsa.pseudocalendar.data.prefs

import android.content.Context
import androidx.datastore.preferences.core.booleanPreferencesKey
import int20h.troipsa.pseudocalendar.data.prefs.base.BaseDataStoreManager
import int20h.troipsa.pseudocalendar.data.prefs.base.value

class PseudoPrefsManager constructor(
    context: Context
) : BaseDataStoreManager(context) {

    override val fileName = PREFS_FILENAME

    val isEventsGenerated = dataStore.value(IS_EVENTS_GENERATED, false)

    companion object {
        const val PREFS_FILENAME = "pseudo_prefs"

        private val IS_EVENTS_GENERATED = booleanPreferencesKey("is_events_generated")
    }

}