package com.fit321.fitui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.res.vectorResource
import com.fit321.designtokens.R
import com.fit321.fitui.theme.LocalFitTheme
import com.fit321.fitui.tokens.FitColors
import com.fit321.fitui.tokens.FitFont
import com.fit321.fitui.tokens.FitSpacing

// ============================================================================
// FitMaturityProgress — "You're a new coach" graduation block. Leading
// icon plate + title + sub + check-list criteria + Learn more link.
// Caller controls visibility (render only while not yet graduated).
// Mirrors Swift `FitMaturityProgress`.
// See `docs/components.md` § FitMaturityProgress.
// ============================================================================

data class FitMaturityCriterion(
    val label: String,
    val done: Boolean
)

@Composable
fun FitMaturityProgress(
    title: String,
    subtitle: String,
    criteria: List<FitMaturityCriterion>,
    learnMoreLabel: String = "Learn more",
    onLearnMore: (() -> Unit)? = null,
    modifier: Modifier = Modifier
) {
    val theme = LocalFitTheme.current
    val shape = RoundedCornerShape(14.dp)

    Column(
        modifier = modifier
            .fillMaxWidth()
            .border(1.dp, theme.divider, shape)
            .padding(FitSpacing.sp4)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            FitIconPlate(
                icon = ImageVector.vectorResource(R.drawable.ic_fit_star_filled),
                tone = FitIconPlateTone.Success,
                size = FitIconPlateSize.Md
            )
            Text(
                title,
                style = FitFont.body1.copy(fontSize = 15.sp, fontWeight = FontWeight.Medium),
                color = theme.textPrimary
            )
        }

        Text(
            subtitle,
            style = FitFont.footnote,
            color = theme.textSecondary,
            modifier = Modifier.padding(top = FitSpacing.sp2, bottom = 14.dp)
        )

        Column(verticalArrangement = Arrangement.spacedBy(FitSpacing.sp2)) {
            criteria.forEach { c ->
                CriterionRow(c)
            }
        }

        if (onLearnMore != null) {
            Row(
                modifier = Modifier
                    .padding(top = 14.dp)
                    .clickable { onLearnMore() },
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                Text(
                    learnMoreLabel,
                    style = FitFont.footnote.copy(fontWeight = FontWeight.Medium),
                    color = FitColors.brandPrimary
                )
                Icon(
                    ImageVector.vectorResource(R.drawable.ic_fit_chevron_right),
                    contentDescription = null,
                    tint = FitColors.brandPrimary,
                    modifier = Modifier.size(13.dp)
                )
            }
        }
    }
}

@Composable
private fun CriterionRow(c: FitMaturityCriterion) {
    val theme = LocalFitTheme.current
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        Box(
            modifier = Modifier
                .size(18.dp)
                .background(
                    if (c.done) FitColors.Teal.t500 else Color.Transparent,
                    CircleShape
                )
                .then(
                    if (c.done) Modifier
                    else Modifier.border(1.5.dp, theme.divider, CircleShape)
                ),
            contentAlignment = Alignment.Center
        ) {
            if (c.done) {
                Icon(
                    ImageVector.vectorResource(R.drawable.ic_fit_check),
                    contentDescription = null,
                    tint = Color.White,
                    modifier = Modifier.size(11.dp)
                )
            }
        }
        Text(
            c.label,
            style = FitFont.footnote.copy(
                textDecoration = if (c.done) TextDecoration.LineThrough else TextDecoration.None
            ),
            color = if (c.done) theme.textTertiary else theme.textSecondary
        )
    }
}
