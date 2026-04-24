package com.fit321.fitui.theme

import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.compositionLocalOf
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
    CompositionLocalProvider(LocalFitTheme provides theme) {
        content()
    }
}
