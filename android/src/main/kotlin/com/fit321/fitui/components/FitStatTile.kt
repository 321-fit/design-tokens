package com.fit321.fitui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.res.vectorResource
import com.fit321.designtokens.R
import com.fit321.fitui.theme.LocalFitTheme
import com.fit321.fitui.tokens.FitFont
import com.fit321.fitui.tokens.FitRadius
import com.fit321.fitui.tokens.FitSpacing

// ============================================================================
// FitStatTile — clickable card-level stat row.
// Mirrors Swift FitStatTile. See `docs/components.md` § FitStatTile.
// ============================================================================

enum class FitStatTileSize { Default, Large }

@Composable
fun FitStatTile(
    title: String,
    subtitle: String? = null,
    icon: ImageVector? = null,
    tone: FitIconPlateTone = FitIconPlateTone.Neutral,
    showChevron: Boolean = true,
    onTap: (() -> Unit)? = null,
    size: FitStatTileSize = FitStatTileSize.Default,
    modifier: Modifier = Modifier
) {
    val theme = LocalFitTheme.current
    val verticalPadding = when (size) {
        FitStatTileSize.Default -> FitSpacing.sp3
        FitStatTileSize.Large -> FitSpacing.sp3_5
    }
    val iconPlateSize = when (size) {
        FitStatTileSize.Default -> FitIconPlateSize.Md
        FitStatTileSize.Large -> FitIconPlateSize.MdLg
    }
    Row(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(FitRadius.lg))
            .background(theme.surfaceHigh, RoundedCornerShape(FitRadius.lg))
            .border(1.dp, theme.divider, RoundedCornerShape(FitRadius.lg))
            .then(if (onTap != null) Modifier.clickable(onClick = onTap) else Modifier)
            .padding(horizontal = 14.dp, vertical = verticalPadding),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(FitSpacing.sp3)
    ) {
        if (icon != null) {
            FitIconPlate(icon = icon, tone = tone, size = iconPlateSize)
        }
        Column(modifier = Modifier.weight(1f)) {
            Text(
                title,
                style = FitFont.body1.copy(fontSize = 15.sp, fontWeight = FontWeight.Medium),
                color = theme.textPrimary,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
            if (subtitle != null) {
                Text(
                    subtitle,
                    style = FitFont.footnote,
                    color = theme.textTertiary,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }
        }
        if (showChevron) {
            Icon(
                ImageVector.vectorResource(R.drawable.ic_fit_chevron_right),
                contentDescription = null,
                tint = theme.textTertiary,
                modifier = Modifier.size(18.dp)
            )
        }
    }
}
