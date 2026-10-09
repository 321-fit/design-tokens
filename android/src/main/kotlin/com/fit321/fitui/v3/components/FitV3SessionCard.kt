package com.fit321.fitui.v3.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.fit321.designtokens.R
import com.fit321.fitui.tokens.FitColors
import com.fit321.fitui.v3.theme.LocalFitV3Palette
import com.fit321.fitui.v3.tokens.FitV3Colors
import com.fit321.fitui.v3.tokens.FitV3Geometry
import com.fit321.fitui.v3.tokens.FitV3Type

enum class FitSessionType { Personal, Group, SelfPaced }

/**
 * The type plate: what kind of session this is, as colour before it is read as a word. Shared by
 * the session card, the next-session card and the rows that stand in for calendar tiles, so the
 * three cannot disagree about what "group" looks like.
 */
@Composable
fun FitTypePlate(
    type: FitSessionType,
    modifier: Modifier = Modifier,
    size: Dp = FitV3Geometry.typePlate,
    radius: Dp = FitV3Geometry.typePlateRadius,
    icon: @Composable () -> Unit,
) {
    Box(
        modifier = modifier
            .size(size)
            .background(type.plateBrush(), RoundedCornerShape(radius)),
        contentAlignment = Alignment.Center,
    ) {
        CompositionLocalProvider(LocalContentColor provides type.plateInk()) { icon() }
    }
}

fun FitSessionType.plateBrush(): Brush = when (this) {
    FitSessionType.Personal -> FitColors.personalTypeGradient
    FitSessionType.Group -> FitColors.groupTypeGradient
    FitSessionType.SelfPaced -> FitColors.selfPacedTypeGradient
}

fun FitSessionType.plateInk(): Color = when (this) {
    FitSessionType.Personal -> FitColors.Teal.t500
    FitSessionType.Group -> FitColors.Blue.b500
    FitSessionType.SelfPaced -> FitColors.Violet.v400
}

@Composable
fun FitSessionCard(
    title: String,
    modifier: Modifier = Modifier,
    type: FitSessionType = FitSessionType.Personal,
    meta: String? = null,
    price: String? = null,
    /** Under the price, e.g. "per person" on a group session. */
    priceSub: String? = null,
    location: String? = null,
    locationIcon: (@Composable () -> Unit)? = null,
    /** A quiet word at the end of the location strip — the booking flow keeps the type there. */
    locationMeta: String? = null,
    /** The meta's ink when it says something live — seats left, the next date. Quiet by default. */
    locationMetaColor: Color? = null,
    /** A hairline around the card for one that is singled out — a special session in a catalog. */
    outline: Color? = null,
    trailing: (@Composable () -> Unit)? = null,
    onClick: (() -> Unit)? = null,
    icon: (@Composable () -> Unit)? = null,
    /** Below the strip, inside the same surface: a card action or the rows a card carries. */
    bottom: (@Composable ColumnScope.() -> Unit)? = null,
) {
    val palette = LocalFitV3Palette.current
    val shape = RoundedCornerShape(FitV3Geometry.sessionCardRadius)
    val plate: Brush = type.plateBrush()
    val plateInk = type.plateInk()
    Column(
        modifier = modifier
            .fillMaxWidth()
            .fitV3Surface(shape)
            .then(if (outline != null) Modifier.border(1.dp, outline, shape) else Modifier)
            .clip(shape)
            .then(if (onClick != null) Modifier.clickable { onClick() } else Modifier),
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 16.dp, end = 16.dp, top = 16.dp, bottom = 12.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(12.dp),
        ) {
            if (icon != null) {
                Box(
                    modifier = Modifier
                        .size(FitV3Geometry.sessionPlate)
                        .background(plate, RoundedCornerShape(FitV3Geometry.leadingPlateRadius)),
                    contentAlignment = Alignment.Center,
                ) {
                    CompositionLocalProvider(LocalContentColor provides plateInk) { icon() }
                }
            }
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = title,
                    style = FitV3Type.rowTitle.copy(fontWeight = FontWeight.SemiBold),
                    color = palette.textPrimary,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                )
                if (meta != null) {
                    Text(
                        text = meta,
                        style = FitV3Type.rowValueSub,
                        color = palette.textSecondary,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        modifier = Modifier.padding(top = 3.dp),
                    )
                }
            }
            if (price != null) {
                Column(horizontalAlignment = Alignment.End) {
                    Text(
                        text = price,
                        style = FitV3Type.rowTitle.copy(fontWeight = FontWeight.SemiBold),
                        color = palette.textPrimary,
                    )
                    if (priceSub != null) {
                        Text(
                            text = priceSub,
                            style = FitV3Type.rowValueSub,
                            color = palette.textSecondary,
                        )
                    }
                }
            }
            trailing?.invoke()
        }
        if (location != null) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(palette.strip)
                    .padding(horizontal = 16.dp, vertical = 10.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp),
            ) {
                if (locationIcon != null) {
                    CompositionLocalProvider(LocalContentColor provides palette.textTertiary) {
                        locationIcon()
                    }
                }
                Text(
                    text = location,
                    style = FitV3Type.rowSub,
                    color = palette.textTertiary,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier.weight(1f),
                )
                if (locationMeta != null) {
                    Text(
                        text = locationMeta,
                        style = if (locationMetaColor != null) {
                            FitV3Type.rowValueSub.copy(fontWeight = FontWeight.Medium)
                        } else {
                            FitV3Type.rowValueSub
                        },
                        color = locationMetaColor ?: palette.textTertiary,
                        maxLines = 1,
                    )
                }
            }
        }
        bottom?.invoke(this)
    }
}

@Composable
fun FitSportChip(
    name: String,
    modifier: Modifier = Modifier,
    icon: (@Composable () -> Unit)? = null,
) {
    val palette = LocalFitV3Palette.current
    Row(
        modifier = modifier
            .background(FitColors.selectionGradient, RoundedCornerShape(percent = 50))
            .padding(start = 8.dp, end = 14.dp, top = 6.dp, bottom = 6.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        if (icon != null) {
            CompositionLocalProvider(LocalContentColor provides palette.onBrandInk) {
                Box(modifier = Modifier.size(24.dp), contentAlignment = Alignment.Center) { icon() }
            }
        }
        Text(
            text = name,
            style = FitV3Type.fieldText,
            color = palette.onBrandInk,
            maxLines = 1,
        )
    }
}

@Composable
fun FitPickRow(
    title: String,
    checked: Boolean,
    modifier: Modifier = Modifier,
    subtitle: String? = null,
    subtitleMaxLines: Int = 1,
    leading: (@Composable () -> Unit)? = null,
    enabled: Boolean = true,
    reason: String? = null,
    onCheckedChange: ((Boolean) -> Unit)? = null,
) {
    FitRow(
        title = title,
        modifier = modifier,
        subtitle = reason ?: subtitle,
        subtitleMaxLines = subtitleMaxLines,
        leading = {
            Row(
                horizontalArrangement = Arrangement.spacedBy(FitV3Geometry.pickCheckGap),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                FitCheckCircle(checked = checked, enabled = enabled)
                Box(modifier = Modifier.alpha(if (enabled) 1f else PICK_LEADING_DIM)) {
                    leading?.invoke()
                }
            }
        },
        muted = !enabled,
        onClick = if (enabled && onCheckedChange != null) {
            { onCheckedChange(!checked) }
        } else {
            null
        },
    )
}

@Composable
fun FitCheckCircle(
    checked: Boolean,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
) {
    val palette = LocalFitV3Palette.current
    val on = checked && enabled
    Box(
        modifier = modifier
            .alpha(if (enabled) 1f else 0.35f)
            .size(FitV3Geometry.checkCircle)
            .background(
                color = if (on) FitColors.Teal.t500 else Color.Transparent,
                shape = CircleShape,
            )
            .then(if (on) Modifier else Modifier.border(1.5.dp, palette.pickRing, CircleShape)),
        contentAlignment = Alignment.Center,
    ) {
        if (on) {
            Icon(
                painter = painterResource(R.drawable.ic_fit_check),
                contentDescription = null,
                tint = FitV3Colors.pickTick,
                modifier = Modifier.size(13.dp),
            )
        }
    }
}

private const val PICK_LEADING_DIM = 0.55f
