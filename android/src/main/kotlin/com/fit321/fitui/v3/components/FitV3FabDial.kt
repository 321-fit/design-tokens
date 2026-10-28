package com.fit321.fitui.v3.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.calculateEndPadding
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.unit.dp
import com.fit321.designtokens.R
import com.fit321.fitui.tokens.FitColors
import com.fit321.fitui.v3.theme.LocalFitV3Palette
import com.fit321.fitui.v3.tokens.FitV3Look
import com.fit321.fitui.v3.tokens.FitV3Type

/** One action of [FitFabDial]: a pill with the glyph left and the word right. */
data class FitFabDialItem(
    val label: String,
    val icon: @Composable () -> Unit,
    val onClick: () -> Unit,
)

private const val TURN_MS = 180
private const val RISE_MS = 160
private const val STAGGER_MS = 30
private val FabSize = 56.dp
private val FabInk = Color(0xFF0B1418)

/**
 * `.fit-fab-dial` (v2.5) — the calendar's speed dial. Tap the white FAB: a scrim dims the
 * screen, `+` turns to `×`, and [items] rise above it as pills, the last one nearest the
 * thumb and first to arrive. Tapping the scrim or `×` closes it; an item closes it too.
 *
 * Fill the screen with it: the scrim is the dial's own, so it dims what the dial covers.
 * [fabPadding] places the FAB from the bottom-end corner; the pills keep 6 more on the end
 * and 14 above the FAB, as the web does. System back is the caller's — the kit does not
 * depend on an activity.
 */
@Composable
fun BoxScope.FitFabDial(
    expanded: Boolean,
    onExpandedChange: (Boolean) -> Unit,
    items: List<FitFabDialItem>,
    modifier: Modifier = Modifier,
    fabPadding: PaddingValues = PaddingValues(end = 20.dp, bottom = 20.dp),
    /** Screen-reader names for the FAB, closed and open; the kit carries no strings. */
    openLabel: String? = null,
    closeLabel: String? = null,
) {
    val palette = LocalFitV3Palette.current
    val light = palette.look == FitV3Look.Light
    val layoutDirection = LocalLayoutDirection.current
    val fabEnd = fabPadding.calculateEndPadding(layoutDirection)
    val fabBottom = fabPadding.calculateBottomPadding()
    val scrimAlpha by animateFloatAsState(if (expanded) 1f else 0f, tween(TURN_MS), label = "fabScrim")
    val turn by animateFloatAsState(if (expanded) 45f else 0f, tween(TURN_MS), label = "fabTurn")

    Box(modifier = modifier.matchParentSize()) {
        if (scrimAlpha > 0f) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .alpha(scrimAlpha)
                    .background(palette.fabScrim)
                    .clickable(
                        interactionSource = remember { MutableInteractionSource() },
                        indication = null,
                    ) { onExpandedChange(false) },
            )
        }
        Column(
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(end = fabEnd + 6.dp, bottom = fabBottom + FabSize + 14.dp),
            horizontalAlignment = Alignment.End,
            verticalArrangement = Arrangement.spacedBy(14.dp),
        ) {
            val rise = with(LocalDensity.current) { 12.dp.roundToPx() }
            items.forEachIndexed { index, item ->
                // Nearest the FAB arrives first, each one above it 30 ms later.
                val delay = (items.lastIndex - index) * STAGGER_MS
                AnimatedVisibility(
                    visible = expanded,
                    enter = fadeIn(tween(RISE_MS, delay)) + slideInVertically(tween(RISE_MS, delay)) { rise },
                    exit = fadeOut(tween(RISE_MS)) + slideOutVertically(tween(RISE_MS)) { rise },
                ) {
                    FabPill(item = item, light = light) {
                        onExpandedChange(false)
                        item.onClick()
                    }
                }
            }
        }
        Box(
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(fabPadding)
                .size(FabSize)
                .shadow(if (light) 10.dp else 14.dp, CircleShape)
                .clip(CircleShape)
                .background(FitColors.Gray.white, CircleShape)
                .clickable { onExpandedChange(!expanded) }
                .semantics { (if (expanded) closeLabel else openLabel)?.let { contentDescription = it } },
            contentAlignment = Alignment.Center,
        ) {
            Icon(
                painter = painterResource(R.drawable.ic_fit_plus),
                contentDescription = null,
                tint = FabInk,
                modifier = Modifier.size(24.dp).rotate(turn),
            )
        }
    }
}

@Composable
private fun FabPill(item: FitFabDialItem, light: Boolean, onClick: () -> Unit) {
    val palette = LocalFitV3Palette.current
    val shape = RoundedCornerShape(22.dp)
    Row(
        modifier = Modifier
            .height(44.dp)
            .shadow(if (light) 6.dp else 16.dp, shape)
            .clip(shape)
            .background(palette.fabItemFill, shape)
            .border(1.dp, palette.fabItemEdge, shape)
            .clickable(onClick = onClick)
            .padding(start = 14.dp, end = 16.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(10.dp),
    ) {
        Box(modifier = Modifier.size(20.dp), contentAlignment = Alignment.Center) {
            CompositionLocalProvider(LocalContentColor provides palette.textPrimary) { item.icon() }
        }
        Text(text = item.label, style = FitV3Type.fabLabel, color = palette.textPrimary, maxLines = 1)
    }
}
