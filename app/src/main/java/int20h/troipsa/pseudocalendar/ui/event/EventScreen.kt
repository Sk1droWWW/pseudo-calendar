package int20h.troipsa.pseudocalendar.ui.event

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.ripple.LocalRippleTheme
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.google.accompanist.flowlayout.FlowRow
import com.marosseleng.compose.material3.datetimepickers.date.ui.dialog.DatePickerDialog
import com.marosseleng.compose.material3.datetimepickers.time.ui.dialog.TimePickerDialog
import int20h.troipsa.pseudocalendar.R
import int20h.troipsa.pseudocalendar.ui.base.ui.PseudoScaffold
import int20h.troipsa.pseudocalendar.ui.base.ui.defaultTopBarProvider
import int20h.troipsa.pseudocalendar.ui.theme.itemBackgroundColor
import int20h.troipsa.pseudocalendar.ui.theme.pageBackgroundColor
import int20h.troipsa.pseudocalendar.ui.theme.toolbarColor
import int20h.troipsa.pseudocalendar.utils.compose.AdditionalRippleTheme
import int20h.troipsa.pseudocalendar.utils.compose.extension.bold
import int20h.troipsa.pseudocalendar.utils.compose.extension.medium
import int20h.troipsa.pseudocalendar.utils.compose.extension.optional
import int20h.troipsa.pseudocalendar.utils.coroutines.launchAndCollect
import int20h.troipsa.pseudocalendar.utils.displayText
import int20h.troipsa.pseudocalendar.utils.extension.formatToDaySchedule
import int20h.troipsa.pseudocalendar.utils.extension.formatToEventTime
import java.util.*

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun EventScreen(
    eventId: Int?,
    popBackStack: () -> Unit,
    navigateToEventType: (String) -> Unit = {},
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
        val eventTypes by viewModel.eventTypes.collectAsState()

        val canDelete by viewModel.canDelete.collectAsState()

        val dateDialogVisible by viewModel.dateDialogVisible.collectAsState()
        val timeDialogVisible by viewModel.timeDialogVisible.collectAsState()
        val timeFrame by viewModel.timeFrame.collectAsState()

        LaunchedEffect(eventId) {
            if (eventId != null) {
                viewModel.initScreenInfo(eventId)
            }
        }

        LaunchedEffect(Unit) {
            viewModel.closeScreen.launchAndCollect(this) {
                popBackStack()
            }
        }

        if (dateDialogVisible) {
            DatePickerDialog(
                initialDate = event.startTime.toLocalDate(),
                onDismissRequest = { viewModel.showDateDialog(false) },
                onDateChange = viewModel::onDateChange,
            )
        }
        if (timeDialogVisible) {
            TimePickerDialog(
                initialTime = if (timeFrame == Timeframes.START) {
                    event.startTime
                } else {
                    event.endTime
                }.toLocalTime(),
                is24HourFormat = true,
                onDismissRequest = { viewModel.showTimeDialog(false) },
                onTimeChange = viewModel::onTimeChange,
            )
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
            CompositionLocalProvider(LocalRippleTheme provides AdditionalRippleTheme) {
                TextField(
                    value = event.description,
                    onValueChange = viewModel::onDescriptionChange,
                    maxLines = 3,
                    textStyle = MaterialTheme.typography.body1,
                    colors = TextFieldDefaults.textFieldColors(
                        textColor = MaterialTheme.colors.onPrimary,
                    ),
                    modifier = Modifier
                        .padding(horizontal = 16.dp, vertical = 8.dp)
                        .background(
                            shape = RoundedCornerShape(8.dp),
                            color = itemBackgroundColor
                        )
                        .fillMaxWidth()
                        .padding(horizontal = 8.dp, vertical = 4.dp)
                )

                //Date
                Text(
                    text = "${event.startTime.dayOfWeek.displayText()}, ${event.startTime.formatToDaySchedule()}",
                    style = MaterialTheme.typography.body1,
                    color = MaterialTheme.colors.onPrimary,
                    modifier = Modifier
                        .padding(horizontal = 16.dp, vertical = 8.dp)
                        .background(
                            shape = RoundedCornerShape(8.dp),
                            color = itemBackgroundColor
                        )
                        .clickable { viewModel.showDateDialog(true) }
                        .fillMaxWidth()
                        .padding(horizontal = 8.dp, vertical = 4.dp)
                )
                // start time
                Text(
                    text = "Start Time: ${event.startTime.formatToEventTime()}",
                    style = MaterialTheme.typography.body1,
                    color = MaterialTheme.colors.onPrimary,
                    modifier = Modifier
                        .padding(horizontal = 16.dp, vertical = 8.dp)
                        .background(
                            shape = RoundedCornerShape(8.dp),
                            color = itemBackgroundColor
                        )
                        .clickable { viewModel.showTimeDialog(true, Timeframes.START) }
                        .fillMaxWidth()
                        .padding(horizontal = 8.dp, vertical = 4.dp)
                )
                // End Time
                Text(
                    text = "End Time: ${event.endTime.formatToEventTime()}",
                    style = MaterialTheme.typography.body1,
                    color = MaterialTheme.colors.onPrimary,
                    modifier = Modifier
                        .padding(horizontal = 16.dp, vertical = 8.dp)
                        .background(
                            shape = RoundedCornerShape(8.dp),
                            color = itemBackgroundColor
                        )
                        .clickable { viewModel.showTimeDialog(true, Timeframes.END) }
                        .fillMaxWidth()
                        .padding(horizontal = 8.dp, vertical = 4.dp)
                )
            }

            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .padding(horizontal = 16.dp, vertical = 8.dp)
                    .fillMaxWidth()
            ) {
                Text(
                    text = "Event Type",
                    style = MaterialTheme.typography.body1.medium(),
                    color = MaterialTheme.colors.onPrimary,
                )
                Spacer(modifier = Modifier.weight(1f))
                IconButton(
                    onClick = { navigateToEventType(event.eventType.id.toString()) },
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_edit),
                        contentDescription = null,
                        tint = MaterialTheme.colors.onPrimary
                    )
                }
                IconButton(
                    onClick = { navigateToEventType(0.toString()) },
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_add),
                        contentDescription = null,
                        tint = MaterialTheme.colors.onPrimary
                    )
                }
            }

            FlowRow(
                modifier = Modifier
                    .padding(horizontal = 16.dp, vertical = 8.dp)
                    .fillMaxWidth()
            ) {
                eventTypes.forEach { eventType ->
                    Text(
                        text = eventType.name,
                        style = MaterialTheme.typography.caption.bold(),
                        color = Color.White,
                        modifier = Modifier
                            .optional(event.eventType == eventType) {
                                border(
                                    width = 2.dp,
                                    color = Color.White,
                                    shape = RoundedCornerShape(8.dp)
                                )
                            }
                            .padding(4.dp)
                            .background(
                                shape = RoundedCornerShape(8.dp),
                                color = Color(eventType.color)
                            )
                            .clickable { viewModel.onEventTypeChange(eventType) }
                            .padding(4.dp)
                    )
                }
            }

            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .padding(horizontal = 16.dp, vertical = 8.dp)
                    .fillMaxWidth()
            ) {
                Text(
                    text = "Participants: ",
                    style = MaterialTheme.typography.body1.medium(),
                    color = MaterialTheme.colors.onPrimary,
                )
                Spacer(modifier = Modifier.weight(1f))
                IconButton(
                    onClick = {  },
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_add),
                        contentDescription = null,
                        tint = MaterialTheme.colors.onPrimary
                    )
                }
            }

            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .padding(horizontal = 16.dp, vertical = 8.dp)
                    .fillMaxWidth()
            ) {
                Text(
                    text = "Attached files: ",
                    style = MaterialTheme.typography.body1.medium(),
                    color = MaterialTheme.colors.onPrimary,
                )
                Spacer(modifier = Modifier.weight(1f))
                IconButton(
                    onClick = {  },
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_add),
                        contentDescription = null,
                        tint = MaterialTheme.colors.onPrimary
                    )
                }
            }

            Spacer(modifier = Modifier.weight(1f))
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
private fun EventHeader(
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
