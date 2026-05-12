package com.fit321.fitui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.PhotoCamera
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.fit321.fitui.tokens.FitColors
import com.fit321.fitui.tokens.FitFont

// ============================================================================
// FitProfileHero — 16:9 hero block with 3-variant media fallback (video /
// cover image / brand-gradient + initials) and optional camera-overlay
// edit affordance. Mirrors Swift `FitProfileHero`.
// See `docs/components.md` § FitProfileHero.
// ============================================================================

sealed class FitProfileHeroMedia {
    data class Video(val url: String) : FitProfileHeroMedia()
    data class Cover(val url: String) : FitProfileHeroMedia()
    data class Initials(val text: String) : FitProfileHeroMedia()
}

@Composable
fun FitProfileHero(
    media: FitProfileHeroMedia,
    onEdit: (() -> Unit)? = null,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .aspectRatio(16f / 9f)
            .background(Color.Black)
    ) {
        when (media) {
            is FitProfileHeroMedia.Video -> VideoPlaceholder()
            is FitProfileHeroMedia.Cover -> CoverPlaceholder()
            is FitProfileHeroMedia.Initials -> InitialsFallback(media.text)
        }

        if (onEdit != null) {
            Box(
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .padding(top = 12.dp, end = 12.dp)
                    .size(36.dp)
                    .clip(CircleShape)
                    .background(Color.Black.copy(alpha = 0.5f))
                    .clickable { onEdit() },
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    Icons.Default.PhotoCamera,
                    contentDescription = "Edit cover",
                    tint = Color.White,
                    modifier = Modifier.size(18.dp)
                )
            }
        }
    }
}

@Composable
private fun VideoPlaceholder() {
    // Real impl: ExoPlayer. Prototype-level placeholder = dark gradient + play glyph.
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .aspectRatio(16f / 9f)
            .background(
                Brush.linearGradient(
                    colors = listOf(
                        Color(0xFF1A1A2E),
                        Color(0xFF16213E)
                    )
                )
            ),
        contentAlignment = Alignment.Center
    ) {
        Icon(
            Icons.Default.PlayArrow,
            contentDescription = null,
            tint = Color.White.copy(alpha = 0.8f),
            modifier = Modifier.size(48.dp)
        )
    }
}

@Composable
private fun CoverPlaceholder() {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .aspectRatio(16f / 9f)
            .background(
                Brush.linearGradient(
                    colors = listOf(
                        Color(0xFF2D5A3D),
                        Color(0xFF4A7C59)
                    )
                )
            )
    )
}

@Composable
private fun InitialsFallback(initials: String) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .aspectRatio(16f / 9f)
            .background(FitColors.brandGradient),
        contentAlignment = Alignment.Center
    ) {
        Text(
            initials.take(2).uppercase(),
            style = FitFont.heading1.copy(
                fontSize = 56.sp,
                fontWeight = FontWeight.SemiBold,
                letterSpacing = 2.sp
            ),
            color = Color.White.copy(alpha = 0.95f)
        )
    }
}
