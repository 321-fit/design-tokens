package com.fit321.fitui.v3.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.unit.dp
import com.fit321.fitui.components.FitNavbarBadge
import com.fit321.fitui.components.FitNavbarItem
import com.fit321.fitui.tokens.FitColors
import com.fit321.fitui.v3.theme.LocalFitV3Palette

/**
 * `.fit-navbar` on the rework canvas: a floating pill that darkens what is under it (tinted) or
 * frosts it (light), with the active tab in the selection wash. Items keep the web's fixed
 * 56px squares, centred with 12px between them, rather than splitting the width.
 */
@Composable
fun <T> FitV3Navbar(
    items: List<FitNavbarItem<T>>,
    activeTab: T,
    onTabChange: (T) -> Unit,
    modifier: Modifier = Modifier,
) {
    val palette = LocalFitV3Palette.current
    val pill = RoundedCornerShape(percent = 50)
    Row(
        modifier = modifier
            .fillMaxWidth()
            .height(56.dp)
            .shadow(24.dp, pill, ambientColor = palette.navbarShadow, spotColor = palette.navbarShadow)
            .clip(pill)
            .background(palette.navbarFill, pill)
            .border(1.dp, palette.navbarEdge, pill)
            .padding(4.dp),
        horizontalArrangement = Arrangement.spacedBy(12.dp, Alignment.CenterHorizontally),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        items.forEach { item ->
            val active = item.tab == activeTab
            Box(contentAlignment = Alignment.Center) {
                Box(
                    modifier = Modifier
                        .size(48.dp)
                        .clip(pill)
                        .then(if (active) Modifier.background(FitColors.selectionGradient, pill) else Modifier)
                        .clickable(
                            interactionSource = remember { MutableInteractionSource() },
                            indication = null,
                        ) { onTabChange(item.tab) },
                    contentAlignment = Alignment.Center,
                ) {
                    Icon(
                        imageVector = item.icon,
                        contentDescription = item.label,
                        tint = if (active) palette.navbarIconActive else palette.navbarIcon,
                        modifier = Modifier.size(24.dp),
                    )
                }
                if (item.dot && !active) {
                    // `.fit-navbar-dot`: 8 at 14/14 inside the web's 56 square, i.e. 10/10 in our 48; the 2 ring sits outside it.
                    Box(
                        modifier = Modifier
                            .align(Alignment.TopEnd)
                            .offset(x = (-8).dp, y = 8.dp)
                            .size(12.dp)
                            .background(palette.navbarDotRing, CircleShape)
                            .padding(2.dp)
                            .background(FitColors.Red.r400, CircleShape),
                    )
                }
                if (item.badgeCount > 0) {
                    Box(modifier = Modifier.size(24.dp)) {
                        FitNavbarBadge(
                            count = item.badgeCount,
                            modifier = Modifier
                                .align(Alignment.TopStart)
                                .wrapContentSize(align = Alignment.TopStart, unbounded = true)
                                .offset(x = 18.dp, y = (-9).dp),
                        )
                    }
                }
            }
        }
    }
}
