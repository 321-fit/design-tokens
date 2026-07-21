package com.fit321.fitui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalDensity
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
// Mode = nav (calendar timeline) | select (invite / group preview).
// FitDayButton is exposed standalone for non-strip flows.
// ============================================================================

enum class FitDayEventType { Personal, Group, External }

enum class FitDayStripMode { Nav, Select }

@Composable
fun FitDayStrip(
    year: Int,
    month: Int,         // 1-12
    days: Int,
    selectedDay: Int,
    onDaySelect: (Int) -> Unit,
    todayDay: Int? = null,
    events: Map<Int, Set<FitDayEventType>> = emptyMap(),
    mode: FitDayStripMode = FitDayStripMode.Nav,
    modifier: Modifier = Modifier
) {
    LazyRow(
        modifier = modifier.padding(horizontal = FitSpacing.sp4),
        horizontalArrangement = Arrangement.Start
    ) {
        items(days) { index ->
            val day = index + 1
            FitDayButton(
                year = year,
                month = month,
                day = day,
                isSelected = selectedDay == day,
                isToday = todayDay == day,
                events = events[day] ?: emptySet(),
                onClick = { onDaySelect(day) }
            )
        }
    }
}

/**
 * Standalone day chip — same 50×62 visual as a strip cell, but composable
 * outside `FitDayStrip` (invite flows, group session schedule preview).
 * Mirrors Swift `FitDayButton`.
 */
@Composable
fun FitDayButton(
    year: Int,
    month: Int,
    day: Int,
    isSelected: Boolean,
    isToday: Boolean = false,
    events: Set<FitDayEventType> = emptySet(),
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val theme = LocalFitTheme.current
    val weekday = weekdayAbbrev(year, month, day)

    Column(
        modifier = modifier
            .size(50.dp, 62.dp)
            .clip(RoundedCornerShape(FitRadius.lg))
            .then(
                if (isSelected)
                    Modifier.background(FitColors.selectionGradient, RoundedCornerShape(FitRadius.lg))
                        .border(1.dp, FitColors.selectionBorder, RoundedCornerShape(FitRadius.lg))
                else Modifier
            )
            .clickable(onClick = onClick),
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
        // Fixed height so the day number doesn't shift up/down as event dots load
        // in (the dots arrive asynchronously when a new month's data is fetched).
        Row(
            modifier = Modifier.height(4.dp),
            horizontalArrangement = Arrangement.spacedBy(3.dp),
        ) {
            if (FitDayEventType.Personal in events) Dot(FitColors.Teal.t500)
            if (FitDayEventType.Group    in events) Dot(FitColors.brandPrimary)
            if (FitDayEventType.External in events) Dot(theme.textTertiary)
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
// FitCalEvent — event block on timeline.
//
// Adaptive 3-tier layout based on tile height + optional recipient/location
// fields + cross-role variant (user's own event from the OTHER role).
// 3pt hairline gap reserved at the bottom so back-to-back events don't merge.
// ============================================================================

sealed class FitCalEventType {
    object Personal : FitCalEventType()
    object Group    : FitCalEventType()
    object External : FitCalEventType()
    data class CrossRole(val role: FitRole) : FitCalEventType()
    object Custom   : FitCalEventType()   // coach's own calendar block — stateless
}

enum class FitCalEventStatus { Planned, Request, Awaiting, Review, Missed, Finished }

enum class FitCalEventTier {
    Tiny, Compact, Standard;

    companion object {
        fun from(heightDp: androidx.compose.ui.unit.Dp): FitCalEventTier = when {
            heightDp <= 30.dp -> Tiny
            heightDp <= 45.dp -> Compact
            else              -> Standard
        }
    }
}

@Composable
fun FitCalEvent(
    title: String,
    time: String,
    type: FitCalEventType,
    height: androidx.compose.ui.unit.Dp,
    recipient: String? = null,
    location: String? = null,
    status: FitCalEventStatus = FitCalEventStatus.Planned,
    overlapped: Boolean = false,
    onTap: () -> Unit = {},
    modifier: Modifier = Modifier
) {
    val theme = LocalFitTheme.current
    val tier = FitCalEventTier.from(height)
    val isCrossRole = type is FitCalEventType.CrossRole

    val cardBg: Color = when {
        status == FitCalEventStatus.Request || status == FitCalEventStatus.Review ->
            FitColors.Yellow.y600.copy(alpha = 0.10f)
        status == FitCalEventStatus.Missed ->
            FitColors.error.copy(alpha = 0.10f)
        type is FitCalEventType.External   -> theme.surfaceHigher
        type is FitCalEventType.Group      -> FitColors.brandPrimary.copy(alpha = 0.12f)
        else                               -> theme.surfaceHigh
    }
    val leftAccent: Color = when (type) {
        is FitCalEventType.Personal  -> FitColors.Teal.t500
        is FitCalEventType.Group     -> FitColors.brandPrimary
        is FitCalEventType.External  -> theme.textTertiary
        is FitCalEventType.CrossRole -> theme.textTertiary
        is FitCalEventType.Custom    -> theme.textTertiary   // solid; cross-role uses dashed
    }
    val borderColor: Color? = when (status) {
        FitCalEventStatus.Request, FitCalEventStatus.Review -> FitColors.Yellow.y600
        FitCalEventStatus.Awaiting -> theme.textTertiary
        FitCalEventStatus.Missed   -> FitColors.error
        else -> null
    }
    val outerOpacity: Float = when {
        status == FitCalEventStatus.Finished -> 0.5f
        type is FitCalEventType.External     -> 0.7f
        isCrossRole                          -> 0.75f
        else                                 -> 1.0f
    }

    // Outer container keeps full inline height; visible card lives inside with
    // padding(.bottom = 3) reserving the hairline gap.
    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(height)
            .alpha(outerOpacity)
            .clickable(onClick = onTap)
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight()
                .padding(bottom = 3.dp)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxSize()
                    .clip(RoundedCornerShape(FitRadius.md))
                    .background(cardBg)
                    .then(
                        if (borderColor != null)
                            Modifier.border(1.dp, borderColor, RoundedCornerShape(FitRadius.md))
                        else Modifier
                    )
            ) {
                // Left stripe
                if (isCrossRole) {
                    DashedVerticalStripe(theme.textTertiary)
                } else {
                    Box(
                        modifier = Modifier
                            .width(3.dp)
                            .fillMaxHeight()
                            .background(leftAccent)
                    )
                }
                FitCalEventContent(
                    title = title,
                    recipient = recipient,
                    time = time,
                    location = location,
                    type = type,
                    status = status,
                    tier = tier,
                    titleColor = theme.textPrimary,
                    secondary = theme.textSecondary,
                    tertiary = theme.textTertiary
                )
            }
        }

        // Cross-role role tag — bottom-right corner of OUTER, inset above the gap.
        if (type is FitCalEventType.CrossRole && tier != FitCalEventTier.Tiny) {
            Box(
                modifier = Modifier
                    .align(Alignment.BottomEnd)
                    .padding(end = 8.dp, bottom = 9.dp)   // 3 (gap) + 6 (visual inset)
            ) {
                FitRoleTag(role = type.role)
            }
        }

        // Overlap overlay — muted red-brown gradient + corner dot.
        // Sits on top of the regular type/status visual to flag conflicts
        // with external events that couldn't be prevented at create time.
        if (overlapped) {
            // Gradient overlay
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .fillMaxHeight()
                    .padding(bottom = 3.dp)
                    .clip(RoundedCornerShape(FitRadius.md))
                    .background(
                        androidx.compose.ui.graphics.Brush.linearGradient(
                            colors = listOf(
                                Color(red = 112, green = 89, blue = 89).copy(alpha = 0.35f),
                                Color(red = 187, green = 127, blue = 127).copy(alpha = 0.35f)
                            )
                        )
                    )
            )
            // Corner dot
            Box(
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .padding(top = 6.dp, end = 8.dp)
                    .size(8.dp)
                    .clip(CircleShape)
                    .background(
                        androidx.compose.ui.graphics.Brush.linearGradient(
                            colors = listOf(
                                Color(red = 187, green = 127, blue = 127),
                                Color(red = 112, green = 89, blue = 89)
                            )
                        )
                    )
                    .border(1.5.dp, theme.screenBg, CircleShape)
            )
        }
    }
}

@Composable
private fun RowScope.FitCalEventContent(
    title: String,
    recipient: String?,
    time: String,
    location: String?,
    type: FitCalEventType,
    status: FitCalEventStatus,
    tier: FitCalEventTier,
    titleColor: Color,
    secondary: Color,
    tertiary: Color
) {
    // Cross-role tiles hide status pill — actions belong to the other role.
    // Custom events are stateless — no 6-status lifecycle.
    val suppressPill = type is FitCalEventType.CrossRole || type is FitCalEventType.Custom
    val pillText: String? = if (suppressPill) null else when (status) {
        FitCalEventStatus.Request  -> "Request"
        FitCalEventStatus.Review   -> "Review"
        FitCalEventStatus.Awaiting -> "Awaiting"
        FitCalEventStatus.Missed   -> "Missed"
        else -> null
    }
    val pillStatus: FitCalEventPillStatus? = if (suppressPill) null else when (status) {
        FitCalEventStatus.Request  -> FitCalEventPillStatus.Request
        FitCalEventStatus.Review   -> FitCalEventPillStatus.Review
        FitCalEventStatus.Awaiting -> FitCalEventPillStatus.Awaiting
        FitCalEventStatus.Missed   -> FitCalEventPillStatus.Missed
        else -> null
    }
    val metaText = recipient?.let { "$it · $time" } ?: time

    Column(
        modifier = Modifier
            .padding(horizontal = FitSpacing.sp3, vertical = if (tier == FitCalEventTier.Tiny) 4.dp else FitSpacing.sp2)
            .padding(start = 4.dp)
            .weight(1f),
        verticalArrangement = Arrangement.spacedBy(2.dp)
    ) {
        when (tier) {
            FitCalEventTier.Tiny -> {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        title,
                        style = FitFont.body2.copy(fontSize = 10.sp, fontWeight = FontWeight.Medium),
                        color = titleColor,
                        maxLines = 1,
                        modifier = Modifier.weight(1f, fill = false)
                    )
                    Text(" · ", style = FitFont.caption, color = tertiary, fontSize = 10.sp)
                    Text(time, style = FitFont.caption, color = tertiary, fontSize = 10.sp, maxLines = 1)
                    if (pillText != null && pillStatus != null) {
                        Spacer(Modifier.weight(1f))
                        FitCalEventPill(pillText, pillStatus)
                    }
                }
            }
            FitCalEventTier.Compact, FitCalEventTier.Standard -> {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        title,
                        style = FitFont.body2.copy(fontSize = 12.sp, fontWeight = FontWeight.Medium),
                        color = titleColor,
                        modifier = Modifier.weight(1f),
                        maxLines = 1
                    )
                    if (pillText != null && pillStatus != null) {
                        FitCalEventPill(pillText, pillStatus)
                    }
                }
                Text(metaText, style = FitFont.caption, color = secondary, maxLines = 1)
                if (tier == FitCalEventTier.Standard && location != null) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Filled.LocationOn,
                            contentDescription = null,
                            tint = tertiary,
                            modifier = Modifier.size(10.dp)
                        )
                        Text(location, style = FitFont.caption, color = tertiary, fontSize = 11.sp, maxLines = 1)
                    }
                }
            }
        }
    }
}

@Composable
private fun DashedVerticalStripe(color: Color) {
    androidx.compose.foundation.Canvas(
        modifier = Modifier
            .width(3.dp)
            .fillMaxHeight()
    ) {
        val dash = floatArrayOf(3.dp.toPx(), 3.dp.toPx())
        val effect = androidx.compose.ui.graphics.PathEffect.dashPathEffect(dash, 0f)
        drawLine(
            color = color,
            start = androidx.compose.ui.geometry.Offset(x = size.width / 2, y = 0f),
            end   = androidx.compose.ui.geometry.Offset(x = size.width / 2, y = size.height),
            strokeWidth = 3.dp.toPx(),
            pathEffect = effect
        )
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
    onLongPressEmptyMinute: ((Int) -> Unit)? = null,
    modifier: Modifier = Modifier
) {
    val theme = LocalFitTheme.current
    val density = LocalDensity.current
    val hourHeightPx = with(density) { hourHeight.toPx() }
    val longPressModifier = if (onLongPressEmptyMinute != null) {
        Modifier.pointerInput(hourHeightPx) {
            detectTapGestures(
                onLongPress = { offset ->
                    val minute = ((offset.y / hourHeightPx) * 60f).toInt().coerceIn(0, 24 * 60 - 1)
                    onLongPressEmptyMinute(minute)
                },
            )
        }
    } else Modifier
    Box(modifier = modifier.verticalScroll(rememberScrollState()).then(longPressModifier)) {
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
