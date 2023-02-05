package int20h.troipsa.pseudocalendar.ui.day_schedule

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.ripple.LocalRippleTheme
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import int20h.troipsa.pseudocalendar.R
import int20h.troipsa.pseudocalendar.domain.models.DaySchedule
import int20h.troipsa.pseudocalendar.domain.models.Event
import int20h.troipsa.pseudocalendar.ui.base.ui.PseudoScaffold
import int20h.troipsa.pseudocalendar.ui.base.ui.defaultTopBarProvider
import int20h.troipsa.pseudocalendar.ui.theme.itemBackgroundColor
import int20h.troipsa.pseudocalendar.ui.theme.pageBackgroundColor
import int20h.troipsa.pseudocalendar.ui.theme.toolbarColor
import int20h.troipsa.pseudocalendar.utils.compose.AdditionalRippleTheme
import int20h.troipsa.pseudocalendar.utils.compose.extension.bold
import int20h.troipsa.pseudocalendar.utils.compose.extension.medium
import int20h.troipsa.pseudocalendar.utils.displayText
import int20h.troipsa.pseudocalendar.utils.extension.formatToDaySchedule
import int20h.troipsa.pseudocalendar.utils.extension.formatToEventTime
import java.time.LocalDateTime
import java.time.ZoneOffset


@Composable
fun DayScheduleScreen(
    epochDay: Long?,
    navigateToEvent: (Int) -> Unit,
    popBackStack: () -> Unit
) {
    BackHandler(
        enabled = true,
        onBack = popBackStack
    )

    PseudoScaffold(
        modifier = Modifier.systemBarsPadding(),
        topBar = defaultTopBarProvider(
            closeIcon = painterResource(id = R.drawable.ic_back_arrow),
            closeAction = popBackStack,
        )
    ) {
        val viewModel = hiltViewModel<DayScheduleViewModel>()
        val daySchedule by viewModel.daySchedule.collectAsState()

        LaunchedEffect(epochDay) {
            if (epochDay != null) {
                viewModel.loadDaySchedule(epochDay)
            }
        }

        Column(
            modifier = Modifier
                .background(color = pageBackgroundColor)
                .fillMaxSize()
        ) {
            DayScheduleHeader(
                daySchedule = daySchedule,
                modifier = Modifier
                    .background(
                        color = toolbarColor,
                    )
                    .fillMaxWidth()
            )

            EventsList(
                onItemClick = navigateToEvent,
                daySchedule = daySchedule,
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
private fun DayScheduleHeader(
    daySchedule: DaySchedule,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
    ) {
        val date = remember(daySchedule.epochDay) {
            getLocalDateTimeOfEpochDay(daySchedule.epochDay, 0, ZoneOffset.UTC)
        }
        Text(
            text = date.dayOfWeek.displayText(short = false),
            style = MaterialTheme.typography.h5.bold(),
            color = Color.White,
            textAlign = TextAlign.Center,
            modifier = Modifier
                .padding(horizontal = 16.dp)
                .padding(top = 8.dp)
                .fillMaxWidth()
        )
        Text(
            text = date.formatToDaySchedule(),
            style = MaterialTheme.typography.body1.medium(),
            color = Color.White,
            textAlign = TextAlign.Center,
            modifier = Modifier
                .padding(bottom = 8.dp)
                .padding(horizontal = 16.dp)
                .fillMaxWidth()
        )
    }
}

@Composable
private fun EventsList(
    onItemClick: (Int) -> Unit,
    daySchedule: DaySchedule,
    modifier: Modifier = Modifier
) {
    LazyColumn(
        verticalArrangement = Arrangement.spacedBy(8.dp), modifier = modifier
    ) {
        item {
            EventsHeader(
                modifier = Modifier
                    .padding(bottom = 8.dp, start = 8.dp)
                    .fillMaxWidth()
            )
        }
        items(daySchedule.events) { event ->
            EventItem(
                event = event,
                modifier = Modifier
                    .height(60.dp)
                    .background(
                        color = pageBackgroundColor,
                        shape = RoundedCornerShape(4.dp)
                    )
                    .clip(RoundedCornerShape(4.dp))
                    .clickable(onClick = { onItemClick(event.id) })
                    .fillMaxWidth()
            )
        }
    }
}

@NonRestartableComposable
@Composable
private fun EventsHeader(
    modifier: Modifier = Modifier
) {
    Text(
        text = stringResource(id = R.string.day_schedule_events_title),
        style = MaterialTheme.typography.body1.medium(),
        color = Color.White,
        textAlign = TextAlign.Start,
        modifier = modifier
    )
}

@Composable
private fun EventItem(
    event: Event,
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
                    .background(Color(event.eventType.color))
            )

            Column(
                modifier = Modifier
                    .padding(horizontal = 8.dp, vertical = 8.dp)
                    .weight(1f)
            ) {
                Text(
                    text = event.name,
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
                        text = "${event.startTime.formatToEventTime()} — ${event.endTime.formatToEventTime()}",
                        style = MaterialTheme.typography.body2,
                        color = Color.White.copy(alpha = 0.6f),
                        textAlign = TextAlign.Start
                    )
                    Text(
                        text = stringResource(id = R.string.day_schedule_event_type),
                        style = MaterialTheme.typography.body2,
                        color = Color.White.copy(alpha = 0.6f),
                        modifier = Modifier.padding(start = 16.dp)
                    )
                    Text(
                        text = event.eventType.name,
                        style = MaterialTheme.typography.body2,
                        color = Color.White.copy(alpha = 0.6f),
                        modifier = Modifier.padding(start = 4.dp),
                        overflow = TextOverflow.Ellipsis,
                        maxLines = 1
                    )
                }
            }

            Icon(
                painter = painterResource(id = R.drawable.ic_edit),
                contentDescription = null,
                tint = Color.White,
                modifier = Modifier
                    .size(24.dp)
                    .weight(0.1f)
                    .padding(end = 16.dp)
            )
        }
    }
}

private fun getLocalDateTimeOfEpochDay(
    epochDay: Long, nanoOfDay: Int, offset: ZoneOffset
): LocalDateTime {
    val epochSecond = epochDay * 86400 + nanoOfDay / 1000000000
    val nanoOfSecond = nanoOfDay % 1000000000
    return LocalDateTime.ofEpochSecond(epochSecond, nanoOfSecond, offset)
}

