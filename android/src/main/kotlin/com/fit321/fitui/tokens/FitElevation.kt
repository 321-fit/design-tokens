package com.fit321.fitui.tokens

import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.unit.dp

/**
 * Kotlin Compose mirror of design-tokens/tokens/elevation.json — same role as [FitColors] for
 * colours. The generated output is a CSS box-shadow STRING, which Compose's `Modifier.shadow`
 * (elevation-dp + colours) can't consume, so the values are mirrored here by hand; keep in sync
 * with elevation.json when it changes.
 */
object FitElevation {

    /**
     * `elevation.2` — the card edge. Light = two layers (a tight 1dp contact shadow that draws
     * the edge + a soft ambient lift); the old single blur read as a smudge. Dark = none — there
     * the edge comes from the surface step, not a shadow.
     */
    fun Modifier.fitCardElevation(isLight: Boolean, shape: Shape): Modifier =
        if (isLight) {
            this
                .shadow(1.dp, shape, spotColor = Color.Black.copy(alpha = 0.10f), ambientColor = Color.Black.copy(alpha = 0.10f))
                .shadow(8.dp, shape, spotColor = Color.Black.copy(alpha = 0.08f), ambientColor = Color.Black.copy(alpha = 0.08f))
        } else {
            this
        }
}
