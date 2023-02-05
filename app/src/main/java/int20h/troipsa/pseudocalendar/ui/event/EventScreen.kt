package int20h.troipsa.pseudocalendar.ui.event

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import int20h.troipsa.pseudocalendar.R
import int20h.troipsa.pseudocalendar.ui.base.ui.PseudoScaffold
import int20h.troipsa.pseudocalendar.ui.base.ui.defaultTopBarProvider
import int20h.troipsa.pseudocalendar.ui.theme.pageBackgroundColor
import int20h.troipsa.pseudocalendar.ui.theme.toolbarColor
import int20h.troipsa.pseudocalendar.utils.coroutines.launchAndCollect
import java.util.*

@Composable
fun EventScreen(
    eventId: Int?,
    popBackStack: () -> Unit
) {
    BackHandler(
        enabled = true,
        onBack = popBackStack
    )

    val viewModel = hiltViewModel<EventViewModel>()
    val canSave by viewModel.canSave.collectAsState()

    PseudoScaffold(
        modifier = Modifier.systemBarsPadding(),
        topBar = defaultTopBarProvider(
            closeIcon = painterResource(id = R.drawable.ic_back_arrow),
            closeAction = popBackStack,
            actionButton = {
                if (canSave) {
                    Text(
                        text = stringResource(id = R.string.event_action_save),
                        color = MaterialTheme.colors.onPrimary,
                        style = MaterialTheme.typography.button,
                        modifier = Modifier.clickable(onClick = viewModel::saveEvent)
                    )
                }
            }
        )
    ) {
        val event by viewModel.updatedEvent.collectAsState()
        val canDelete by viewModel.canDelete.collectAsState()

        LaunchedEffect(eventId) {
            if (eventId != null) {
                viewModel.loadEvent(eventId)
            }
        }

        LaunchedEffect(Unit) {
            viewModel.closeScreen.launchAndCollect(this) {
                popBackStack()
            }
        }

        Column(
            modifier = Modifier
                .background(color = pageBackgroundColor)
                .fillMaxSize()
        ) {
            EventHeader(
                eventName = event.name,
                onNameChange = viewModel::onNameChange,
                modifier = Modifier
                    .background(
                        color = toolbarColor,
                    )
                    .fillMaxWidth()
            )

            DeleteButton(
                canDelete = canDelete,
                onClick = viewModel::deleteEvent,
                modifier = Modifier
                    .padding(horizontal = 16.dp, vertical = 8.dp)
                    .fillMaxWidth()
                    .height(48.dp)
            )
        }

    }
}

@Composable
private fun DeleteButton(
    canDelete: Boolean,
    onClick: () -> Unit,
    modifier: Modifier
) {
    if (canDelete) {
        Button(
            onClick = onClick,
            elevation = ButtonDefaults.elevation(0.dp),
            border = ButtonDefaults.outlinedBorder,
            colors = ButtonDefaults.buttonColors(
                backgroundColor = Color.Transparent,
                contentColor = MaterialTheme.colors.error
            ),
            modifier = modifier
        ) {
            Text(
                text = stringResource(
                    id = R.string.event_name_delete_button
                ).uppercase(Locale.getDefault()),
                textAlign = TextAlign.Center,
                color = MaterialTheme.colors.error,
                modifier = Modifier.fillMaxWidth()
            )
        }
    }
}

@Composable
fun EventHeader(
    eventName: String,
    onNameChange: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    TextField(
        value = eventName,
        onValueChange = onNameChange,
        maxLines = 1,
        textStyle = MaterialTheme.typography.h5,
        colors = TextFieldDefaults.textFieldColors(
            textColor = MaterialTheme.colors.onPrimary,
        ),
        modifier = modifier.padding(16.dp)
    )
}
