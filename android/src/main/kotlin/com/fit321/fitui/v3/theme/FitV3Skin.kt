package com.fit321.fitui.v3.theme

import androidx.compose.foundation.layout.Box
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.remember
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import com.fit321.fitui.theme.FitSkin
import com.fit321.fitui.theme.FitSkinCta
import com.fit321.fitui.theme.LocalFitSkin
import com.fit321.fitui.theme.LocalFitTheme
import com.fit321.fitui.tokens.FitColors
import com.fit321.fitui.v3.components.FitV3Canvas
import com.fit321.fitui.v3.tokens.FitV3Colors
import com.fit321.fitui.v3.tokens.FitV3Look
import com.fit321.fitui.v3.tokens.FitV3Palette

/**
 * The rework look worn by a screen that keeps its shipped layout.
 *
 * A redrawn screen is rebuilt from the `v3` components. A **restyled** one is not: its layout is
 * already right, and rebuilding it would be a rewrite with nothing to show for it. So the canvas
 * and the palette are swapped underneath it instead — the screen keeps calling `FitTheme`,
 * `FitScreen`, `FitCard` and the rest, and those read the skin.
 *
 * Only the colours a surface is made of are mapped. Brand, status tints and the calendar's own
 * washes stay as they are: they carry meaning rather than depth, and the rework did not change
 * what they mean.
 */
@Composable
fun FitV3Skin(
    look: FitV3Look = FitV3Look.Tinted,
    /**
     * False for something that is already *on* a rework screen — a sheet or a dialog opened
     * over one. The canvas is the screen's own gradient; painting a second one behind an
     * overlay would cover the screen the overlay is supposed to sit on.
     */
    canvas: Boolean = true,
    content: @Composable () -> Unit,
) {
    val palette = FitV3Colors.of(look)
    val skin = remember(look) {
        FitSkin(
            theme = palette.toSkinTheme(look),
            cta = FitSkinCta(
                fill = Brush.verticalGradient(palette.ctaStops.map { it.second }),
                ink = palette.ctaLabel,
            ),
            overlay = palette.material,
            overlayEdge = palette.materialEdge,
            circle = palette.headerCircleFill,
            circleEdge = palette.headerCircleBorder,
            accent = palette.accentBadgeInk,
            isLight = look == FitV3Look.Light,
        )
    }
    FitV3Theme(look = look) {
        // Both locals, not just the skin: a screen that does not call `FitTheme` itself
        // would otherwise keep the role's palette under the rework canvas and paint its own
        // opaque background over the gradient.
        val skinned: @Composable () -> Unit = {
            CompositionLocalProvider(
                LocalFitSkin provides skin,
                LocalFitTheme provides skin.theme,
            ) {
                Box { content() }
            }
        }
        if (canvas) FitV3Canvas { skinned() } else skinned()
    }
}

private fun FitV3Palette.toSkinTheme(look: FitV3Look): FitColors.Theme {
    val base = if (look == FitV3Look.Light) FitColors.Theme.light else FitColors.Theme.dark
    return base.copy(
        // Transparent, not a colour: the gradient is painted once behind the whole screen, and
        // every surface above it is a darkening of what is there. A screen background of its
        // own would cover the gradient on the very first element that fills the screen.
        screenBg = Color.Transparent,
        surfaceLow = surface,
        surfaceDefault = surface,
        surfaceHigh = surface,
        surfaceHigher = raised,
        textPrimary = textPrimary,
        textSecondary = textSecondary,
        textTertiary = textTertiary,
        textPlaceholder = textTertiary,
        divider = divider,
        textError = textError,
    )
}
