package com.fit321.fitui.components

import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.fit321.fitui.theme.LocalFitTheme
import com.fit321.fitui.tokens.FitRadius
import com.fit321.fitui.tokens.FitSpacing

// ============================================================================
// Shimmer modifier — creates the animated gradient effect
// ============================================================================

@Composable
fun Modifier.fitShimmer(): Modifier {
    val theme = LocalFitTheme.current
    val transition = rememberInfiniteTransition(label = "fit-shimmer")
    val phase by transition.animateFloat(
        initialValue = -1f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 1400),
            repeatMode = RepeatMode.Restart
        ),
        label = "fit-shimmer-phase"
    )
    return this.background(
        brush = Brush.horizontalGradient(
            colors = listOf(theme.surfaceHigher, theme.surfaceHigh, theme.surfaceHigher),
            startX = phase * 800f,
            endX = (phase + 1) * 800f
        )
    )
}

// ============================================================================
// Primitives — card / row / circle / line / block / button / strip
// ============================================================================

@Composable
fun FitSkeletonCard(
    compact: Boolean = false,
    modifier: Modifier = Modifier,
    content: @Composable ColumnScope.() -> Unit
) {
    val theme = LocalFitTheme.current
    Column(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(if (compact) FitRadius.md else FitRadius.lg))
            .background(theme.surfaceHigh)
            .padding(if (compact) FitSpacing.sp3 else FitSpacing.sp4),
        verticalArrangement = Arrangement.spacedBy(FitSpacing.sp3),
        content = content
    )
}

@Composable
fun FitSkeletonLine(width: Dp? = null, short: Boolean = false) {
    val height = if (short) 10.dp else 12.dp
    Box(
        modifier = Modifier
            .height(height)
            .then(if (width != null) Modifier.width(width) else Modifier.fillMaxWidth(if (short) 0.5f else 1f))
            .clip(RoundedCornerShape(6.dp))
            .fitShimmer()
    )
}

@Composable
fun FitSkeletonCircle(size: Dp = 44.dp, isSquare: Boolean = false) {
    val shape = if (isSquare) RoundedCornerShape(10.dp) else CircleShape
    Box(
        modifier = Modifier
            .size(size)
            .clip(shape)
            .fitShimmer()
    )
}

@Composable
fun FitSkeletonBlock(width: Dp? = null, height: Dp = 14.dp) {
    Box(
        modifier = Modifier
            .height(height)
            .then(if (width != null) Modifier.width(width) else Modifier.fillMaxWidth())
            .clip(RoundedCornerShape(7.dp))
            .fitShimmer()
    )
}

@Composable
fun FitSkeletonBtn() {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(40.dp)
            .clip(CircleShape)
            .fitShimmer()
    )
}

@Composable
fun FitSkeletonStrip() {
    val theme = LocalFitTheme.current
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(36.dp)
            .background(theme.divider.copy(alpha = 0.3f))
    )
}

@Composable
fun FitSkeletonRow(content: @Composable RowScope.() -> Unit) {
    Row(
        horizontalArrangement = Arrangement.spacedBy(FitSpacing.sp3),
        verticalAlignment = androidx.compose.ui.Alignment.CenterVertically,
        content = content
    )
}
