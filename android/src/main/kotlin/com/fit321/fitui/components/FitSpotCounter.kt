package com.fit321.fitui.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.fit321.fitui.theme.LocalFitTheme
import com.fit321.fitui.tokens.FitFont

// ============================================================================
// FitSpotCounter — group training capacity bar.
// Mirrors Swift FitSpotCounter. See `docs/components.md` § FitSpotCounter.
// ============================================================================

@Composable
fun FitSpotCounter(
    available: Int,
    total: Int,
    showLabel: Boolean = true,
    compact: Boolean = false,
    modifier: Modifier = Modifier
) {
    val theme = LocalFitTheme.current
    val safeTotal = total.coerceAtLeast(1)
    val safeAvail = available.coerceAtLeast(0).coerceAtMost(safeTotal)
    val ratio = safeAvail.toFloat() / safeTotal.toFloat()
    val height = if (compact) 8.dp else 12.dp

    Box(modifier = modifier.height(height)) {
        // The bar itself is [FitProgressBar]: this component adds capacity semantics and
        // the centred label on top of it, and must not carry a second copy of the drawing.
        FitProgressBar(progress = ratio, height = height, modifier = Modifier.fillMaxWidth())
        if (showLabel) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    "$safeAvail of $safeTotal spots",
                    style = FitFont.pill.copy(fontSize = 11.sp, fontWeight = FontWeight.Medium),
                    color = theme.textPrimary
                )
            }
        }
    }
}
