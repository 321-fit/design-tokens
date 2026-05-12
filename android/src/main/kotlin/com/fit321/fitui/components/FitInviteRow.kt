package com.fit321.fitui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.fit321.fitui.theme.LocalFitTheme
import com.fit321.fitui.tokens.FitColors
import com.fit321.fitui.tokens.FitFont

// ============================================================================
// FitInviteRow — referral list row. Avatar + name + when, with an optional
// trailing slot for Phase 2 status pills / chevrons. No trailing in MVP
// per `invite-coach.md` decision (share-link path can't track "pending").
// Mirrors Swift `FitInviteRow`. See `docs/components.md` § FitInviteRow.
// ============================================================================

@Composable
fun FitInviteRow(
    initials: String,
    name: String,
    whenText: String,
    onTap: (() -> Unit)? = null,
    modifier: Modifier = Modifier,
    trailing: (@Composable () -> Unit)? = null
) {
    val theme = LocalFitTheme.current
    val isLight = theme.screenBg != FitColors.Gray.g900
    val shape = RoundedCornerShape(12.dp)

    val containerModifier = modifier
        .fillMaxWidth()
        .clip(shape)
        .background(if (isLight) FitColors.Gray.white else theme.surfaceHigh)
        .then(if (isLight) Modifier.border(1.dp, theme.divider, shape) else Modifier)
        .then(if (onTap != null) Modifier.clickable { onTap() } else Modifier)
        .padding(12.dp)

    Row(
        modifier = containerModifier,
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        FitAvatar(initials = initials, size = FitAvatarSize.Md)

        Column(
            modifier = Modifier.weight(1f),
            verticalArrangement = Arrangement.spacedBy(2.dp)
        ) {
            Text(
                name,
                style = FitFont.body1.copy(fontSize = 15.sp, fontWeight = FontWeight.Medium),
                color = theme.textPrimary,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
            Text(
                whenText,
                style = FitFont.footnote,
                color = theme.textTertiary,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        }

        if (trailing != null) {
            trailing()
        }
    }
}
