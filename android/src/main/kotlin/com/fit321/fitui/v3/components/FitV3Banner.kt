package com.fit321.fitui.v3.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.fit321.fitui.v3.theme.LocalFitV3Palette
import com.fit321.fitui.v3.tokens.FitV3Geometry
import com.fit321.fitui.v3.tokens.FitV3Type

enum class FitBannerTone { Attention, Error, Neutral }

@Composable
fun FitStatusBanner(
    text: String,
    modifier: Modifier = Modifier,
    tone: FitBannerTone = FitBannerTone.Attention,
    actionLabel: String? = null,
    onAction: (() -> Unit)? = null,
    leading: (@Composable () -> Unit)? = null,
) {
    val palette = LocalFitV3Palette.current
    val ink = when (tone) {
        FitBannerTone.Attention -> palette.perimeterAttention
        FitBannerTone.Error -> palette.textError
        FitBannerTone.Neutral -> palette.textSecondary
    }
    val fill = when (tone) {
        FitBannerTone.Attention -> palette.attentionTint
        FitBannerTone.Error -> palette.textError.copy(alpha = 0.13f)
        FitBannerTone.Neutral -> Color.Transparent
    }
    val shape = RoundedCornerShape(FitV3Geometry.rowRadius)
    Row(
        modifier = modifier
            .fillMaxWidth()
            .clip(shape)
            .background(fill, shape)
            .then(
                if (tone == FitBannerTone.Neutral) {
                    Modifier.border(1.dp, palette.divider, shape)
                } else {
                    Modifier
                },
            )
            .padding(horizontal = 14.dp, vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(10.dp),
    ) {
        if (leading != null) {
            CompositionLocalProvider(LocalContentColor provides ink) { leading() }
        }
        Text(
            text = text,
            style = FitV3Type.nextWhat,
            color = ink,
            modifier = Modifier.weight(1f),
        )
        if (actionLabel != null && onAction != null) {
            Box(
                modifier = Modifier
                    .height(32.dp)
                    .clip(CircleShape)
                    .border(1.dp, ink, CircleShape)
                    .clickable { onAction() }
                    .padding(horizontal = 14.dp),
                contentAlignment = Alignment.Center,
            ) {
                Text(text = actionLabel, style = FitV3Type.rowSub, color = ink, maxLines = 1)
            }
        }
    }
}

@Composable
fun FitEmptyPanel(
    title: String,
    modifier: Modifier = Modifier,
    subtitle: String? = null,
    actions: (@Composable () -> Unit)? = null,
) {
    val palette = LocalFitV3Palette.current
    FitPanel(modifier = modifier) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 14.dp, vertical = 18.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(6.dp),
        ) {
            Text(
                text = title,
                style = FitV3Type.nextWhen,
                color = palette.textPrimary,
                textAlign = TextAlign.Center,
            )
            if (subtitle != null) {
                Text(
                    text = subtitle,
                    style = FitV3Type.nextWhat,
                    color = palette.textTertiary,
                    textAlign = TextAlign.Center,
                )
            }
            if (actions != null) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 8.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp),
                ) {
                    actions()
                }
            }
        }
    }
}
