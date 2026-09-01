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
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.style.TextOverflow
import com.fit321.designtokens.R
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

/** Room kept on both sides of the centred title so a long one cannot slide under the buttons. */
private val TitleSideReserve = 48.dp

@Composable
fun FitHeader(
    title: String,
    modifier: Modifier = Modifier,
    showBack: Boolean = true,
    onBack: (() -> Unit)? = null,
    rightActions: List<FitHeaderAction> = emptyList(),
    backTestTag: String? = null,
    maxLines: Int = Int.MAX_VALUE,
    overflow: TextOverflow = TextOverflow.Clip,
    subtitle: String? = null,
    onTitleClick: (() -> Unit)? = null,
    leading: (@Composable () -> Unit)? = null,
    transparent: Boolean = false,
    trailing: (@Composable () -> Unit)? = null
) {
    val theme = LocalFitTheme.current
    Box(
        modifier = modifier
            .fillMaxWidth()
            .heightIn(min = 48.dp)
            // Transparent for a header that floats over media — a cover photo, a video. The
            // opaque plate is right everywhere else and stays the default.
            .background(if (transparent) Color.Transparent else theme.screenBg)
            .padding(horizontal = FitSpacing.sp4, vertical = FitSpacing.sp2)
    ) {
        // A caller-built leading wins over the back button, the way trailing wins over the
        // icon list: a header starts with a close, a cancel or a step counter often enough
        // that screens were rebuilding the whole bar to change one corner.
        if (leading != null) {
            Box(
                modifier = Modifier.align(Alignment.CenterStart),
                contentAlignment = Alignment.Center
            ) {
                leading()
            }
        } else if (showBack && onBack != null) {
            Box(
                modifier = Modifier
                    .align(Alignment.CenterStart)
                    .size(FitSize.iconBtnSize)
                    .clip(CircleShape)
                    .background(theme.surfaceHigh)
                    .clickable { onBack() }
                    .then(if (backTestTag != null) Modifier.testTag(backTestTag) else Modifier),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    painter = painterResource(R.drawable.ic_fit_chevron_left),
                    contentDescription = null,
                    tint = theme.textPrimary,
                    modifier = Modifier.size(FitSize.iconMd)
                )
            }
        }

        // The title is a block rather than a line: a chat header carries who you are talking
        // to and whether they are online, and a screen that opens a profile from its own title
        // needs the tap to cover both lines, not just the first.
        Column(
            modifier = Modifier
                .align(Alignment.Center)
                .padding(horizontal = TitleSideReserve)
                .then(
                    if (onTitleClick != null) {
                        Modifier.clickable(
                            interactionSource = null,
                            indication = null,
                            onClick = onTitleClick
                        )
                    } else {
                        Modifier
                    }
                ),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                title,
                style = FitFont.navTitle,
                color = theme.textPrimary,
                maxLines = maxLines,
                overflow = overflow
            )
            if (!subtitle.isNullOrBlank()) {
                Text(
                    subtitle,
                    style = FitFont.caption,
                    color = theme.textTertiary,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }
        }

        // A caller-built trailing wins over the icon list: a header ends in a menu, a badge
        // or a text action at least as often as it ends in a row of glyphs.
        when {
            trailing != null -> Box(
                modifier = Modifier.align(Alignment.CenterEnd),
                contentAlignment = Alignment.Center
            ) {
                trailing()
            }

            rightActions.isNotEmpty() -> Row(
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

enum class FitNavTabEnum(val icon: ImageVector) {
    Dashboard(Icons.Default.Home),
    Clients(Icons.Default.People),
    Calendar(Icons.Default.CalendarMonth),
    Messages(Icons.Default.ChatBubbleOutline),
    Settings(Icons.Default.PersonOutline)
}

@Composable
fun FitNavbar(
    activeTab: FitNavTabEnum,
    onTabChange: (FitNavTabEnum) -> Unit,
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
        FitNavTabEnum.values().forEach { tab ->
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
fun FitCardRow(icon: ImageVector, text: String) = FitCardRow(text = text) { tint, size ->
    Icon(icon, null, tint = tint, modifier = Modifier.size(size))
}

/** [FitCardRow] with a caller-supplied glyph — see the note on [FitChip]. */
@Composable
fun FitCardRow(text: String, leading: @Composable (tint: Color, size: Dp) -> Unit) {
    val theme = LocalFitTheme.current
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(FitSpacing.sp2)
    ) {
        leading(theme.textSecondary, FitSize.iconMd)
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
            Icon(ImageVector.vectorResource(R.drawable.ic_fit_chevron_right), null, tint = theme.textTertiary, modifier = Modifier.size(FitSize.iconMd))
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
