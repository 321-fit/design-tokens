package com.fit321.fitui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
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
import com.fit321.fitui.tokens.FitFont
import com.fit321.fitui.tokens.FitRadius
import com.fit321.fitui.tokens.FitSpacing
import kotlin.math.abs

// ============================================================================
// FitEarningsHero — hero summary card on balance / earnings.
// Mirrors Swift FitEarningsHero. See `docs/components.md` § FitEarningsHero.
// ============================================================================

sealed class FitEarningsTrend {
    data class Up(val percent: Double) : FitEarningsTrend()
    data class Down(val percent: Double) : FitEarningsTrend()
    object Flat : FitEarningsTrend()
}

data class FitEarningsHeroBreakdown(
    val label: String,
    val value: String
)

@Composable
fun FitEarningsHero(
    amount: String,
    period: String,
    trend: FitEarningsTrend? = null,
    breakdown: List<FitEarningsHeroBreakdown> = emptyList(),
    planned: String? = null,
    isEmpty: Boolean = false,
    modifier: Modifier = Modifier
) {
    val theme = LocalFitTheme.current

    Column(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(FitRadius.lg))
            .background(theme.surfaceHigh, RoundedCornerShape(FitRadius.lg))
            .padding(FitSpacing.sp4),
        verticalArrangement = Arrangement.spacedBy(FitSpacing.sp3)
    ) {
        // Header
        Row(verticalAlignment = Alignment.Top) {
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    if (isEmpty) "—" else amount,
                    style = FitFont.headline.copy(fontSize = 28.sp, fontWeight = FontWeight.SemiBold),
                    color = theme.textPrimary
                )
                Text(
                    period,
                    style = FitFont.footnote,
                    color = theme.textTertiary
                )
            }
            if (trend != null && !isEmpty) {
                trendBadge(trend)
            }
        }

        if (breakdown.isNotEmpty() && !isEmpty) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(1.dp)
                    .background(theme.divider)
            )
            Row(
                horizontalArrangement = Arrangement.spacedBy(FitSpacing.sp3),
                verticalAlignment = Alignment.Top
            ) {
                if (breakdown.size >= 1) {
                    BreakdownColumn(breakdown[0], modifier = Modifier.weight(0.6f))
                }
                if (breakdown.size >= 2) {
                    BreakdownColumn(breakdown[1], modifier = Modifier.weight(0.4f))
                }
            }
        }

        if (planned != null && !isEmpty) {
            Text(
                planned,
                style = FitFont.footnote,
                color = theme.textTertiary
            )
        }
    }
}

@Composable
private fun BreakdownColumn(row: FitEarningsHeroBreakdown, modifier: Modifier = Modifier) {
    val theme = LocalFitTheme.current
    Column(modifier = modifier) {
        Text(row.label, style = FitFont.footnote, color = theme.textTertiary)
        Text(
            row.value,
            style = FitFont.body1.copy(fontWeight = FontWeight.Medium),
            color = theme.textPrimary
        )
    }
}

@Composable
private fun trendBadge(trend: FitEarningsTrend) {
    when (trend) {
        is FitEarningsTrend.Up   -> FitBadge("↑ ${formatPct(trend.percent)}", FitBadgeStyle.Success)
        is FitEarningsTrend.Down -> FitBadge("↓ ${formatPct(trend.percent)}", FitBadgeStyle.Danger)
        FitEarningsTrend.Flat    -> FitBadge("—", FitBadgeStyle.Neutral)
    }
}

private fun formatPct(p: Double): String = "${abs(p).toInt()}%"
