package int20h.troipsa.pseudocalendar.ui.navigation

import androidx.annotation.StringRes
import int20h.troipsa.pseudocalendar.R

sealed class Screen(
    val route: String,
    @StringRes val resourceId: Int
) {
    object Calendar : Screen("calendar", R.string.nav_bar_calendar_title)
    object Contacts : Screen("contacts", R.string.nav_bar_friends_list)

}

