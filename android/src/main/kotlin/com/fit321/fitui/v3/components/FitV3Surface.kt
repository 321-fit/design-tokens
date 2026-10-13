package com.fit321.fitui.v3.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.unit.dp
import com.fit321.fitui.tokens.FitElevation
import com.fit321.fitui.v3.theme.LocalFitV3Palette

@Composable
fun Modifier.fitV3Surface(
    shape: Shape,
    fill: Color? = null,
    brush: Brush? = null,
    hairline: Color? = null,
): Modifier {
    val palette = LocalFitV3Palette.current
    val body = fill ?: palette.surface
    val edge = hairline ?: palette.surfaceHairline
    val lifted = if (palette.surfaceLifted && brush == null) {
        with(FitElevation) { this@fitV3Surface.fitCardElevation(isLight = true, shape = shape) }
    } else {
        this
    }
    return lifted
        .then(if (brush != null) Modifier.background(brush, shape) else Modifier.background(body, shape))
        .then(if (edge == Color.Transparent) Modifier else Modifier.border(1.dp, edge, shape))
}
