package com.fit321.fitui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
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
    onClick: () -> Unit
) {
    val theme = LocalFitTheme.current
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

    val fg: Color = when (style) {
        FitButtonStyle.Primary -> Color.White
        FitButtonStyle.Secondary -> theme.textPrimary
        FitButtonStyle.Destructive,
        FitButtonStyle.DestructiveLow,
        FitButtonStyle.DestructiveMinimal -> FitColors.error
        FitButtonStyle.DestructiveHigh -> Color.White
        FitButtonStyle.Disabled -> theme.textTertiary
    }

    val bgModifier = when (style) {
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
        FitButtonStyle.Disabled ->
            Modifier.background(brush = SolidColor(theme.surfaceLow), shape = CircleShape)
    }

    val borderModifier: Modifier = when (style) {
        FitButtonStyle.Secondary -> Modifier.border(1.dp, theme.divider, CircleShape)
        FitButtonStyle.DestructiveLow -> Modifier.border(1.dp, FitColors.error, CircleShape)
        else -> Modifier
    }

    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(height)
            .clip(CircleShape)
            .then(bgModifier)
            .then(borderModifier)
            .clickable(enabled = style != FitButtonStyle.Disabled) { onClick() }
            .alpha(if (style == FitButtonStyle.Disabled) 0.7f else 1f),
        contentAlignment = Alignment.Center
    ) {
        Text(title, color = fg, style = textStyle)
    }
}
