package com.fit321.fitui.v3.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.fit321.fitui.v3.theme.LocalFitV3Palette
import com.fit321.fitui.v3.tokens.FitV3Geometry
import com.fit321.fitui.v3.tokens.FitV3Palette
import com.fit321.fitui.v3.tokens.FitV3Type

enum class FitV3Tone { Neutral, Muted, Income, Info, Danger }

fun FitV3Tone.fill(palette: FitV3Palette): Color = when (this) {
    FitV3Tone.Neutral -> palette.raised
    FitV3Tone.Muted -> palette.txnNeutralFill
    FitV3Tone.Income -> palette.txnIncomeFill
    FitV3Tone.Info -> palette.txnInfoFill
    FitV3Tone.Danger -> palette.txnDangerFill
}

fun FitV3Tone.ink(palette: FitV3Palette): Color = when (this) {
    FitV3Tone.Neutral -> palette.textPrimary
    FitV3Tone.Muted -> palette.textSecondary
    FitV3Tone.Income -> palette.txnIncomeInk
    FitV3Tone.Info -> palette.txnInfoInk
    FitV3Tone.Danger -> palette.textError
}

@Composable
fun FitDetailHero(
    amount: String,
    modifier: Modifier = Modifier,
    sub: String? = null,
    amountTone: FitV3Tone = FitV3Tone.Neutral,
    subTone: FitV3Tone = FitV3Tone.Muted,
) {
    val palette = LocalFitV3Palette.current
    val shape = RoundedCornerShape(FitV3Geometry.heroRadius)
    Column(
        modifier = modifier
            .fillMaxWidth()
            .fitV3Surface(shape)
            .clip(shape)
            .padding(FitV3Geometry.heroPadding),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(10.dp),
    ) {
        Text(
            text = amount,
            style = FitV3Type.detailHero,
            color = amountTone.ink(palette),
        )
        if (sub != null) {
            Text(
                text = sub,
                style = FitV3Type.detailHeroSub,
                color = if (subTone == FitV3Tone.Muted) palette.textTertiary else subTone.ink(palette),
                textAlign = TextAlign.Center,
            )
        }
    }
}

@Composable
fun FitTxnGroup(
    modifier: Modifier = Modifier,
    content: @Composable ColumnScope.() -> Unit,
) {
    val shape = RoundedCornerShape(FitV3Geometry.txnGroupRadius)
    Column(
        modifier = modifier
            .fillMaxWidth()
            .fitV3Surface(shape)
            .clip(shape),
        content = content,
    )
}

@Composable
fun FitTxnDivider(modifier: Modifier = Modifier) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .padding(start = FitV3Geometry.screenInset)
            .height(1.dp)
            .background(LocalFitV3Palette.current.divider),
    )
}

@Composable
fun FitTxnPlate(
    tone: FitV3Tone,
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit,
) {
    val palette = LocalFitV3Palette.current
    Box(
        modifier = modifier
            .size(FitV3Geometry.txnPlate)
            .background(tone.fill(palette), CircleShape),
        contentAlignment = Alignment.Center,
    ) {
        CompositionLocalProvider(LocalContentColor provides tone.ink(palette)) { content() }
    }
}

@Composable
fun FitTxnRow(
    title: String,
    modifier: Modifier = Modifier,
    sub: String? = null,
    note: String? = null,
    noteTone: FitV3Tone = FitV3Tone.Muted,
    amount: String? = null,
    amountTone: FitV3Tone = FitV3Tone.Neutral,
    tone: FitV3Tone = FitV3Tone.Neutral,
    onClick: (() -> Unit)? = null,
    leading: (@Composable () -> Unit)? = null,
) {
    val palette = LocalFitV3Palette.current
    val interaction = remember { MutableInteractionSource() }
    val pressed by interaction.collectIsPressedAsState()
    val clickable = if (onClick != null) {
        Modifier.clickable(interactionSource = interaction, indication = null) { onClick() }
    } else {
        Modifier
    }
    Row(
        modifier = modifier
            .fillMaxWidth()
            .then(clickable)
            .background(if (pressed) palette.pressed else Color.Transparent)
            .padding(horizontal = FitV3Geometry.screenInset, vertical = FitV3Geometry.txnPadding),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(FitV3Geometry.rowGap),
    ) {
        if (leading != null) FitTxnPlate(tone = tone, content = leading)
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = title,
                style = FitV3Type.rowValue,
                color = palette.textPrimary,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
            )
            if (sub != null) {
                Text(
                    text = sub,
                    style = FitV3Type.rowSub,
                    color = palette.textTertiary,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier.padding(top = FitV3Geometry.rowSubGap),
                )
            }
            if (note != null) {
                Text(
                    text = note,
                    style = FitV3Type.rowSub,
                    color = noteTone.ink(palette),
                    maxLines = 1,
                    modifier = Modifier.padding(top = FitV3Geometry.rowSubGap),
                )
            }
        }
        if (amount != null) {
            Text(
                text = amount,
                style = FitV3Type.rowValue,
                color = amountTone.ink(palette),
                maxLines = 1,
            )
        }
    }
}
