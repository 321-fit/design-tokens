package com.fit321.fitui.v3.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.fit321.fitui.v3.theme.LocalFitV3Palette
import com.fit321.fitui.v3.tokens.FitV3Geometry
import com.fit321.fitui.v3.tokens.FitV3Type

/**
 * `.ms-gav` — a group in the slot a person occupies: two faces set diagonally inside the same
 * 40dp footprint, not the horizontal overlap [FitFaceStack] draws.
 */
@Composable
fun FitGroupFaces(
    initials: List<String>,
    modifier: Modifier = Modifier,
    size: Dp = FitV3Geometry.leadingPlate,
) {
    val palette = LocalFitV3Palette.current
    val face = size * FACE_RATIO
    val fontSize = (face.value * FACE_INK_RATIO).sp
    Box(modifier = modifier.size(size)) {
        initials.take(2).forEachIndexed { index, value ->
            // The ring is `box-shadow: 0 0 0 2px`, which sits *outside* the face — a Compose
            // border would eat into it instead, shrinking the circle until the two stop
            // overlapping. Hence a ring-coloured plate with the face drawn inside it.
            Box(
                modifier = Modifier
                    .size(face + FACE_RING * 2)
                    .align(if (index == 0) Alignment.TopStart else Alignment.BottomEnd)
                    .background(palette.stackedFaceRing, CircleShape)
                    .padding(FACE_RING)
                    .background(palette.stackedFace, CircleShape),
                contentAlignment = Alignment.Center,
            ) {
                Text(
                    text = value,
                    style = FitV3Type.statLabelSmall.copy(
                        fontSize = fontSize,
                        fontWeight = FontWeight.SemiBold,
                    ),
                    color = palette.textPrimary,
                    maxLines = 1,
                )
            }
        }
    }
}

/** `.ms-tag` — a word about what a row is, carried on the row's own plate. */
@Composable
fun FitRowTag(text: String, modifier: Modifier = Modifier) {
    val palette = LocalFitV3Palette.current
    Box(
        modifier = modifier
            .background(palette.raised, RoundedCornerShape(TAG_RADIUS))
            .padding(horizontal = TAG_PADDING_H, vertical = TAG_PADDING_V),
    ) {
        Text(text = text, style = FitV3Type.rowTag, color = palette.textSecondary, maxLines = 1)
    }
}

private const val FACE_RATIO = 0.625f
private const val FACE_INK_RATIO = 0.36f
private val FACE_RING = 2.dp
private val TAG_RADIUS = 6.dp
private val TAG_PADDING_H = 7.dp
private val TAG_PADDING_V = 2.dp

@Composable
fun FitChatBubble(text: String, mine: Boolean, modifier: Modifier = Modifier) {
    val palette = LocalFitV3Palette.current
    val radius = FitV3Geometry.bubbleRadius
    val tail = FitV3Geometry.bubbleTail
    val shape = if (mine) {
        RoundedCornerShape(radius, radius, tail, radius)
    } else {
        RoundedCornerShape(radius, radius, radius, tail)
    }
    Box(
        modifier = modifier
            .background(if (mine) palette.bubbleMine else palette.surface, shape)
            .then(
                if (mine) {
                    Modifier
                } else {
                    Modifier.border(1.dp, palette.surfaceHairline, shape)
                },
            )
            .padding(horizontal = BUBBLE_PADDING_H, vertical = BUBBLE_PADDING_V),
    ) {
        Text(text = text, style = FitV3Type.bubble, color = palette.textPrimary)
    }
}

@Composable
fun FitChatDateSeparator(text: String, modifier: Modifier = Modifier) {
    val palette = LocalFitV3Palette.current
    Text(
        text = text,
        style = FitV3Type.chatSeparator,
        color = palette.textTertiary,
        modifier = modifier.padding(top = SEPARATOR_TOP, bottom = SEPARATOR_BOTTOM),
    )
}

@Composable
fun FitChatUnreadDivider(text: String, modifier: Modifier = Modifier) {
    val palette = LocalFitV3Palette.current
    Row(
        modifier = modifier.fillMaxWidth().padding(vertical = UNREAD_PADDING_V),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(UNREAD_GAP),
    ) {
        Rule(modifier = Modifier.weight(1f))
        Text(text = text, style = FitV3Type.chatUnread, color = palette.textSecondary, maxLines = 1)
        Rule(modifier = Modifier.weight(1f))
    }
}

@Composable
private fun Rule(modifier: Modifier = Modifier) {
    Box(modifier = modifier.height(1.dp).background(LocalFitV3Palette.current.divider))
}

@Composable
fun FitChatSenderBubble(
    text: String,
    initials: String,
    modifier: Modifier = Modifier,
    name: String? = null,
    imageUrl: String? = null,
    showFace: Boolean = true,
) {
    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.spacedBy(SENDER_GAP),
        verticalAlignment = Alignment.Bottom,
    ) {
        if (showFace) {
            FitV3Avatar(
                initials = initials,
                size = FitV3Geometry.senderFace,
                imageUrl = imageUrl,
            )
        } else {
            Spacer(modifier = Modifier.width(FitV3Geometry.senderFace))
        }
        Column {
            if (name != null) {
                Text(
                    text = name,
                    style = FitV3Type.senderName,
                    color = LocalFitV3Palette.current.textTertiary,
                    maxLines = 1,
                    modifier = Modifier.padding(start = SENDER_NAME_INDENT, bottom = SENDER_NAME_GAP),
                )
            }
            FitChatBubble(text = text, mine = false)
        }
    }
}

@Composable
fun FitChatHello(
    name: String,
    initials: String,
    modifier: Modifier = Modifier,
    imageUrl: String? = null,
    line: String? = null,
) {
    val palette = LocalFitV3Palette.current
    Column(
        modifier = modifier.fillMaxWidth().padding(horizontal = HELLO_INSET),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        FitV3Avatar(
            initials = initials,
            size = FitV3Geometry.profileAvatar,
            imageUrl = imageUrl,
            plate = FitV3AvatarPlate.OnCanvas,
        )
        Text(
            text = name,
            style = FitV3Type.compactName,
            color = palette.textPrimary,
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(top = HELLO_NAME_TOP),
        )
        if (line != null) {
            Text(
                text = line,
                style = FitV3Type.identityContext,
                color = palette.textSecondary,
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(top = HELLO_LINE_TOP),
            )
        }
    }
}

private val BUBBLE_PADDING_H = 13.dp
private val BUBBLE_PADDING_V = 9.dp
private val SEPARATOR_TOP = 8.dp
private val SEPARATOR_BOTTOM = 4.dp
private val UNREAD_PADDING_V = 6.dp
private val UNREAD_GAP = 10.dp
private val SENDER_GAP = 8.dp
private val SENDER_NAME_INDENT = 12.dp
private val SENDER_NAME_GAP = 3.dp
private val HELLO_INSET = 32.dp
private val HELLO_NAME_TOP = 12.dp
private val HELLO_LINE_TOP = 6.dp

@Composable
fun FitChatComposer(
    value: String,
    onValueChange: (String) -> Unit,
    onSend: () -> Unit,
    modifier: Modifier = Modifier,
    placeholder: String? = null,
    sendIcon: @Composable () -> Unit,
) {
    val palette = LocalFitV3Palette.current
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(
                start = FitV3Geometry.screenInset,
                end = FitV3Geometry.screenInset,
                top = COMPOSER_TOP,
                bottom = COMPOSER_BOTTOM,
            ),
        horizontalArrangement = Arrangement.spacedBy(COMPOSER_GAP),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Box(
            modifier = Modifier
                .weight(1f)
                .height(FitV3Geometry.composerHeight)
                .background(palette.surface, CircleShape)
                .border(1.dp, palette.surfaceHairline, CircleShape)
                .padding(horizontal = FIELD_INSET),
            contentAlignment = Alignment.CenterStart,
        ) {
            BasicTextField(
                value = value,
                onValueChange = onValueChange,
                singleLine = false,
                textStyle = FitV3Type.fieldText.copy(color = palette.textPrimary),
                cursorBrush = SolidColor(palette.textPrimary),
                modifier = Modifier.fillMaxWidth(),
                decorationBox = { inner ->
                    if (value.isEmpty() && placeholder != null) {
                        Text(
                            text = placeholder,
                            style = FitV3Type.fieldText,
                            color = palette.textTertiary,
                            maxLines = 1,
                        )
                    }
                    inner()
                },
            )
        }
        Box(
            modifier = Modifier
                .size(FitV3Geometry.composerHeight)
                .shadow(
                    elevation = SEND_LIFT,
                    shape = CircleShape,
                    clip = false,
                    ambientColor = palette.ctaShadow,
                    spotColor = palette.ctaShadow,
                )
                .clip(CircleShape)
                .background(Brush.verticalGradient(colorStops = palette.ctaStops.toTypedArray()))
                .clickable { if (value.isNotBlank()) onSend() },
            contentAlignment = Alignment.Center,
        ) {
            CompositionLocalProvider(LocalContentColor provides palette.ctaLabel) { sendIcon() }
        }
    }
}

private val COMPOSER_TOP = 10.dp
private val COMPOSER_BOTTOM = 12.dp
private val COMPOSER_GAP = 10.dp
private val FIELD_INSET = 18.dp
private val SEND_LIFT = 12.dp
