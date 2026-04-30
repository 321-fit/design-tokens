package com.fit321.fitui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import com.fit321.fitui.theme.LocalFitTheme
import com.fit321.fitui.tokens.FitColors
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

data class FitNavbarItem(
    val tab: FitNavTab,
    val icon: ImageVector
)

@Composable
fun FitNavbar(
    items: List<FitNavbarItem>,
    activeTab: FitNavTab,
    onTabChange: (FitNavTab) -> Unit,
    modifier: Modifier = Modifier
) {
    val theme = LocalFitTheme.current
    val pill = RoundedCornerShape(percent = 50)

    Row(
        modifier = modifier
            .padding(horizontal = FitSpacing.sp4, vertical = FitSpacing.sp4)
            .clip(pill)
            .background(theme.surfaceHigh.copy(alpha = 0.85f))
            .padding(FitSpacing.sp1),
        horizontalArrangement = Arrangement.spacedBy(FitSpacing.sp3),
        verticalAlignment = Alignment.CenterVertically
    ) {
        items.forEach { item ->
            val isActive = item.tab == activeTab
            Box(
                modifier = Modifier
                    .size(FitSize.navbarItemSize)
                    .clip(pill)
                    .then(
                        if (isActive)
                            Modifier.background(FitColors.selectionGradient, pill)
                        else Modifier
                    )
                    .clickable { onTabChange(item.tab) },
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = item.icon,
                    contentDescription = item.tab.name,
                    tint = if (isActive) theme.textPrimary else FitColors.Gray.g300,
                    modifier = Modifier.size(FitSize.iconLg)
                )
            }
        }
    }
}
