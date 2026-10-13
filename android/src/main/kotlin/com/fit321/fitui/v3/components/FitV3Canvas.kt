package com.fit321.fitui.v3.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import com.fit321.fitui.v3.theme.LocalFitV3Palette
import com.fit321.fitui.v3.tokens.FitV3CanvasSpec

@Composable
fun FitV3Canvas(
    modifier: Modifier = Modifier,
    content: @Composable BoxScope.() -> Unit,
) {
    val spec = LocalFitV3Palette.current.canvas
    Box(
        modifier = modifier
            .fillMaxSize()
            .drawBehind { drawCanvas(spec) },
        content = content,
    )
}

fun Modifier.fitV3Canvas(spec: FitV3CanvasSpec): Modifier =
    this.drawBehind { drawCanvas(spec) }

private fun androidx.compose.ui.graphics.drawscope.DrawScope.drawCanvas(spec: FitV3CanvasSpec) {
    drawRect(
        brush = Brush.verticalGradient(
            colorStops = spec.baseStops.toTypedArray(),
            startY = 0f,
            endY = size.height,
        ),
    )
    if (spec.glowStops.isEmpty()) return
    drawRect(
        brush = Brush.radialGradient(
            colorStops = spec.glowStops.toTypedArray(),
            center = Offset(size.width * spec.glowCenterX, size.height * spec.glowCenterY),
            radius = size.width * spec.glowRadius,
        ),
    )
}
