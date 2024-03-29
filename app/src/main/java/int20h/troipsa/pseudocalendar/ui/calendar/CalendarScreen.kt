package int20h.troipsa.pseudocalendar.ui.calendar

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.ripple.LocalRippleTheme
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.BiasAlignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.kizitonwose.calendar.compose.ContentHeightMode
import com.kizitonwose.calendar.compose.HorizontalCalendar
import com.kizitonwose.calendar.compose.rememberCalendarState
import com.kizitonwose.calendar.core.*
import int20h.troipsa.pseudocalendar.domain.models.Event
import int20h.troipsa.pseudocalendar.domain.models.EventType
import int20h.troipsa.pseudocalendar.ui.base.ui.PseudoScaffold
import int20h.troipsa.pseudocalendar.ui.theme.*
import int20h.troipsa.pseudocalendar.utils.compose.AdditionalRippleTheme
import int20h.troipsa.pseudocalendar.utils.compose.StatusBarColorUpdateEffect
import int20h.troipsa.pseudocalendar.utils.compose.extension.medium
import int20h.troipsa.pseudocalendar.utils.displayText
import kotlinx.coroutines.launch
import java.time.DayOfWeek
import java.time.LocalDate
import java.time.YearMonth
import java.util.*

@Composable
fun CalendarScreen(
    navController: NavHostController,
    navigateToDaySchedule: (Long) -> Unit,
    navigateToEventScreen: (Long) -> Unit,
) {
    var floatingPointVisible by remember { mutableStateOf(true) }
    val showFloatingPoint: (Boolean) -> Unit = { floatingPointVisible = it }

    PseudoScaffold(
        modifier = Modifier.systemBarsPadding(),
        floatingActionButtonPosition = FabPosition.End,
        floatingActionButton = {
            AnimatedVisibility(visible = floatingPointVisible) {
                FloatingActionButton(
                    onClick = { navigateToEventScreen(0) },
                    backgroundColor = MaterialTheme.colors.secondary,
                    contentColor = Color.White,
                    content = {
                        Text(
                            text = "+",
                            fontSize = 24.sp,
                            fontWeight = FontWeight.Bold,
                        )
                    }
                )
            }
        },
    ) {
        val viewModel = hiltViewModel<CalendarViewModel>()
        val eventsMap by viewModel.eventsMap.collectAsState()
        val eventTypes by viewModel.changedEventTypes.collectAsState()

        CalendarScreen(
            eventsMap = eventsMap,
            eventTypes = eventTypes,
            showFloatingPoint = showFloatingPoint,
            navigateToDaySchedule = navigateToDaySchedule,
            resetEventTypes = viewModel::resetEventTypes,
            onEventTypeClick = viewModel::onEventTypeClick,
            onApplyFilters = viewModel::onApplyFiltersClick,
        )
    }
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun CalendarScreen(
    eventsMap: Map<LocalDate, List<Event>>,
    eventTypes: List<EventType>,
    showFloatingPoint: (Boolean) -> Unit,
    onEventTypeClick: (EventType) -> Unit,
    resetEventTypes: () -> Unit,
    onApplyFilters: () -> Unit,
    navigateToDaySchedule: (Long) -> Unit
) {
    val currentMonth = remember { YearMonth.now() }
    val startMonth = remember { currentMonth.minusMonths(500) }
    val endMonth = remember { currentMonth.plusMonths(500) }
    var selection by remember { mutableStateOf<CalendarDay?>(null) }
    val daysOfWeek = remember { daysOfWeek() }

    val sheetState = rememberModalBottomSheetState(ModalBottomSheetValue.Hidden)
    val coroutineScope = rememberCoroutineScope()

    LaunchedEffect(sheetState.isVisible) {
        showFloatingPoint(!sheetState.isVisible)
    }

    StatusBarColorUpdateEffect(toolbarColor)

    ModalBottomSheetLayout(
        sheetContent = {
            FiltersContent(
                eventTypes = eventTypes,
                onEventTypeClick = onEventTypeClick,
                onApplyFilters = {
                    onApplyFilters()
                    coroutineScope.launch { sheetState.hide() }
                }
            )
        },
        sheetShape = RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp),
        sheetBackgroundColor = pageBackgroundColor,
        sheetState = sheetState,
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(pageBackgroundColor),
        ) {
            val state = rememberCalendarState(
                startMonth = startMonth,
                endMonth = endMonth,
                firstVisibleMonth = currentMonth,
                firstDayOfWeek = daysOfWeek.first(),
                outDateStyle = OutDateStyle.EndOfGrid,
            )
            val visibleMonth = rememberFirstCompletelyVisibleMonth(state)

            LaunchedEffect(visibleMonth) {
                // Clear selection if we scroll to a new month.
                selection = null
            }

            // Draw light content on dark background.
            CompositionLocalProvider(LocalContentColor provides darkColors().onSurface) {
                SimpleCalendarTitle(
                    currentMonth = visibleMonth.yearMonth,
                    goToPrevious = {
                        coroutineScope.launch {
                            state.animateScrollToMonth(state.firstVisibleMonth.yearMonth.previousMonth)
                        }
                    },
                    goToNext = {
                        coroutineScope.launch {
                            state.animateScrollToMonth(state.firstVisibleMonth.yearMonth.nextMonth)
                        }
                    },
                    modifier = Modifier
                        .background(toolbarColor)
                        .padding(horizontal = 8.dp, vertical = 12.dp),
                )

                HorizontalCalendar(
                    modifier = Modifier.weight(1f),
                    state = state,
                    contentHeightMode = ContentHeightMode.Fill,
                    dayContent = { day ->
                        CompositionLocalProvider(LocalRippleTheme provides AdditionalRippleTheme) {
                            val isSelected = selection == day

                            val eventsInDay =if (day.position == DayPosition.MonthDate) {
                                eventsMap[day.date].orEmpty().filter { it.eventType.visible }
                            } else {
                                emptyList()
                            }

                            Day(
                                day = day,
                                eventsInDay = eventsInDay,
                                modifier = Modifier
                                    .fillMaxHeight()
                                    .border(
                                        width = if (isSelected) 1.dp else 0.dp,
                                        color = if (isSelected) selectedItemColor else Color.Transparent,
                                    )
                                    .padding(1.dp)
                                    .background(color = itemBackgroundColor)
                                    .clickable(
                                        enabled = day.position == DayPosition.MonthDate,
                                        onClick = {
                                            selection = day
                                            if (eventsInDay.isNotEmpty()) {
                                                navigateToDaySchedule(day.date.toEpochDay())
                                            }
                                        },
                                    )
                                    .fillMaxWidth()
                                    .padding(bottom = 4.dp)
                            )
                        }
                    },
                    monthHeader = {
                        MonthHeader(
                            modifier = Modifier.padding(vertical = 8.dp),
                            daysOfWeek = daysOfWeek,
                        )
                    },
                )
                Button(
                    onClick = {
                        coroutineScope.launch {
                            resetEventTypes()
                            sheetState.show()
                        }
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(end = 90.dp, start = 16.dp)
                        .padding(vertical = 8.dp)
                ) {
                    Text(
                        text = "Show Filters".uppercase(Locale.getDefault()),
                        style = MaterialTheme.typography.button.medium(),
                        color = Color.White,
                    )
                }
                Divider(color = pageBackgroundColor)
            }
        }
    }

}

@Composable
private fun Day(
    day: CalendarDay,
    modifier: Modifier = Modifier,
    eventsInDay: List<Event> = emptyList(),
) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy((3.5).dp),
    ) {
        DayHeader(
            day = day,
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 3.dp, end = 4.dp, bottom = 2.dp)
        )

        eventsInDay.forEachIndexed { index, event ->
            when {
                eventsInDay.size > 4 && index > 3 -> return@forEachIndexed
                eventsInDay.size > 4 && index == 3 -> {
                    EventItemPoints(
                        modifier = Modifier
                            .padding(horizontal = 5.dp)
                            .fillMaxWidth()
                            .height(16.dp)
                    )
                }
                else -> {
                    EventItem(
                        event = event,
                        modifier = Modifier
                            .padding(horizontal = 3.dp)
                            .fillMaxWidth()
                            .height(16.dp)
                    )
                }
            }
        }
    }
}

@Composable
private fun DayHeader(
    modifier: Modifier, day: CalendarDay
) {
    Row(
        modifier = modifier, horizontalArrangement = Arrangement.End
    ) {
        val textColor = when (day.position) {
            DayPosition.MonthDate -> Color.Unspecified
            DayPosition.InDate, DayPosition.OutDate -> inActiveTextColor
        }
        Text(
            text = day.date.dayOfMonth.toString(),
            color = textColor,
            fontSize = 12.sp,
        )
    }
}

@Composable
private fun EventItemPoints(
    modifier: Modifier = Modifier
) {
    Row(
        verticalAlignment = BiasAlignment.Vertical(-0.4f), modifier = modifier
    ) {
        repeat(3) {
            Box(
                modifier = Modifier
                    .padding(end = 1.dp)
                    .size((2.6).dp)
                    .background(
                        shape = CircleShape, color = Color.White.copy(alpha = 0.6f)
                    )
            )
        }
    }
}

@Composable
private fun EventItem(
    event: Event, modifier: Modifier = Modifier
) {
    Box(
        contentAlignment = Alignment.CenterStart,
        modifier = modifier.background(
                color = Color(event.eventType.color), shape = RoundedCornerShape(2.dp)
            ),
    ) {
        Text(
            text = event.name.lowercase(Locale.getDefault()),
            color = Color.White,
            fontSize = 9.sp,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
            modifier = Modifier
                .padding(horizontal = (2.5).dp)
                .align(Alignment.CenterStart),
        )
    }
}

@Composable
private fun MonthHeader(
    modifier: Modifier = Modifier,
    daysOfWeek: List<DayOfWeek> = emptyList(),
) {
    Row(modifier.fillMaxWidth()) {
        for (dayOfWeek in daysOfWeek) {
            Text(
                modifier = Modifier.weight(1f),
                textAlign = TextAlign.Center,
                fontSize = 12.sp,
                color = Color.White,
                text = dayOfWeek.displayText(uppercase = true),
                fontWeight = FontWeight.Light,
            )
        }
    }
}

@Preview(heightDp = 600)
@Composable
private fun Example3Preview() {
//    Example3Page()
}
