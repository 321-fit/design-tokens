package com.fit321.fitui.v3.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.fit321.fitui.v3.theme.LocalFitV3Palette
import com.fit321.fitui.v3.tokens.FitV3Geometry
import com.fit321.fitui.v3.tokens.FitV3Type

enum class FitNextSessionState { Planned, Request, Awaiting }

@Composable
fun FitNextSessionCard(
    whenLine: String,
    modifier: Modifier = Modifier,
    whatLine: String? = null,
    state: FitNextSessionState = FitNextSessionState.Planned,
    leading: (@Composable () -> Unit)? = null,
    onClick: (() -> Unit)? = null,
) {
    val palette = LocalFitV3Palette.current
    val shape = RoundedCornerShape(FitV3Geometry.panelRadius)
    val perimeter = when (state) {
        FitNextSessionState.Planned -> palette.perimeterPlanned
        FitNextSessionState.Request, FitNextSessionState.Awaiting -> palette.perimeterAttention
    }
    val tint = when (state) {
        FitNextSessionState.Request -> palette.attentionTint
        else -> Color.Transparent
    }
    val density = LocalDensity.current
    val strokeWidth = with(density) { 1.dp.toPx() }
    val dash = with(density) { 6.dp.toPx() }
    val radius = with(density) { FitV3Geometry.panelRadius.toPx() }
    val dashed = state == FitNextSessionState.Awaiting
    Row(
        modifier = modifier
            .fillMaxWidth()
            .fitV3Surface(shape)
            .clip(shape)
            .then(if (tint != Color.Transparent) Modifier.background(tint, shape) else Modifier)
            .then(
                if (dashed) {
                    Modifier.drawBehind {
                        drawRoundRect(
                            color = perimeter,
                            cornerRadius = CornerRadius(radius, radius),
                            style = Stroke(
                                width = strokeWidth,
                                pathEffect = PathEffect.dashPathEffect(floatArrayOf(dash, dash)),
                            ),
                        )
                    }
                } else {
                    Modifier.border(1.dp, perimeter, shape)
                },
            )
            .then(if (onClick != null) Modifier.clickable { onClick() } else Modifier)
            .padding(FitV3Geometry.rowPadding + FitV3Geometry.panelPadding),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(FitV3Geometry.rowGap),
    ) {
        leading?.invoke()
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = whenLine,
                style = FitV3Type.nextWhen,
                color = palette.textPrimary,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
            )
            if (whatLine != null) {
                Text(
                    text = whatLine,
                    style = FitV3Type.nextWhat,
                    color = palette.textTertiary,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier.padding(top = 3.dp),
                )
            }
        }
    }
}
