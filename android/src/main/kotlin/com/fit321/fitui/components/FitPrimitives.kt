package com.fit321.fitui.components

import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.fit321.fitui.theme.LocalFitTheme
import com.fit321.fitui.tokens.FitColors
import com.fit321.fitui.tokens.FitFont
import com.fit321.fitui.tokens.FitRadius
import com.fit321.fitui.tokens.FitSize
import com.fit321.fitui.tokens.FitSpacing

// ============================================================================
// FitIconBtn — circular 32dp icon-only button
// ============================================================================

enum class FitIconBtnColor { Primary, Brand, Error, Success }

@Composable
fun FitIconBtn(
    icon: ImageVector,
    color: FitIconBtnColor = FitIconBtnColor.Primary,
    tintedBg: Boolean = false,
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    val theme = LocalFitTheme.current
    val iconColor = when (color) {
        FitIconBtnColor.Primary -> theme.textSecondary
        FitIconBtnColor.Brand -> FitColors.brandPrimary
        FitIconBtnColor.Error -> FitColors.error
        FitIconBtnColor.Success -> FitColors.success
    }
    val bgColor = if (tintedBg) {
        when (color) {
            FitIconBtnColor.Error -> FitColors.error.copy(alpha = 0.10f)
            FitIconBtnColor.Brand -> FitColors.brandPrimary.copy(alpha = 0.10f)
            FitIconBtnColor.Success -> FitColors.success.copy(alpha = 0.12f)
            FitIconBtnColor.Primary -> theme.surfaceHigh
        }
    } else theme.surfaceHigh

    Box(
        modifier = modifier
            .size(FitSize.iconBtnSize)
            .clip(CircleShape)
            .background(bgColor)
            .clickable { onClick() },
        contentAlignment = Alignment.Center
    ) {
        Icon(icon, contentDescription = null, tint = iconColor, modifier = Modifier.size(FitSize.iconMd))
    }
}

// ============================================================================
// FitAvatar — initials in 5 sizes, circle or rounded rect
// ============================================================================

enum class FitAvatarSize(val px: Dp, val fontSp: Int) {
    Xs(FitSize.avatarXs, 10),
    Sm(FitSize.avatarSm, 12),
    Md(FitSize.avatarMd, 14),
    Lg(FitSize.avatarLg, 16),
    Xl(FitSize.avatarXl, 28)
}

enum class FitAvatarShape { Circle, Rect10 }

@Composable
fun FitAvatar(
    initials: String,
    size: FitAvatarSize = FitAvatarSize.Md,
    bg: Brush = FitColors.brandGradient,
    shape: FitAvatarShape = FitAvatarShape.Circle,
    isPaid: Boolean = false,
    modifier: Modifier = Modifier
) {
    val shapeValue = when (shape) {
        FitAvatarShape.Circle -> CircleShape
        FitAvatarShape.Rect10 -> RoundedCornerShape(10.dp)
    }
    Box(
        modifier = modifier
            .size(size.px)
            .clip(shapeValue)
            .background(bg, shapeValue),
        contentAlignment = Alignment.Center
    ) {
        Text(
            initials.take(2).uppercase(),
            color = Color.White.copy(alpha = if (isPaid) 0.5f else 1f),
            style = FitFont.body1.copy(fontSize = size.fontSp.sp(), fontWeight = FontWeight.Medium)
        )
    }
}

// ============================================================================
// FitCheckbox — 28dp square with check
// ============================================================================

@Composable
fun FitCheckbox(
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit,
    label: String? = null,
    enabled: Boolean = true,
    modifier: Modifier = Modifier
) {
    val theme = LocalFitTheme.current
    Row(
        modifier = modifier
            .clickable(enabled = enabled) { onCheckedChange(!checked) },
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(FitSpacing.sp3)
    ) {
        Box(
            modifier = Modifier
                .size(FitSize.checkboxSize)
                .clip(RoundedCornerShape(6.dp))
                .background(if (checked) FitColors.Teal.t600 else Color.Transparent)
                .border(2.dp, if (checked) FitColors.Teal.t600 else theme.textTertiary, RoundedCornerShape(6.dp)),
            contentAlignment = Alignment.Center
        ) {
            if (checked) {
                Icon(Icons.Default.Check, null, tint = Color.White, modifier = Modifier.size(14.dp))
            }
        }
        if (label != null) {
            Text(label, style = FitFont.body1, color = theme.textPrimary)
        }
    }
}

// ============================================================================
// FitToggle — iOS-style switch 48×28
// ============================================================================

@Composable
fun FitToggle(
    isOn: Boolean,
    onChange: (Boolean) -> Unit,
    label: String? = null,
    enabled: Boolean = true,
    modifier: Modifier = Modifier
) {
    val theme = LocalFitTheme.current
    val thumbOffset by animateDpAsState(
        if (isOn) FitSize.toggleWidth - FitSize.toggleThumb - 3.dp else 3.dp,
        label = "toggle-thumb"
    )

    Row(
        modifier = modifier.clickable(enabled = enabled) { onChange(!isOn) },
        verticalAlignment = Alignment.CenterVertically
    ) {
        if (label != null) {
            Text(label, style = FitFont.body1, color = theme.textPrimary)
            Spacer(Modifier.weight(1f))
        }
        Box(
            modifier = Modifier
                .size(FitSize.toggleWidth, FitSize.toggleHeight)
                .clip(CircleShape)
                .background(if (isOn) FitColors.Teal.t500 else theme.surfaceHigher),
            contentAlignment = Alignment.CenterStart
        ) {
            Spacer(
                Modifier
                    .offset(x = thumbOffset)
                    .size(FitSize.toggleThumb)
                    .clip(CircleShape)
                    .background(Color.White)
            )
        }
    }
}

// ============================================================================
// FitStepper — ±48 buttons + value in middle
// ============================================================================

@Composable
fun FitStepper(
    value: Int,
    onChange: (Int) -> Unit,
    min: Int = 1,
    max: Int = 100,
    unit: String? = null,
    modifier: Modifier = Modifier
) {
    val theme = LocalFitTheme.current
    Row(
        modifier = modifier
            .clip(RoundedCornerShape(FitRadius.md))
            .height(FitSize.stepperHeight)
    ) {
        StepperButton(Icons.Default.Remove, enabled = value > min) { if (value > min) onChange(value - 1) }
        Box(
            modifier = Modifier
                .weight(1f)
                .fillMaxHeight()
                .background(theme.surfaceHigh),
            contentAlignment = Alignment.Center
        ) {
            Text(
                unit?.let { "$value $it" } ?: value.toString(),
                style = FitFont.button1,
                color = theme.textPrimary
            )
        }
        StepperButton(Icons.Default.Add, enabled = value < max) { if (value < max) onChange(value + 1) }
    }
}

@Composable
private fun StepperButton(icon: ImageVector, enabled: Boolean, onClick: () -> Unit) {
    val theme = LocalFitTheme.current
    Box(
        modifier = Modifier
            .size(FitSize.stepperButton, FitSize.stepperHeight)
            .background(theme.surfaceHigher)
            .clickable(enabled = enabled, onClick = onClick),
        contentAlignment = Alignment.Center
    ) {
        Icon(icon, null, tint = theme.textPrimary.copy(alpha = if (enabled) 1f else 0.4f))
    }
}

// ============================================================================
// FitBadge — 12dp text pill in 13 color variants
// ============================================================================

enum class FitBadgeStyle {
    Group, Personal, Full, Joined, Pending, Special, Error,
    Neutral, Crm, Danger, Info, Success, Accent
}

@Composable
fun FitBadge(text: String, style: FitBadgeStyle, modifier: Modifier = Modifier) {
    val theme = LocalFitTheme.current
    val (fg, bg) = colorsFor(style, theme)
    Box(
        modifier = modifier
            .clip(RoundedCornerShape(FitRadius.badge))
            .background(bg)
            .padding(horizontal = 10.dp, vertical = 3.dp)
    ) {
        Text(text, style = FitFont.caption.copy(fontWeight = FontWeight.Medium), color = fg)
    }
}

private fun colorsFor(s: FitBadgeStyle, theme: FitColors.Theme): Pair<Color, Color> = when (s) {
    FitBadgeStyle.Group    -> FitColors.Blue.b500 to FitColors.Blue.b500.copy(alpha = 0.15f)
    FitBadgeStyle.Personal -> theme.textSecondary to theme.surfaceHigh
    FitBadgeStyle.Full,
    FitBadgeStyle.Danger   -> FitColors.error to FitColors.error.copy(alpha = 0.12f)
    FitBadgeStyle.Joined,
    FitBadgeStyle.Success  -> FitColors.Green.g500 to FitColors.Teal.t500.copy(alpha = 0.12f)
    FitBadgeStyle.Pending  -> FitColors.Yellow.y400 to FitColors.Yellow.y400.copy(alpha = 0.12f)
    FitBadgeStyle.Special,
    FitBadgeStyle.Info     -> FitColors.Blue.b500 to FitColors.Blue.b500.copy(alpha = 0.12f)
    FitBadgeStyle.Error    -> FitColors.error to FitColors.error.copy(alpha = 0.12f)
    FitBadgeStyle.Neutral  -> theme.textTertiary to theme.surfaceHigher
    FitBadgeStyle.Crm      -> FitColors.Teal.t500 to FitColors.Teal.t500.copy(alpha = 0.15f)
    FitBadgeStyle.Accent   -> FitColors.brandPrimary to FitColors.brandPrimary.copy(alpha = 0.12f)
}

// Helper for Int → .sp
private fun Int.sp() = androidx.compose.ui.unit.TextUnit(this.toFloat(), androidx.compose.ui.unit.TextUnitType.Sp)
