package int20h.troipsa.pseudocalendar.ui.calendar

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.google.accompanist.flowlayout.FlowRow
import int20h.troipsa.pseudocalendar.domain.models.EventType
import int20h.troipsa.pseudocalendar.utils.compose.extension.bold
import int20h.troipsa.pseudocalendar.utils.compose.extension.medium
import int20h.troipsa.pseudocalendar.utils.compose.extension.optional
import java.util.*

@Composable
fun FiltersContent(
    eventTypes: List<EventType>,
    selectedEventTypes: List<EventType>,
    onEventTypeClick: (EventType, Boolean) -> Unit,
    onApplyFilters: () -> Unit,
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
    ) {
        Text(
            text = "Event types",
            style = MaterialTheme.typography.body1.medium(),
            color = Color.White,
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 16.dp)
        )
        FlowRow(
            modifier = Modifier
                .padding(horizontal = 16.dp, vertical = 8.dp)
                .fillMaxWidth()
        ) {
            eventTypes.forEach { eventType ->
                val selected = selectedEventTypes.contains(eventType)
                Text(
                    text = eventType.name,
                    style = MaterialTheme.typography.caption.bold(),
                    color = Color.White,
                    modifier = Modifier
                        .optional(selected) {
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
                        .clickable { onEventTypeClick(eventType, selected) }
                        .padding(4.dp)
                )
            }
        }

        Button(
            onClick = onApplyFilters,
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 16.dp)
        ) {
            Text(
                text = "Apply filters".uppercase(Locale.getDefault()),
                style = MaterialTheme.typography.button.medium(),
                color = Color.White,
            )
        }
    }
}