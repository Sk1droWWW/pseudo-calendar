package int20h.troipsa.pseudocalendar.ui.event_type

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.material.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.godaddy.android.colorpicker.ClassicColorPicker
import com.godaddy.android.colorpicker.HsvColor
import int20h.troipsa.pseudocalendar.R
import int20h.troipsa.pseudocalendar.ui.base.ui.PseudoScaffold
import int20h.troipsa.pseudocalendar.ui.base.ui.defaultTopBarProvider
import int20h.troipsa.pseudocalendar.ui.theme.pageBackgroundColor
import int20h.troipsa.pseudocalendar.ui.theme.toolbarColor
import int20h.troipsa.pseudocalendar.utils.compose.extension.medium
import int20h.troipsa.pseudocalendar.utils.coroutines.launchAndCollect

@Composable
fun EventTypeScreen(
    eventTypeId: Int?,
    popBackStack: () -> Unit,
) {
    BackHandler(
        enabled = true,
        onBack = popBackStack
    )

    val viewModel = hiltViewModel<EventTypeViewModel>()

    PseudoScaffold(
        modifier = Modifier.systemBarsPadding(),
        topBar = defaultTopBarProvider(
            closeIcon = painterResource(id = R.drawable.ic_back_arrow),
            closeAction = popBackStack,
            actionButton = {
                Text(
                    text = stringResource(id = R.string.event_action_save),
                    color = MaterialTheme.colors.onPrimary,
                    style = MaterialTheme.typography.button,
                    modifier = Modifier.clickable(onClick = viewModel::saveEventType)
                )
            }
        )
    ) {
        val eventType by viewModel.eventType.collectAsState()

        LaunchedEffect(eventTypeId) {
            if (eventTypeId != null) {
                viewModel.loadEventType(eventTypeId)
            }
        }

        LaunchedEffect(Unit) {
            viewModel.updateEventTypeColor(eventType.color)
            viewModel.closeScreen.launchAndCollect(this) {
                popBackStack()
            }
        }

        Column(
            modifier = Modifier
                .background(color = pageBackgroundColor)
                .fillMaxSize()
        ) {
            TextField(
                value = eventType.name,
                onValueChange = viewModel::updateEventTypeName,
                maxLines = 1,
                textStyle = MaterialTheme.typography.h5,
                colors = TextFieldDefaults.textFieldColors(
                    textColor = MaterialTheme.colors.onPrimary,
                ),
                modifier = Modifier
                    .background(
                        color = toolbarColor,
                    )
                    .fillMaxWidth()
                    .padding(16.dp)
            )

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            ) {
                Text(
                    text = "Color: ",
                    color = MaterialTheme.colors.onPrimary,
                    style = MaterialTheme.typography.body1.medium(),
                )
                Box(
                    modifier = Modifier
                        .height(24.dp)
                        .fillMaxWidth()
                        .background(color = Color(eventType.color))
                        .padding(8.dp)
                )
            }

            ClassicColorPicker(
                showAlphaBar = true,
                color = HsvColor.from(Color(eventType.color)),
                onColorChanged = { color: HsvColor ->
                    viewModel.updateEventTypeColor(color.toColor().toArgb().toLong())
                },
                modifier = Modifier
                    .padding(16.dp)
                    .height(300.dp)
                    .fillMaxWidth()
            )
        }
    }

}