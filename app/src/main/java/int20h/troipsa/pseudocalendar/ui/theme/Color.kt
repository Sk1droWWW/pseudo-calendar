package int20h.troipsa.pseudocalendar.ui.theme

import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import int20h.troipsa.pseudocalendar.R

val Purple200 = Color(0xFFBB86FC)
val Purple500 = Color(0xFF6200EE)
val Purple700 = Color(0xFF3700B3)
val Teal200 = Color(0xFF03DAC5)

val pageBackgroundColor: Color @Composable get() = colorResource(R.color.example_5_page_bg_color)
val itemBackgroundColor: Color @Composable get() = colorResource(R.color.example_5_item_view_bg_color)
val toolbarColor: Color @Composable get() = colorResource(R.color.example_5_toolbar_color)
val selectedItemColor: Color @Composable get() = colorResource(R.color.example_5_text_grey)
val inActiveTextColor: Color @Composable get() = colorResource(R.color.example_5_text_grey_light)
