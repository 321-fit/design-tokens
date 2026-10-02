package com.fit321.fitui.v3.components

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.fit321.fitui.tokens.FitColors
import com.fit321.fitui.v3.theme.LocalFitV3Palette
import com.fit321.fitui.v3.tokens.FitV3Geometry
import com.fit321.fitui.v3.tokens.FitV3Type
import androidx.compose.foundation.Canvas

data class FitDayBar(
    val label: String,
    val value: String,
    val note: String? = null,
    val fraction: Float,
    val danger: Boolean = false,
)

@Composable
fun FitDayRing(
    value: String,
    modifier: Modifier = Modifier,
    total: String? = null,
    label: String? = null,
    fraction: Float = 0f,
    size: Dp = 132.dp,
    stroke: Dp = 9.dp,
) {
    val palette = LocalFitV3Palette.current
    val progress by animateFloatAsState(
        targetValue = fraction.coerceIn(0f, 1f),
        animationSpec = tween(durationMillis = 900),
        label = "fitDayRing",
    )
    Box(modifier = modifier.size(size), contentAlignment = Alignment.Center) {
        Canvas(modifier = Modifier.fillMaxSize()) {
            val width = stroke.toPx()
            val inset = width / 2f
            val arcSize = Size(this.size.width - width, this.size.height - width)
            drawArc(
                color = palette.divider,
                startAngle = 0f,
                sweepAngle = 360f,
                useCenter = false,
                topLeft = Offset(inset, inset),
                size = arcSize,
                style = Stroke(width = width),
            )
            if (progress > 0f) {
                drawArc(
                    brush = Brush.linearGradient(colorStops = palette.ringStops.toTypedArray()),
                    startAngle = -90f,
                    sweepAngle = 360f * progress,
                    useCenter = false,
                    topLeft = Offset(inset, inset),
                    size = arcSize,
                    style = Stroke(width = width, cap = StrokeCap.Round),
                )
            }
        }
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Row(verticalAlignment = Alignment.Bottom) {
                Text(
                    text = value,
                    style = FitV3Type.ringValue,
                    color = palette.textPrimary,
                )
                if (total != null) {
                    Text(
                        text = total,
                        style = FitV3Type.ringTotal,
                        color = palette.textTertiary,
                    )
                }
            }
            if (label != null) {
                Text(
                    text = label,
                    style = FitV3Type.statLabel,
                    color = palette.textTertiary,
                    textAlign = TextAlign.Center,
                )
            }
        }
    }
}

@Composable
fun FitDayWidget(
    ringValue: String,
    modifier: Modifier = Modifier,
    ringTotal: String? = null,
    ringLabel: String? = null,
    ringFraction: Float = 0f,
    date: String? = null,
    dateNote: String? = null,
    leftValue: String? = null,
    leftLabel: String? = null,
    rightValue: String? = null,
    rightLabel: String? = null,
    bars: List<FitDayBar> = emptyList(),
    line: String? = null,
    linkLabel: String? = null,
    onLink: (() -> Unit)? = null,
) {
    val palette = LocalFitV3Palette.current
    Column(
        modifier = modifier
            .fillMaxWidth()
            .then(if (onLink != null) Modifier.clickable { onLink() } else Modifier)
            .padding(start = 14.dp, end = 14.dp, top = 16.dp, bottom = 14.dp),
    ) {
        if (date != null) {
            Row(modifier = Modifier.fillMaxWidth().padding(bottom = 10.dp)) {
                Text(
                    text = date,
                    style = FitV3Type.fieldLabel,
                    color = palette.textSecondary,
                    modifier = Modifier.weight(1f),
                )
                if (dateNote != null) {
                    Text(text = dateNote, style = FitV3Type.rowSub, color = palette.textTertiary)
                }
            }
        }
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            FitDaySide(value = leftValue, label = leftLabel, modifier = Modifier.weight(1f))
            FitDayRing(
                value = ringValue,
                total = ringTotal,
                label = ringLabel,
                fraction = ringFraction,
            )
            FitDaySide(value = rightValue, label = rightLabel, modifier = Modifier.weight(1f))
        }
        if (bars.isNotEmpty()) {
            Row(
                modifier = Modifier.fillMaxWidth().padding(top = 18.dp),
                horizontalArrangement = Arrangement.spacedBy(12.dp),
            ) {
                bars.forEach { bar ->
                    FitDayBarCell(bar = bar, modifier = Modifier.weight(1f))
                }
            }
        }
        if (line != null) {
            Text(
                text = line,
                style = FitV3Type.identityContext,
                color = palette.textSecondary,
                modifier = Modifier.padding(top = 16.dp),
            )
        }
        if (linkLabel != null) {
            Text(
                text = linkLabel,
                style = FitV3Type.fieldLabel,
                color = FitColors.Teal.t500,
                modifier = Modifier.padding(top = 10.dp),
            )
        }
    }
}

@Composable
private fun FitDaySide(
    value: String?,
    label: String?,
    modifier: Modifier = Modifier,
) {
    val palette = LocalFitV3Palette.current
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        if (value != null) {
            Text(text = value, style = FitV3Type.compactName, color = palette.textPrimary)
        }
        if (label != null) {
            Text(
                text = label,
                style = FitV3Type.statLabel,
                color = palette.textTertiary,
                textAlign = TextAlign.Center,
            )
        }
    }
}

@Composable
private fun FitDayBarCell(
    bar: FitDayBar,
    modifier: Modifier = Modifier,
) {
    val palette = LocalFitV3Palette.current
    Column(modifier = modifier) {
        Text(
            text = bar.label,
            style = FitV3Type.statLabel,
            color = palette.textTertiary,
            maxLines = 1,
        )
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 6.dp)
                .height(4.dp)
                .clip(RoundedCornerShape(percent = 50))
                .background(palette.divider),
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth(bar.fraction.coerceIn(0f, 1f))
                    .height(4.dp)
                    .clip(RoundedCornerShape(percent = 50))
                    .background(if (bar.danger) palette.textError else FitColors.brandSecondary),
            )
        }
        Row(
            modifier = Modifier.padding(top = 6.dp),
            verticalAlignment = Alignment.Bottom,
            horizontalArrangement = Arrangement.spacedBy(4.dp),
        ) {
            Text(
                text = bar.value,
                style = FitV3Type.rowValueBold,
                color = palette.textPrimary,
                maxLines = 1,
            )
            if (bar.note != null) {
                Text(
                    text = bar.note,
                    style = FitV3Type.rowValueSub,
                    color = palette.textTertiary,
                    maxLines = 1,
                )
            }
        }
    }
}

@Composable
fun FitDayWidgetAnchor(
    ringValue: String,
    modifier: Modifier = Modifier,
    ringTotal: String? = null,
    ringLabel: String? = null,
    ringFraction: Float = 0f,
    date: String? = null,
    dateNote: String? = null,
    leftValue: String? = null,
    leftLabel: String? = null,
    rightValue: String? = null,
    rightLabel: String? = null,
    onClick: (() -> Unit)? = null,
) {
    FitPanel(modifier = modifier) {
        CompositionLocalProvider(LocalContentColor provides LocalFitV3Palette.current.textPrimary) {
            FitDayWidget(
                ringValue = ringValue,
                ringTotal = ringTotal,
                ringLabel = ringLabel,
                ringFraction = ringFraction,
                date = date,
                dateNote = dateNote,
                leftValue = leftValue,
                leftLabel = leftLabel,
                rightValue = rightValue,
                rightLabel = rightLabel,
                onLink = onClick,
            )
        }
    }
}
