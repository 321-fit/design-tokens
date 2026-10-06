package com.fit321.fitui.v3.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.unit.dp
import com.fit321.fitui.v3.theme.LocalFitV3Palette
import com.fit321.fitui.v3.tokens.FitV3Geometry
import com.fit321.fitui.v3.tokens.FitV3Type
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape

enum class FitV3ButtonStyle { Primary, Secondary, Destructive }

@Composable
fun FitButton(
    title: String,
    modifier: Modifier = Modifier,
    style: FitV3ButtonStyle = FitV3ButtonStyle.Primary,
    enabled: Boolean = true,
    loading: Boolean = false,
    leading: (@Composable () -> Unit)? = null,
    onClick: () -> Unit,
) {
    val palette = LocalFitV3Palette.current
    val fill: Brush = when (style) {
        FitV3ButtonStyle.Primary -> Brush.verticalGradient(colorStops = palette.ctaStops.toTypedArray())
        FitV3ButtonStyle.Secondary -> SolidColor(palette.secondaryFill)
        FitV3ButtonStyle.Destructive -> SolidColor(palette.textError)
    }
    val ink = when (style) {
        FitV3ButtonStyle.Primary -> palette.ctaLabel
        FitV3ButtonStyle.Secondary -> palette.textPrimary
        FitV3ButtonStyle.Destructive -> Color.White
    }
    val edge = when (style) {
        FitV3ButtonStyle.Primary -> palette.ctaHighlight
        FitV3ButtonStyle.Secondary -> palette.secondaryBorder
        FitV3ButtonStyle.Destructive -> Color.Transparent
    }
    val lift = if (style == FitV3ButtonStyle.Primary) 12.dp else 0.dp
    Row(
        modifier = modifier
            .fillMaxWidth()
            .height(FitV3Geometry.footerCtaHeight)
            .alpha(if (enabled) 1f else 0.4f)
            .then(
                if (lift > 0.dp) {
                    Modifier.shadow(
                        elevation = lift,
                        shape = CircleShape,
                        clip = false,
                        ambientColor = palette.ctaShadow,
                        spotColor = palette.ctaShadow,
                    )
                } else {
                    Modifier
                },
            )
            .clip(CircleShape)
            .background(fill, CircleShape)
            .then(
                if (edge == Color.Transparent) Modifier else Modifier.border(1.dp, edge, CircleShape),
            )
            .clickable(enabled = enabled && !loading) { onClick() },
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center,
    ) {
        if (loading) {
            CircularProgressIndicator(
                modifier = Modifier.size(20.dp),
                color = ink,
                strokeWidth = 2.dp,
            )
        } else {
            CompositionLocalProvider(LocalContentColor provides ink) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                ) {
                    leading?.invoke()
                    Text(text = title, style = FitV3Type.rowTitle, color = ink, maxLines = 1)
                }
            }
        }
    }
}

/**
 * One quiet action on a row — Restore, Unblock.
 *
 * Not a [FitButton]: a CTA is the thing the screen is for, and there is one of it. This is an
 * action that belongs to the row it sits on, repeated down a list, and it has to read as
 * smaller than the name beside it.
 */
@Composable
fun FitPillButton(
    label: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
) {
    val palette = LocalFitV3Palette.current
    val shape = RoundedCornerShape(percent = 50)
    Box(
        modifier = modifier
            .height(FitV3Geometry.pillHeight)
            .clip(shape)
            .border(1.dp, palette.divider, shape)
            .then(if (enabled) Modifier.clickable { onClick() } else Modifier)
            .alpha(if (enabled) 1f else 0.4f)
            .padding(horizontal = 14.dp),
        contentAlignment = Alignment.Center,
    ) {
        Text(text = label, style = FitV3Type.pillLabel, color = palette.textPrimary, maxLines = 1)
    }
}
