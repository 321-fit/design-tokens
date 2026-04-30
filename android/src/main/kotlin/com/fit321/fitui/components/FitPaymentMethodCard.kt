package com.fit321.fitui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.fit321.fitui.theme.LocalFitTheme
import com.fit321.fitui.tokens.FitColors
import com.fit321.fitui.tokens.FitFont
import com.fit321.fitui.tokens.FitRadius
import com.fit321.fitui.tokens.FitSpacing

// ============================================================================
// FitPaymentMethodCard — provider selector / connected method card.
// Mirrors Swift FitPaymentMethodCard. See `docs/components.md`
// § FitPaymentMethodCard.
// ============================================================================

enum class FitPaymentMethodStatus { Connected, ActionRequired, NotConnected, ComingSoon }

@Composable
fun FitPaymentMethodCard(
    logoLetter: String,
    title: String,
    subtitle: String,
    status: FitPaymentMethodStatus = FitPaymentMethodStatus.NotConnected,
    actionLabel: String? = null,
    onAction: (() -> Unit)? = null,
    disabled: Boolean = false,
    onTap: (() -> Unit)? = null,
    modifier: Modifier = Modifier
) {
    val theme = LocalFitTheme.current

    val statusDot: Color? = when (status) {
        FitPaymentMethodStatus.Connected -> FitColors.Teal.t500
        FitPaymentMethodStatus.ActionRequired -> FitColors.Yellow.y400
        else -> null
    }

    Row(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(FitRadius.md))
            .background(theme.surfaceHigh, RoundedCornerShape(FitRadius.md))
            .then(if (onTap != null && !disabled) Modifier.clickable(onClick = onTap) else Modifier)
            .padding(horizontal = 14.dp, vertical = FitSpacing.sp3)
            .alpha(if (disabled) 0.6f else 1f),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(FitSpacing.sp3)
    ) {
        // Logo container
        Box(
            modifier = Modifier
                .size(40.dp)
                .clip(RoundedCornerShape(FitRadius.sm))
                .background(theme.surfaceHigher),
            contentAlignment = Alignment.Center
        ) {
            Text(
                logoLetter.take(1).uppercase(),
                style = FitFont.heading3.copy(fontSize = 18.sp, fontWeight = FontWeight.SemiBold),
                color = theme.textPrimary
            )
        }

        Column(modifier = Modifier.weight(1f)) {
            Text(
                title,
                style = FitFont.body1.copy(fontSize = 15.sp, fontWeight = FontWeight.Medium),
                color = theme.textPrimary,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(FitSpacing.sp1)
            ) {
                if (statusDot != null) {
                    Box(modifier = Modifier.size(6.dp).clip(CircleShape).background(statusDot))
                }
                Text(
                    subtitle,
                    style = FitFont.footnote,
                    color = theme.textTertiary,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }
        }

        when (status) {
            FitPaymentMethodStatus.ComingSoon -> FitBadge("Coming soon", FitBadgeStyle.Neutral)
            else -> {
                if (actionLabel != null && onAction != null) {
                    // Inline secondary button (FitButton fills width — wrap so it
                    // sizes to content within the row).
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(FitRadius.full))
                            .background(theme.surfaceHigher)
                            .clickable(onClick = onAction)
                            .padding(horizontal = FitSpacing.sp4, vertical = FitSpacing.sp2)
                    ) {
                        Text(
                            actionLabel,
                            style = FitFont.body2.copy(fontWeight = FontWeight.Medium),
                            color = theme.textPrimary
                        )
                    }
                }
            }
        }
    }
}
