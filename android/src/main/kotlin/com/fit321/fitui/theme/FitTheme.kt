package com.fit321.fitui.theme

import androidx.compose.foundation.text.LocalAutofillHighlightColor
import androidx.compose.runtime.Composable
import androidx.compose.runtime.ReadOnlyComposable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.compositionLocalOf
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import com.fit321.fitui.tokens.FitColors

/**
 * Theme provider — wraps content and injects current FitColors.Theme
 * via CompositionLocal. Mirrors Swift `@Environment(\.fitTheme)`.
 *
 * Usage:
 *   FitTheme(isDark = true) {
 *     MyScreen()
 *   }
 *
 *   @Composable fun MyScreen() {
 *     val theme = LocalFitTheme.current
 *     Text("…", color = theme.textPrimary)
 *   }
 */
val LocalFitTheme = compositionLocalOf { FitColors.Theme.dark }

/**
 * What a screen wears instead of its own look. [FitV3Skin] sets it so that a shipped screen
 * keeps its layout and its components and still renders in the rework look — the screens that
 * are *restyled* rather than redrawn. A nested `FitTheme(isDark = …)` must not undo that, which
 * is why the check lives in [FitTheme] and not at each call site: those calls are spread over a
 * hundred screens and are how the role picks dark or light in the first place.
 *
 * [cta] and [overlay] exist because two of the rework's answers have no token to travel in. The
 * primary CTA is a brush, not a colour, and on the tinted canvas it stops being the brand
 * gradient. The overlay is the sharper split: a sheet and a menu are layers *over* the screen,
 * and the rework needs them opaque while the screen itself lets the gradient through — one
 * `screenBg` cannot be both.
 */
val LocalFitSkin = compositionLocalOf<FitSkin?> { null }

data class FitSkin(
    val theme: FitColors.Theme,
    val cta: FitSkinCta,
    /** Sheets and menus: a layer over the screen, not a surface on it. */
    val overlay: Color,
    val overlayEdge: Color,
    val isLight: Boolean,
)

data class FitSkinCta(val fill: Brush, val ink: Color)

/**
 * Light or dark, asked rather than guessed.
 *
 * Components used to infer it by comparing the theme against the two canonical instances —
 * `theme === Theme.dark`, or `screenBg != gray.900`. A skinned screen breaks both: its theme is
 * a copy, and its screen background is transparent so the gradient can show through. Every one
 * of those checks then answered "light" on the darkest canvas in the app, which is how a card
 * on the rework canvas grew a drop shadow it should never have.
 */
@Composable
@ReadOnlyComposable
fun fitIsLight(): Boolean =
    LocalFitSkin.current?.isLight ?: (LocalFitTheme.current.screenBg != FitColors.Gray.g900)

@Composable
fun FitTheme(
    isDark: Boolean = true,
    content: @Composable () -> Unit
) {
    val theme = LocalFitSkin.current?.theme ?: if (isDark) FitColors.Theme.dark else FitColors.Theme.light
    CompositionLocalProvider(
        LocalFitTheme provides theme,
        // Compose paints a translucent yellow plate over a field it has just autofilled. It
        // is a rectangle, so on a rounded input it sits proud of the shape, and on the dark
        // theme it reads as damage rather than as feedback. The field already shows its own
        // state. (Compose draws this itself since 1.8 — the platform's `autofilledHighlight`
        // theme attribute has no effect on it.)
        LocalAutofillHighlightColor provides Color.Transparent,
    ) {
        content()
    }
}
