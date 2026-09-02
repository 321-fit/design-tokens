package com.fit321.fitui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.Spacer
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.fit321.fitui.theme.LocalFitTheme
import com.fit321.fitui.tokens.FitFont
import com.fit321.fitui.tokens.FitRadius
import com.fit321.fitui.tokens.FitSpacing
import com.fit321.fitui.tokens.fitCardElevation
import com.fit321.designtokens.R

/**
 * The list-row card: an elevated rounded surface with an optional leading visual, a title
 * (+ optional inline trailing / subtitle) and an optional trailing element, a chevron by
 * default.
 *
 * Every part of it is a slot or a token, which is what a row in the wild turns out to need:
 * a status pill beside the title, a second line that is not a string, a surface that is not
 * the default one. [FitSettingsCard] is the same row with the decisions already made — reach
 * for it when a settings list wants exactly its shape, and for this when it does not.
 */
@Composable
fun FitListRow(
    title: String,
    modifier: Modifier = Modifier,
    leading: (@Composable () -> Unit)? = null,
    titleStyle: TextStyle = FitFont.body1,
    titleColor: Color? = null,
    titleTrailing: (@Composable RowScope.() -> Unit)? = null,
    subtitle: String? = null,
    subtitleColor: Color? = null,
    subtitleStyle: TextStyle = FitFont.body2,
    subtitleSlot: (@Composable ColumnScope.() -> Unit)? = null,
    trailing: (@Composable () -> Unit)? = { FitRowChevron() },
    onClick: (() -> Unit)? = null,
    shape: Shape = RoundedCornerShape(FitRadius.card),
    surface: Color? = null,
    contentPadding: PaddingValues = PaddingValues(FitSpacing.sp3),
    itemSpacing: Dp = FitSpacing.sp3,
) {
    val theme = LocalFitTheme.current
    Row(
        modifier = modifier
            .fillMaxWidth()
            .fitCardElevation(shape)
            .clip(shape)
            .background(surface ?: theme.surfaceHigh)
            .then(if (onClick != null) Modifier.clickable(onClick = onClick) else Modifier)
            .padding(contentPadding),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(itemSpacing),
    ) {
        leading?.invoke()
        Column(modifier = Modifier.weight(1f)) {
            if (titleTrailing != null) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(FitSpacing.sp1_5),
                ) {
                    Text(
                        text = title,
                        style = titleStyle,
                        color = titleColor ?: theme.textPrimary,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        modifier = Modifier.weight(1f, fill = false),
                    )
                    titleTrailing()
                }
            } else {
                Text(
                    text = title,
                    style = titleStyle,
                    color = titleColor ?: theme.textPrimary,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                )
            }
            when {
                subtitleSlot != null -> subtitleSlot()
                subtitle != null -> {
                    Spacer(Modifier.size(FitSpacing.sp1))
                    Text(
                        text = subtitle,
                        style = subtitleStyle,
                        color = subtitleColor ?: theme.textSecondary,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                    )
                }
            }
        }
        trailing?.invoke()
    }
}

@Composable
fun FitRowChevron() {
    Icon(
        painter = painterResource(R.drawable.ic_fit_chevron_right),
        contentDescription = null,
        tint = LocalFitTheme.current.textTertiary,
        modifier = Modifier.size(16.dp),
    )
}
