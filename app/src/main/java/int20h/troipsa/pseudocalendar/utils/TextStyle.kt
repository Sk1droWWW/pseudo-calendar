package int20h.troipsa.pseudocalendar.utils

import androidx.compose.runtime.Stable
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight

@Stable
fun TextStyle.bold() = takeIf { fontWeight == FontWeight.Bold }
    ?: copy(fontWeight = FontWeight.Bold)

@Stable
fun TextStyle.medium() = takeIf { fontWeight == FontWeight.Medium }
    ?: copy(fontWeight = FontWeight.Medium)

@Stable
fun TextStyle.regular() = takeIf { fontWeight == FontWeight.Normal }
    ?: copy(fontWeight = FontWeight.Normal)