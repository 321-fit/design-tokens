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
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
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
// Extension: context (settings/location/space), isDefault badge,
// addressOrSubtitle (single-line ellipsised) for non-settings contexts.
// ============================================================================

enum class FitSettingsTrailing { Chevron, None }

enum class FitSettingsCardContext { Settings, Location, Space }

@Composable
fun FitSettingsCard(
    icon: ImageVector,
    title: String,
    subtitle: String? = null,
    addressOrSubtitle: String? = null,
    context: FitSettingsCardContext = FitSettingsCardContext.Settings,
    isDefault: Boolean = false,
    trailing: FitSettingsTrailing = FitSettingsTrailing.Chevron,
    destructive: Boolean = false,
    onClick: (() -> Unit)? = null,
    modifier: Modifier = Modifier
) {
    val theme = LocalFitTheme.current
    val titleColor = if (destructive) FitColors.error else theme.textPrimary
    val effectiveSubtitle = when (context) {
        FitSettingsCardContext.Location, FitSettingsCardContext.Space -> addressOrSubtitle ?: subtitle
        FitSettingsCardContext.Settings -> subtitle
    }

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
        when (context) {
            FitSettingsCardContext.Settings ->
                Icon(icon, null, tint = titleColor, modifier = Modifier.size(FitSize.settingsCardIcon))
            FitSettingsCardContext.Location, FitSettingsCardContext.Space ->
                Box(
                    modifier = Modifier
                        .size(FitSize.avatarMd)
                        .clip(RoundedCornerShape(FitRadius.sm))
                        .background(theme.surfaceHigher),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(icon, null, tint = titleColor, modifier = Modifier.size(FitSize.iconLg))
                }
        }

        Column(modifier = Modifier.weight(1f)) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(FitSpacing.sp2)
            ) {
                Text(title, style = FitFont.body1, color = titleColor, maxLines = 1, overflow = TextOverflow.Ellipsis)
                if (isDefault) {
                    FitBadge("Default", FitBadgeStyle.Accent)
                }
            }
            if (effectiveSubtitle != null) {
                val maxLines = if (context == FitSettingsCardContext.Settings) 2 else 1
                Text(
                    effectiveSubtitle,
                    style = FitFont.body2,
                    color = theme.textSecondary,
                    maxLines = maxLines,
                    overflow = TextOverflow.Ellipsis
                )
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
// Extensions: leading (avatar | icon), trailing (chevron|edit|dot|swipe|none),
// state (default|connected|disconnected|disabled|destructive).
// ============================================================================

enum class FitParticipantPayment { Cash, Card, None }

sealed class FitParticipantLeading {
    data class Avatar(val initials: String) : FitParticipantLeading()
    data class IconPlate(val icon: ImageVector, val tone: FitIconPlateTone = FitIconPlateTone.Neutral)
        : FitParticipantLeading()
}

sealed class FitParticipantTrailing {
    object Chevron : FitParticipantTrailing()
    object Edit : FitParticipantTrailing()
    data class Dot(val color: Color) : FitParticipantTrailing()
    object Swipe : FitParticipantTrailing()
    object None : FitParticipantTrailing()
}

enum class FitParticipantState { Default, Connected, Disconnected, Disabled, Destructive }

@Composable
fun FitParticipant(
    name: String,
    subtitle: String,
    leading: FitParticipantLeading,
    trailing: FitParticipantTrailing = FitParticipantTrailing.Chevron,
    state: FitParticipantState = FitParticipantState.Default,
    isRemovable: Boolean = false,
    onRemove: (() -> Unit)? = null,
    isPaid: Boolean = false,
    payment: FitParticipantPayment = FitParticipantPayment.None,
    isYou: Boolean = false,
    onTap: (() -> Unit)? = null,
    modifier: Modifier = Modifier
) {
    val theme = LocalFitTheme.current

    val nameColor: Color = when {
        isPaid -> theme.textSecondary
        state == FitParticipantState.Destructive -> FitColors.error
        state == FitParticipantState.Disabled -> theme.textTertiary
        else -> theme.textPrimary
    }
    val subtitleColor: Color = when {
        isYou -> FitColors.brandPrimary
        state == FitParticipantState.Destructive -> FitColors.error.copy(alpha = 0.85f)
        else -> theme.textSecondary
    }
    val rowBg: Color = when {
        isYou -> Color.Transparent           // gradient overlays via background brush below
        state == FitParticipantState.Connected -> theme.bgSuccessSubtle
        state == FitParticipantState.Disconnected -> theme.bgWarningSubtle
        state == FitParticipantState.Destructive -> theme.bgErrorSubtle
        else -> Color.Transparent
    }
    val rowRadius =
        if (isYou || state in setOf(
                FitParticipantState.Connected,
                FitParticipantState.Disconnected,
                FitParticipantState.Destructive
            )
        ) FitRadius.md else 0.dp

    Row(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(rowRadius))
            .then(
                if (isYou) Modifier.background(FitColors.selectionGradient, RoundedCornerShape(rowRadius))
                else Modifier.background(rowBg)
            )
            .then(
                if (onTap != null && state != FitParticipantState.Disabled)
                    Modifier.clickable(onClick = onTap) else Modifier
            )
            .padding(horizontal = if (isYou) FitSpacing.sp3 else 0.dp, vertical = FitSpacing.sp3)
            .alpha(if (state == FitParticipantState.Disabled) 0.6f else 1f),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(FitSpacing.sp3)
    ) {
        // Leading
        when (val l = leading) {
            is FitParticipantLeading.Avatar ->
                FitAvatar(initials = l.initials, size = FitAvatarSize.Lg, isPaid = isPaid)
            is FitParticipantLeading.IconPlate ->
                FitIconPlate(icon = l.icon, tone = l.tone, size = FitIconPlateSize.Lg)
        }

        Column(modifier = Modifier.weight(1f)) {
            Text(
                name,
                style = FitFont.body2.copy(fontWeight = FontWeight.Medium),
                color = nameColor
            )
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(FitSpacing.sp1)
            ) {
                Text(subtitle, style = FitFont.caption, color = subtitleColor)
                when (payment) {
                    FitParticipantPayment.Cash -> FitBadge("Cash", FitBadgeStyle.Neutral)
                    FitParticipantPayment.Card -> FitBadge("Card", FitBadgeStyle.Neutral)
                    FitParticipantPayment.None -> {}
                }
            }
        }

        // Trailing — explicit remove button takes precedence (legacy support)
        when {
            isRemovable && onRemove != null -> {
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
            trailing is FitParticipantTrailing.Chevron ->
                Icon(Icons.Default.ChevronRight, null, tint = theme.textTertiary, modifier = Modifier.size(16.dp))
            trailing is FitParticipantTrailing.Edit ->
                Icon(Icons.Default.Edit, null, tint = theme.textTertiary, modifier = Modifier.size(16.dp))
            trailing is FitParticipantTrailing.Dot ->
                Box(modifier = Modifier.size(8.dp).clip(CircleShape).background(trailing.color))
            trailing is FitParticipantTrailing.Swipe -> {}
            trailing is FitParticipantTrailing.None -> {}
        }
    }
}

// ============================================================================
// FitChip — tag-like selectable button (now with size enum)
// ============================================================================

enum class FitChipSize(val heightDp: Int) {
    Sm(40),
    Md(48),
    Lg(56)
}

@Composable
fun FitChip(
    label: String,
    isSelected: Boolean,
    onClick: () -> Unit,
    icon: ImageVector? = null,
    size: FitChipSize = FitChipSize.Md,
    modifier: Modifier = Modifier
) {
    val theme = LocalFitTheme.current
    Row(
        modifier = modifier
            .height(size.heightDp.dp)
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
        val style = if (size == FitChipSize.Sm) FitFont.body2 else FitFont.body1
        Text(label, style = style, color = theme.textPrimary)
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

// ----------------------------------------------------------------------------
// Internal helper — alpha modifier (avoids importing graphicsLayer for simple alpha)
// ----------------------------------------------------------------------------

private fun Modifier.alpha(alpha: Float): Modifier =
    this.then(androidx.compose.ui.draw.alpha(alpha))
