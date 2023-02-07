package int20h.troipsa.pseudocalendar.ui.contacts

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.provider.ContactsContract
import android.provider.ContactsContract.Contacts
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.ripple.LocalRippleTheme
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.core.app.ActivityCompat.startActivityForResult
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import int20h.troipsa.pseudocalendar.R
import int20h.troipsa.pseudocalendar.domain.models.Contact
import int20h.troipsa.pseudocalendar.domain.models.Event
import int20h.troipsa.pseudocalendar.ui.base.ui.PseudoScaffold
import int20h.troipsa.pseudocalendar.ui.base.ui.homeTopBarProvider
import int20h.troipsa.pseudocalendar.ui.navigation.Screen
import int20h.troipsa.pseudocalendar.ui.theme.itemBackgroundColor
import int20h.troipsa.pseudocalendar.ui.theme.pageBackgroundColor
import int20h.troipsa.pseudocalendar.utils.compose.AdditionalRippleTheme
import int20h.troipsa.pseudocalendar.utils.compose.extension.medium
import int20h.troipsa.pseudocalendar.utils.extension.formatToEventTime

@Composable
fun ContactsScreen(
    navController: NavHostController,
    context: Context = LocalContext.current
    ) {
    PseudoScaffold(
        topBar = homeTopBarProvider(
            title = stringResource(Screen.Contacts.resourceId),
        )
    ) {
        val viewModel = hiltViewModel<ContactsVievModel>()
        val contactsList by viewModel.contactList.collectAsState()
        val activity = LocalContext.current as Activity

        Column {
            Button(
                // on below line adding on click for button.
                onClick = {
                    // on below line checking if permission is granted.
                    if (viewModel.hasContactPermission(context)) {
                        // if permission granted open intent to pick contact/
                        val intent = Intent(Intent.ACTION_GET_CONTENT)
                        intent.type = ContactsContract.CommonDataKinds.Phone.CONTENT_ITEM_TYPE
                        startActivityForResult(activity, intent, 1, null)
                    } else {
                        // if permission not granted requesting permission .
                        viewModel.requestContactPermission(context, activity)
                    }
                },
                // adding padding to button.
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(10.dp)
            ) {
                // displaying text in our button.
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
                        text = "Phone ${contact.phoneNumber}",
                        style = MaterialTheme.typography.body2,
                        color = Color.White.copy(alpha = 0.6f),
                        textAlign = TextAlign.Start
                    )
                }
            }
        }
    }
}