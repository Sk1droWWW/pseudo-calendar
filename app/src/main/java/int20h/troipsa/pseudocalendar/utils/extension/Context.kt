package int20h.troipsa.pseudocalendar.utils.extension

import android.app.Activity
import android.content.Context
import android.content.ContextWrapper
import android.content.pm.PackageManager
import androidx.annotation.ColorRes
import androidx.core.app.ActivityCompat
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

fun Context.hasContactPermission(): Boolean {
    return ContextCompat.checkSelfPermission(this, android.Manifest.permission.READ_CONTACTS) ==
            PackageManager.PERMISSION_GRANTED;
}

fun Context.requestContactPermission(activity: Activity = this.findActivity()) {
    if (!this.hasContactPermission()) {
        ActivityCompat.requestPermissions(activity, arrayOf(android.Manifest.permission.READ_CONTACTS), 1)
    }
}