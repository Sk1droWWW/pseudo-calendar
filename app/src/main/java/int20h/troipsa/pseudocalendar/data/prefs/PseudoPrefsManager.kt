package int20h.troipsa.pseudocalendar.data.prefs

import android.content.Context
import int20h.troipsa.pseudocalendar.data.prefs.base.BaseDataStoreManager

class PseudoPrefsManager constructor(
    context: Context
) : BaseDataStoreManager(context) {

    override val fileName = PREFS_FILENAME

    companion object {
        const val PREFS_FILENAME = "pseudo_prefs"
    }

}