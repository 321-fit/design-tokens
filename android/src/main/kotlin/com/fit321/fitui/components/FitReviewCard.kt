package com.fit321.fitui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.res.vectorResource
import com.fit321.designtokens.R
import com.fit321.fitui.theme.LocalFitTheme
import com.fit321.fitui.tokens.FitColors
import com.fit321.fitui.tokens.FitFont
import com.fit321.fitui.tokens.FitSpacing

// ============================================================================
// FitReviewCard — 280dp snap card with reviewer + stars + clamped body, or a
// trailing "Show all N reviews" call-to-action card. Designed to live in
// `FitReviewCarousel` (horizontal scroller).
// Mirrors Swift `FitReviewCard`. See `docs/components.md` § FitReviewCard.
// ============================================================================

sealed class FitReviewCardData {
    data class Review(
        val reviewer: String,
        val initials: String,
        val whenText: String,
        val stars: Int,
        val body: String
    ) : FitReviewCardData()

    data class ShowAll(val total: Int) : FitReviewCardData()
}

@Composable
fun FitReviewCard(
    data: FitReviewCardData,
    onTap: (() -> Unit)? = null,
    modifier: Modifier = Modifier
) {
    val theme = LocalFitTheme.current
    val shape = RoundedCornerShape(14.dp)
    val isLight = theme.screenBg != FitColors.Gray.g900

    val container = modifier
        .width(280.dp)
        .clip(shape)
        .background(if (isLight) FitColors.Gray.white else theme.surfaceHigh)
        .then(if (isLight) Modifier.border(1.dp, theme.divider, shape) else Modifier)
        .then(if (onTap != null) Modifier.clickable { onTap() } else Modifier)
        .padding(14.dp)

    when (data) {
        is FitReviewCardData.Review -> {
            Column(modifier = container, verticalArrangement = Arrangement.spacedBy(FitSpacing.sp2)) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    FitAvatar(
                        initials = data.initials,
                        size = FitAvatarSize.Sm,
                        bg = SolidColor(FitColors.Gray.g600)
                    )
                    Column {
                        Text(
                            data.reviewer,
                            style = FitFont.body2.copy(fontWeight = FontWeight.Medium),
                            color = theme.textPrimary
                        )
                        Text(
                            data.whenText,
                            style = FitFont.caption,
                            color = theme.textTertiary
                        )
                    }
                }
                StarRow(filled = data.stars, total = 5, theme = theme)
                Text(
                    data.body,
                    style = FitFont.body2,
                    color = theme.textSecondary,
                    maxLines = 4,
                    overflow = TextOverflow.Ellipsis
                )
            }
        }
        is FitReviewCardData.ShowAll -> {
            Box(
                modifier = container.height(160.dp),
                contentAlignment = Alignment.Center
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Icon(
                        Icons.AutoMirrored.Filled.ArrowForward,
                        contentDescription = null,
                        tint = FitColors.brandPrimary,
                        modifier = Modifier.size(28.dp)
                    )
                    Text(
                        "Show all ${data.total} reviews",
                        style = FitFont.button2,
                        color = FitColors.brandPrimary
                    )
                }
            }
        }
    }
}

@Composable
private fun StarRow(filled: Int, total: Int, theme: com.fit321.fitui.tokens.FitColors.Theme) {
    Row(horizontalArrangement = Arrangement.spacedBy(2.dp)) {
        repeat(total) { i ->
            Icon(
                ImageVector.vectorResource(R.drawable.ic_fit_star_filled),
                contentDescription = null,
                tint = if (i < filled) FitColors.Teal.t500 else theme.textTertiary,
                modifier = Modifier.size(12.dp)
            )
        }
    }
}

// ============================================================================
// FitReviewCarousel — horizontal scroller with snap-friendly spacing.
// Pass a list of `FitReviewCardData` plus an optional tap handler keyed by
// index (e.g., last "Show all" card vs review tap routing).
// ============================================================================

@Composable
fun FitReviewCarousel(
    cards: List<FitReviewCardData>,
    onCardTap: ((Int) -> Unit)? = null,
    modifier: Modifier = Modifier
) {
    val scroll = rememberScrollState()
    Row(
        modifier = modifier
            .horizontalScroll(scroll)
            .padding(horizontal = FitSpacing.sp4),
        horizontalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        cards.forEachIndexed { index, data ->
            FitReviewCard(
                data = data,
                onTap = onCardTap?.let { handler -> { handler(index) } }
            )
        }
    }
}
