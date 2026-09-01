package com.fit321.fitui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
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
import androidx.compose.ui.autofill.ContentType
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.semantics.contentType
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.fit321.designtokens.R
import com.fit321.fitui.theme.LocalFitTheme
import com.fit321.fitui.tokens.FitColors
import com.fit321.fitui.tokens.FitFont
import com.fit321.fitui.tokens.FitRadius
import com.fit321.fitui.tokens.FitSize
import com.fit321.fitui.tokens.FitSpacing

// ============================================================================
// FitInput — text input with label + error + secure mode
// ============================================================================

/**
 * What the field holds, for the system's password manager.
 *
 * A closed set rather than the platform's own type: the manager only helps if a field says
 * what it is, and leaving that to each screen is how one of them ends up marked as a
 * username while the one next to it says nothing. [NewPassword] is the sign-up case — it is
 * what makes a manager offer to generate and store one, rather than autofill the old one.
 */
enum class FitInputContent { None, Email, Username, Password, NewPassword }

enum class FitInputKeyboard { Default, Number, Email, Url, Phone }

@Composable
fun FitInput(
    value: String,
    onValueChange: (String) -> Unit,
    // Null when the placeholder already names the field. The rule the screens follow is that
    // the label or the placeholder names it, never both — a label saying "Email" over a
    // placeholder saying "Email" is the word twice. Labels stay where the placeholder is a
    // format hint rather than a name.
    label: String? = null,
    placeholder: String? = null,
    isSecure: Boolean = false,
    isError: Boolean = false,
    errorText: String? = null,
    keyboardType: FitInputKeyboard = FitInputKeyboard.Default,
    enabled: Boolean = true,
    height: Dp = FitSize.inputHeight,
    content: FitInputContent = FitInputContent.None,
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
        if (label != null) {
            Text(
                label,
                style = FitFont.caption.copy(fontWeight = FontWeight.SemiBold),
                color = theme.textSecondary
            )
        }
        Box(
            modifier = modifier
                .fillMaxWidth()
                .height(height)
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
            val autofill = when (content) {
                FitInputContent.Email -> ContentType.EmailAddress
                FitInputContent.Username -> ContentType.Username
                FitInputContent.Password -> ContentType.Password
                FitInputContent.NewPassword -> ContentType.NewPassword
                FitInputContent.None -> null
            }
            BasicTextField(
                value = value,
                onValueChange = onValueChange,
                enabled = enabled,
                singleLine = true,
                // Fills the 56dp row, not just the line of text. A text field sized to its
                // own content leaves ~16dp of dead pixels above and below inside a box that
                // looks entirely tappable: aiming at the top of the field did nothing, which
                // reads as the screen ignoring you rather than as a small target.
                modifier = Modifier
                    .fillMaxSize()
                    .then(
                        if (autofill != null) {
                            Modifier.semantics { contentType = autofill }
                        } else {
                            Modifier
                        }
                    ),
                textStyle = FitFont.body1.copy(color = theme.textPrimary),
                cursorBrush = androidx.compose.ui.graphics.SolidColor(FitColors.brandPrimary),
                visualTransformation = if (showSecure) PasswordVisualTransformation() else VisualTransformation.None,
                keyboardOptions = KeyboardOptions(keyboardType = kbType),
                decorationBox = { inner ->
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Box(modifier = Modifier.weight(1f), contentAlignment = Alignment.CenterStart) {
                            if (value.isEmpty() && placeholder != null) {
                                Text(placeholder, style = FitFont.body1, color = theme.textPlaceholder)
                            }
                            inner()
                        }
                        // A masked field with no way to unmask it is a field people retype from
                        // scratch after every slip. The state existed and nothing ever flipped it.
                        if (isSecure) {
                            Icon(
                                painter = painterResource(
                                    if (showSecure) R.drawable.ic_fit_eye_off else R.drawable.ic_fit_eye
                                ),
                                contentDescription = null,
                                tint = theme.textTertiary,
                                modifier = Modifier
                                    .padding(start = FitSpacing.sp2)
                                    .size(FitSize.iconLg)
                                    .clickable { showSecure = !showSecure }
                            )
                        }
                    }
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
) = FitRating(
    rating = rating,
    onRate = onRate,
    starSize = size.px,
    readOnly = readOnly,
    modifier = modifier
) { _, filled, tint, px ->
    Icon(
        imageVector = if (filled) Icons.Default.Star else Icons.Outlined.StarOutline,
        contentDescription = null,
        tint = tint,
        modifier = Modifier.size(px)
    )
}

/**
 * [FitRating] with a caller-supplied glyph — see the note on [FitChip].
 *
 * The star is the one glyph a product is most likely to own: the Material pair is a
 * filled star against a thin outline, and a set drawn as one family reads as one
 * control. The slot is handed the index, whether that position is filled, the tint and
 * the size the row would have used, so a caller swapping the glyph does not re-derive
 * any of them — and can hang its own test tag on the position it draws.
 *
 * [starSize] is a [Dp] rather than a [FitRatingSize] because the sizes in the wild are
 * not the three the enum names: a review screen sets its stars at the size of the
 * question it asks, and a summary row at the size of the line it sits in.
 *
 * There is no ripple: at these sizes a bounded indication paints a square behind the
 * star, which reads as a selection box rather than a tap.
 */
@Composable
fun FitRating(
    rating: Int,
    onRate: (Int) -> Unit,
    starSize: Dp,
    spacing: Dp = 10.dp,
    readOnly: Boolean = false,
    tint: Color = FitColors.Yellow.y400,
    modifier: Modifier = Modifier,
    star: @Composable (index: Int, filled: Boolean, tint: Color, size: Dp) -> Unit
) {
    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.spacedBy(spacing)
    ) {
        (1..5).forEach { index ->
            Box(
                modifier = Modifier.clickable(
                    interactionSource = remember { MutableInteractionSource() },
                    indication = null,
                    enabled = !readOnly
                ) { onRate(index) }
            ) {
                star(index, index <= rating, tint, starSize)
            }
        }
    }
}
