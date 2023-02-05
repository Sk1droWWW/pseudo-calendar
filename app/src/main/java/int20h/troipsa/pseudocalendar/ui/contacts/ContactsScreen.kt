package int20h.troipsa.pseudocalendar.ui.contacts

import androidx.compose.foundation.layout.Column
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavHostController
import int20h.troipsa.pseudocalendar.ui.base.ui.PseudoScaffold
import int20h.troipsa.pseudocalendar.ui.base.ui.homeTopBarProvider
import int20h.troipsa.pseudocalendar.ui.navigation.Screen

@Composable
fun ContactsScreen(navController: NavHostController) {
    PseudoScaffold(
        topBar = homeTopBarProvider(
            title = stringResource(Screen.Contacts.resourceId),
        )
    ) {
        Column {
            Text(text = stringResource(Screen.Contacts.resourceId))
        }
    }
}