package com.fit321.fitui.v3.components

import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsFocusedAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import com.fit321.fitui.tokens.FitColors
import com.fit321.fitui.v3.theme.LocalFitV3Palette
import com.fit321.fitui.v3.tokens.FitV3Geometry
import com.fit321.fitui.v3.tokens.FitV3Type
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.style.TextOverflow
import com.fit321.designtokens.R

@Composable
fun FitInput(
    value: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier,
    label: String? = null,
    optional: Boolean = false,
    optionalLabel: String = "optional",
    placeholder: String? = null,
    hint: String? = null,
    errorText: String? = null,
    counterMax: Int? = null,
    enabled: Boolean = true,
    singleLine: Boolean = true,
    minLines: Int = 1,
    keyboardType: KeyboardType = KeyboardType.Text,
    visualTransformation: VisualTransformation = VisualTransformation.None,
    leading: (@Composable () -> Unit)? = null,
) {
    val palette = LocalFitV3Palette.current
    val interaction = remember { MutableInteractionSource() }
    val focused by interaction.collectIsFocusedAsState()
    val shape = RoundedCornerShape(FitV3Geometry.fieldRadius)
    val edge = when {
        errorText != null -> palette.textError
        focused -> FitColors.Teal.t600
        else -> null
    }
    Column(modifier = modifier.fillMaxWidth()) {
        if (label != null) {
            Row(
                modifier = Modifier.padding(bottom = 6.dp),
                horizontalArrangement = Arrangement.spacedBy(4.dp),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Text(text = label, style = FitV3Type.fieldLabel, color = palette.textSecondary)
                if (optional) {
                    Text(
                        text = optionalLabel,
                        style = FitV3Type.fieldLabel,
                        color = palette.textTertiary,
                    )
                }
            }
        }
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .then(
                    if (singleLine) {
                        Modifier.heightIn(min = FitV3Geometry.fieldHeight)
                    } else {
                        Modifier.defaultMinSize(minHeight = FitV3Geometry.fieldHeight)
                    },
                )
                .fitV3Surface(shape = shape, hairline = edge ?: palette.surfaceHairline)
                .padding(horizontal = 16.dp, vertical = if (singleLine) 0.dp else 14.dp),
            contentAlignment = if (singleLine) Alignment.CenterStart else Alignment.TopStart,
        ) {
            BasicTextField(
                value = value,
                onValueChange = onValueChange,
                enabled = enabled,
                singleLine = singleLine,
                minLines = minLines,
                textStyle = FitV3Type.fieldText.copy(color = palette.textPrimary),
                cursorBrush = SolidColor(FitColors.Teal.t600),
                interactionSource = interaction,
                keyboardOptions = KeyboardOptions(keyboardType = keyboardType),
                visualTransformation = visualTransformation,
                modifier = Modifier.fillMaxWidth(),
                decorationBox = { inner ->
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(10.dp),
                    ) {
                        if (leading != null) {
                            CompositionLocalProvider(LocalContentColor provides palette.textTertiary) {
                                leading()
                            }
                        }
                        Box(modifier = Modifier.weight(1f)) {
                            if (value.isEmpty() && placeholder != null) {
                                Text(
                                    text = placeholder,
                                    style = FitV3Type.fieldText,
                                    color = palette.textTertiary,
                                )
                            }
                            inner()
                        }
                    }
                },
            )
        }
        if (hint != null || errorText != null || counterMax != null) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 6.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalAlignment = Alignment.Top,
            ) {
                val below = errorText ?: hint
                Text(
                    text = below.orEmpty(),
                    style = FitV3Type.fieldHint,
                    color = if (errorText != null) palette.textError else palette.textTertiary,
                    modifier = Modifier.weight(1f),
                )
                if (counterMax != null) {
                    Text(
                        text = "${value.length}/$counterMax",
                        style = FitV3Type.fieldHint,
                        color = palette.textTertiary,
                    )
                }
            }
        }
    }
}

/**
 * Search over a list — not a form field.
 *
 * Its own component rather than a short [FitInput]: the field in a form is a 56dp control the
 * eye stops at, and this one sits above a list the reader is already looking at. Lower, quieter,
 * and it never carries a label.
 */
@Composable
fun FitSearchField(
    value: String,
    onValueChange: (String) -> Unit,
    placeholder: String,
    modifier: Modifier = Modifier,
) {
    val palette = LocalFitV3Palette.current
    val shape = RoundedCornerShape(FitV3Geometry.chipRadius)
    Row(
        modifier = modifier
            .fillMaxWidth()
            .fitV3Surface(shape)
            .padding(horizontal = 12.dp, vertical = 10.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        Icon(
            painter = painterResource(R.drawable.ic_fit_search),
            contentDescription = null,
            tint = palette.textTertiary,
            modifier = Modifier.size(16.dp),
        )
        Box(modifier = Modifier.weight(1f)) {
            BasicTextField(
                value = value,
                onValueChange = onValueChange,
                singleLine = true,
                textStyle = FitV3Type.fieldText.copy(color = palette.textPrimary),
                cursorBrush = SolidColor(FitColors.Teal.t600),
                modifier = Modifier.fillMaxWidth(),
            )
            if (value.isEmpty()) {
                Text(text = placeholder, style = FitV3Type.fieldText, color = palette.textTertiary)
            }
        }
        if (value.isNotEmpty()) {
            Icon(
                painter = painterResource(R.drawable.ic_fit_close),
                contentDescription = null,
                tint = palette.textTertiary,
                modifier = Modifier
                    .size(16.dp)
                    .clickable { onValueChange("") },
            )
        }
    }
}

/**
 * A field that opens something instead of taking typing — `.cd-f-input.cd-f-row`. It wears the
 * field's chrome rather than a row's, because in a form the thing the eye follows is the column
 * of boxes: a picker that looked like a list row would break that column in half.
 */
@Composable
fun FitSelectField(
    value: String?,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    label: String? = null,
    optional: Boolean = false,
    optionalLabel: String = "optional",
    placeholder: String? = null,
    hint: String? = null,
    leading: (@Composable () -> Unit)? = null,
) {
    val palette = LocalFitV3Palette.current
    val shape = RoundedCornerShape(FitV3Geometry.fieldRadius)
    Column(modifier = modifier.fillMaxWidth()) {
        if (label != null) {
            Row(
                modifier = Modifier.padding(bottom = 6.dp),
                horizontalArrangement = Arrangement.spacedBy(4.dp),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Text(text = label, style = FitV3Type.fieldLabel, color = palette.textSecondary)
                if (optional) {
                    Text(
                        text = optionalLabel,
                        style = FitV3Type.fieldLabel,
                        color = palette.textTertiary,
                    )
                }
            }
        }
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .heightIn(min = FitV3Geometry.fieldHeight)
                .fitV3Surface(shape = shape, hairline = palette.surfaceHairline)
                .clip(shape)
                .clickable { onClick() }
                .padding(horizontal = 16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(10.dp),
        ) {
            if (leading != null) {
                CompositionLocalProvider(LocalContentColor provides palette.textTertiary) {
                    leading()
                }
            }
            Text(
                text = value ?: placeholder.orEmpty(),
                style = FitV3Type.fieldText,
                color = if (value != null) palette.textPrimary else palette.textTertiary,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                modifier = Modifier.weight(1f),
            )
            FitRowChevron()
        }
        if (hint != null) {
            Text(
                text = hint,
                style = FitV3Type.fieldHint,
                color = palette.textTertiary,
                modifier = Modifier.padding(top = 6.dp),
            )
        }
    }
}
