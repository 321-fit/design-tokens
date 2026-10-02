package com.fit321.fitui.v3.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.fit321.designtokens.R
import com.fit321.fitui.tokens.FitColors
import com.fit321.fitui.v3.theme.LocalFitV3Palette
import com.fit321.fitui.v3.tokens.FitV3Geometry
import com.fit321.fitui.v3.tokens.FitV3Type

@Composable
fun <T> FitSegmented(
    options: List<T>,
    selected: T,
    onSelectedChange: (T) -> Unit,
    modifier: Modifier = Modifier,
    label: (T) -> String,
) {
    val palette = LocalFitV3Palette.current
    val shape = RoundedCornerShape(FitV3Geometry.fieldRadius)
    Row(
        modifier = modifier
            .fillMaxWidth()
            .fitV3Surface(shape)
            .clip(shape)
            .padding(4.dp),
        horizontalArrangement = Arrangement.spacedBy(4.dp),
    ) {
        options.forEach { option ->
            val isSelected = option == selected
            Box(
                modifier = Modifier
                    .weight(1f)
                    .clip(RoundedCornerShape(FitV3Geometry.fieldRadius - 4.dp))
                    .background(
                        if (isSelected) palette.segmentedSelected else Color.Transparent,
                    )
                    .clickable { onSelectedChange(option) }
                    .padding(vertical = 10.dp),
                contentAlignment = Alignment.Center,
            ) {
                Text(
                    text = label(option),
                    style = FitV3Type.rowValue,
                    color = if (isSelected) palette.textPrimary else palette.textSecondary,
                    textAlign = TextAlign.Center,
                    maxLines = 1,
                )
            }
        }
    }
}

@Composable
fun FitStepper(
    value: String,
    modifier: Modifier = Modifier,
    canDecrease: Boolean = true,
    canIncrease: Boolean = true,
    onDecrease: () -> Unit = {},
    onIncrease: () -> Unit = {},
    decreaseIcon: (@Composable () -> Unit)? = null,
    increaseIcon: (@Composable () -> Unit)? = null,
) {
    val palette = LocalFitV3Palette.current
    val shape = RoundedCornerShape(FitV3Geometry.fieldRadius)
    Row(
        modifier = modifier
            .fillMaxWidth()
            .fitV3Surface(shape)
            .clip(shape)
            .padding(8.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        FitStepperButton(enabled = canDecrease, onClick = onDecrease, content = decreaseIcon)
        Text(
            text = value,
            style = FitV3Type.fieldText,
            color = palette.textPrimary,
            textAlign = TextAlign.Center,
            modifier = Modifier.weight(1f),
        )
        FitStepperButton(enabled = canIncrease, onClick = onIncrease, content = increaseIcon)
    }
}

@Composable
private fun FitStepperButton(
    enabled: Boolean,
    onClick: () -> Unit,
    content: (@Composable () -> Unit)?,
) {
    val palette = LocalFitV3Palette.current
    Box(
        modifier = Modifier
            .size(FitV3Geometry.stepperButton)
            .alpha(if (enabled) 1f else 0.35f)
            .background(palette.raised, CircleShape)
            .clickable(enabled = enabled) { onClick() },
        contentAlignment = Alignment.Center,
    ) {
        CompositionLocalProvider(LocalContentColor provides palette.textPrimary) {
            content?.invoke()
        }
    }
}

@Composable
fun FitSelectListRow(
    title: String,
    selected: Boolean,
    modifier: Modifier = Modifier,
    subtitle: String? = null,
    onClick: () -> Unit = {},
) {
    val palette = LocalFitV3Palette.current
    val shape = RoundedCornerShape(FitV3Geometry.fieldRadius)
    Box(
        modifier = modifier
            .fillMaxWidth()
            .fitV3Surface(
                shape = shape,
                brush = if (selected) palette.selectedFill else null,
                hairline = if (selected) palette.selectedBorder else null,
            )
            .clip(shape)
            .clickable { onClick() },
    ) {
        FitRow(
            title = title,
            subtitle = subtitle,
            trailing = if (selected) {
                { FitSelectCheck() }
            } else {
                null
            },
        )
    }
}

@Composable
fun FitV3Snackbar(
    text: String,
    modifier: Modifier = Modifier,
    actionLabel: String? = null,
    onAction: (() -> Unit)? = null,
) {
    val palette = LocalFitV3Palette.current
    val shape = RoundedCornerShape(percent = 50)
    Row(
        modifier = modifier
            .fillMaxWidth()
            .background(palette.snackbar, shape)
            .border(1.dp, palette.snackbarEdge, shape)
            .padding(horizontal = 16.dp, vertical = 10.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(12.dp),
    ) {
        Text(
            text = text,
            style = FitV3Type.identityContext,
            color = FitColors.Gray.white,
            maxLines = 2,
            modifier = Modifier.weight(1f),
        )
        if (actionLabel != null && onAction != null) {
            Text(
                text = actionLabel,
                style = FitV3Type.chip,
                color = FitColors.Teal.t400,
                modifier = Modifier
                    .clip(RoundedCornerShape(percent = 50))
                    .clickable { onAction() }
                    .padding(horizontal = 4.dp, vertical = 2.dp),
            )
        }
    }
}

@Composable
fun FitSelectCheck(modifier: Modifier = Modifier) {
    Box(
        modifier = modifier
            .size(FitV3Geometry.checkCircle)
            .background(FitColors.Teal.t600, RoundedCornerShape(6.dp)),
        contentAlignment = Alignment.Center,
    ) {
        Icon(
            painter = painterResource(R.drawable.ic_fit_check),
            contentDescription = null,
            tint = FitColors.Gray.white,
            modifier = Modifier.size(12.dp),
        )
    }
}

const val FIT_V3_SNACKBAR_UNDO_MS = 5_000L
