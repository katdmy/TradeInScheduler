package ru.katdmy.tradein.ui.main

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.Layout
import androidx.compose.ui.layout.ParentDataModifier
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import ru.katdmy.tradein.ui.theme.WeekScheduleTheme
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.format.DateTimeFormatter
import java.time.temporal.ChronoUnit
import kotlin.math.roundToInt

class CalendarActivity: ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            WeekScheduleTheme {
                // A surface container using the 'background' color from the theme
                Surface(color = MaterialTheme.colors.background) {
                    Schedule(events = sampleEvents)
                }
            }
        }
    }
}

data class Event(
    val name: String,
    val color: Color,
    val start: LocalDateTime,
    val end: LocalDateTime,
    val description: String? = null
)

val EventTimeFormatter = DateTimeFormatter.ofPattern("HH:mm")
val DayFormatter = DateTimeFormatter.ofPattern("EEEE, d MMM")
val HourFormatter = DateTimeFormatter.ofPattern("HH:mm")

inline class SplitType private constructor(val value: Int) {
    companion object {
        val None = SplitType(0)
        val Start = SplitType(1)
        val End = SplitType(2)
        val Both = SplitType(3)
    }
}

data class PositionedEvent(
    val event: Event,
    val splitType: SplitType,
    val date: LocalDate,
    val start: LocalTime,
    val end: LocalTime
)

@Composable
fun BasicEvent(
    positionedEvent: PositionedEvent,
    modifier: Modifier = Modifier
) {
    val event = positionedEvent.event
    val topRadius = if (positionedEvent.splitType == SplitType.Start || positionedEvent.splitType == SplitType.Both) 0.dp else 4.dp
    val bottomRadius = if (positionedEvent.splitType == SplitType.End || positionedEvent.splitType == SplitType.Both) 0.dp else 4.dp
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(end = 2.dp, bottom = if (positionedEvent.splitType == SplitType.End) 0.dp else 2.dp)
            .background(
                color = event.color,
                shape = RoundedCornerShape(
                    topStart = topRadius,
                    topEnd = topRadius,
                    bottomStart = bottomRadius,
                    bottomEnd = bottomRadius
                )
            )
            .padding(4.dp)
    ) {
        Text(
            text = "${event.start.format(EventTimeFormatter)} - ${event.end.format(EventTimeFormatter)}",
            style = MaterialTheme.typography.caption
        )

        Text(
            text = event.name,
            style = MaterialTheme.typography.body1,
            fontWeight = FontWeight.Bold
        )

        if (event.description != null) {
            Text(
                text = event.description,
                style = MaterialTheme.typography.body2,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        }
    }
}

val sampleEvents = listOf(
    Event(
        name = "Google I/O Keynote",
        color = Color(0xFFAFBBF2),
        start = LocalDateTime.parse("2021-10-01T09:00:00"),
        end = LocalDateTime.parse("2021-10-01T11:00:00"),
        description = "Tune in to find out about how we're furthering our mission to organize the world’s information and make it universally accessible and useful.",
    ),
    Event(
        name = "Developer Keynote",
        color = Color(0xFFAFBBF2),
        start = LocalDateTime.parse("2021-10-01T11:15:00"),
        end = LocalDateTime.parse("2021-10-01T12:15:00"),
        description = "Learn about the latest updates to our developer products and platforms from Google Developers.",
    ),
    Event(
        name = "What's new in Android",
        color = Color(0xFF1B998B),
        start = LocalDateTime.parse("2021-10-01T12:30:00"),
        end = LocalDateTime.parse("2021-10-01T15:00:00"),
        description = "In this Keynote, Chet Haase, Dan Sandler, and Romain Guy discuss the latest Android features and enhancements for developers.",
    ),
    Event(
        name = "What's new in Machine Learning",
        color = Color(0xFFF4BFDB),
        start = LocalDateTime.parse("2021-10-02T09:30:00"),
        end = LocalDateTime.parse("2021-10-02T11:00:00"),
        description = "Learn about the latest and greatest in ML from Google. We’ll cover what’s available to developers when it comes to creating, understanding, and deploying models for a variety of different applications.",
    ),
    Event(
        name = "What's new in Material Design",
        color = Color(0xFF6DD3CE),
        start = LocalDateTime.parse("2021-10-02T11:00:00"),
        end = LocalDateTime.parse("2021-10-03T11:45:00"),
        description = "Learn about the latest design improvements to help you build personal dynamic experiences with Material Design.",
    ),
    Event(
        name = "Jetpack Compose Basics",
        color = Color(0xFF1B998B),
        start = LocalDateTime.parse("2021-10-03T12:00:00"),
        end = LocalDateTime.parse("2021-10-03T13:00:00"),
        description = "This Workshop will take you through the basics of building your first app with Jetpack Compose, Android's new modern UI toolkit that simplifies and accelerates UI development on Android.",
    ),
)

@Composable
fun BasicSchedule(
    events: List<Event>,
    modifier: Modifier = Modifier,
    eventContent: @Composable (positionedEvent: PositionedEvent) -> Unit = { BasicEvent(positionedEvent = it) },
    minDate: LocalDate = events.minByOrNull(Event::start)!!.start.toLocalDate(),
    maxDate: LocalDate = events.maxByOrNull(Event::end)!!.end.toLocalDate(),
    dayWidth: Dp,
    hourHeight: Dp
) {
    val numDays = ChronoUnit.DAYS.between(minDate, maxDate).toInt() + 1
    val dividerColor = if (MaterialTheme.colors.isLight) Color.LightGray else Color.DarkGray
    val splitEvents = remember(events) { splitEvents(events.sortedBy(Event::start)) }
    Layout(
        content = {
                  splitEvents.forEach { splitEvent ->
                      Box(modifier = Modifier.eventData(splitEvent)) {
                          eventContent(splitEvent)
                      }
                  }
        },
        modifier = modifier
            .drawBehind {
                repeat(16) {
                    drawLine(
                        dividerColor,
                        start = Offset(0f, (it) * hourHeight.toPx()),
                        end = Offset(size.width, (it) * hourHeight.toPx()),
                        strokeWidth = 1.dp.toPx()
                    )
                }
                repeat(numDays) {
                    drawLine(
                        dividerColor,
                        start = Offset((it) * dayWidth.toPx(), 0f),
                        end = Offset((it) * dayWidth.toPx(), size.height),
                        strokeWidth = 1.dp.toPx()
                    )
                }
            }
    ) { measureables, constraints ->
        val height = hourHeight.roundToPx() * 16
        val width = dayWidth.roundToPx() * numDays
        val placeablesWithEvents = measureables.map { measurable ->
            val splitEvent = measurable.parentData as PositionedEvent
            val eventDurationMinutes = ChronoUnit.MINUTES.between(splitEvent.start, splitEvent.end)
            val eventHeight = ((eventDurationMinutes / 60f) * hourHeight.toPx()).roundToInt()
            val placeable = measurable.measure(constraints.copy(minWidth = dayWidth.roundToPx(), maxWidth = dayWidth.roundToPx(), minHeight = eventHeight, maxHeight = eventHeight))
            Pair(placeable, splitEvent)
        }
        layout(width, height) {
            placeablesWithEvents.forEach { (placeable, splitEvent) ->
                val eventOffsetMinutes = ChronoUnit.MINUTES.between(LocalTime.of(6,0), splitEvent.start)
                val eventY = ((eventOffsetMinutes / 60f) * hourHeight.toPx()).roundToInt()
                val eventOffsetDays = ChronoUnit.DAYS.between(minDate, splitEvent.date).toInt()
                val eventX = eventOffsetDays * dayWidth.roundToPx()
                placeable.place(eventX, eventY)
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun SchedulePreview() {
    WeekScheduleTheme {
        Schedule(sampleEvents)
    }
}

class EventDataModifier(
    val positionedEvent: PositionedEvent
) : ParentDataModifier {
    override fun Density.modifyParentData(parentData: Any?) = positionedEvent
}

private fun Modifier.eventData(positionedEvent: PositionedEvent) = this.then(EventDataModifier(positionedEvent))

@Composable
fun BasicDayHeader(
    day: LocalDate,
    modifier: Modifier = Modifier
) {
    Text(
        text = day.format(DayFormatter),
        textAlign = TextAlign.Center,
        modifier = modifier
            .fillMaxWidth()
            .padding(4.dp)
    )
}

@Composable
fun ScheduleHeader(
    minDate: LocalDate,
    maxDate: LocalDate,
    dayWidth: Dp,
    modifier: Modifier = Modifier,
    dayHeader: @Composable (day: LocalDate) -> Unit = { BasicDayHeader(day = it) }
) {
    val numDays = ChronoUnit.DAYS.between(minDate, maxDate).toInt() + 1
    val dividerColor = if (MaterialTheme.colors.isLight) Color.LightGray else Color.DarkGray
    Row(
        modifier = modifier
            .drawBehind {
                repeat(numDays) {
                    drawLine(
                        dividerColor,
                        start = Offset((it) * dayWidth.toPx(), 0f),
                        end = Offset((it) * dayWidth.toPx(), size.height),
                        strokeWidth = 1.dp.toPx()
                    )
                }
            }
    ) {
        val numDays = ChronoUnit.DAYS.between(minDate, maxDate).toInt() + 1
        repeat(numDays) { i ->
            Box(modifier = Modifier.width(dayWidth)) {
                dayHeader(minDate.plusDays(i.toLong()))
            }
        }
    }
}

private fun splitEvents(events: List<Event>): List<PositionedEvent> {
    return events
        .map { event ->
            val startDate = event.start.toLocalDate()
            val endDate = event.start.toLocalDate()
            if (startDate == endDate) {
                listOf(PositionedEvent(event, SplitType.None, event.start.toLocalDate(), event.start.toLocalTime(), event.end.toLocalTime()))
            } else {
                val days = ChronoUnit.DAYS.between(startDate, endDate)
                val splitEvents = mutableListOf<PositionedEvent>()
                for (i in 0..days) {
                    val date = startDate.plusDays(i)
                    splitEvents += PositionedEvent(
                        event,
                        splitType = if (date == startDate) SplitType.End else if (date == endDate) SplitType.Start else SplitType.Both,
                        date = date,
                        start = if (date == startDate) event.start.toLocalTime() else LocalTime.MIN,
                        end = if (date == endDate) event.end.toLocalTime() else LocalTime.MAX
                    )
                }
                splitEvents
            }
        }
        .flatten()
}

@Composable
fun Schedule(
    events: List<Event>,
    modifier: Modifier = Modifier,
    eventContent: @Composable (positionedEvent: PositionedEvent) -> Unit = { BasicEvent(positionedEvent = it) },
    dayHeader: @Composable (day: LocalDate) -> Unit = { BasicDayHeader(day = it) },
    minDate: LocalDate = events.minByOrNull(Event::start)!!.start.toLocalDate(),
    maxDate: LocalDate = events.maxByOrNull(Event::end)!!.end.toLocalDate()
) {
    val dayWidth = 256.dp
    val hourHeight = 64.dp
    val verticalScrollState = rememberScrollState()
    val horizontalScrollState = rememberScrollState()
    var sidebarWidth by remember { mutableStateOf(0) }
    Column(modifier = modifier) {
        ScheduleHeader(
            minDate = minDate,
            maxDate = maxDate,
            dayWidth = dayWidth,
            dayHeader = dayHeader,
            modifier = Modifier
                .padding(start = with(LocalDensity.current) { sidebarWidth.toDp() })
                .horizontalScroll(horizontalScrollState)
        )
        Row(modifier = Modifier.weight(1f)) {
            ScheduleSidebar(
                hourHeight = hourHeight,
                modifier = Modifier
                    .verticalScroll(verticalScrollState)
                    .onGloballyPositioned { sidebarWidth = it.size.width }
            )
            BasicSchedule(
                events = events,
                eventContent = eventContent,
                minDate = minDate,
                maxDate = maxDate,
                dayWidth = dayWidth,
                hourHeight = hourHeight,
                modifier = Modifier
                    .verticalScroll(verticalScrollState)
                    .horizontalScroll(horizontalScrollState)
            )
        }
    }
}

@Composable
fun BasicSidebarLabel(
    time: LocalTime,
    modifier: Modifier = Modifier
) {
    Text(
        text = time.format(HourFormatter),
        modifier = modifier
            .fillMaxHeight()
            .padding(4.dp)
    )
}

@Composable
fun ScheduleSidebar(
    hourHeight: Dp,
    modifier: Modifier = Modifier,
    label: @Composable (time: LocalTime) -> Unit = { BasicSidebarLabel(time = it) }
) {
    val dividerColor = if (MaterialTheme.colors.isLight) Color.LightGray else Color.DarkGray
    Column(
        modifier = modifier
            .drawBehind {
                repeat(16) {
                    drawLine(
                        dividerColor,
                        start = Offset(0f, (it) * hourHeight.toPx()),
                        end = Offset(size.width, (it) * hourHeight.toPx()),
                        strokeWidth = 1.dp.toPx()
                    )
                }
            }
    ) {
        val startTime = LocalTime.of(6,0)
        repeat(16) { i ->
            Box(modifier = Modifier.height(hourHeight)) {
                label(startTime.plusHours(i.toLong()))
            }
        }
    }
}