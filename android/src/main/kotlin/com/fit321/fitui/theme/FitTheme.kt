package com.fit321.fitui.theme

import androidx.compose.foundation.text.LocalAutofillHighlightColor
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.compositionLocalOf
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

@Composable
fun FitTheme(
    isDark: Boolean = true,
    content: @Composable () -> Unit
) {
    val theme = if (isDark) FitColors.Theme.dark else FitColors.Theme.light
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
