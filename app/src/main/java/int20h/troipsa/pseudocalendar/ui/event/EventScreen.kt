package int20h.troipsa.pseudocalendar.ui.event

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import int20h.troipsa.pseudocalendar.R
import int20h.troipsa.pseudocalendar.domain.models.Event
import int20h.troipsa.pseudocalendar.ui.base.ui.PseudoScaffold
import int20h.troipsa.pseudocalendar.ui.base.ui.defaultTopBarProvider
import int20h.troipsa.pseudocalendar.ui.theme.pageBackgroundColor
import int20h.troipsa.pseudocalendar.ui.theme.toolbarColor

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
        val event by viewModel.event.collectAsState()

        LaunchedEffect(eventId) {
            if (eventId != null) {
                viewModel.loadEvent(eventId)
            }
        }

        Column(
            modifier = Modifier
                .background(color = pageBackgroundColor)
                .fillMaxSize()
        ) {
            EventHeader(
                event = event,
                onNameChange = viewModel::onNameChange,
                modifier = Modifier
                    .background(
                        color = toolbarColor,
                    )
                    .fillMaxWidth()
            )
        }

    }
}

@Composable
fun EventHeader(
    event: Event?,
    onNameChange: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    TextField(
        value = event?.name ?: "",
        onValueChange = onNameChange,
        maxLines = 1,
        placeholder = { Text(text = stringResource(id = R.string.event_name_placeholder)) },
        modifier = modifier.padding(16.dp)
    )
}
