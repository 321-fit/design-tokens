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
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.fit321.fitui.tokens.FitColors
import com.fit321.fitui.v3.theme.LocalFitV3Palette
import com.fit321.fitui.v3.tokens.FitV3Geometry
import com.fit321.fitui.v3.tokens.FitV3Type

enum class FitActionCircleStyle { Outline, Primary, Danger, Ask }

enum class FitActionBadgeTone { Money, Review, Calm, Unread }

enum class FitNeedsTone { Money, Question, Review, Waiting }

@Composable
fun FitActionCircleRow(
    modifier: Modifier = Modifier,
    /** Wider where a short row would otherwise spread three circles across the screen. */
    gap: Dp = 6.dp,
    content: @Composable RowScope.() -> Unit,
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        horizontalArrangement = Arrangement.spacedBy(gap),
        verticalAlignment = Alignment.Top,
        content = content,
    )
}

/**
 * One of the things a coach does on this screen. The row splits evenly between them —
 * `.fit-action-circle { flex: 1 }` — so the share is taken here rather than asked for at every
 * call site: a row that forgot it bunched its circles against the left edge.
 */
@Composable
fun RowScope.FitActionCircle(
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
            .weight(1f)
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
                FitCountBadge(
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

/** The count pill a circle wears — shared by the action circles and the header's own. */
@Composable
fun FitCountBadge(
    text: String,
    modifier: Modifier = Modifier,
    tone: FitActionBadgeTone = FitActionBadgeTone.Money,
) {
    val palette = LocalFitV3Palette.current
    val background = when (tone) {
        FitActionBadgeTone.Money -> palette.textError
        FitActionBadgeTone.Review -> palette.perimeterAttention
        FitActionBadgeTone.Calm -> palette.raised
        FitActionBadgeTone.Unread -> palette.unreadFill
    }
    val ink = when (tone) {
        FitActionBadgeTone.Calm -> palette.textSecondary
        FitActionBadgeTone.Review -> palette.badgeReviewInk
        FitActionBadgeTone.Money -> FitColors.Gray.white
        FitActionBadgeTone.Unread -> palette.unreadInk
    }
    Box(
        modifier = modifier
            .defaultMinSize(minWidth = FitV3Geometry.actionCircleBadge)
            .height(FitV3Geometry.actionCircleBadge)
            .background(background, CircleShape)
            .padding(horizontal = 5.dp),
        contentAlignment = Alignment.Center,
    ) {
        Text(text = text, style = FitV3Type.countBadge, color = ink, maxLines = 1)
    }
}

@Composable
fun FitNeedsRow(
    modifier: Modifier = Modifier,
    /**
     * A short, fixed set of chips under a centred hero — `.cd-wd-chips` is
     * `justify-content: center`. Centring and scrolling cannot both be true, so a centred row
     * does not scroll: it is only ever used where the chips are known to fit.
     */
    centered: Boolean = false,
    content: @Composable RowScope.() -> Unit,
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .then(if (centered) Modifier else Modifier.horizontalScroll(rememberScrollState()))
            .padding(start = FitV3Geometry.screenInset, end = FitV3Geometry.screenInset, top = 4.dp),
        horizontalArrangement = if (centered) {
            Arrangement.spacedBy(8.dp, Alignment.CenterHorizontally)
        } else {
            Arrangement.spacedBy(8.dp)
        },
        verticalAlignment = Alignment.CenterVertically,
        content = content,
    )
}

@Composable
fun FitNeedsChip(
    text: String,
    modifier: Modifier = Modifier,
    /** Null for a chip that names a filter rather than a thing waiting — no dot. */
    tone: FitNeedsTone? = FitNeedsTone.Waiting,
    /**
     * The one chip in a row that is currently chosen, drawn inverted. A filter row and a
     * needs row are the same chip: one is a set of doors, the other a set of choices, and
     * only the chosen one needs saying.
     */
    selected: Boolean = false,
    onClick: () -> Unit = {},
) {
    val palette = LocalFitV3Palette.current
    val shape = RoundedCornerShape(FitV3Geometry.chipRadius)
    val dot = when (tone) {
        FitNeedsTone.Money -> palette.textError
        FitNeedsTone.Question -> FitColors.brandPrimary
        FitNeedsTone.Review -> FitColors.Yellow.y400
        FitNeedsTone.Waiting -> FitColors.Gray.g500
        null -> Color.Transparent
    }
    Row(
        modifier = modifier
            .then(
                if (selected) {
                    Modifier.background(palette.textPrimary, shape)
                } else {
                    Modifier.fitV3Surface(shape)
                },
            )
            .clip(shape)
            .clickable { onClick() }
            .padding(horizontal = FitV3Geometry.chipPaddingX, vertical = FitV3Geometry.chipPaddingY),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        if (tone != null) {
            Box(
                modifier = Modifier
                    .size(FitV3Geometry.chipDot)
                    .background(dot, CircleShape),
            )
        }
        Text(
            text = text,
            style = FitV3Type.chip,
            // The same ink the CTA uses: both sit on the brightest fill the look has.
            color = if (selected) palette.ctaLabel else palette.textPrimary,
            maxLines = 1,
        )
    }
}
