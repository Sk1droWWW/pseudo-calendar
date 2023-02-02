package int20h.troipsa.pseudocalendar.utils

import android.app.Activity
import android.content.Context
import android.content.ContextWrapper
import androidx.annotation.ColorRes
import androidx.core.content.ContextCompat


internal fun Context.getColorCompat(@ColorRes color: Int) =
    ContextCompat.getColor(this, color)

fun Context.findActivity(): Activity {
    var context = this
    while (context is ContextWrapper) {
        if (context is Activity) return context
        context = context.baseContext
    }
    throw IllegalStateException("no activity")
}