package com.fit321.fitui.v3.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.dp
import com.fit321.fitui.v3.theme.LocalFitV3Palette
import com.fit321.fitui.v3.tokens.FitV3Type

/** One line of a [FitV3Menu]: a glyph, a verb, and whether it is the destructive one. */
class FitV3MenuItem(
    val label: String,
    val destructive: Boolean = false,
    /** A rule above this item — the group a destructive act is kept apart from. */
    val dividerBefore: Boolean = false,
    val modifier: Modifier = Modifier,
    val onClick: () -> Unit,
    val icon: @Composable () -> Unit,
)

/**
 * The short list a "⋯" hangs off — `fit-ui.css .fit-context-menu`.
 *
 * It floats above the screen, so it wears the sheet's own surface rather than the canvas-tinted
 * panel every row on the page behind it is made of: a menu that borrowed the page's surface
 * would read as part of the page, which is the one thing a menu must not do.
 */
@Composable
fun FitV3Menu(
    expanded: Boolean,
    onDismiss: () -> Unit,
    items: List<FitV3MenuItem>,
    modifier: Modifier = Modifier,
) {
    val palette = LocalFitV3Palette.current
    DropdownMenu(
        expanded = expanded,
        onDismissRequest = onDismiss,
        // `.fit-context-menu--anchor-header` is `top: calc(100% - 6px)` on the header, so the
        // sheet rides up over the lower half of the circle it belongs to instead of dropping
        // clear of it. Anchored under a 40 circle, that is half its height back.
        offset = DpOffset(0.dp, -MENU_RIDE),
        shape = RoundedCornerShape(14.dp),
        containerColor = palette.material,
        border = androidx.compose.foundation.BorderStroke(1.dp, palette.materialEdge),
        shadowElevation = 12.dp,
        modifier = modifier.widthIn(min = 220.dp),
    ) {
        Column(modifier = Modifier.padding(6.dp)) {
            items.forEach { item ->
                if (item.dividerBefore) {
                    HorizontalDivider(
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                        thickness = 1.dp,
                        color = palette.divider,
                    )
                }
                MenuRow(item = item, onDismiss = onDismiss)
            }
        }
    }
}

@Composable
private fun MenuRow(item: FitV3MenuItem, onDismiss: () -> Unit) {
    val palette = LocalFitV3Palette.current
    val ink: Color = if (item.destructive) palette.textError else palette.textPrimary
    val glyph: Color = if (item.destructive) palette.textError else palette.textSecondary
    Row(
        modifier = item.modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(8.dp))
            .clickable {
                onDismiss()
                item.onClick()
            }
            .padding(horizontal = 12.dp, vertical = 10.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(12.dp),
    ) {
        CompositionLocalProvider(LocalContentColor provides glyph) { item.icon() }
        Text(text = item.label, style = FitV3Type.menuItem, color = ink)
    }
}

private val MENU_RIDE = 22.dp
