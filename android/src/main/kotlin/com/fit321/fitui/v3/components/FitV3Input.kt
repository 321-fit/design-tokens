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
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
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
) {
    val palette = LocalFitV3Palette.current
    val interaction = remember { MutableInteractionSource() }
    val focused by interaction.collectIsFocusedAsState()
    val shape = RoundedCornerShape(FitV3Geometry.fieldRadius)
    val edge = when {
        errorText != null -> FitColors.Red.r400
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
                    if (value.isEmpty() && placeholder != null) {
                        Text(
                            text = placeholder,
                            style = FitV3Type.fieldText,
                            color = palette.textTertiary,
                        )
                    }
                    inner()
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
                    color = if (errorText != null) FitColors.Red.r400 else palette.textTertiary,
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
