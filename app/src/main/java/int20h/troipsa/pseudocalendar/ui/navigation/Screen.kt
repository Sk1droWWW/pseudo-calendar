package int20h.troipsa.pseudocalendar.ui.navigation

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.List
import androidx.compose.ui.graphics.vector.ImageVector
import int20h.troipsa.pseudocalendar.R

sealed class Screen(
    val route: String,
    @StringRes val resourceId: Int,
    @DrawableRes val iconRes: Int,
) {
    object Calendar : Screen("calendar", R.string.nav_bar_calendar_title, R.drawable.ic_calendar)
    object Contacts : Screen("contacts", R.string.nav_bar_friends_list, R.drawable.ic_baseline_list)

}

