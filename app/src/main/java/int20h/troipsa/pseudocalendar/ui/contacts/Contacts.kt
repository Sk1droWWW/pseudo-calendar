package int20h.troipsa.pseudocalendar.ui.contacts

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavHostController
import int20h.troipsa.pseudocalendar.ui.basic.PseudoScaffold
import int20h.troipsa.pseudocalendar.ui.basic.homeTopBarProvider
import int20h.troipsa.pseudocalendar.ui.navigation.Screen

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Contacts(navController: NavHostController) {
    PseudoScaffold(
        topBar = homeTopBarProvider(
            title = stringResource(Screen.Calendar.resourceId),
        )
    ) {
        Column {
            Text(text = stringResource(Screen.Contacts.resourceId))
        }
    }
}