package com.fit321.fitui.v3.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.fit321.fitui.tokens.FitColors
import com.fit321.fitui.v3.theme.LocalFitV3Palette
import com.fit321.fitui.v3.tokens.FitV3Geometry
import com.fit321.fitui.v3.tokens.FitV3Type

enum class FitActionCircleStyle { Outline, Primary, Danger, Ask }

enum class FitActionBadgeTone { Money, Review, Calm }

enum class FitNeedsTone { Money, Question, Review, Waiting }

@Composable
fun FitActionCircleRow(
    modifier: Modifier = Modifier,
    content: @Composable RowScope.() -> Unit,
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        horizontalArrangement = Arrangement.spacedBy(6.dp),
        verticalAlignment = Alignment.Top,
        content = content,
    )
}

@Composable
fun FitActionCircle(
    label: String,
    modifier: Modifier = Modifier,
    style: FitActionCircleStyle = FitActionCircleStyle.Outline,
    badge: String? = null,
    badgeTone: FitActionBadgeTone = FitActionBadgeTone.Calm,
    enabled: Boolean = true,
    onClick: () -> Unit = {},
    icon: @Composable () -> Unit,
) {
    val palette = LocalFitV3Palette.current
    val fill: Brush? = when (style) {
        FitActionCircleStyle.Primary -> Brush.verticalGradient(colorStops = palette.ctaStops.toTypedArray())
        FitActionCircleStyle.Danger -> SolidColor(palette.textError)
        else -> null
    }
    val border = when (style) {
        FitActionCircleStyle.Outline -> palette.circleOutline
        FitActionCircleStyle.Ask -> FitColors.Teal.t500
        else -> null
    }
    val ink = when (style) {
        FitActionCircleStyle.Primary -> palette.ctaLabel
        FitActionCircleStyle.Danger -> FitColors.Gray.white
        FitActionCircleStyle.Ask -> FitColors.Teal.t500
        FitActionCircleStyle.Outline -> palette.textPrimary
    }
    val labelColor = when (style) {
        FitActionCircleStyle.Primary -> palette.actionPrimaryLabel
        FitActionCircleStyle.Danger -> palette.textError
        FitActionCircleStyle.Ask -> FitColors.Teal.t500
        FitActionCircleStyle.Outline -> palette.textPrimary
    }
    Column(
        modifier = modifier
            .alpha(if (enabled) 1f else 0.32f)
            .clickable(enabled = enabled) { onClick() }
            .padding(vertical = 4.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        Box(contentAlignment = Alignment.Center) {
            Box(
                modifier = Modifier
                    .size(FitV3Geometry.actionCircle)
                    .clip(CircleShape)
                    .then(if (fill != null) Modifier.background(fill, CircleShape) else Modifier)
                    .then(if (border != null) Modifier.border(1.5.dp, border, CircleShape) else Modifier),
                contentAlignment = Alignment.Center,
            ) {
                CompositionLocalProvider(LocalContentColor provides ink) { icon() }
            }
            if (badge != null) {
                FitActionBadge(
                    text = badge,
                    tone = badgeTone,
                    modifier = Modifier
                        .align(Alignment.TopEnd)
                        .padding(top = 0.dp),
                )
            }
        }
        Text(
            text = label,
            style = FitV3Type.actionLabel,
            color = labelColor,
            textAlign = TextAlign.Center,
            maxLines = 1,
        )
    }
}

@Composable
private fun FitActionBadge(
    text: String,
    tone: FitActionBadgeTone,
    modifier: Modifier = Modifier,
) {
    val palette = LocalFitV3Palette.current
    val background = when (tone) {
        FitActionBadgeTone.Money -> palette.textError
        FitActionBadgeTone.Review -> palette.perimeterAttention
        FitActionBadgeTone.Calm -> palette.raised
    }
    val ink = when (tone) {
        FitActionBadgeTone.Calm -> palette.textSecondary
        FitActionBadgeTone.Review -> palette.badgeReviewInk
        FitActionBadgeTone.Money -> FitColors.Gray.white
    }
    Box(
        modifier = modifier
            .defaultMinSize(minWidth = FitV3Geometry.actionCircleBadge)
            .height(FitV3Geometry.actionCircleBadge)
            .background(background, CircleShape)
            .padding(horizontal = 5.dp),
        contentAlignment = Alignment.Center,
    ) {
        Text(text = text, style = FitV3Type.rowValueSub, color = ink, maxLines = 1)
    }
}

@Composable
fun FitNeedsRow(
    modifier: Modifier = Modifier,
    content: @Composable RowScope.() -> Unit,
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .horizontalScroll(rememberScrollState())
            .padding(start = FitV3Geometry.screenInset, end = FitV3Geometry.screenInset, top = 4.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalAlignment = Alignment.CenterVertically,
        content = content,
    )
}

@Composable
fun FitNeedsChip(
    text: String,
    modifier: Modifier = Modifier,
    tone: FitNeedsTone = FitNeedsTone.Waiting,
    onClick: () -> Unit = {},
) {
    val palette = LocalFitV3Palette.current
    val shape = RoundedCornerShape(FitV3Geometry.chipRadius)
    val dot = when (tone) {
        FitNeedsTone.Money -> palette.textError
        FitNeedsTone.Question -> FitColors.brandPrimary
        FitNeedsTone.Review -> FitColors.Yellow.y400
        FitNeedsTone.Waiting -> FitColors.Gray.g500
    }
    Row(
        modifier = modifier
            .fitV3Surface(shape)
            .clip(shape)
            .clickable { onClick() }
            .padding(horizontal = FitV3Geometry.chipPaddingX, vertical = FitV3Geometry.chipPaddingY),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        Box(
            modifier = Modifier
                .size(FitV3Geometry.chipDot)
                .background(dot, CircleShape),
        )
        Text(text = text, style = FitV3Type.chip, color = palette.textPrimary, maxLines = 1)
    }
}
