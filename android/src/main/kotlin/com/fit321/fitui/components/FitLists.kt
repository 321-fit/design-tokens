package com.fit321.fitui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.fit321.fitui.theme.LocalFitTheme
import com.fit321.fitui.tokens.FitColors
import com.fit321.fitui.tokens.FitFont
import com.fit321.fitui.tokens.FitRadius
import com.fit321.fitui.tokens.FitSize
import com.fit321.fitui.tokens.FitSpacing

// ============================================================================
// FitSelectRow — selectable list row
// ============================================================================

enum class FitSelectRowTrailing { Chevron, Check, Toggle, None }

@Composable
fun FitSelectRow(
    label: String,
    isSelected: Boolean,
    icon: ImageVector? = null,
    trailing: FitSelectRowTrailing = FitSelectRowTrailing.Chevron,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val theme = LocalFitTheme.current
    Row(
        modifier = modifier
            .fillMaxWidth()
            .heightIn(min = 48.dp)
            .clip(RoundedCornerShape(FitRadius.selectRow))
            .then(
                if (isSelected)
                    Modifier.background(FitColors.selectionGradient, RoundedCornerShape(FitRadius.selectRow))
                        .border(1.dp, FitColors.selectionBorder, RoundedCornerShape(FitRadius.selectRow))
                else Modifier.background(theme.surfaceHigher)
            )
            .clickable(onClick = onClick)
            .padding(horizontal = 12.dp, vertical = 10.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(FitSpacing.sp3)
    ) {
        if (icon != null) {
            Icon(icon, null, tint = theme.textSecondary, modifier = Modifier.size(FitSize.iconLg))
        }
        Text(label, style = FitFont.body1, color = theme.textPrimary, modifier = Modifier.weight(1f))
        when (trailing) {
            FitSelectRowTrailing.Chevron ->
                Icon(Icons.Default.ChevronRight, null, tint = theme.textTertiary,
                    modifier = Modifier.size(FitSize.iconMd))
            FitSelectRowTrailing.Check -> if (isSelected) {
                Box(
                    modifier = Modifier
                        .size(FitSize.selectRowCheck)
                        .clip(RoundedCornerShape(6.dp))
                        .background(FitColors.Teal.t600),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(Icons.Default.Check, null, tint = Color.White, modifier = Modifier.size(14.dp))
                }
            }
            FitSelectRowTrailing.Toggle, FitSelectRowTrailing.None -> {}
        }
    }
}

// ============================================================================
// FitSettingsCard — icon + title + subtitle + trailing
// ============================================================================

enum class FitSettingsTrailing { Chevron, None }

@Composable
fun FitSettingsCard(
    icon: ImageVector,
    title: String,
    subtitle: String? = null,
    trailing: FitSettingsTrailing = FitSettingsTrailing.Chevron,
    destructive: Boolean = false,
    onClick: (() -> Unit)? = null,
    modifier: Modifier = Modifier
) {
    val theme = LocalFitTheme.current
    val titleColor = if (destructive) FitColors.error else theme.textPrimary
    Row(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(FitRadius.settingsCard))
            .background(theme.cardBg)
            .then(if (onClick != null) Modifier.clickable(onClick = onClick) else Modifier)
            .padding(FitSpacing.sp3),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(FitSpacing.sp3)
    ) {
        Icon(icon, null, tint = titleColor, modifier = Modifier.size(FitSize.settingsCardIcon))
        Column(modifier = Modifier.weight(1f)) {
            Text(title, style = FitFont.body1, color = titleColor)
            if (subtitle != null) {
                Text(subtitle, style = FitFont.body2, color = theme.textSecondary)
            }
        }
        if (trailing == FitSettingsTrailing.Chevron) {
            Icon(Icons.Default.ChevronRight, null, tint = theme.textTertiary,
                modifier = Modifier.size(FitSize.settingsCardChevron))
        }
    }
}

// ============================================================================
// FitParticipant — list row with avatar + name + sub + remove/paid/you states
// ============================================================================

enum class FitParticipantPayment { Cash, Card, None }

@Composable
fun FitParticipant(
    name: String,
    subtitle: String,
    avatarInitials: String,
    isRemovable: Boolean = false,
    onRemove: (() -> Unit)? = null,
    isPaid: Boolean = false,
    payment: FitParticipantPayment = FitParticipantPayment.None,
    isYou: Boolean = false,
    modifier: Modifier = Modifier
) {
    val theme = LocalFitTheme.current
    Row(
        modifier = modifier
            .fillMaxWidth()
            .then(
                if (isYou) Modifier.clip(RoundedCornerShape(FitRadius.md))
                    .background(FitColors.selectionGradient, RoundedCornerShape(FitRadius.md))
                    .padding(FitSpacing.sp3)
                else Modifier.padding(vertical = FitSpacing.sp3)
            ),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(FitSpacing.sp3)
    ) {
        FitAvatar(initials = avatarInitials, size = FitAvatarSize.Lg, isPaid = isPaid)
        Column(modifier = Modifier.weight(1f)) {
            Text(
                name,
                style = FitFont.body2.copy(fontWeight = FontWeight.Medium),
                color = if (isPaid) theme.textSecondary else theme.textPrimary
            )
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(FitSpacing.sp1)
            ) {
                Text(
                    subtitle,
                    style = FitFont.caption,
                    color = if (isYou) FitColors.brandPrimary else theme.textSecondary
                )
                when (payment) {
                    FitParticipantPayment.Cash -> FitBadge("Cash", FitBadgeStyle.Neutral)
                    FitParticipantPayment.Card -> FitBadge("Card", FitBadgeStyle.Neutral)
                    FitParticipantPayment.None -> {}
                }
            }
        }
        if (isRemovable && onRemove != null) {
            Box(
                modifier = Modifier
                    .size(28.dp)
                    .clip(CircleShape)
                    .background(FitColors.error.copy(alpha = 0.1f))
                    .clickable(onClick = onRemove),
                contentAlignment = Alignment.Center
            ) {
                Icon(Icons.Default.Close, null, tint = FitColors.error, modifier = Modifier.size(12.dp))
            }
        }
    }
}

// ============================================================================
// FitChip — tag-like selectable button
// ============================================================================

@Composable
fun FitChip(
    label: String,
    isSelected: Boolean,
    onClick: () -> Unit,
    icon: ImageVector? = null,
    modifier: Modifier = Modifier
) {
    val theme = LocalFitTheme.current
    Row(
        modifier = modifier
            .height(48.dp)
            .clip(RoundedCornerShape(FitRadius.md))
            .then(
                if (isSelected)
                    Modifier.background(FitColors.selectionGradient, RoundedCornerShape(FitRadius.md))
                        .border(1.dp, FitColors.selectionBorder, RoundedCornerShape(FitRadius.md))
                else Modifier.background(theme.surfaceHigh)
            )
            .clickable(onClick = onClick)
            .padding(horizontal = FitSpacing.sp3),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(FitSpacing.sp2)
    ) {
        if (icon != null) {
            Icon(icon, null, tint = theme.textPrimary, modifier = Modifier.size(FitSize.iconMd))
        }
        Text(label, style = FitFont.body1, color = theme.textPrimary)
    }
}

// ============================================================================
// FitSelectionGroup — equal-width chip group, single or multi select
//
// Used for: Personal/Group, Recurring/One-off, payment method (Cash/Card),
// online provider (Zoom/Meet/Custom), etc.
//
// Provide either:
//   selectedSingle + onSelectSingle   (single-select / radio behavior)
//   selectedMulti  + onSelectMulti    (multi-select)
// ============================================================================

@Composable
fun <Option> FitSelectionGroup(
    options: List<Option>,
    label: (Option) -> String,
    modifier: Modifier = Modifier,
    selectedSingle: Option? = null,
    onSelectSingle: ((Option) -> Unit)? = null,
    selectedMulti: Set<Option> = emptySet(),
    onSelectMulti: ((Set<Option>) -> Unit)? = null,
) {
    require((onSelectSingle != null) xor (onSelectMulti != null)) {
        "FitSelectionGroup: provide exactly one of onSelectSingle or onSelectMulti"
    }
    val theme = LocalFitTheme.current

    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(FitSpacing.sp2),
    ) {
        options.forEach { option ->
            val isSelected = if (onSelectSingle != null) {
                selectedSingle == option
            } else {
                option in selectedMulti
            }

            Box(
                modifier = Modifier
                    .weight(1f)
                    .height(48.dp)
                    .clip(RoundedCornerShape(FitRadius.md))
                    .then(
                        if (isSelected)
                            Modifier
                                .background(FitColors.selectionGradient, RoundedCornerShape(FitRadius.md))
                                .border(1.dp, FitColors.selectionBorder, RoundedCornerShape(FitRadius.md))
                        else Modifier.background(theme.surfaceHigh)
                    )
                    .clickable {
                        if (onSelectSingle != null) {
                            onSelectSingle(option)
                        } else {
                            val current = selectedMulti
                            val next = if (option in current) current - option else current + option
                            onSelectMulti?.invoke(next)
                        }
                    },
                contentAlignment = Alignment.Center,
            ) {
                Text(
                    label(option),
                    style = FitFont.body1,
                    color = theme.textPrimary,
                )
            }
        }
    }
}
