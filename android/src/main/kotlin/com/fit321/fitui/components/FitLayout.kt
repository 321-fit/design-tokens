package com.fit321.fitui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.ChatBubbleOutline
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.People
import androidx.compose.material.icons.filled.PersonOutline
import androidx.compose.material.icons.filled.Schedule
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
import androidx.compose.ui.unit.sp
import com.fit321.fitui.theme.LocalFitTheme
import com.fit321.fitui.tokens.FitColors
import com.fit321.fitui.tokens.FitFont
import com.fit321.fitui.tokens.FitRadius
import com.fit321.fitui.tokens.FitSize
import com.fit321.fitui.tokens.FitSpacing

// ============================================================================
// FitHeader — centered title + optional back + optional right actions
// ============================================================================

data class FitHeaderAction(val icon: ImageVector, val onClick: () -> Unit)

@Composable
fun FitHeader(
    title: String,
    showBack: Boolean = false,
    onBack: (() -> Unit)? = null,
    rightActions: List<FitHeaderAction> = emptyList(),
    modifier: Modifier = Modifier
) {
    val theme = LocalFitTheme.current
    Box(
        modifier = modifier
            .fillMaxWidth()
            .heightIn(min = 48.dp)
            .background(theme.screenBg)
            .padding(horizontal = FitSpacing.sp4, vertical = FitSpacing.sp2)
    ) {
        // Left
        if (showBack && onBack != null) {
            Box(
                modifier = Modifier
                    .size(FitSize.iconBtnSize)
                    .clip(CircleShape)
                    .background(theme.surfaceHigh.copy(alpha = 0.3f))
                    .clickable { onBack() }
                    .align(Alignment.CenterStart),
                contentAlignment = Alignment.Center
            ) {
                Icon(Icons.Default.ArrowBack, null, tint = theme.textPrimary, modifier = Modifier.size(FitSize.iconMd))
            }
        }

        // Title (centered)
        Text(
            title,
            style = FitFont.navTitle,
            color = theme.textPrimary,
            modifier = Modifier.align(Alignment.Center)
        )

        // Right actions
        if (rightActions.isNotEmpty()) {
            Row(
                modifier = Modifier.align(Alignment.CenterEnd),
                horizontalArrangement = Arrangement.spacedBy(FitSpacing.sp3)
            ) {
                rightActions.forEach { action ->
                    FitIconBtn(icon = action.icon, onClick = action.onClick)
                }
            }
        }
    }
}

// ============================================================================
// FitFooter — sticky bottom with safe area + content
// ============================================================================

@Composable
fun FitFooter(
    topPadding: androidx.compose.ui.unit.Dp = 12.dp,
    content: @Composable () -> Unit
) {
    val theme = LocalFitTheme.current
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .background(theme.screenBg)
            .padding(horizontal = FitSpacing.sp4)
            .padding(top = topPadding, bottom = FitSpacing.sp8)
    ) {
        content()
    }
}

// ============================================================================
// FitNavbar — 5-tab floating pill
// ============================================================================

enum class FitNavTab(val icon: ImageVector) {
    Dashboard(Icons.Default.Home),
    Clients(Icons.Default.People),
    Calendar(Icons.Default.CalendarMonth),
    Messages(Icons.Default.ChatBubbleOutline),
    Settings(Icons.Default.PersonOutline)
}

@Composable
fun FitNavbar(
    activeTab: FitNavTab,
    onTabChange: (FitNavTab) -> Unit,
    modifier: Modifier = Modifier
) {
    val theme = LocalFitTheme.current
    Row(
        modifier = modifier
            .padding(horizontal = FitSpacing.sp4, vertical = FitSpacing.sp4)
            .clip(CircleShape)
            .background(theme.surfaceHigh.copy(alpha = 0.85f))
            .padding(FitSpacing.sp1),
        horizontalArrangement = Arrangement.spacedBy(FitSpacing.sp3)
    ) {
        FitNavTab.values().forEach { tab ->
            val active = activeTab == tab
            Box(
                modifier = Modifier
                    .size(FitSize.navbarItemSize)
                    .clip(CircleShape)
                    .then(
                        if (active) Modifier.background(FitColors.selectionGradient, CircleShape)
                        else Modifier
                    )
                    .clickable { onTabChange(tab) },
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    tab.icon,
                    null,
                    tint = if (active) theme.textPrimary else FitColors.Gray.g300,
                    modifier = Modifier.size(FitSize.iconLg)
                )
            }
        }
    }
}
// ============================================================================
// FitCard — content container with optional title + edit
// ============================================================================

@Composable
fun FitCard(
    title: String? = null,
    onEdit: (() -> Unit)? = null,
    modifier: Modifier = Modifier,
    content: @Composable ColumnScope.() -> Unit
) {
    val theme = LocalFitTheme.current
    Column(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(FitRadius.card))
            .background(theme.cardBg)
            .border(1.dp, theme.divider, RoundedCornerShape(FitRadius.card))
            .padding(FitSpacing.sp5),
        verticalArrangement = Arrangement.spacedBy(FitSpacing.sp3)
    ) {
        if (title != null) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                    title,
                    style = FitFont.body1.copy(fontSize = 18.sp, fontWeight = FontWeight.Medium),
                    color = theme.textPrimary,
                    modifier = Modifier.weight(1f)
                )
                if (onEdit != null) {
                    Icon(Icons.Default.Edit, null, tint = theme.textTertiary, modifier = Modifier.size(FitSize.iconMd).clickable(onClick = onEdit))
                }
            }
        }
        content()
    }
}

@Composable
fun FitCardRow(icon: ImageVector, text: String) {
    val theme = LocalFitTheme.current
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(FitSpacing.sp2)
    ) {
        Icon(icon, null, tint = theme.textSecondary, modifier = Modifier.size(FitSize.iconMd))
        Text(text, style = FitFont.body2, color = theme.textSecondary)
    }
}

// ============================================================================
// FitTicket — compact event summary card
// ============================================================================

@Composable
fun FitTicket(
    title: String,
    time: String,
    coachName: String,
    coachInitials: String,
    price: String,
    meta: List<String> = emptyList(),
    onTap: (() -> Unit)? = null,
    modifier: Modifier = Modifier
) {
    val theme = LocalFitTheme.current
    Column(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(FitRadius.md))
            .background(theme.cardBg)
            .then(if (onTap != null) Modifier.clickable { onTap() } else Modifier)
            .padding(FitSpacing.sp4),
        verticalArrangement = Arrangement.spacedBy(FitSpacing.sp3)
    ) {
        Row(verticalAlignment = Alignment.Top) {
            Text(title, style = FitFont.button1, color = theme.textPrimary, modifier = Modifier.weight(1f))
            Icon(Icons.Default.ChevronRight, null, tint = theme.textTertiary, modifier = Modifier.size(FitSize.iconMd))
        }
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(FitSpacing.sp2)
        ) {
            Icon(Icons.Default.Schedule, null, tint = theme.textSecondary, modifier = Modifier.size(13.dp))
            Text(time, style = FitFont.body2, color = theme.textSecondary)
        }
        if (meta.isNotEmpty()) {
            Row(horizontalArrangement = Arrangement.spacedBy(FitSpacing.sp2)) {
                meta.forEach { Text(it, style = FitFont.caption, color = theme.textTertiary) }
            }
        }
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(FitSpacing.sp3)
        ) {
            FitAvatar(initials = coachInitials, size = FitAvatarSize.Sm)
            Text(coachName, style = FitFont.body2.copy(fontWeight = FontWeight.Medium), color = theme.textPrimary, modifier = Modifier.weight(1f))
            Text(price, style = FitFont.button2, color = FitColors.Teal.t500)
        }
    }
}
