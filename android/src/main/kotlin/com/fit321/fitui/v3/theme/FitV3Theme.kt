package com.fit321.fitui.v3.theme

import androidx.compose.foundation.text.LocalAutofillHighlightColor
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.compositionLocalOf
import androidx.compose.ui.graphics.Color
import com.fit321.fitui.v3.tokens.FitV3Colors
import com.fit321.fitui.v3.tokens.FitV3Look
import com.fit321.fitui.v3.tokens.FitV3Palette

val LocalFitV3Palette = compositionLocalOf { FitV3Colors.tinted }

@Composable
fun FitV3Theme(
    look: FitV3Look = FitV3Look.Tinted,
    content: @Composable () -> Unit,
) {
    CompositionLocalProvider(
        LocalFitV3Palette provides FitV3Colors.of(look),
        LocalAutofillHighlightColor provides Color.Transparent,
    ) {
        content()
    }
}

object FitV3 {
    val palette: FitV3Palette
        @Composable get() = LocalFitV3Palette.current

    val look: FitV3Look
        @Composable get() = LocalFitV3Palette.current.look
}
