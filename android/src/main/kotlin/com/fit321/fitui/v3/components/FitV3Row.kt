package com.fit321.fitui.v3.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.fit321.designtokens.R
import com.fit321.fitui.v3.theme.LocalFitV3Palette
import com.fit321.fitui.v3.tokens.FitV3Geometry
import com.fit321.fitui.v3.tokens.FitV3Type

@Composable
fun FitRow(
    title: String,
    modifier: Modifier = Modifier,
    subtitle: String? = null,
    leading: (@Composable () -> Unit)? = null,
    trailing: (@Composable () -> Unit)? = null,
    titleBadge: (@Composable () -> Unit)? = null,
    /**
     * Under the sub-line, inside the text column: a bar, a meter, anything that measures what
     * the row is about. It belongs here and not in [trailing] because it reads as a second
     * line of the row's own text — a progress bar beside the value would be a third column.
     */
    below: (@Composable () -> Unit)? = null,
    /**
     * One line where the sub-line is a fact that must not push the row taller — a state and a
     * date beside a fixed action. Two by default: a sub-line is usually a sentence.
     */
    titleMaxLines: Int = 1,
    subtitleMaxLines: Int = 2,
    /** Null for the usual tertiary. A sub-line only takes a colour when it *says* something. */
    subtitleColor: Color? = null,
    subtitleStrong: Boolean = false,
    /**
     * Null for the usual ink. A title takes a colour when the row *is* a link — "See all 12
     * dates" is the accent because the words are the door, not a label over one.
     */
    titleColor: Color? = null,
    /** Null for [FitV3Type.rowTitle]. A row that reads as a link sits a notch smaller. */
    titleStyle: TextStyle? = null,
    muted: Boolean = false,
    /** `.ms-row.muted` drops both the title and the sub-line to 0.6 — a chat that is quiet reads quiet. */
    dimmed: Boolean = false,
    /**
     * `.ms-meta` sits at `align-self: flex-start`: a timestamp belongs on the title's line, not
     * floating between the two lines of a row that happens to have a sub-line.
     */
    trailingAlignment: Alignment.Vertical = Alignment.CenterVertically,
    onClick: (() -> Unit)? = null,
) {
    val palette = LocalFitV3Palette.current
    val shape = RoundedCornerShape(FitV3Geometry.rowRadius)
    val interaction = remember { MutableInteractionSource() }
    val pressed by interaction.collectIsPressedAsState()
    val clickable = if (onClick != null) {
        Modifier.clickable(interactionSource = interaction, indication = null) { onClick() }
    } else {
        Modifier
    }
    Row(
        modifier = modifier
            .fillMaxWidth()
            .clip(shape)
            .then(clickable)
            .background(if (pressed) palette.pressed else Color.Transparent, shape)
            .padding(FitV3Geometry.rowPadding),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(FitV3Geometry.rowGap),
    ) {
        leading?.invoke()
        Column(modifier = Modifier.weight(1f).alpha(if (dimmed) ROW_DIM else 1f)) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(6.dp),
            ) {
                Text(
                    text = title,
                    style = titleStyle ?: FitV3Type.rowTitle,
                    color = titleColor
                        ?: if (muted) palette.textSecondary else palette.textPrimary,
                    maxLines = titleMaxLines,
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier.weight(1f, fill = false),
                )
                titleBadge?.invoke()
            }
            if (subtitle != null) {
                Text(
                    text = subtitle,
                    style = if (subtitleStrong) FitV3Type.rowSubStrong else FitV3Type.rowSub,
                    color = subtitleColor ?: palette.textTertiary,
                    maxLines = subtitleMaxLines,
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier.padding(top = FitV3Geometry.rowSubGap),
                )
            }
            if (below != null) {
                Box(modifier = Modifier.padding(top = 7.dp)) { below() }
            }
        }
        if (trailing != null) {
            Box(modifier = Modifier.align(trailingAlignment)) { trailing() }
        }
    }
}

@Composable
fun FitRowValue(
    text: String,
    modifier: Modifier = Modifier,
    sub: String? = null,
    /** Null for the usual ink. Money that is owed is red wherever it is read. */
    color: Color? = null,
    /**
     * True where the sub belongs beside the value rather than under it — "3 of 10" is one
     * reading, and stacking it makes the row claim two facts where it has one.
     */
    inline: Boolean = false,
) {
    val palette = LocalFitV3Palette.current
    val value: @Composable () -> Unit = {
        Text(text = text, style = FitV3Type.rowValue, color = color ?: palette.textPrimary)
    }
    if (inline) {
        Row(
            modifier = modifier,
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(3.dp),
        ) {
            value()
            if (sub != null) {
                Text(text = sub, style = FitV3Type.rowValueSub, color = palette.textTertiary)
            }
        }
        return
    }
    Column(modifier = modifier, horizontalAlignment = Alignment.End) {
        value()
        if (sub != null) {
            Text(
                text = sub,
                style = FitV3Type.rowValueSub,
                color = palette.textTertiary,
                modifier = Modifier.padding(top = FitV3Geometry.rowSubGap),
            )
        }
    }
}

@Composable
fun FitRowChevron(modifier: Modifier = Modifier) {
    Icon(
        painter = painterResource(R.drawable.ic_fit_chevron_right),
        contentDescription = null,
        tint = LocalFitV3Palette.current.textTertiary,
        modifier = modifier.size(16.dp),
    )
}

@Composable
fun FitStatusBadge(
    text: String,
    modifier: Modifier = Modifier,
    tone: FitV3Tone = FitV3Tone.Danger,
) {
    val palette = LocalFitV3Palette.current
    // `.fit-badge-neutral` is the one badge whose fill is a *surface* rather than a tinted
    // accent: `background: var(--fit-surface-high)`, which under the rework is a translucent
    // darkening. Laid on a panel made of the same darkening it compounds, so the badge reads
    // as a notch deeper than the list — the txn-plate grey it used to borrow lightened instead.
    val fill = if (tone == FitV3Tone.Muted) palette.surface else tone.fill(palette)
    val ink = if (tone == FitV3Tone.Muted) palette.textTertiary else tone.ink(palette)
    Text(
        text = text,
        style = FitV3Type.badgeLabel,
        color = ink,
        // A rounded rectangle, not a pill: a pill is a control you press, and this says
        // something about the row beside it.
        modifier = modifier
            .background(fill, RoundedCornerShape(FitV3Geometry.badgeRadius))
            .padding(horizontal = 10.dp, vertical = 3.dp),
    )
}

@Composable
fun FitRowGo(
    label: String,
    modifier: Modifier = Modifier,
) {
    val palette = LocalFitV3Palette.current
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(2.dp),
    ) {
        Text(text = label, style = FitV3Type.compactStats, color = palette.textPrimary)
        Icon(
            painter = painterResource(R.drawable.ic_fit_chevron_right),
            contentDescription = null,
            tint = palette.textPrimary,
            modifier = Modifier.size(16.dp),
        )
    }
}

/**
 * The day a row belongs to, as a block rather than a sentence: a history read top to bottom is
 * scanned by date first, and a date inside the sub-line cannot be scanned at all.
 */
@Composable
fun FitDateBlock(
    day: String,
    weekday: String,
    modifier: Modifier = Modifier,
) {
    val palette = LocalFitV3Palette.current
    Column(
        modifier = modifier.size(FitV3Geometry.leadingPlate),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
    ) {
        Text(text = day, style = FitV3Type.dateBlockDay, color = palette.textPrimary, maxLines = 1)
        Text(text = weekday, style = FitV3Type.statLabelSmall, color = palette.textTertiary, maxLines = 1)
    }
}

@Composable
fun FitRowPlate(
    modifier: Modifier = Modifier,
    size: Dp = FitV3Geometry.leadingPlate,
    radius: Dp = FitV3Geometry.leadingPlateRadius,
    content: @Composable () -> Unit,
) {
    val palette = LocalFitV3Palette.current
    Box(
        modifier = modifier
            .size(size)
            .background(palette.raised, RoundedCornerShape(radius)),
        contentAlignment = Alignment.Center,
    ) {
        CompositionLocalProvider(LocalContentColor provides palette.textSecondary) { content() }
    }
}

@Composable
fun FitAddRow(
    title: String,
    modifier: Modifier = Modifier,
    subtitle: String? = null,
    leading: (@Composable () -> Unit)? = null,
    onClick: () -> Unit,
) {
    val palette = LocalFitV3Palette.current
    val shape = RoundedCornerShape(FitV3Geometry.panelRadius)
    val density = LocalDensity.current
    val stroke = with(density) { 1.dp.toPx() }
    val dash = with(density) { 6.dp.toPx() }
    val radius = with(density) { FitV3Geometry.panelRadius.toPx() }
    Box(
        modifier = modifier
            .fillMaxWidth()
            .clip(shape)
            .clickable { onClick() }
            .drawBehind {
                drawRoundRect(
                    color = palette.dashed,
                    cornerRadius = CornerRadius(radius, radius),
                    style = Stroke(
                        width = stroke,
                        pathEffect = PathEffect.dashPathEffect(floatArrayOf(dash, dash)),
                    ),
                )
            }
            .padding(FitV3Geometry.panelPadding),
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(FitV3Geometry.rowPadding),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(FitV3Geometry.rowGap),
        ) {
            leading?.invoke()
            Column(modifier = Modifier.weight(1f)) {
                Text(text = title, style = FitV3Type.rowTitle, color = palette.textPrimary)
                if (subtitle != null) {
                    Text(
                        text = subtitle,
                        style = FitV3Type.rowSub,
                        color = palette.textTertiary,
                        modifier = Modifier.padding(top = FitV3Geometry.rowSubGap),
                    )
                }
            }
        }
    }
}

private const val ROW_DIM = 0.6f
