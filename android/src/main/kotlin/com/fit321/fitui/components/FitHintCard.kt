package com.fit321.fitui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.fit321.fitui.theme.LocalFitTheme
import com.fit321.fitui.tokens.FitFont
import com.fit321.fitui.tokens.FitSpacing
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ChevronRight

@Composable
fun FitHintCard(
    title: String,
    subtitle: String,
    icon: ImageVector,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) = FitHintCard(
    title = title,
    subtitle = subtitle,
    onClick = onClick,
    modifier = modifier,
) { tint, size -> Icon(icon, null, tint = tint, modifier = Modifier.size(size)) }

/** [FitHintCard] with a caller-supplied glyph — see the note on [FitChip]. */
@Composable
fun FitHintCard(
    title: String,
    subtitle: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    leading: @Composable (tint: Color, size: Dp) -> Unit,
) {
    val theme = LocalFitTheme.current
    val shape = RoundedCornerShape(14.dp)
    val borderColor = theme.divider
    Row(
        modifier = modifier
            .fillMaxWidth()
            .clip(shape)
            .background(theme.surfaceHigh)
            .drawBehind {
                val stroke = 1.dp.toPx()
                drawRoundRect(
                    color = borderColor,
                    topLeft = Offset(stroke / 2f, stroke / 2f),
                    size = Size(size.width - stroke, size.height - stroke),
                    cornerRadius = CornerRadius(14.dp.toPx() - stroke / 2f),
                    style = Stroke(
                        width = stroke,
                        pathEffect = PathEffect.dashPathEffect(
                            floatArrayOf(6.dp.toPx(), 4.dp.toPx()),
                            0f,
                        ),
                    ),
                )
            }
            .clickable { onClick() }
            .padding(FitSpacing.sp3_5),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(FitSpacing.sp3),
    ) {
        FitIconPlate(
            tone = FitIconPlateTone.Info,
            size = FitIconPlateSize.MdLg,
            icon = leading,
        )
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = title,
                style = FitFont.body1.copy(fontSize = 15.sp, fontWeight = FontWeight.Medium),
                color = theme.textPrimary,
            )
            Text(
                text = subtitle,
                style = FitFont.footnote,
                color = theme.textTertiary,
            )
        }
        Icon(
            imageVector = Icons.Default.ChevronRight,
            contentDescription = null,
            tint = theme.textTertiary,
            modifier = Modifier.size(18.dp),
        )
    }
}
