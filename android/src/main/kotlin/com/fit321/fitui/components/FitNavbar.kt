package com.fit321.fitui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.fit321.fitui.theme.LocalFitTheme
import com.fit321.fitui.tokens.FitColors
import com.fit321.fitui.tokens.FitFont
import com.fit321.fitui.tokens.FitSize
import com.fit321.fitui.tokens.FitSpacing

/**
 * Floating bottom navigation bar (glass pill). Active tab gets the
 * selection-gradient capsule. Mirrors Swift `FitNavbar`.
 *
 * Visibility: render only on the 5 root tab screens
 * (Dashboard, Clients, Calendar, Messages, Settings) — see
 * `feedback_navbar_visibility` rule. Caller is responsible for filtering.
 *
 * Icon source is intentionally decoupled from any specific icon set:
 * pass `ImageVector` per tab. Consumers can mix Material icons with
 * custom 24×24 vectors per the design (sport-set glyphs, etc.) without
 * forcing the library to take a side.
 */
enum class FitNavTab { Dashboard, Clients, Calendar, Messages, Settings }

data class FitNavbarItem<T>(
    val tab: T,
    val icon: ImageVector,
    /** Unread count on this tab. Zero draws nothing. */
    val badgeCount: Int = 0
)

/**
 * The tab type is the consumer's own, not this library's: a product with a role-specific
 * tab set, or one tab the design gives the middle slot, cannot express itself in a closed
 * enum. [FitNavTab] is still there for a consumer that wants the named five.
 *
 * The bar draws the pill and nothing around it: the margin under it and the room a scrolling
 * tab has to leave for it are the caller's, because only the caller knows the window insets.
 *
 * Two deliberate departures from `.fit-navbar`, both because the web rule does not survive
 * the platform. The pill is an opaque surface rather than translucent white — Compose has no
 * backdrop blur below API 31, and translucency without the blur behind it reads as a smear of
 * whatever scrolled under it. And the shadow is drawn on light only, where the pill has to
 * separate from a pale page; on dark it lands on a dark ground and does nothing but cost a
 * layer. Items take equal shares of the width instead of a centred row of fixed squares, so
 * the touch targets grow with the screen rather than clustering in the middle of a tablet.
 */
@Composable
fun <T> FitNavbar(
    items: List<FitNavbarItem<T>>,
    activeTab: T,
    onTabChange: (T) -> Unit,
    modifier: Modifier = Modifier
) {
    val theme = LocalFitTheme.current
    val isDark = theme === FitColors.Theme.dark
    val pill = RoundedCornerShape(percent = 50)

    Row(
        modifier = modifier
            .fillMaxWidth()
            .height(FitSize.navbarHeight)
            .then(if (isDark) Modifier else Modifier.shadow(12.dp, pill))
            .clip(pill)
            .background(if (isDark) theme.surfaceHigher else theme.surfaceHigh)
            .padding(horizontal = FitSpacing.sp1),
        horizontalArrangement = Arrangement.spacedBy(FitSpacing.sp1),
        verticalAlignment = Alignment.CenterVertically
    ) {
        items.forEach { item ->
            val isActive = item.tab == activeTab
            Box(
                modifier = Modifier
                    .weight(1f)
                    .height(FitSize.navbarItemSize),
                contentAlignment = Alignment.Center
            ) {
                Box(
                    modifier = Modifier
                        .size(FitSize.navbarItemSize)
                        .clip(pill)
                        .then(
                            if (isActive) Modifier.background(FitColors.selectionGradient, pill)
                            else Modifier
                        )
                        .clickable(
                            interactionSource = remember { MutableInteractionSource() },
                            indication = null
                        ) { onTabChange(item.tab) },
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = item.icon,
                        contentDescription = null,
                        tint = when {
                            isActive && isDark -> FitColors.Gray.white
                            isActive -> theme.textPrimary
                            isDark -> FitColors.Gray.g300
                            else -> FitColors.Gray.g400
                        },
                        modifier = Modifier.size(FitSize.iconLg)
                    )
                }
                if (item.badgeCount > 0) {
                    Box(modifier = Modifier.size(FitSize.iconLg)) {
                        FitNavbarBadge(
                            count = item.badgeCount,
                            modifier = Modifier
                                .align(Alignment.TopStart)
                                .wrapContentSize(align = Alignment.TopStart, unbounded = true)
                                .offset(x = FitSize.iconLg - 6.dp, y = (-9).dp)
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun FitNavbarBadge(count: Int, modifier: Modifier = Modifier) {
    Box(
        modifier = modifier
            .defaultMinSize(minWidth = 16.dp)
            .height(16.dp)
            .clip(RoundedCornerShape(percent = 50))
            .background(FitColors.Red.r400)
            .padding(horizontal = 4.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = if (count > 9) "9+" else count.toString(),
            style = FitFont.captionMicro.copy(fontSize = 10.sp, fontWeight = FontWeight.SemiBold),
            color = FitColors.Gray.white
        )
    }
}
