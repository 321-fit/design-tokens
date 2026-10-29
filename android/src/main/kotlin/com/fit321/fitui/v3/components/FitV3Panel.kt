package com.fit321.fitui.v3.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.unit.dp
import com.fit321.fitui.v3.theme.LocalFitV3Palette
import com.fit321.fitui.v3.tokens.FitV3Geometry
import com.fit321.fitui.v3.tokens.FitV3Type

/**
 * [kicker] is the warning line an [attention] panel leads with (`.cd-sp-urgent-k`, "DUE
 * TOMORROW · REFUND IF NOT BUILT"). [dashed] is a panel about something still on its way — the
 * coach is building it — that has nothing in it yet to press.
 */
@Composable
fun FitPanel(
    modifier: Modifier = Modifier,
    attention: Boolean = false,
    kicker: String? = null,
    dashed: Boolean = false,
    content: @Composable ColumnScope.() -> Unit,
) {
    val palette = LocalFitV3Palette.current
    val shape = RoundedCornerShape(FitV3Geometry.panelRadius)
    val surface = when {
        attention -> Modifier.fitV3Surface(shape, fill = palette.attentionTint, hairline = palette.perimeterAttention)
        dashed -> Modifier.fitV3Surface(shape, hairline = Color.Transparent).dashedEdge(palette.dashed)
        else -> Modifier.fitV3Surface(shape)
    }
    Column(
        modifier = modifier
            .fillMaxWidth()
            .then(surface)
            .clip(shape)
            .padding(FitV3Geometry.panelPadding),
    ) {
        if (kicker != null) {
            Text(
                text = kicker.uppercase(),
                style = FitV3Type.panelKicker,
                color = palette.perimeterAttention,
                modifier = Modifier.padding(start = 10.dp, end = 10.dp, top = 8.dp),
            )
        }
        content()
    }
}

private fun Modifier.dashedEdge(color: Color): Modifier = drawBehind {
    val radius = FitV3Geometry.panelRadius.toPx()
    drawRoundRect(
        color = color,
        cornerRadius = CornerRadius(radius, radius),
        style = Stroke(
            width = 1.dp.toPx(),
            pathEffect = PathEffect.dashPathEffect(floatArrayOf(6.dp.toPx(), 6.dp.toPx())),
        ),
    )
}

@Composable
fun FitSectionTitle(
    text: String,
    modifier: Modifier = Modifier,
    first: Boolean = false,
    /**
     * `fit-section-title--md`: 16/500 in the primary ink, for a heading that titles a screen's
     * one subject rather than labelling one band among several.
     */
    strong: Boolean = false,
    trailing: (@Composable RowScope.() -> Unit)? = null,
) {
    val palette = LocalFitV3Palette.current
    FitSectionTitleRow(modifier = modifier, first = first) {
        Text(
            text = text,
            style = if (strong) FitV3Type.sectionTitleStrong else FitV3Type.sectionTitle,
            color = if (strong) palette.textPrimary else palette.textSecondary,
            modifier = Modifier.weight(1f),
        )
        trailing?.invoke(this)
    }
}

/**
 * A section header whose left-hand side is a control rather than a label — "Select all" over a
 * picker, where the header has nothing to name and everything to offer.
 */
@Composable
fun FitSectionTitleRow(
    modifier: Modifier = Modifier,
    first: Boolean = false,
    content: @Composable RowScope.() -> Unit,
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(
                start = FitV3Geometry.sectionTitleSide,
                end = FitV3Geometry.sectionTitleSide,
                top = if (first) FitV3Geometry.sectionTitleFirstTop else FitV3Geometry.sectionTitleTop,
                bottom = FitV3Geometry.sectionTitleBottom,
            ),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        content()
    }
}
