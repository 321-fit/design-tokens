package com.fit321.fitui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.fit321.fitui.theme.LocalFitTheme
import com.fit321.fitui.tokens.FitColors
import com.fit321.fitui.tokens.FitFont
import com.fit321.fitui.tokens.FitSpacing

@Composable
fun FitTipCard(
    title: String,
    subtitle: String,
    cta: String,
    icon: ImageVector,
    onTap: () -> Unit,
    onDismiss: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val theme = LocalFitTheme.current
    val shape = RoundedCornerShape(14.dp)
    Box(
        modifier = modifier
            .fillMaxWidth()
            .clip(shape)
            .background(theme.surfaceHigh)
            .border(1.dp, theme.divider, shape)
            .height(IntrinsicSize.Min)
            .clickable { onTap() },
    ) {
        Box(
            modifier = Modifier
                .align(Alignment.CenterStart)
                .fillMaxHeight()
                .width(3.dp)
                .background(FitColors.Teal.t500),
        )
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(
                    start = FitSpacing.sp3_5,
                    top = FitSpacing.sp3_5,
                    bottom = FitSpacing.sp3_5,
                    end = FitSpacing.sp9,
                ),
            verticalAlignment = Alignment.Top,
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = FitColors.Yellow.y400,
                modifier = Modifier.size(16.dp),
            )
            Spacer(Modifier.width(FitSpacing.sp3))
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = title,
                    style = FitFont.body2.copy(fontWeight = FontWeight.Medium),
                    color = theme.textPrimary,
                )
                Spacer(Modifier.size(FitSpacing.sp0_5))
                Text(
                    text = subtitle,
                    style = FitFont.footnote,
                    color = theme.textTertiary,
                )
                Spacer(Modifier.size(FitSpacing.sp2_5))
                Text(
                    text = "$cta ›",
                    style = FitFont.footnote.copy(fontWeight = FontWeight.Medium),
                    color = FitColors.Blue.b500,
                    modifier = Modifier.clickable { onTap() },
                )
            }
        }
        Box(
            modifier = Modifier
                .align(Alignment.TopEnd)
                .padding(10.dp)
                .size(24.dp)
                .clip(RoundedCornerShape(12.dp))
                .clickable { onDismiss() },
            contentAlignment = Alignment.Center,
        ) {
            Icon(
                imageVector = Icons.Default.Close,
                contentDescription = null,
                tint = theme.textTertiary,
                modifier = Modifier.size(14.dp),
            )
        }
    }
}
