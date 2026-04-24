package com.fit321.fitui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.fit321.fitui.theme.LocalFitTheme
import com.fit321.fitui.tokens.FitColors
import com.fit321.fitui.tokens.FitFont
import com.fit321.fitui.tokens.FitRadius
import com.fit321.fitui.tokens.FitSpacing
import java.util.Calendar
import java.util.Locale

// ============================================================================
// FitDayStrip — horizontal day chips with dots
// ============================================================================

enum class FitDayEventType { Personal, Group, External }

@Composable
fun FitDayStrip(
    year: Int,
    month: Int,         // 1-12
    days: Int,
    selectedDay: Int,
    onDaySelect: (Int) -> Unit,
    todayDay: Int? = null,
    events: Map<Int, Set<FitDayEventType>> = emptyMap(),
    modifier: Modifier = Modifier
) {
    val theme = LocalFitTheme.current
    LazyRow(
        modifier = modifier.padding(horizontal = FitSpacing.sp4),
        horizontalArrangement = Arrangement.Start
    ) {
        items(days) { index ->
            val day = index + 1
            val isSelected = selectedDay == day
            val isToday = todayDay == day
            val weekday = weekdayAbbrev(year, month, day)

            Column(
                modifier = Modifier
                    .size(50.dp, 62.dp)
                    .clip(RoundedCornerShape(FitRadius.lg))
                    .then(
                        if (isSelected)
                            Modifier.background(FitColors.selectionGradient, RoundedCornerShape(FitRadius.lg))
                                .border(1.dp, FitColors.selectionBorder, RoundedCornerShape(FitRadius.lg))
                        else Modifier
                    )
                    .clickable { onDaySelect(day) },
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Text(
                    weekday.uppercase(),
                    style = FitFont.captionMicro,
                    color = if (isSelected) theme.textPrimary else theme.textTertiary
                )
                Spacer(Modifier.height(FitSpacing.sp1))
                Text(
                    "$day",
                    style = FitFont.body2.copy(
                        fontSize = 16.sp,
                        fontWeight = if (isSelected) FontWeight.SemiBold else FontWeight.Medium
                    ),
                    color = when {
                        isSelected -> theme.textPrimary
                        isToday -> FitColors.brandPrimary
                        else -> theme.textSecondary
                    }
                )
                Spacer(Modifier.height(2.dp))
                val types = events[day] ?: emptySet()
                Row(horizontalArrangement = Arrangement.spacedBy(3.dp)) {
                    if (FitDayEventType.Personal in types) Dot(FitColors.Teal.t500)
                    if (FitDayEventType.Group    in types) Dot(FitColors.brandPrimary)
                    if (FitDayEventType.External in types) Dot(theme.textTertiary)
                }
            }
        }
    }
}

@Composable
private fun Dot(color: Color) {
    Box(modifier = Modifier.size(4.dp).clip(CircleShape).background(color))
}

private fun weekdayAbbrev(year: Int, month: Int, day: Int): String {
    val cal = Calendar.getInstance()
    cal.set(year, month - 1, day)
    return when (cal.get(Calendar.DAY_OF_WEEK)) {
        Calendar.SUNDAY -> "Sun"
        Calendar.MONDAY -> "Mon"
        Calendar.TUESDAY -> "Tue"
        Calendar.WEDNESDAY -> "Wed"
        Calendar.THURSDAY -> "Thu"
        Calendar.FRIDAY -> "Fri"
        else -> "Sat"
    }
}

// ============================================================================
// FitCalEvent — event block on timeline
// ============================================================================

enum class FitCalEventType { Personal, Group, External }
enum class FitCalEventStatus { Planned, Request, Awaiting, Review, Missed, Finished }

@Composable
fun FitCalEvent(
    title: String,
    time: String,
    type: FitCalEventType,
    status: FitCalEventStatus = FitCalEventStatus.Planned,
    isTiny: Boolean = false,
    onTap: () -> Unit = {},
    modifier: Modifier = Modifier
) {
    val theme = LocalFitTheme.current
    val bg = when {
        status == FitCalEventStatus.Request || status == FitCalEventStatus.Review ->
            FitColors.Yellow.y600.copy(alpha = 0.10f)
        status == FitCalEventStatus.Missed ->
            FitColors.error.copy(alpha = 0.10f)
        type == FitCalEventType.External ->
            theme.surfaceHigher
        else -> theme.surfaceHigh
    }
    val leftAccent = when (type) {
        FitCalEventType.Personal -> FitColors.Teal.t500
        FitCalEventType.Group    -> FitColors.brandPrimary
        FitCalEventType.External -> theme.textTertiary
    }
    val pillText: String? = when (status) {
        FitCalEventStatus.Request -> "Request"
        FitCalEventStatus.Review -> "Review"
        FitCalEventStatus.Awaiting -> "Awaiting"
        FitCalEventStatus.Missed -> "Missed"
        else -> null
    }
    val pillStatus: FitCalEventPillStatus? = when (status) {
        FitCalEventStatus.Request -> FitCalEventPillStatus.Request
        FitCalEventStatus.Review -> FitCalEventPillStatus.Review
        FitCalEventStatus.Awaiting -> FitCalEventPillStatus.Awaiting
        FitCalEventStatus.Missed -> FitCalEventPillStatus.Missed
        else -> null
    }

    Row(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(FitRadius.md))
            .background(bg)
            .clickable(onClick = onTap)
            .padding(horizontal = if (isTiny) 8.dp else 0.dp)
            .height(IntrinsicSize.Min)
    ) {
        // Left accent stripe
        Box(
            modifier = Modifier
                .width(3.dp)
                .fillMaxHeight()
                .background(leftAccent)
        )
        Column(
            modifier = Modifier
                .padding(horizontal = FitSpacing.sp3, vertical = if (isTiny) 2.dp else FitSpacing.sp2)
                .weight(1f),
            verticalArrangement = Arrangement.spacedBy(2.dp)
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                    title,
                    style = FitFont.body2.copy(
                        fontSize = if (isTiny) 10.sp else 12.sp,
                        fontWeight = FontWeight.Medium
                    ),
                    color = if (type == FitCalEventType.External) theme.textSecondary else theme.textPrimary,
                    modifier = Modifier.weight(1f),
                    maxLines = 1
                )
                if (pillText != null && pillStatus != null) {
                    FitCalEventPill(pillText, pillStatus)
                }
            }
            if (!isTiny) {
                Text(time, style = FitFont.caption, color = theme.textSecondary)
            }
        }
    }
}

// ============================================================================
// FitTimeline — 24h vertical grid with absolutely positioned events
// ============================================================================

data class FitTimelineEvent(
    val startMinute: Int,
    val durationMin: Int,
    val view: @Composable () -> Unit
)

@Composable
fun FitTimeline(
    events: List<FitTimelineEvent>,
    currentMinute: Int? = null,
    hourHeight: androidx.compose.ui.unit.Dp = 96.dp,
    modifier: Modifier = Modifier
) {
    val theme = LocalFitTheme.current
    Box(modifier = modifier.verticalScroll(rememberScrollState())) {
        // Hour grid
        Column(Modifier.fillMaxWidth()) {
            (0 until 24).forEach { hour ->
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(hourHeight),
                    verticalAlignment = Alignment.Top
                ) {
                    Text(
                        "%02d:00".format(hour),
                        style = FitFont.captionMicro,
                        color = theme.textTertiary,
                        modifier = Modifier.width(40.dp).padding(top = 4.dp, end = 8.dp)
                    )
                    Box(modifier = Modifier.fillMaxSize().background(theme.divider.copy(alpha = 0.3f)))
                }
            }
        }
        // Events (absolutely positioned via offset)
        events.forEach { event ->
            val yOffset = hourHeight * (event.startMinute / 60f)
            val height = hourHeight * (event.durationMin / 60f)
            Box(
                modifier = Modifier
                    .padding(start = 48.dp, end = 8.dp)
                    .offset(y = yOffset)
                    .heightIn(min = 24.dp, max = height)
            ) { event.view() }
        }
        // Now line
        if (currentMinute != null) {
            val y = hourHeight * (currentMinute / 60f)
            Row(
                modifier = Modifier.offset(y = y - 1.dp).padding(start = 44.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(Modifier.size(8.dp).clip(CircleShape).background(FitColors.brandPrimary))
                Box(Modifier.height(2.dp).fillMaxWidth().background(FitColors.brandPrimary))
            }
        }
    }
}

// Helper for Int → sp
private fun Int.sp() = androidx.compose.ui.unit.TextUnit(this.toFloat(), androidx.compose.ui.unit.TextUnitType.Sp)
