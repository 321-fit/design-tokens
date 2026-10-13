package com.fit321.fitui.v3.components

import androidx.compose.animation.AnimatedVisibility
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
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.fit321.fitui.v3.theme.LocalFitV3Palette
import com.fit321.fitui.v3.tokens.FitV3Geometry
import com.fit321.fitui.v3.tokens.FitV3Type

@Composable
fun BoxScope.FitV3Sheet(
    visible: Boolean,
    onDismiss: () -> Unit,
    modifier: Modifier = Modifier,
    content: @Composable ColumnScope.() -> Unit,
) {
    val palette = LocalFitV3Palette.current
    val interaction = remember { MutableInteractionSource() }
    AnimatedVisibility(
        visible = visible,
        enter = fadeIn(),
        exit = fadeOut(),
        modifier = Modifier.matchParentSize(),
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(palette.materialScrim)
                .clickable(interactionSource = interaction, indication = null) { onDismiss() },
        )
    }
    AnimatedVisibility(
        visible = visible,
        enter = slideInVertically { it },
        exit = slideOutVertically { it },
        modifier = Modifier.align(Alignment.BottomCenter),
    ) {
        val shape = RoundedCornerShape(
            topStart = FitV3Geometry.sheetRadius,
            topEnd = FitV3Geometry.sheetRadius,
            bottomStart = 0.dp,
            bottomEnd = 0.dp,
        )
        Column(
            modifier = modifier
                .fillMaxWidth()
                .clip(shape)
                .background(palette.material, shape)
                .border(1.dp, palette.materialEdge, shape)
                .navigationBarsPadding()
                .padding(
                    start = FitV3Geometry.sheetSide,
                    end = FitV3Geometry.sheetSide,
                    top = 8.dp,
                    bottom = FitV3Geometry.sheetBottom,
                ),
        ) {
            Box(
                modifier = Modifier
                    .align(Alignment.CenterHorizontally)
                    .padding(bottom = 16.dp)
                    .width(FitV3Geometry.sheetHandleWidth)
                    .height(FitV3Geometry.sheetHandleHeight)
                    .background(palette.sheetHandle, CircleShape),
            )
            content()
        }
    }
}

@Composable
fun FitSheetTitle(
    title: String,
    modifier: Modifier = Modifier,
    sub: String? = null,
) {
    val palette = LocalFitV3Palette.current
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(top = 6.dp, bottom = 18.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Text(
            text = title,
            style = FitV3Type.sheetTitle,
            color = palette.textPrimary,
            textAlign = TextAlign.Center,
        )
        if (sub != null) {
            Text(
                text = sub,
                style = FitV3Type.rowSub,
                color = palette.textTertiary,
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(top = 4.dp),
            )
        }
    }
}

@Composable
fun FitSheetActionRow(
    title: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    sub: String? = null,
    tone: FitV3Tone = FitV3Tone.Neutral,
    enabled: Boolean = true,
    leading: (@Composable () -> Unit)? = null,
) {
    val palette = LocalFitV3Palette.current
    val shape = RoundedCornerShape(FitV3Geometry.fieldRadius)
    Row(
        modifier = modifier
            .fillMaxWidth()
            .clip(shape)
            .background(palette.surface, shape)
            .clickable(enabled = enabled) { onClick() }
            .padding(FitV3Geometry.screenInset),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(14.dp),
    ) {
        if (leading != null) {
            Box(
                modifier = Modifier
                    .size(FitV3Geometry.txnPlate)
                    .background(tone.fill(palette), CircleShape),
                contentAlignment = Alignment.Center,
            ) {
                CompositionLocalProvider(LocalContentColor provides tone.ink(palette)) { leading() }
            }
        }
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = title,
                style = FitV3Type.rowTitle,
                color = if (enabled) palette.textPrimary else palette.textTertiary,
            )
            if (sub != null) {
                Text(
                    text = sub,
                    style = FitV3Type.rowSub,
                    color = palette.textTertiary,
                    modifier = Modifier.padding(top = 3.dp),
                )
            }
        }
    }
}

@Composable
fun FitSheetCloseButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val palette = LocalFitV3Palette.current
    Box(
        modifier = modifier
            .fillMaxWidth()
            .padding(top = 14.dp)
            .clip(CircleShape)
            .height(FitV3Geometry.footerCtaHeight)
            .clickable { onClick() },
        contentAlignment = Alignment.Center,
    ) {
        Text(text = text, style = FitV3Type.rowTitle, color = palette.textSecondary)
    }
}
