package int20h.troipsa.pseudocalendar.ui.day_schedule

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import int20h.troipsa.pseudocalendar.ui.basic.PseudoScaffold
import int20h.troipsa.pseudocalendar.utils.compose.extension.bold
import int20h.troipsa.pseudocalendar.utils.compose.extension.medium
import int20h.troipsa.pseudocalendar.utils.displayText
import int20h.troipsa.pseudocalendar.utils.extension.formatToDaySchedule
import java.time.LocalDateTime
import java.time.ZoneOffset


@Composable
fun DaySchedule(
    epochDay: Long?,
    popBackStack: () -> Unit
) {
    BackHandler(
        enabled = true,
        onBack = popBackStack
    )

    PseudoScaffold(
        modifier = Modifier.systemBarsPadding()
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
                .fillMaxSize()
        ) {
            val date = remember(daySchedule.epochDay) {
                getLocalDateTimeOfEpochDay(daySchedule.epochDay, 0, ZoneOffset.UTC)
            }

            Text(
                text = date.dayOfWeek.displayText(),
                style = MaterialTheme.typography.h5.bold(),
                color = Color.White,
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .padding(16.dp)
                    .fillMaxSize()
            )
            Text(
                text = date.formatToDaySchedule(),
                style = MaterialTheme.typography.body1.medium(),
                color = Color.White,
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .padding(16.dp)
                    .fillMaxSize()
            )
        }

    }
}

private fun getLocalDateTimeOfEpochDay(
    epochDay: Long,
    nanoOfDay: Int,
    offset: ZoneOffset
): LocalDateTime {
    val epochSecond = epochDay * 86400 + nanoOfDay / 1000000000
    val nanoOfSecond = nanoOfDay % 1000000000
    return LocalDateTime.ofEpochSecond(epochSecond, nanoOfSecond, offset)
}

