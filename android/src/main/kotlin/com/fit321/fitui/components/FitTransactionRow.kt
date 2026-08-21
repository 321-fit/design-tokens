package com.fit321.fitui.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.ArrowOutward
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.sp
import com.fit321.fitui.theme.LocalFitTheme
import com.fit321.fitui.tokens.FitColors
import com.fit321.fitui.tokens.FitFont
import com.fit321.fitui.tokens.FitSpacing

// ============================================================================
// FitTransactionRow — balance txn entry. Mirrors Swift FitTransactionRow.
// See `docs/components.md` § FitTransactionRow.
// ============================================================================

enum class FitTransactionType { Earning, Payout, Refund }

@Composable
fun FitTransactionRow(
    type: FitTransactionType,
    title: String,
    subtitle: String,
    amount: String,
    date: String? = null,
    isPending: Boolean = false,
    onTap: (() -> Unit)? = null,
    modifier: Modifier = Modifier
) {
    val theme = LocalFitTheme.current

    val (icon: ImageVector, tone: FitIconPlateTone) = when (type) {
        FitTransactionType.Earning -> Icons.Default.ArrowOutward to FitIconPlateTone.Success
        FitTransactionType.Payout  -> Icons.AutoMirrored.Filled.ArrowForward to FitIconPlateTone.Success
        FitTransactionType.Refund  -> Icons.AutoMirrored.Filled.ArrowBack to FitIconPlateTone.Error
    }
    val amountColor: Color = when {
        isPending -> theme.textTertiary
        type == FitTransactionType.Refund -> FitColors.Red.r400
        else -> FitColors.Teal.t500
    }

    Row(
        modifier = modifier
            .fillMaxWidth()
            .then(if (onTap != null) Modifier.clickable(onClick = onTap) else Modifier)
            .padding(vertical = FitSpacing.sp3),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(FitSpacing.sp3)
    ) {
        FitIconPlate(icon = icon, tone = tone, size = FitIconPlateSize.Md)
        Column(modifier = Modifier.weight(1f)) {
            Text(
                title,
                style = FitFont.body1.copy(fontSize = 15.sp, fontWeight = FontWeight.Medium),
                color = theme.textPrimary,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
            Text(
                subtitle,
                style = FitFont.footnote,
                color = theme.textTertiary,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        }
        Column(horizontalAlignment = Alignment.End) {
            Text(
                amount,
                style = FitFont.body1.copy(fontSize = 15.sp, fontWeight = FontWeight.Medium),
                color = amountColor
            )
            if (date != null) {
                Text(
                    date,
                    style = FitFont.pill,
                    color = theme.textTertiary
                )
            }
        }
    }
}
