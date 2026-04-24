package com.fit321.fitui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.outlined.StarOutline
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.fit321.fitui.theme.LocalFitTheme
import com.fit321.fitui.tokens.FitColors
import com.fit321.fitui.tokens.FitFont
import com.fit321.fitui.tokens.FitRadius
import com.fit321.fitui.tokens.FitSize
import com.fit321.fitui.tokens.FitSpacing

// ============================================================================
// FitInput — text input with label + error + secure mode
// ============================================================================

enum class FitInputKeyboard { Default, Number, Email, Url, Phone }

@Composable
fun FitInput(
    label: String,
    value: String,
    onValueChange: (String) -> Unit,
    placeholder: String? = null,
    isSecure: Boolean = false,
    isError: Boolean = false,
    errorText: String? = null,
    keyboardType: FitInputKeyboard = FitInputKeyboard.Default,
    enabled: Boolean = true,
    modifier: Modifier = Modifier
) {
    val theme = LocalFitTheme.current
    val kbType = when (keyboardType) {
        FitInputKeyboard.Number -> KeyboardType.Number
        FitInputKeyboard.Email -> KeyboardType.Email
        FitInputKeyboard.Url -> KeyboardType.Uri
        FitInputKeyboard.Phone -> KeyboardType.Phone
        FitInputKeyboard.Default -> KeyboardType.Text
    }
    var showSecure by remember { mutableStateOf(isSecure) }

    Column(verticalArrangement = Arrangement.spacedBy(FitSpacing.sp2)) {
        Text(
            label,
            style = FitFont.caption.copy(fontWeight = FontWeight.SemiBold),
            color = theme.textSecondary
        )
        Box(
            modifier = modifier
                .fillMaxWidth()
                .height(FitSize.inputHeight)
                .clip(RoundedCornerShape(FitRadius.input))
                .background(theme.surfaceLow)
                .then(
                    if (isError)
                        Modifier.border(1.dp, FitColors.error, RoundedCornerShape(FitRadius.input))
                    else Modifier
                )
                .padding(horizontal = 12.dp),
            contentAlignment = Alignment.CenterStart
        ) {
            BasicTextField(
                value = value,
                onValueChange = onValueChange,
                enabled = enabled,
                textStyle = FitFont.body1.copy(color = theme.textPrimary),
                cursorBrush = androidx.compose.ui.graphics.SolidColor(FitColors.brandPrimary),
                visualTransformation = if (showSecure) PasswordVisualTransformation() else VisualTransformation.None,
                keyboardOptions = KeyboardOptions(keyboardType = kbType),
                decorationBox = { inner ->
                    if (value.isEmpty() && placeholder != null) {
                        Text(placeholder, style = FitFont.body1, color = theme.textPlaceholder)
                    }
                    inner()
                }
            )
        }
        if (isError && errorText != null) {
            Text(errorText, style = FitFont.caption, color = FitColors.error)
        }
    }
}

// ============================================================================
// FitRating — 5-star tap-to-rate
// ============================================================================

enum class FitRatingSize(val px: Dp) {
    Small(28.dp),
    Medium(36.dp),
    Large(48.dp)
}

@Composable
fun FitRating(
    rating: Int,
    onRate: (Int) -> Unit,
    size: FitRatingSize = FitRatingSize.Medium,
    readOnly: Boolean = false,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        (1..5).forEach { index ->
            Icon(
                imageVector = if (index <= rating) Icons.Default.Star else Icons.Outlined.StarOutline,
                contentDescription = null,
                tint = FitColors.Yellow.y400,
                modifier = Modifier
                    .size(size.px)
                    .clickable(enabled = !readOnly) { onRate(index) }
            )
        }
    }
}
