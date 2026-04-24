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
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
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

@Composable
fun FitSheet(
    isVisible: Boolean,
    onDismiss: () -> Unit,
    variant: FitSheetVariant = FitSheetVariant.Standard,
    content: @Composable () -> Unit
) {
    val theme = LocalFitTheme.current
    if (isVisible) {
        Dialog(onDismissRequest = onDismiss) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.Black.copy(alpha = 0.5f))
                    .clickable { onDismiss() },
                contentAlignment = Alignment.BottomCenter
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(topStart = FitRadius.lg, topEnd = FitRadius.lg))
                        .background(theme.screenBg)
                        .padding(
                            top = FitSpacing.sp2,
                            start = FitSpacing.sp4,
                            end = FitSpacing.sp4,
                            bottom = if (variant == FitSheetVariant.Standard) FitSpacing.sp9 else 28.dp
                        )
                        .clickable(enabled = false) {}    // absorb taps inside sheet
                ) {
                    // Handle
                    Box(
                        modifier = Modifier
                            .size(36.dp, 4.dp)
                            .clip(RoundedCornerShape(2.dp))
                            .background(theme.divider)
                            .align(Alignment.CenterHorizontally)
                    )
                    Spacer(Modifier.height(FitSpacing.sp4))
                    content()
                }
            }
        }
    }
}

@Composable
fun FitSheetStatusHeader(descriptor: String, pill: (@Composable () -> Unit)? = null) {
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
    }
}

// ============================================================================
// FitCalEventPill — 11sp pill with state color
// ============================================================================

enum class FitCalEventPillStatus { Request, Review, Awaiting, Missed }

@Composable
fun FitCalEventPill(text: String, status: FitCalEventPillStatus) {
    val bg = when (status) {
        FitCalEventPillStatus.Request, FitCalEventPillStatus.Review -> FitColors.Yellow.y600
        FitCalEventPillStatus.Awaiting -> FitColors.Gray.g500
        FitCalEventPillStatus.Missed -> FitColors.error
    }
    Box(
        modifier = Modifier
            .clip(CircleShape)
            .background(bg)
            .padding(horizontal = 6.dp, vertical = 2.dp)
    ) {
        Text(text, style = FitFont.pill, color = Color.White)
    }
}

// ============================================================================
// FitSnackbar — bottom pill with optional action
// ============================================================================

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
        Row(
            modifier = Modifier
                .padding(horizontal = FitSpacing.sp5)
                .clip(CircleShape)
                .background(FitColors.Gray.black)
                .border(1.dp, Color.White.copy(alpha = 0.08f), CircleShape)
                .padding(horizontal = FitSpacing.sp4, vertical = 10.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(FitSpacing.sp3)
        ) {
            if (showDot) {
                Box(
                    Modifier.size(6.dp).clip(CircleShape).background(FitColors.Yellow.y400)
                )
            }
            Text(message, style = FitFont.body2, color = Color.White, modifier = Modifier.weight(1f))
            if (actionLabel != null && onAction != null) {
                Text(
                    actionLabel,
                    style = FitFont.body2.copy(fontWeight = FontWeight.Medium),
                    color = FitColors.Teal.t500,
                    modifier = Modifier.clickable { onAction() }
                )
            }
        }
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
    modifier: Modifier = Modifier
) {
    val theme = LocalFitTheme.current
    var expanded by remember { mutableStateOf(false) }
    Box(modifier = modifier) {
        FitIconBtn(icon = icon) { expanded = true }
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
