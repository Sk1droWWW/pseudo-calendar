package int20h.troipsa.pseudocalendar.ui.main


import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import dagger.hilt.android.AndroidEntryPoint
import int20h.troipsa.pseudocalendar.ui.navigation.PseudoCalendarNavHost
import int20h.troipsa.pseudocalendar.ui.theme.PseudocalendarTheme


@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            PseudocalendarTheme {
                PseudoCalendarNavHost()
            }
        }
    }
}


