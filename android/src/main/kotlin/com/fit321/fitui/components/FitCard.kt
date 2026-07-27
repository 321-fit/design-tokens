package com.fit321.fitui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.PaddingValues
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
import com.fit321.fitui.tokens.FitElevation
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
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
 * Surface = `theme.surfaceDefault`. Edge: light = soft drop shadow only (no
 * border); dark = tonal contrast only (no border). Keeping both borderless
 * avoids a hard hairline flashing on theme toggle. Components must never
 * hardcode colors — wrap with `FitTheme { FitCard { ... } }`.
 */
@Composable
fun FitCard(
    modifier: Modifier = Modifier,
    shape: Shape = RoundedCornerShape(FitRadius.card),
    surface: Color? = null,
    contentPadding: PaddingValues = PaddingValues(FitSpacing.sp5),
    verticalArrangement: Arrangement.Vertical = Arrangement.spacedBy(FitSpacing.sp3),
    title: String? = null,
    onEdit: (() -> Unit)? = null,
    onClick: (() -> Unit)? = null,
    content: @Composable () -> Unit
) {
    val theme = LocalFitTheme.current
    val isLight = theme.screenBg != FitColors.Gray.g900

    val clickableMod = if (onClick != null) Modifier.clickable { onClick() } else Modifier

    Column(
        modifier = modifier
            .fillMaxWidth()
            .then(with(FitElevation) { Modifier.fitCardElevation(isLight, shape) })
            .clip(shape)
            .background(surface ?: theme.surfaceDefault)
            .then(clickableMod)
            .padding(contentPadding),
        verticalArrangement = verticalArrangement
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
