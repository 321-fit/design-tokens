package com.fit321.fitui.v3.components

import com.fit321.designtokens.R
import androidx.compose.ui.res.painterResource
import androidx.compose.material3.Icon
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.fit321.fitui.tokens.FitColors
import com.fit321.fitui.v3.theme.LocalFitV3Palette
import com.fit321.fitui.v3.tokens.FitV3Geometry
import com.fit321.fitui.v3.tokens.FitV3Type

/**
 * A setting that is on or off — `profile-drafts.html .cd-st-toggle`.
 *
 * It lives in a row's trailing slot, which is why it has no label of its own: the row already
 * says what it turns on, and a switch that repeated it would be the only control on the screen
 * explaining itself twice.
 */
@Composable
fun FitToggle(
    checked: Boolean,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    onCheckedChange: (Boolean) -> Unit = {},
) {
    val palette = LocalFitV3Palette.current
    val track by animateColorAsState(
        targetValue = if (checked) FitColors.Teal.t500 else palette.raised,
        label = "toggleTrack",
    )
    val knobOffset by animateDpAsState(
        targetValue = if (checked) {
            FitV3Geometry.toggleWidth - FitV3Geometry.toggleKnob - 3.dp
        } else {
            3.dp
        },
        label = "toggleKnob",
    )
    Box(
        modifier = modifier
            .width(FitV3Geometry.toggleWidth)
            .height(FitV3Geometry.toggleHeight)
            .clip(CircleShape)
            .background(track)
            .clickable(enabled = enabled) { onCheckedChange(!checked) },
    ) {
        Box(
            modifier = Modifier
                .offset(x = knobOffset, y = 3.dp)
                .size(FitV3Geometry.toggleKnob)
                .background(FitColors.Gray.white, CircleShape),
        )
    }
}

/**
 * A fact and its number, side by side — `fit-ui.css .fit-kv`.
 *
 * Money screens end on a stack of these: what you receive, the fee, when it arrives. They are
 * not rows you tap, so they carry no plate, no chevron and no press state — only the reading.
 */
@Composable
fun FitKeyValue(
    label: String,
    value: String,
    modifier: Modifier = Modifier,
    /** Muted where the value is a caveat rather than an amount — "1-2 business days". */
    muted: Boolean = false,
) {
    val palette = LocalFitV3Palette.current
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(12.dp),
    ) {
        Text(
            text = label,
            style = FitV3Type.kvLabel,
            color = palette.textSecondary,
            modifier = Modifier.weight(1f),
        )
        Text(
            text = value,
            style = FitV3Type.kvValue,
            color = if (muted) palette.textTertiary else palette.textPrimary,
            textAlign = TextAlign.End,
        )
    }
}

/** The stack [FitKeyValue] rows live in: one surface, hairlines between. */
@Composable
fun FitKeyValueGroup(
    modifier: Modifier = Modifier,
    content: @Composable ColumnScopeMarker.() -> Unit,
) {
    val palette = LocalFitV3Palette.current
    val shape = RoundedCornerShape(FitV3Geometry.fieldRadius)
    Column(
        modifier = modifier
            .fillMaxWidth()
            .background(palette.surface, shape)
            .clip(shape),
    ) {
        ColumnScopeMarker(palette.divider).content()
    }
}

/** Lets [FitKeyValueGroup] draw the hairline between rows without the caller remembering to. */
class ColumnScopeMarker internal constructor(private val divider: Color) {
    private var drawn = false

    @Composable
    fun row(label: String, value: String, muted: Boolean = false) {
        if (drawn) HorizontalDivider(thickness = 1.dp, color = divider)
        drawn = true
        FitKeyValue(label = label, value = value, muted = muted)
    }
}

/**
 * One choice of two or three, each taking an equal share — `fit-ui.css .fit-selection-chip`.
 *
 * Not [FitSegmented]: that one switches between views of the same screen and slides a pill. This
 * one is a form answer — ID type, payout destination, how far a home visit may be — and stays
 * where the finger left it.
 */
@Composable
fun RowScope.FitSelectionChip(
    label: String,
    selected: Boolean,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    onClick: () -> Unit = {},
    leading: (@Composable () -> Unit)? = null,
) {
    val palette = LocalFitV3Palette.current
    val shape = RoundedCornerShape(FitV3Geometry.fieldRadius)
    Row(
        modifier = modifier
            .weight(1f)
            .height(FitV3Geometry.selectionChipHeight)
            .clip(shape)
            .then(
                if (selected) {
                    Modifier
                        .background(palette.selectedFill, shape)
                        .border(1.dp, palette.selectedBorder, shape)
                } else {
                    Modifier.background(palette.surface, shape)
                },
            )
            .clickable(enabled = enabled) { onClick() }
            .padding(horizontal = 12.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(8.dp, Alignment.CenterHorizontally),
    ) {
        if (leading != null) {
            CompositionLocalProvider(LocalContentColor provides palette.textPrimary) { leading() }
        }
        Text(text = label, style = FitV3Type.selectionChip, color = palette.textPrimary)
    }
}

/** The row [FitSelectionChip]s share, so the equal split happens once rather than per screen. */
@Composable
fun FitSelectionRow(
    modifier: Modifier = Modifier,
    content: @Composable RowScope.() -> Unit,
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        content = content,
    )
}

/**
 * A rating as the stars a reader counts — `profile-drafts.html .cd-pr-stars`.
 *
 * Whole stars only: a review is left as a whole number, and a half star would claim a precision
 * the coach never gave.
 */
@Composable
fun FitStars(
    rating: Int,
    modifier: Modifier = Modifier,
    total: Int = 5,
) {
    val palette = LocalFitV3Palette.current
    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.spacedBy(1.dp),
    ) {
        repeat(total) { index ->
            Icon(
                painter = painterResource(
                    if (index < rating) R.drawable.ic_fit_star_filled else R.drawable.ic_fit_star,
                ),
                contentDescription = null,
                tint = if (index < rating) FitColors.Yellow.y400 else palette.divider,
                modifier = Modifier.size(13.dp),
            )
        }
    }
}
