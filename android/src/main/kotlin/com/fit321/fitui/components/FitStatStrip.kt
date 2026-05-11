package com.fit321.fitui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.fit321.fitui.theme.LocalFitTheme
import com.fit321.fitui.tokens.FitColors
import com.fit321.fitui.tokens.FitFont
import com.fit321.fitui.tokens.FitSpacing

// ============================================================================
// FitStatStrip — horizontal 4-column readout used on coach profile
// (athlete-view and coach-view). One column may be accented via `accent = true`
// (e.g. price-from).
// Mirrors Swift `FitStatStrip`. See `docs/components.md` § FitStatStrip.
// ============================================================================

data class FitStatStripItem(
    val value: String,
    val label: String,
    val accent: Boolean = false
)

@Composable
fun FitStatStrip(
    items: List<FitStatStripItem>,
    modifier: Modifier = Modifier
) {
    val theme = LocalFitTheme.current
    Row(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(12.dp))
            .background(theme.surfaceHigh)
            .padding(horizontal = FitSpacing.sp4, vertical = FitSpacing.sp3),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(0.dp)
    ) {
        items.forEachIndexed { index, item ->
            Column(
                modifier = Modifier.weight(1f),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    item.value,
                    style = FitFont.body1.copy(fontSize = 18.sp, fontWeight = FontWeight.SemiBold),
                    color = if (item.accent) FitColors.Teal.t500 else theme.textPrimary
                )
                Text(
                    item.label,
                    style = FitFont.caption,
                    color = theme.textTertiary
                )
            }
            if (index < items.size - 1) {
                Box(
                    modifier = Modifier
                        .width(1.dp)
                        .height(32.dp)
                        .background(theme.divider)
                )
            }
        }
    }
}
