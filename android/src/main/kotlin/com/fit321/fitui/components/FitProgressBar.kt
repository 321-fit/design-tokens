package com.fit321.fitui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.fit321.fitui.theme.LocalFitTheme
import com.fit321.fitui.tokens.FitColors
import com.fit321.fitui.tokens.FitRadius

// ============================================================================
// FitProgressBar — bare determinate bar, no label.
// Mirrors Swift FitProgressBar. See `docs/components.md` § FitProgressBar.
// ============================================================================

/**
 * Tone carries the meaning, the same way it does on badges: [Brand] is ordinary
 * progress, [Warning] says the thing being measured is running out and someone
 * should act — a pack down to its last credits, not merely a bar that is nearly full.
 */
enum class FitProgressTone { Brand, Warning, Neutral }

/**
 * What the empty part of the bar is drawn in — and it is not cosmetic.
 *
 * [Surface] is for a bar sitting directly on the screen background (a capacity bar on a
 * card); [Divider] is for one inside a plain list row, where `surfaceHigh` is so close to
 * the background that an empty bar vanishes entirely and "0 of 5 used" loses its picture.
 */
enum class FitProgressTrack { Surface, Divider }

/**
 * Why this exists next to [FitSpotCounter]: that one is a *capacity* bar and owns a
 * centred "X of Y spots" label, so it cannot sit inside a dense row that already
 * carries its own counter line. This is the bar alone.
 *
 * @param progress fraction in `0f..1f`; values outside are clamped rather than drawn.
 */
@Composable
fun FitProgressBar(
    progress: Float,
    modifier: Modifier = Modifier,
    tone: FitProgressTone = FitProgressTone.Brand,
    track: FitProgressTrack = FitProgressTrack.Surface,
    height: androidx.compose.ui.unit.Dp = 4.dp,
) {
    val theme = LocalFitTheme.current
    val ratio = progress.coerceIn(0f, 1f)
    val fill: Color = when (tone) {
        FitProgressTone.Brand -> FitColors.Teal.t500
        FitProgressTone.Warning -> FitColors.warning
        FitProgressTone.Neutral -> theme.textTertiary
    }
    val trackColor = when (track) {
        FitProgressTrack.Surface -> theme.surfaceHigh
        FitProgressTrack.Divider -> theme.divider
    }
    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(height)
            .clip(RoundedCornerShape(FitRadius.md))
            .background(trackColor),
    ) {
        // A zero-width Box still paints its rounded cap, which reads as "a little bit
        // done" on an empty bar — so nothing is drawn until there is progress.
        if (ratio > 0f) {
            Box(
                modifier = Modifier
                    .fillMaxHeight()
                    .fillMaxWidth(ratio)
                    .clip(RoundedCornerShape(FitRadius.md))
                    .background(fill),
            )
        }
    }
}
