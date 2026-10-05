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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
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
    muted: Boolean = false,
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
        Column(modifier = Modifier.weight(1f)) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(6.dp),
            ) {
                Text(
                    text = title,
                    style = FitV3Type.rowTitle,
                    color = if (muted) palette.textSecondary else palette.textPrimary,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier.weight(1f, fill = false),
                )
                titleBadge?.invoke()
            }
            if (subtitle != null) {
                Text(
                    text = subtitle,
                    style = FitV3Type.rowSub,
                    color = palette.textTertiary,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier.padding(top = FitV3Geometry.rowSubGap),
                )
            }
        }
        trailing?.invoke()
    }
}

@Composable
fun FitRowValue(
    text: String,
    modifier: Modifier = Modifier,
    sub: String? = null,
) {
    val palette = LocalFitV3Palette.current
    Column(modifier = modifier, horizontalAlignment = Alignment.End) {
        Text(text = text, style = FitV3Type.rowValue, color = palette.textPrimary)
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
    Text(
        text = text,
        style = FitV3Type.rowValueSub,
        color = tone.ink(palette),
        modifier = modifier
            .background(tone.fill(palette), RoundedCornerShape(percent = 50))
            .padding(horizontal = 8.dp, vertical = 2.dp),
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
