package com.fit321.fitui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.MoreHoriz
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Videocam
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.res.vectorResource
import com.fit321.designtokens.R
import com.fit321.fitui.theme.LocalFitTheme
import com.fit321.fitui.tokens.FitColors
import com.fit321.fitui.tokens.FitFont

// ============================================================================
// FitVideoUploadCard — 16:9 state-aware video upload card for direct-upload
// integrations (Mux is the primary consumer). Mirrors Swift `FitVideoUploadCard`.
// See `docs/components.md` § FitVideoUploadCard.
// ============================================================================

sealed class FitVideoUploadCardState {
    object Idle : FitVideoUploadCardState()
    data class Uploading(val progress: Float, val filename: String) : FitVideoUploadCardState()
    object Processing : FitVideoUploadCardState()
    data class Ready(val thumbnailUrl: String?) : FitVideoUploadCardState()
    data class Errored(val errorCode: String?) : FitVideoUploadCardState()
    object Pending : FitVideoUploadCardState()
}

@Composable
fun FitVideoUploadCard(
    state: FitVideoUploadCardState,
    onTap: () -> Unit,
    onCancel: (() -> Unit)? = null,
    onMore: (() -> Unit)? = null,
    modifier: Modifier = Modifier
) {
    val theme = LocalFitTheme.current
    val shape = RoundedCornerShape(12.dp)

    val isTappable = when (state) {
        is FitVideoUploadCardState.Uploading,
        is FitVideoUploadCardState.Processing -> false
        else -> true
    }

    val tapModifier = if (isTappable) Modifier.clickable { onTap() } else Modifier

    // Compose Modifier.border doesn't natively support dashed strokes for
    // RoundedCornerShape; iOS uses StrokeStyle dash. Android uses solid
    // border at v1; switch to drawWithCache custom paint if dashed becomes
    // a strict requirement later (e.g. accessibility audit).
    val borderColor: Color
    val borderWidth: androidx.compose.ui.unit.Dp
    val backgroundColor: Color

    when (state) {
        is FitVideoUploadCardState.Idle,
        is FitVideoUploadCardState.Pending -> {
            borderColor = theme.divider
            borderWidth = 1.5.dp
            backgroundColor = Color.Transparent
        }
        is FitVideoUploadCardState.Uploading,
        is FitVideoUploadCardState.Processing -> {
            borderColor = FitColors.warning
            borderWidth = 1.dp
            backgroundColor = theme.bgWarningSubtle
        }
        is FitVideoUploadCardState.Ready -> {
            borderColor = theme.divider
            borderWidth = 1.dp
            backgroundColor = theme.surfaceHigher
        }
        is FitVideoUploadCardState.Errored -> {
            borderColor = FitColors.error
            borderWidth = 1.dp
            backgroundColor = theme.bgErrorSubtle
        }
    }

    Box(
        modifier = modifier
            .fillMaxWidth()
            .aspectRatio(16f / 9f)
            .clip(shape)
            .background(backgroundColor)
            .border(borderWidth, borderColor, shape)
            .then(tapModifier)
    ) {
        when (state) {
            is FitVideoUploadCardState.Idle,
            is FitVideoUploadCardState.Pending -> IdleSlot(theme)
            is FitVideoUploadCardState.Uploading -> UploadingSlot(state.progress, state.filename, theme)
            is FitVideoUploadCardState.Processing -> ProcessingSlot(theme)
            is FitVideoUploadCardState.Ready -> ReadySlot()
            is FitVideoUploadCardState.Errored -> ErroredSlot(state.errorCode, theme)
        }

        // Top-right overlay controls
        when (state) {
            is FitVideoUploadCardState.Uploading -> if (onCancel != null) {
                CornerButton(Alignment.TopEnd, size = 28.dp, onClick = onCancel) {
                    Icon(ImageVector.vectorResource(R.drawable.ic_fit_close), null, tint = Color.White, modifier = Modifier.size(14.dp))
                }
            }
            is FitVideoUploadCardState.Ready -> if (onMore != null) {
                CornerButton(Alignment.TopEnd, size = 32.dp, onClick = onMore) {
                    Icon(Icons.Default.MoreHoriz, null, tint = Color.White, modifier = Modifier.size(18.dp))
                }
            }
            else -> { /* no overlay */ }
        }
    }
}

@Composable
private fun BoxScope.IdleSlot(theme: FitColors.Theme) {
    Column(
        modifier = Modifier.align(Alignment.Center).padding(12.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Icon(
            Icons.Default.Videocam,
            contentDescription = null,
            tint = theme.textSecondary.copy(alpha = 0.6f),
            modifier = Modifier.size(32.dp)
        )
        Text(
            "Upload intro video",
            style = FitFont.body1.copy(fontWeight = FontWeight.Medium, fontSize = 15.sp),
            color = theme.textPrimary,
            textAlign = TextAlign.Center
        )
        Text(
            "Up to 200 MB · 2 min · mp4/mov",
            style = FitFont.caption.copy(fontSize = 12.sp),
            color = theme.textTertiary,
            textAlign = TextAlign.Center
        )
    }
}

@Composable
private fun BoxScope.UploadingSlot(
    progress: Float,
    filename: String,
    theme: FitColors.Theme
) {
    val clamped = progress.coerceIn(0f, 1f)
    Column(
        modifier = Modifier
            .align(Alignment.Center)
            .fillMaxSize()
            .padding(horizontal = 16.dp, vertical = 14.dp),
        verticalArrangement = Arrangement.Center
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            Icon(
                Icons.Default.Videocam,
                contentDescription = null,
                tint = theme.textPrimary,
                modifier = Modifier.size(14.dp)
            )
            Text(
                filename,
                style = FitFont.body2.copy(fontSize = 13.sp),
                color = theme.textPrimary,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        }
        Spacer(Modifier.height(10.dp))
        // Progress bar — 6.dp track with yellow fill scaled by progress
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(6.dp)
                .clip(RoundedCornerShape(3.dp))
                .background(theme.surfaceHigh)
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth(clamped)
                    .fillMaxSize()
                    .background(FitColors.warning)
            )
        }
        Spacer(Modifier.height(6.dp))
        Text(
            "Uploading… ${(clamped * 100).toInt()}%",
            style = FitFont.caption.copy(fontSize = 12.sp),
            color = theme.textTertiary,
            textAlign = TextAlign.End,
            modifier = Modifier.fillMaxWidth()
        )
    }
}

@Composable
private fun BoxScope.ProcessingSlot(theme: FitColors.Theme) {
    Column(
        modifier = Modifier.align(Alignment.Center).padding(12.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        CircularProgressIndicator(
            color = FitColors.warning,
            strokeWidth = 2.5.dp,
            modifier = Modifier.size(28.dp)
        )
        Text(
            "Processing your video…",
            style = FitFont.body1.copy(fontWeight = FontWeight.Medium, fontSize = 14.sp),
            color = FitColors.warning,
            textAlign = TextAlign.Center
        )
        Text(
            "Usually 30-60 seconds",
            style = FitFont.caption.copy(fontSize = 12.sp),
            color = theme.textTertiary,
            textAlign = TextAlign.Center
        )
    }
}

@Composable
private fun BoxScope.ReadySlot() {
    // Real impl: ExoPlayer or AsyncImage from Coil with thumbnailUrl.
    // Prototype-level placeholder = brand-gradient fill + centered play overlay.
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(FitColors.brandGradient)
    ) {
        // Dim scrim
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.Black.copy(alpha = 0.25f))
        )
        Icon(
            Icons.Default.PlayArrow,
            contentDescription = null,
            tint = Color.White,
            modifier = Modifier
                .align(Alignment.Center)
                .size(48.dp)
        )
    }
}

@Composable
private fun BoxScope.ErroredSlot(
    errorCode: String?,
    theme: FitColors.Theme
) {
    Column(
        modifier = Modifier.align(Alignment.Center).padding(12.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(6.dp)
    ) {
        Icon(
            Icons.Default.Warning,
            contentDescription = null,
            tint = FitColors.error,
            modifier = Modifier.size(32.dp)
        )
        Text(
            "Couldn’t process this video",
            style = FitFont.body1.copy(fontWeight = FontWeight.Medium, fontSize = 14.sp),
            color = theme.textPrimary,
            textAlign = TextAlign.Center
        )
        Text(
            "Try a different file",
            style = FitFont.body1.copy(fontWeight = FontWeight.Medium, fontSize = 14.sp),
            color = FitColors.error,
            textAlign = TextAlign.Center
        )
        if (errorCode != null) {
            Text(
                "code: $errorCode",
                style = FitFont.caption.copy(fontSize = 11.sp),
                color = theme.textTertiary,
                textAlign = TextAlign.Center
            )
        }
    }
}

@Composable
private fun BoxScope.CornerButton(
    alignment: Alignment,
    size: androidx.compose.ui.unit.Dp,
    onClick: () -> Unit,
    content: @Composable () -> Unit
) {
    Box(
        modifier = Modifier
            .align(alignment)
            .padding(8.dp)
            .size(size)
            .clip(CircleShape)
            .background(Color.Black.copy(alpha = 0.55f))
            .clickable { onClick() },
        contentAlignment = Alignment.Center
    ) {
        content()
    }
}
