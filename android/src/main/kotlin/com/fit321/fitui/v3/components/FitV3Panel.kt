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
import androidx.compose.ui.unit.dp
import com.fit321.fitui.v3.theme.LocalFitV3Palette
import com.fit321.fitui.v3.tokens.FitV3Geometry
import com.fit321.fitui.v3.tokens.FitV3Type

@Composable
fun FitPanel(
    modifier: Modifier = Modifier,
    attention: Boolean = false,
    content: @Composable ColumnScope.() -> Unit,
) {
    val palette = LocalFitV3Palette.current
    val shape = RoundedCornerShape(FitV3Geometry.panelRadius)
    val surface = if (attention) {
        Modifier.fitV3Surface(shape, fill = palette.attentionTint, hairline = palette.perimeterAttention)
    } else {
        Modifier.fitV3Surface(shape)
    }
    Column(
        modifier = modifier
            .fillMaxWidth()
            .then(surface)
            .clip(shape)
            .padding(FitV3Geometry.panelPadding),
        content = content,
    )
}

@Composable
fun FitSectionTitle(
    text: String,
    modifier: Modifier = Modifier,
    first: Boolean = false,
    trailing: (@Composable RowScope.() -> Unit)? = null,
) {
    val palette = LocalFitV3Palette.current
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
        Text(
            text = text,
            style = FitV3Type.sectionTitle,
            color = palette.textSecondary,
            modifier = Modifier.weight(1f),
        )
        trailing?.invoke(this)
    }
}
