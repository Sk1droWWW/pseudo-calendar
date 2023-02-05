package int20h.troipsa.pseudocalendar.ui.main


import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import dagger.hilt.android.AndroidEntryPoint
import int20h.troipsa.pseudocalendar.ui.navigation.PseudoCalendarNavHost
import int20h.troipsa.pseudocalendar.ui.theme.PseudocalendarTheme


@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    private val viewModel by viewModels<MainViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewModel.generateEvents()

        setContent {
            PseudocalendarTheme {
                PseudoCalendarNavHost()
            }
        }
    }
}


