package int20h.troipsa.pseudocalendar.utils.compose

import android.app.Activity
import android.view.View
import androidx.annotation.ColorInt
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalInspectionMode
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.core.graphics.ColorUtils
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleOwner
import int20h.troipsa.pseudocalendar.R
import int20h.troipsa.pseudocalendar.utils.findActivity
import int20h.troipsa.pseudocalendar.utils.getColorCompat
import java.lang.ref.WeakReference


@Composable
fun StatusBarColorUpdateEffect(color: Color) {
    if (LocalInspectionMode.current) return // findActivity() will not work in preview.
    val activity = LocalContext.current.findActivity()
    val lifecycleOwner = LocalLifecycleOwner.current
    val observer = remember {
        StatusBarColorLifecycleObserver(activity, color.toArgb())
    }
    LaunchedEffect(lifecycleOwner) {
        lifecycleOwner.lifecycle.addObserver(observer)
    }
}

class StatusBarColorLifecycleObserver(
    activity: Activity,
    @ColorInt private val color: Int,
) : DefaultLifecycleObserver {
    private val isLightColor = ColorUtils.calculateLuminance(color) > 0.5
    private val defaultStatusBarColor = activity.getColorCompat(R.color.colorPrimaryDark)
    private val activity = WeakReference(activity)

    override fun onStart(owner: LifecycleOwner) {
        activity.get()?.window?.apply {
            statusBarColor = color
            if (isLightColor) {
                decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
            }
        }
    }

    override fun onStop(owner: LifecycleOwner) {
        activity.get()?.window?.apply {
            statusBarColor = defaultStatusBarColor
            if (isLightColor) decorView.systemUiVisibility = 0
        }
    }

    override fun onDestroy(owner: LifecycleOwner) = activity.clear()
}
