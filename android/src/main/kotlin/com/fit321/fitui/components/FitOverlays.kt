package com.fit321.fitui.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.fit321.fitui.theme.LocalFitTheme
import com.fit321.fitui.tokens.FitColors
import com.fit321.fitui.tokens.FitFont
import com.fit321.fitui.tokens.FitRadius
import com.fit321.fitui.tokens.FitSpacing
import kotlinx.coroutines.delay

// ============================================================================
// FitSheet — bottom sheet modal
// ============================================================================

enum class FitSheetVariant { Standard, Compact }

/**
 * The app's one bottom sheet.
 *
 * Built on Material 3 `ModalBottomSheet`, which is what gives it the behaviour a sheet is
 * expected to have and a hand-rolled `Dialog` cannot: **swipe down to dismiss**, drag up /
 * drag down between a partially-expanded and a fully-expanded height for tall content,
 * nested scroll (scrolling the content to its top and continuing to pull drags the sheet
 * instead), velocity-aware settling, and predictive back. The scrim dismisses on tap with
 * no ripple — the old version put `clickable` on the full-screen scrim, so every outside
 * tap painted a screen-sized ripple blob.
 *
 * [isVisible] is kept as the API because ~50 call sites drive the sheet from their own
 * state. The sheet stays composed for the length of its own hide animation after
 * [isVisible] flips to false, so closing from a button inside the sheet slides out exactly
 * like a swipe instead of vanishing.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FitSheet(
    isVisible: Boolean,
    onDismiss: () -> Unit,
    variant: FitSheetVariant = FitSheetVariant.Standard,
    content: @Composable () -> Unit
) {
    val theme = LocalFitTheme.current
    // Content sheets open straight to full height — a half-expanded first stop makes a short
    // sheet peek and forces a second drag to see its CTA.
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    var rendered by remember { mutableStateOf(false) }

    LaunchedEffect(isVisible) {
        if (isVisible) {
            rendered = true
        } else if (rendered) {
            // hide() throws if the sheet was already torn down (e.g. a swipe dismiss that
            // flipped isVisible on its own) — that path has already animated, so skip it.
            runCatching { sheetState.hide() }
            rendered = false
        }
    }

    if (!rendered) return

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        sheetState = sheetState,
        shape = RoundedCornerShape(topStart = FitRadius.lg, topEnd = FitRadius.lg),
        containerColor = theme.screenBg,
        contentColor = theme.textPrimary,
        tonalElevation = 0.dp,
        scrimColor = Color.Black.copy(alpha = 0.5f),
        dragHandle = { FitSheetDragHandle() },
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(
                    start = FitSpacing.sp4,
                    end = FitSpacing.sp4,
                    // Same bottom breathing room as before, plus the gesture bar — the
                    // sheet's own insets only cover the top.
                    bottom = if (variant == FitSheetVariant.Standard) FitSpacing.sp9 else 28.dp
                )
                .navigationBarsPadding()
        ) {
            content()
        }
    }
}

/** 36×4 pill in divider grey — the handle the app has always drawn, kept off M3 defaults. */
@Composable
private fun FitSheetDragHandle() {
    val theme = LocalFitTheme.current
    Box(
        modifier = Modifier.fillMaxWidth().padding(top = FitSpacing.sp2, bottom = FitSpacing.sp4),
        contentAlignment = Alignment.Center
    ) {
        Box(
            modifier = Modifier
                .size(36.dp, 4.dp)
                .clip(RoundedCornerShape(2.dp))
                .background(theme.divider)
        )
    }
}

/**
 * Sheet status header: descriptor + optional status pill + optional trailing actions.
 *
 * [actions] is the right-hand action slot — icon-sized affordances that belong to the
 * sheet as a whole rather than to a single state (Message, the `⋯` action hub). Keep the
 * footer for the state's primary response; anything always-available goes here. Pass
 * [FitIconBtn] / [FitContextMenuTrigger] children; they lay out in a tighter row than the
 * descriptor/pill spacing so a pair of 32dp buttons doesn't read as two separate groups.
 */
@Composable
fun FitSheetStatusHeader(
    descriptor: String,
    pill: (@Composable () -> Unit)? = null,
    actions: (@Composable RowScope.() -> Unit)? = null
) {
    val theme = LocalFitTheme.current
    Row(
        modifier = Modifier.fillMaxWidth().padding(bottom = 28.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(FitSpacing.sp3)
    ) {
        Text(
            descriptor,
            style = FitFont.body1.copy(fontSize = 18.sp, fontWeight = FontWeight.Medium),
            color = theme.textPrimary,
            modifier = Modifier.weight(1f)
        )
        pill?.invoke()
        if (actions != null) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(FitSpacing.sp2),
                content = actions
            )
        }
    }
}

// ============================================================================
// FitCalEventPill — 11sp pill with state color
// ============================================================================

enum class FitCalEventPillStatus { Request, Review, Awaiting, Missed }

@Composable
fun FitCalEventPill(text: String, status: FitCalEventPillStatus) {
    // Fill weight carries "is this on me?", not just "which state":
    //   Request / Review — the coach owes an answer → FILLED yellow, white text.
    //   Awaiting         — nobody owes you anything, you're waiting → OUTLINED, same hue,
    //                      opposite weight. Mirrors the tile's dashed border.
    //   Missed           — filled red.
    // Same rule in both themes; the outline reads on light and dark alike because the hue,
    // not a surface fill, is doing the work. Canon: event-statuses.md § status pills.
    val accent = when (status) {
        FitCalEventPillStatus.Request, FitCalEventPillStatus.Review -> FitColors.Yellow.y600
        FitCalEventPillStatus.Awaiting -> FitColors.Yellow.y600
        FitCalEventPillStatus.Missed -> FitColors.error
    }
    val outlined = status == FitCalEventPillStatus.Awaiting
    Box(
        modifier = Modifier
            .clip(CircleShape)
            .then(
                if (outlined) {
                    Modifier.border(1.dp, accent, CircleShape)
                } else {
                    Modifier.background(accent)
                }
            )
            // The outlined variant drops 1px of padding so the border sits inside the same
            // overall pill size as the filled one — a row of pills stays on one baseline.
            .padding(
                horizontal = if (outlined) 5.dp else 6.dp,
                vertical = if (outlined) 1.dp else 2.dp,
            ),
    ) {
        Text(
            text,
            style = FitFont.pill,
            color = if (outlined) accent else Color.White,
            maxLines = 1,
            softWrap = false,
        )
    }
}

// ============================================================================
// FitSnackbar — bottom pill with optional action
// ============================================================================

@Composable
fun FitSnackbarContent(
    message: String,
    modifier: Modifier = Modifier,
    dotColor: Color? = null,
    actionLabel: String? = null,
    onAction: (() -> Unit)? = null
) {
    val theme = LocalFitTheme.current
    Box(
        modifier = modifier
            .fillMaxWidth()
            .clip(CircleShape)
            .background(theme.surfaceLow)
            .border(1.dp, theme.divider, CircleShape)
            .padding(horizontal = FitSpacing.sp4, vertical = FitSpacing.sp2_5)
    ) {
        Row(
            modifier = Modifier.align(Alignment.Center),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(FitSpacing.sp1_5)
        ) {
            if (dotColor != null) {
                Box(Modifier.size(6.dp).clip(CircleShape).background(dotColor))
            }
            Text(message, style = FitFont.body2, color = theme.textPrimary)
        }
        if (actionLabel != null && onAction != null) {
            Text(
                actionLabel,
                style = FitFont.body2.copy(fontWeight = FontWeight.Medium),
                color = FitColors.Teal.t500,
                modifier = Modifier
                    .align(Alignment.CenterEnd)
                    .clickable { onAction() }
            )
        }
    }
}

@Composable
fun FitSnackbar(
    isVisible: Boolean,
    onDismiss: () -> Unit,
    message: String,
    actionLabel: String? = null,
    onAction: (() -> Unit)? = null,
    durationMs: Long = 4000,
    showDot: Boolean = false
) {
    LaunchedEffect(isVisible) {
        if (isVisible) {
            delay(durationMs)
            onDismiss()
        }
    }

    AnimatedVisibility(
        visible = isVisible,
        enter = fadeIn() + slideInVertically { it / 2 },
        exit = fadeOut()
    ) {
        FitSnackbarContent(
            message = message,
            modifier = Modifier.padding(horizontal = FitSpacing.sp5),
            dotColor = if (showDot) FitColors.Yellow.y400 else null,
            actionLabel = actionLabel,
            onAction = onAction
        )
    }
}

// ============================================================================
// FitToast — top pill with type (success/error/info)
// ============================================================================

enum class FitToastType { Success, Error, Info }

@Composable
fun FitToast(
    isVisible: Boolean,
    message: String,
    type: FitToastType = FitToastType.Info,
    onDismiss: () -> Unit,
    durationMs: Long = 3000
) {
    val theme = LocalFitTheme.current
    LaunchedEffect(isVisible) {
        if (isVisible) { delay(durationMs); onDismiss() }
    }
    val borderColor = when (type) {
        FitToastType.Success -> FitColors.Teal.t500
        FitToastType.Error -> FitColors.error
        FitToastType.Info -> FitColors.brandPrimary
    }

    AnimatedVisibility(
        visible = isVisible,
        enter = fadeIn() + slideInVertically { -it },
        exit = fadeOut()
    ) {
        Row(
            modifier = Modifier
                .padding(horizontal = FitSpacing.sp5)
                .clip(RoundedCornerShape(FitRadius.md))
                .background(theme.surfaceHigh)
                .border(
                    width = 0.dp,
                    color = Color.Transparent,
                    shape = RoundedCornerShape(FitRadius.md)
                )
                .drawLeftBorder(color = borderColor, widthDp = 3.dp)
                .padding(FitSpacing.sp3),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(FitSpacing.sp3)
        ) {
            Text(message, style = FitFont.body2, color = theme.textPrimary)
        }
    }
}

// Helper: draw a solid colored left border (since Modifier.border is uniform)
private fun Modifier.drawLeftBorder(color: Color, widthDp: androidx.compose.ui.unit.Dp): Modifier =
    this.then(
        Modifier.padding(start = widthDp).background(color.copy(alpha = 0f))
    )

// ============================================================================
// FitEmptyState — illustration + title + subtitle + optional CTA
// ============================================================================

@Composable
fun FitEmptyState(
    title: String,
    subtitle: String,
    icon: ImageVector? = null,
    actionLabel: String? = null,
    onAction: (() -> Unit)? = null,
    modifier: Modifier = Modifier
) {
    val theme = LocalFitTheme.current
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = FitSpacing.sp8, vertical = FitSpacing.sp8),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        if (icon != null) {
            Icon(icon, null, tint = theme.textTertiary, modifier = Modifier.size(40.dp))
            Spacer(Modifier.height(FitSpacing.sp4))
        }
        Text(
            title,
            style = FitFont.body1.copy(fontWeight = FontWeight.Medium),
            color = theme.textSecondary,
            textAlign = TextAlign.Center
        )
        Spacer(Modifier.height(FitSpacing.sp2))
        Text(
            subtitle,
            style = FitFont.body2,
            color = theme.textTertiary,
            textAlign = TextAlign.Center
        )
        if (actionLabel != null && onAction != null) {
            Spacer(Modifier.height(FitSpacing.sp5))
            FitButton(actionLabel, size = FitButtonSize.Md, onClick = onAction)
        }
    }
}

// ============================================================================
// FitContextMenu — wrapper around DropdownMenu (native Material 3)
// ============================================================================

data class FitContextMenuItem(
    val title: String,
    val icon: ImageVector? = null,
    val destructive: Boolean = false,
    val onClick: () -> Unit
)

@Composable
fun FitContextMenuTrigger(
    items: List<FitContextMenuItem>,
    icon: ImageVector = Icons.Default.MoreVert,
    style: FitIconBtnStyle = FitIconBtnStyle.Filled,
    contentDescription: String? = null,
    modifier: Modifier = Modifier
) {
    val theme = LocalFitTheme.current
    var expanded by remember { mutableStateOf(false) }
    Box(modifier = modifier) {
        FitIconBtn(icon = icon, style = style, contentDescription = contentDescription) { expanded = true }
        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false }
        ) {
            items.forEach { item ->
                DropdownMenuItem(
                    text = {
                        Text(
                            item.title,
                            color = if (item.destructive) FitColors.error else theme.textPrimary
                        )
                    },
                    onClick = { expanded = false; item.onClick() },
                    leadingIcon = item.icon?.let {
                        { Icon(it, null, tint = if (item.destructive) FitColors.error else theme.textSecondary) }
                    }
                )
            }
        }
    }
}
