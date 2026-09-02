package com.fit321.fitui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import com.fit321.fitui.tokens.FitSpacing
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.key
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.fit321.fitui.theme.LocalFitTheme
import com.fit321.fitui.tokens.FitColors
import com.fit321.fitui.tokens.FitFont
import com.fit321.fitui.tokens.FitSize

enum class FitButtonStyle {
    Primary, Secondary,
    Destructive, DestructiveHigh, DestructiveLow, DestructiveMinimal,
    Disabled
}

enum class FitButtonSize { Lg, Md, Sm }

@Composable
fun FitButton(
    title: String,
    style: FitButtonStyle = FitButtonStyle.Primary,
    size: FitButtonSize = FitButtonSize.Lg,
    modifier: Modifier = Modifier,
    /**
     * Stretch to the full width of the parent (the default, for footer CTAs), or hug the label.
     * An empty state's action is a centred pill in the design — `width:auto` in `fit-ui.css` —
     * which was unreachable while the fill was applied unconditionally after [modifier].
     */
    fillWidth: Boolean = true,
    /**
     * A button the screen is not ready for yet — a form CTA before the form is valid.
     * It renders the disabled grammar and swallows the tap, so callers stop pairing an
     * `if (valid) Primary else Disabled` with their own `clickable(enabled = …)`.
     */
    enabled: Boolean = true,
    /**
     * A tap the screen has accepted and is still working on — a form CTA between submit and
     * the answer. The label gives way to a spinner and the button stops taking taps, so a
     * caller no longer has to gate `onClick` itself and leave the button looking idle while
     * the request is in flight.
     */
    loading: Boolean = false,
    onClick: () -> Unit
) {
    val theme = LocalFitTheme.current
    val effectiveStyle = if (enabled) style else FitButtonStyle.Disabled
    val height: Dp = when (size) {
        FitButtonSize.Lg -> FitSize.buttonLgHeight
        FitButtonSize.Md -> FitSize.buttonMdHeight
        FitButtonSize.Sm -> FitSize.buttonSmHeight
    }
    val textStyle = when (size) {
        FitButtonSize.Lg -> FitFont.button1
        FitButtonSize.Md -> FitFont.button2
        FitButtonSize.Sm -> FitFont.body2
    }

    val fg: Color = when (effectiveStyle) {
        FitButtonStyle.Primary -> Color.White
        FitButtonStyle.Secondary -> theme.textPrimary
        FitButtonStyle.Destructive,
        FitButtonStyle.DestructiveLow,
        FitButtonStyle.DestructiveMinimal -> FitColors.error
        FitButtonStyle.DestructiveHigh -> Color.White
        FitButtonStyle.Disabled -> theme.textTertiary
    }

    val bgModifier = when (effectiveStyle) {
        FitButtonStyle.Primary ->
            Modifier.background(brush = FitColors.brandGradient, shape = CircleShape)
        FitButtonStyle.Secondary ->
            Modifier.background(brush = SolidColor(theme.surfaceHigh), shape = CircleShape)
        FitButtonStyle.Destructive ->
            Modifier.background(brush = SolidColor(FitColors.error.copy(alpha = 0.15f)), shape = CircleShape)
        FitButtonStyle.DestructiveHigh ->
            Modifier.background(brush = SolidColor(FitColors.error), shape = CircleShape)
        FitButtonStyle.DestructiveLow,
        FitButtonStyle.DestructiveMinimal -> Modifier
        // Inactive is the outline grammar, not a filled plate. It used to take `surfaceLow`,
        // the very token an input uses, so a dead button and a live field were the same
        // colour — see `.fit-btn-disabled` in fit-ui.css and specs/theme-contrast.md §1.
        FitButtonStyle.Disabled -> Modifier
    }

    val borderModifier: Modifier = when (effectiveStyle) {
        FitButtonStyle.Secondary -> Modifier.border(1.dp, theme.divider, CircleShape)
        FitButtonStyle.DestructiveLow -> Modifier.border(1.dp, FitColors.error, CircleShape)
        FitButtonStyle.Disabled -> Modifier.border(1.dp, theme.divider, CircleShape)
        else -> Modifier
    }

    key(effectiveStyle) {
        Box(
            modifier = modifier
                .then(if (fillWidth) Modifier.fillMaxWidth() else Modifier)
                .height(height)
                .clip(CircleShape)
                .then(bgModifier)
                .then(borderModifier)
                // Only meaningful when hugging the label: a full-width button centres its text and
                // needs none. `.fit-btn` in fit-ui.css pads an auto-width button by 24px.
                .then(if (fillWidth) Modifier else Modifier.padding(horizontal = FitSpacing.sp6))
                .clickable(enabled = effectiveStyle != FitButtonStyle.Disabled && !loading) { onClick() },
            contentAlignment = Alignment.Center
        ) {
            if (loading) {
                CircularProgressIndicator(
                    modifier = Modifier.size(FitSize.iconLg),
                    color = fg,
                    strokeWidth = 2.dp
                )
            } else {
                Text(title, color = fg, style = textStyle)
            }
        }
    }
}
