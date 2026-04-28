package com.fit321.fitui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import com.fit321.fitui.theme.LocalFitTheme
import com.fit321.fitui.tokens.FitColors
import com.fit321.fitui.tokens.FitFont
import com.fit321.fitui.tokens.FitRadius
import com.fit321.fitui.tokens.FitSize
import com.fit321.fitui.tokens.FitSpacing

/**
 * Content container for grouped information. Optional header with edit
 * action. Mirrors Swift `FitCard`. See `docs/components.md`.
 *
 * Surface = `theme.surfaceDefault`. Light adds a 1-px divider stroke + soft
 * drop shadow. Dark relies on tonal contrast only. Components must never
 * hardcode colors — wrap with `FitTheme { FitCard { ... } }`.
 */
@Composable
fun FitCard(
    modifier: Modifier = Modifier,
    title: String? = null,
    onEdit: (() -> Unit)? = null,
    onClick: (() -> Unit)? = null,
    content: @Composable () -> Unit
) {
    val theme = LocalFitTheme.current
    val isLight = theme.screenBg != FitColors.Gray.g900
    val shape = RoundedCornerShape(FitRadius.card)

    val shadowed = if (isLight) Modifier.shadow(4.dp, shape) else Modifier
    val borderMod = if (isLight) Modifier.border(1.dp, theme.divider, shape) else Modifier
    val clickableMod = if (onClick != null) Modifier.clickable { onClick() } else Modifier

    Column(
        modifier = modifier
            .fillMaxWidth()
            .then(shadowed)
            .clip(shape)
            .background(theme.surfaceDefault)
            .then(borderMod)
            .then(clickableMod)
            .padding(FitSpacing.sp5),
        verticalArrangement = Arrangement.spacedBy(FitSpacing.sp3)
    ) {
        if (title != null) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                    text = title,
                    color = theme.textPrimary,
                    style = FitFont.heading3
                )
                Spacer(modifier = Modifier.weight(1f))
                if (onEdit != null) {
                    Icon(
                        imageVector = Icons.Default.Edit,
                        contentDescription = null,
                        tint = theme.textTertiary,
                        modifier = Modifier
                            .size(FitSize.iconMd)
                            .clickable { onEdit() }
                    )
                }
            }
        }
        content()
    }
}

/**
 * Single info row inside a `FitCard` — leading icon (16dp) + secondary text.
 * Mirrors Swift `FitCardRow`.
 */
@Composable
fun FitCardRow(
    icon: ImageVector,
    text: String,
    modifier: Modifier = Modifier
) {
    val theme = LocalFitTheme.current
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = theme.textSecondary,
            modifier = Modifier.size(FitSize.iconMd)
        )
        Spacer(modifier = Modifier.width(FitSpacing.sp2))
        Text(
            text = text,
            color = theme.textSecondary,
            style = FitFont.body2
        )
    }
}
