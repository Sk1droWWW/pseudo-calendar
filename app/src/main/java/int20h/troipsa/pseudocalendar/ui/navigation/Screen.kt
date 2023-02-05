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
    @DrawableRes val iconRes: Int? = null,
) {
    object Calendar : Screen("calendar", R.string.nav_bar_calendar_title, R.drawable.ic_calendar)
    object Contacts : Screen("contacts", R.string.nav_bar_friends_list, R.drawable.ic_baseline_list)
    object DaySchedule : Screen("day_schedule", R.string.day_schedule_title) {
        val epochDay = "epochDay"
    }

    // build navigation path (for screen navigation)
    fun withArgs(vararg args: String): String {
        return buildString {
            append(route)
            args.forEach{ arg ->
                append("/$arg")
            }
        }
    }

    // build and setup route format (in navigation graph)
    fun withArgsFormat(vararg args: String) : String {
        return buildString {
            append(route)
            args.forEach{ arg ->
                append("/{$arg}")
            }
        }
    }
}

