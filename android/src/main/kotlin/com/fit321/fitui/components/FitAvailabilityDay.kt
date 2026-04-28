package com.fit321.fitui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.fit321.fitui.theme.LocalFitTheme
import com.fit321.fitui.tokens.FitColors
import com.fit321.fitui.tokens.FitFont
import com.fit321.fitui.tokens.FitRadius
import com.fit321.fitui.tokens.FitSize
import com.fit321.fitui.tokens.FitSpacing

// ============================================================================
// FitAvailabilityDay — coach availability row for one day-of-week.
// Mirrors Swift FitAvailabilityDay. See `docs/components.md`
// § FitAvailabilityDay.
// ============================================================================

enum class FitDayOfWeek(val label: String) {
    Monday("Monday"),
    Tuesday("Tuesday"),
    Wednesday("Wednesday"),
    Thursday("Thursday"),
    Friday("Friday"),
    Saturday("Saturday"),
    Sunday("Sunday")
}

data class FitAvailabilityInterval(val start: String, val end: String)

@Composable
fun FitAvailabilityDay(
    day: FitDayOfWeek,
    isActive: Boolean,
    onActiveChange: (Boolean) -> Unit,
    intervals: List<FitAvailabilityInterval>,
    onAddInterval: (() -> Unit)? = null,
    onRemoveInterval: ((Int) -> Unit)? = null,
    onIntervalEdit: ((Int) -> Unit)? = null,
    validationError: String? = null,
    modifier: Modifier = Modifier
) {
    val theme = LocalFitTheme.current
    val leftIndent = FitSize.checkboxSize + FitSpacing.sp3

    Column(
        modifier = modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(FitSpacing.sp3)
    ) {
        // Header
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(FitSpacing.sp3)
        ) {
            FitCheckbox(
                checked = isActive,
                onCheckedChange = onActiveChange
            )
            Text(
                day.label,
                style = FitFont.body1.copy(fontWeight = FontWeight.Medium),
                color = if (isActive) theme.textPrimary else theme.textTertiary
            )
        }

        if (isActive) {
            Column(
                modifier = Modifier.padding(start = leftIndent),
                verticalArrangement = Arrangement.spacedBy(FitSpacing.sp2)
            ) {
                intervals.forEachIndexed { idx, interval ->
                    IntervalRow(
                        interval = interval,
                        index = idx,
                        onEdit = onIntervalEdit,
                        onRemove = onRemoveInterval
                    )
                }
                if (onAddInterval != null) {
                    Row(
                        modifier = Modifier.clickable(onClick = onAddInterval),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(FitSpacing.sp2)
                    ) {
                        Icon(
                            Icons.Default.Add, null,
                            tint = FitColors.brandPrimary,
                            modifier = Modifier.size(14.dp)
                        )
                        Text("Add interval", style = FitFont.body2, color = FitColors.brandPrimary)
                    }
                }
            }
        }

        if (validationError != null) {
            Text(
                validationError,
                style = FitFont.footnote,
                color = FitColors.Red.r400,
                modifier = Modifier.padding(start = leftIndent)
            )
        }
    }
}

@Composable
private fun IntervalRow(
    interval: FitAvailabilityInterval,
    index: Int,
    onEdit: ((Int) -> Unit)?,
    onRemove: ((Int) -> Unit)?
) {
    val theme = LocalFitTheme.current
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(FitSpacing.sp2)
    ) {
        TimeInputBox(value = interval.start, onClick = { onEdit?.invoke(index) })
        Text("to", style = FitFont.body2, color = theme.textSecondary)
        TimeInputBox(value = interval.end, onClick = { onEdit?.invoke(index) })

        Spacer(Modifier.weight(1f))

        if (onRemove != null) {
            Box(
                modifier = Modifier
                    .size(28.dp)
                    .clip(CircleShape)
                    .background(FitColors.error.copy(alpha = 0.10f))
                    .clickable { onRemove(index) },
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    Icons.Default.Close, null,
                    tint = FitColors.error,
                    modifier = Modifier.size(12.dp)
                )
            }
        }
    }
}

@Composable
private fun TimeInputBox(value: String, onClick: () -> Unit) {
    val theme = LocalFitTheme.current
    Box(
        modifier = Modifier
            .size(56.dp)
            .clip(RoundedCornerShape(FitRadius.input))
            .background(theme.surfaceLow)
            .clickable(onClick = onClick),
        contentAlignment = Alignment.Center
    ) {
        Text(
            value,
            style = FitFont.body2.copy(fontWeight = FontWeight.Medium),
            color = theme.textPrimary
        )
    }
}
