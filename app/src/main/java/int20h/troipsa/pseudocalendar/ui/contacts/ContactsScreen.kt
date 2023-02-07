package int20h.troipsa.pseudocalendar.ui.contacts

import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.ripple.LocalRippleTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import int20h.troipsa.pseudocalendar.domain.models.Contact
import int20h.troipsa.pseudocalendar.ui.base.ui.PseudoScaffold
import int20h.troipsa.pseudocalendar.ui.base.ui.homeTopBarProvider
import int20h.troipsa.pseudocalendar.ui.navigation.Screen
import int20h.troipsa.pseudocalendar.ui.theme.itemBackgroundColor
import int20h.troipsa.pseudocalendar.ui.theme.pageBackgroundColor
import int20h.troipsa.pseudocalendar.utils.compose.AdditionalRippleTheme
import int20h.troipsa.pseudocalendar.utils.compose.extension.medium
import int20h.troipsa.pseudocalendar.utils.extension.hasContactPermission
import int20h.troipsa.pseudocalendar.utils.extension.requestContactPermission

@Composable
fun ContactsScreen(
    navController: NavHostController,
) {
    PseudoScaffold(
        topBar = homeTopBarProvider(
            title = stringResource(Screen.Contacts.resourceId),
        )
    ) {
        val context = LocalContext.current

        val viewModel = hiltViewModel<ContactsVievModel>()
        val contactsList by viewModel.contactList.collectAsState()

        val launcher =
            rememberLauncherForActivityResult(ActivityResultContracts.PickContact()) { uri ->
                viewModel.addContact(uri, context)
            }

        Column {
            Button(
                onClick = {
                    if (context.hasContactPermission()) {
                        launcher.launch(null)
                    } else {
                        context.requestContactPermission()
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(10.dp)
            ) {
                Text(text = "Pick Contact")
            }

            Text(text = stringResource(Screen.Contacts.resourceId))

            ContactsList(
                contactsList = contactsList,
                modifier = Modifier
                    .padding(top = 16.dp)
                    .background(
                        color = itemBackgroundColor,
                        shape = RoundedCornerShape(4.dp)
                    )
                    .padding(16.dp)
                    .fillMaxSize()
            )
        }
    }
}

@Composable
private fun ContactsList(
    contactsList: List<Contact>,
    modifier: Modifier = Modifier
) {
    LazyColumn(
        verticalArrangement = Arrangement.spacedBy(8.dp), modifier = modifier
    ) {
        items(contactsList) { contact ->
            ContactItem(
                contact = contact,
                modifier = Modifier
                    .height(60.dp)
                    .background(
                        color = pageBackgroundColor,
                        shape = RoundedCornerShape(4.dp)
                    )
                    .fillMaxWidth()
            )
        }
    }
}

@Composable
private fun ContactItem(
    contact: Contact,
    modifier: Modifier = Modifier
) {
    CompositionLocalProvider(LocalRippleTheme provides AdditionalRippleTheme) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = modifier

        ) {
            Box(
                modifier = Modifier
                    .width(8.dp)
                    .fillMaxHeight()
            )
            Column(
                modifier = Modifier
                    .padding(horizontal = 8.dp, vertical = 8.dp)
                    .weight(1f)
            ) {
                Text(
                    text = contact.name,
                    style = MaterialTheme.typography.body2.medium(),
                    color = Color.White,
                    textAlign = TextAlign.Start,
                    overflow = TextOverflow.Ellipsis,
                    maxLines = 1
                )
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .padding(top = 4.dp)
                        .fillMaxWidth()
                ) {
                    Text(
                        text = "Phone ${contact.phone}",
                        style = MaterialTheme.typography.body2,
                        color = Color.White.copy(alpha = 0.6f),
                        textAlign = TextAlign.Start
                    )
                }
            }
        }
    }
}