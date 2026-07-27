package com.fit321.fitui.tokens

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.unit.dp
import com.fit321.fitui.theme.LocalFitTheme

/**
 * Kotlin Compose mirror of design-tokens/tokens/elevation.json — same role as [FitColors] for
 * colours. The generated output is a CSS box-shadow STRING, which Compose's `Modifier.shadow`
 * (elevation-dp + colours) can't consume, so the values are mirrored here by hand; keep in sync
 * with elevation.json when it changes.
 */
object FitElevation {

    /**
     * `elevation.2` — the card edge. Light = a 3dp drop shadow using the platform's default shadow
     * colours. The explicit low-alpha (8-10%) two-layer version that mirrors the CSS box-shadow
     * rendered nearly invisible in Compose, so the Kotlin side uses default colours to actually
     * lift the card off the #F2F2F7 canvas. Dark = none — the edge comes from the surface step.
     */
    fun Modifier.fitCardElevation(isLight: Boolean, shape: Shape): Modifier =
        if (isLight) {
            this.shadow(elevation = 3.dp, shape = shape, clip = false)
        } else {
            this
        }
}

/**
 * Composable convenience over [FitElevation.fitCardElevation] — reads the theme to decide
 * light vs dark, so call sites don't repeat the `isLight` check. Apply
 * `Modifier.fitCardElevation(shape)` to any card container that sits on the screen background.
 */
@Composable
fun Modifier.fitCardElevation(shape: Shape = RoundedCornerShape(FitRadius.card)): Modifier {
    val isLight = LocalFitTheme.current.screenBg != FitColors.Gray.g900
    return with(FitElevation) { this@fitCardElevation.fitCardElevation(isLight, shape) }
}
