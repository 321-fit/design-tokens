package com.fit321.fitui.v3.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.fit321.designtokens.R
import com.fit321.fitui.v3.theme.LocalFitV3Palette
import com.fit321.fitui.v3.tokens.FitV3Colors
import com.fit321.fitui.v3.tokens.FitV3Geometry
import com.fit321.fitui.v3.tokens.FitV3Type

@Composable
fun FitClipStrip(
    modifier: Modifier = Modifier,
    content: @Composable RowScope.() -> Unit,
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .horizontalScroll(rememberScrollState())
            .padding(horizontal = FitV3Geometry.screenInset, vertical = 0.dp),
        horizontalArrangement = Arrangement.spacedBy(FitV3Geometry.clipGap),
        content = content,
    )
}

@Composable
fun FitClipCard(
    title: String,
    modifier: Modifier = Modifier,
    sub: String? = null,
    duration: String? = null,
    emptyLabel: String? = null,
    onClick: (() -> Unit)? = null,
    poster: (@Composable () -> Unit)? = null,
) {
    val palette = LocalFitV3Palette.current
    val shape = RoundedCornerShape(FitV3Geometry.clipPosterRadius)
    Column(
        modifier = modifier
            .width(FitV3Geometry.clipCardWidth)
            .then(if (onClick != null) Modifier.clickable { onClick() } else Modifier),
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(FitV3Geometry.clipPosterHeight)
                .fitV3Surface(shape, fill = palette.clipPoster)
                .clip(shape),
            contentAlignment = Alignment.Center,
        ) {
            poster?.invoke()
            if (emptyLabel == null) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(
                            Brush.verticalGradient(
                                0f to Color.Transparent,
                                0.5f to Color.Transparent,
                                1f to FitV3Colors.clipScrim,
                            ),
                        ),
                )
                Box(
                    modifier = Modifier
                        .size(FitV3Geometry.clipPlay)
                        .background(FitV3Colors.clipPlayFill, CircleShape),
                    contentAlignment = Alignment.Center,
                ) {
                    Icon(
                        painter = painterResource(R.drawable.ic_fit_play),
                        contentDescription = null,
                        tint = FitV3Colors.clipPlayInk,
                        modifier = Modifier.size(18.dp),
                    )
                }
                if (duration != null) {
                    Text(
                        text = duration,
                        style = FitV3Type.clipDuration,
                        color = Color.White,
                        modifier = Modifier
                            .align(Alignment.BottomEnd)
                            .padding(end = 10.dp, bottom = 8.dp),
                    )
                }
            } else {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(6.dp),
                ) {
                    CompositionLocalProvider(LocalContentColor provides palette.textTertiary) {
                        Icon(
                            painter = painterResource(R.drawable.ic_fit_check_circle),
                            contentDescription = null,
                            modifier = Modifier.size(24.dp),
                        )
                    }
                    Text(
                        text = emptyLabel,
                        style = FitV3Type.rowSub,
                        color = palette.textTertiary,
                    )
                }
            }
        }
        Text(
            text = title,
            style = FitV3Type.rowValue,
            color = palette.textPrimary,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
            modifier = Modifier.padding(top = 8.dp),
        )
        if (sub != null) {
            Text(
                text = sub,
                style = FitV3Type.rowSub,
                color = palette.textTertiary,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                modifier = Modifier.padding(top = 3.dp),
            )
        }
    }
}

@Composable
fun FitThinProgress(
    fraction: Float,
    modifier: Modifier = Modifier,
    tone: FitV3Tone = FitV3Tone.Income,
) {
    val palette = LocalFitV3Palette.current
    val shape = RoundedCornerShape(percent = 50)
    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(FitV3Geometry.progressBarHeight)
            .clip(shape)
            .background(palette.divider),
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth(fraction.coerceIn(0f, 1f))
                .height(FitV3Geometry.progressBarHeight)
                .clip(shape)
                .background(tone.ink(palette)),
        )
    }
}
