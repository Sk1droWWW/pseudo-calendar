package int20h.troipsa.pseudocalendar.ui.calendar

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Divider
import androidx.compose.material.LocalContentColor
import androidx.compose.material.Text
import androidx.compose.material.darkColors
import androidx.compose.material.ripple.LocalRippleTheme
import androidx.compose.material.ripple.RippleTheme
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.BiasAlignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
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
import int20h.troipsa.pseudocalendar.R
import int20h.troipsa.pseudocalendar.domain.models.Event
import int20h.troipsa.pseudocalendar.ui.basic.PseudoScaffold
import int20h.troipsa.pseudocalendar.utils.compose.StatusBarColorUpdateEffect
import int20h.troipsa.pseudocalendar.utils.displayText
import kotlinx.coroutines.launch
import java.time.DayOfWeek
import java.time.LocalDate
import java.time.YearMonth


private val pageBackgroundColor: Color @Composable get() = colorResource(R.color.example_5_page_bg_color)
private val itemBackgroundColor: Color @Composable get() = colorResource(R.color.example_5_item_view_bg_color)
private val toolbarColor: Color @Composable get() = colorResource(R.color.example_5_toolbar_color)
private val selectedItemColor: Color @Composable get() = colorResource(R.color.example_5_text_grey)
private val inActiveTextColor: Color @Composable get() = colorResource(R.color.example_5_text_grey_light)


@Composable
fun Calendar(
    navController: NavHostController, navigateToDaySchedule: (Long) -> Unit
) {
    PseudoScaffold(
        modifier = Modifier.systemBarsPadding()
    ) {
        val viewModel = hiltViewModel<CalendarViewModel>()
        val eventsMap by viewModel.eventsMap.collectAsState()

        CalendarScreen(
            eventsMap = eventsMap,
            navigateToDaySchedule = navigateToDaySchedule,
        )
    }
}

@Composable
fun CalendarScreen(
    eventsMap: Map<LocalDate, List<Event>>, navigateToDaySchedule: (Long) -> Unit
) {
    val currentMonth = remember { YearMonth.now() }
    val startMonth = remember { currentMonth.minusMonths(500) }
    val endMonth = remember { currentMonth.plusMonths(500) }
    var selection by remember { mutableStateOf<CalendarDay?>(null) }
    val daysOfWeek = remember { daysOfWeek() }
    /* val flightsInSelectedDate = remember {
         derivedStateOf {
             val date = selection?.date
             if (date == null) emptyList() else events[date].orEmpty()
         }
     }*/

    StatusBarColorUpdateEffect(toolbarColor)

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
        val coroutineScope = rememberCoroutineScope()
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
                modifier = Modifier
                    .wrapContentWidth()
                    .padding(bottom = 16.dp),
                state = state,
                contentHeightMode = ContentHeightMode.Fill,
                dayContent = { day ->
                    CompositionLocalProvider(LocalRippleTheme provides CalendarRippleTheme) {
                        val isSelected = selection == day
                        val eventsInDay =if (day.position == DayPosition.MonthDate) {
                            eventsMap[day.date].orEmpty()
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
            Divider(color = pageBackgroundColor)
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
            text = event.name,
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


// The default dark them ripple is too bright so we tone it down.
private object CalendarRippleTheme : RippleTheme {
    @Composable
    override fun defaultColor() = RippleTheme.defaultRippleColor(Color.Gray, lightTheme = false)

    @Composable
    override fun rippleAlpha() = RippleTheme.defaultRippleAlpha(Color.Gray, lightTheme = false)
}

@Preview(heightDp = 600)
@Composable
private fun Example3Preview() {
//    Example3Page()
}
