package com.fit321.fitui.v3.components

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.drawOutline
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.translate
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.fit321.designtokens.R
import com.fit321.fitui.tokens.FitColors
import com.fit321.fitui.v3.tokens.FitV3Colors
import com.fit321.fitui.v3.tokens.FitV3Look
import com.fit321.fitui.v3.theme.LocalFitV3Palette
import com.fit321.fitui.v3.tokens.FitV3Geometry
import com.fit321.fitui.v3.tokens.FitV3Type

/** `.ob-prog` — back, how far along, and `N of M`: the fixed top of every setup step. */
@Composable
fun FitStepProgress(
    step: Int,
    total: Int,
    label: String,
    modifier: Modifier = Modifier,
    onBack: (() -> Unit)? = null,
) {
    val palette = LocalFitV3Palette.current
    val fraction by animateFloatAsState(
        targetValue = if (total > 0) step.toFloat() / total else 0f,
        animationSpec = tween(300),
        label = "step",
    )
    val fill: Brush = if (palette.look == FitV3Look.Tinted) {
        SolidColor(palette.textPrimary)
    } else {
        FitColors.brandGradient
    }
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(start = 20.dp, end = 20.dp, top = 14.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(12.dp),
    ) {
        Box(
            modifier = Modifier
                .size(FitV3Geometry.stepBack)
                .clip(CircleShape)
                .background(palette.headerCircleFill)
                .then(if (onBack != null) Modifier.clickable { onBack() } else Modifier),
            contentAlignment = Alignment.Center,
        ) {
            Icon(
                painter = painterResource(R.drawable.ic_fit_chevron_left),
                contentDescription = null,
                tint = palette.textPrimary,
                modifier = Modifier.size(16.dp),
            )
        }
        Box(
            modifier = Modifier
                .weight(1f)
                .height(FitV3Geometry.progressBarHeight)
                .clip(CircleShape)
                .background(palette.raised),
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth(fraction.coerceIn(0f, 1f))
                    .height(FitV3Geometry.progressBarHeight)
                    .clip(CircleShape)
                    .background(fill),
            )
        }
        Text(text = label, style = FitV3Type.stepCount, color = palette.textTertiary)
    }
}

/** `.ob-q` + `.ob-hint` — the question, and at most one line on why it is asked. */
@Composable
fun FitStepQuestion(
    question: String,
    modifier: Modifier = Modifier,
    hint: String? = null,
    centered: Boolean = false,
) {
    val palette = LocalFitV3Palette.current
    val side = if (centered) 28.dp else 20.dp
    val align = if (centered) TextAlign.Center else TextAlign.Start
    Column(modifier = modifier.fillMaxWidth().padding(start = side, end = side, top = 22.dp)) {
        Text(
            text = question,
            style = FitV3Type.stepQuestion,
            color = palette.textPrimary,
            textAlign = align,
            modifier = Modifier.fillMaxWidth(),
        )
        if (hint != null) {
            Text(
                text = hint,
                style = FitV3Type.stepHint,
                color = palette.textTertiary,
                textAlign = align,
                modifier = Modifier.fillMaxWidth().padding(top = 8.dp),
            )
        }
    }
}

/**
 * `.cd-pf-ph` — an empty slot on a card being built, dashed so it reads as "still to come".
 * [focused] breathes it: the slot the current step fills.
 */
@Composable
fun FitDraftSlot(
    text: String,
    modifier: Modifier = Modifier,
    focused: Boolean = false,
) {
    val palette = LocalFitV3Palette.current
    Box(
        modifier = modifier
            .fitBreathing(focused, RoundedCornerShape(12.dp))
            .height(FitV3Geometry.draftSlotHeight)
            .fitDashedOutline(palette.draftSlotEdge, if (focused) 12.dp else FitV3Geometry.draftSlotHeight / 2)
            .padding(horizontal = 12.dp),
        contentAlignment = Alignment.Center,
    ) {
        Text(text = text, style = FitV3Type.draftSlot, color = palette.textTertiary)
    }
}

/**
 * The avatar of a card being built when it is also the photo control: the camera badge says a
 * tap changes the picture (`.ob-av-tap`).
 */
@Composable
fun FitCardPhoto(
    initials: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    imageUrl: String? = null,
    focused: Boolean = false,
    size: Dp = FitV3Geometry.profileCompactAvatar,
) {
    val palette = LocalFitV3Palette.current
    Box(modifier = modifier.size(size)) {
        Box(
            modifier = Modifier
                .fitBreathing(focused, CircleShape)
                .clip(CircleShape)
                .clickable { onClick() },
        ) {
            FitV3Avatar(
                initials = initials,
                size = size,
                imageUrl = imageUrl,
                plate = FitV3AvatarPlate.Brand,
            )
        }
        PhotoBadge(
            icon = R.drawable.ic_fit_camera,
            size = FitV3Geometry.cardPhotoBadge,
            iconSize = 13.dp,
            ink = palette.unreadInk,
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .offset(x = 2.dp, y = 2.dp),
        )
    }
}

/** `.ob-av` — the photo as the subject of a step: the picture, or a camera on a quiet plate, and a plus. */
@Composable
fun FitPhotoControl(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    imageUrl: String? = null,
) {
    val palette = LocalFitV3Palette.current
    val size = FitV3Geometry.photoControl
    Box(modifier = modifier.size(size)) {
        Box(
            modifier = Modifier
                .size(size)
                .fitV3Surface(CircleShape)
                .clip(CircleShape)
                .clickable { onClick() },
            contentAlignment = Alignment.Center,
        ) {
            if (imageUrl != null) {
                FitV3Avatar(initials = "", size = size, imageUrl = imageUrl)
            } else {
                Icon(
                    painter = painterResource(R.drawable.ic_fit_camera),
                    contentDescription = null,
                    tint = palette.textTertiary,
                    modifier = Modifier.size(30.dp),
                )
            }
        }
        PhotoBadge(
            icon = R.drawable.ic_fit_plus,
            size = FitV3Geometry.photoBadge,
            iconSize = 16.dp,
            ink = palette.ctaLabel,
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .offset(y = (-2).dp),
        )
    }
}

@Composable
private fun PhotoBadge(
    icon: Int,
    size: Dp,
    iconSize: Dp,
    ink: Color,
    modifier: Modifier = Modifier,
) {
    Box(
        modifier = modifier
            .size(size)
            .shadow(8.dp, CircleShape)
            .background(Color.White, CircleShape),
        contentAlignment = Alignment.Center,
    ) {
        Icon(
            painter = painterResource(icon),
            contentDescription = null,
            tint = ink,
            modifier = Modifier.size(iconSize),
        )
    }
}

/** `.sp-card` — one sport in the picker's two-column grid. */
@Composable
fun FitSportTile(
    label: String,
    selected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    icon: (@Composable () -> Unit)? = null,
) {
    val palette = LocalFitV3Palette.current
    val shape = RoundedCornerShape(FitV3Geometry.chipRadius)
    Row(
        modifier = modifier
            .heightIn(min = FitV3Geometry.sportTileMinHeight)
            .then(
                // `.sp-card.selected` is the selection gradient with a teal-600 edge in every look;
                // unselected has a transparent border, so no hairline either.
                if (selected) {
                    Modifier
                        .background(palette.surface, shape)
                        .background(FitColors.selectionGradient, shape)
                        .border(1.dp, FitColors.selectionBorder, shape)
                } else {
                    Modifier.fitV3Surface(shape, hairline = Color.Transparent)
                },
            )
            .clip(shape)
            .clickable { onClick() }
            .padding(12.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(10.dp),
    ) {
        if (icon != null) {
            CompositionLocalProvider(
                LocalContentColor provides if (selected) palette.textPrimary else palette.textSecondary,
            ) { icon() }
        }
        Text(text = label, style = FitV3Type.tileLabel, color = palette.textPrimary)
    }
}

/** The picker's grid: two equal columns, tiles 10dp apart. */
@Composable
fun <T> FitSportTileGrid(
    items: List<T>,
    modifier: Modifier = Modifier,
    tile: @Composable (item: T, modifier: Modifier) -> Unit,
) {
    Column(modifier = modifier.fillMaxWidth(), verticalArrangement = Arrangement.spacedBy(FitV3Geometry.tileGap)) {
        items.chunked(2).forEach { pair ->
            Row(horizontalArrangement = Arrangement.spacedBy(FitV3Geometry.tileGap)) {
                pair.forEach { item -> tile(item, Modifier.weight(1f)) }
                if (pair.size == 1) Box(modifier = Modifier.weight(1f))
            }
        }
    }
}

/**
 * `.ob-return` — a real setup screen opened from onboarding says so, names where it lives
 * later, and offers the way back.
 */
@Composable
fun FitSetupStrip(
    title: String,
    actionLabel: String,
    onAction: () -> Unit,
    modifier: Modifier = Modifier,
    subtitle: String? = null,
) {
    val palette = LocalFitV3Palette.current
    val shape = RoundedCornerShape(14.dp)
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(start = 16.dp, end = 16.dp, top = 4.dp, bottom = 10.dp)
            .background(palette.setupStripFill, shape)
            .border(1.dp, palette.setupStripEdge, shape)
            .padding(horizontal = 12.dp, vertical = 10.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(10.dp),
    ) {
        Column(modifier = Modifier.weight(1f)) {
            Text(text = title, style = FitV3Type.stripTitle, color = FitColors.Teal.t500)
            if (subtitle != null) {
                Text(text = subtitle, style = FitV3Type.stripSub, color = palette.textSecondary)
            }
        }
        Box(
            modifier = Modifier
                .height(FitV3Geometry.stepBack)
                .clip(CircleShape)
                .border(1.dp, palette.secondaryBorder, CircleShape)
                .clickable { onAction() }
                .padding(horizontal = 12.dp),
            contentAlignment = Alignment.Center,
        ) {
            Text(text = actionLabel, style = FitV3Type.stripTitle, color = palette.textPrimary)
        }
    }
}

/** `.ob-w-near` — a count with a pin, on a quiet pill. */
@Composable
fun FitNearPill(
    text: String,
    modifier: Modifier = Modifier,
    icon: (@Composable () -> Unit)? = null,
) {
    val palette = LocalFitV3Palette.current
    Row(
        modifier = modifier
            .fitV3Surface(CircleShape)
            .padding(horizontal = 14.dp, vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(6.dp),
    ) {
        if (icon != null) {
            CompositionLocalProvider(LocalContentColor provides FitColors.Teal.t500) { icon() }
        }
        Text(text = text, style = FitV3Type.chip, color = palette.textPrimary)
    }
}

/**
 * `.ob-w-art` — a finished sample of what is about to be built, tilted over two ghosts of
 * the same card and a soft teal glow.
 */
@Composable
fun FitShowcase(
    modifier: Modifier = Modifier,
    card: @Composable () -> Unit,
) {
    val palette = LocalFitV3Palette.current
    val ghostShape = RoundedCornerShape(24.dp)
    val glow = palette.showcaseGlow
    Box(
        modifier = modifier
            .fillMaxWidth()
            .drawBehind {
                val radius = 160.dp.toPx()
                drawCircle(
                    brush = Brush.radialGradient(
                        0f to glow,
                        0.65f to Color.Transparent,
                        center = center.copy(y = size.height * 0.4f),
                        radius = radius,
                    ),
                    radius = radius,
                    center = center.copy(y = size.height * 0.4f),
                )
            }
            .padding(top = 36.dp, bottom = 10.dp),
    ) {
        Box(
            modifier = Modifier
                .padding(horizontal = 64.dp)
                .offset(y = (-28).dp)
                .fillMaxWidth()
                .height(150.dp)
                .rotate(9f)
                .alpha(0.6f)
                .background(palette.quietLift, ghostShape)
                .border(1.dp, palette.surfaceHairline, ghostShape),
        )
        Box(
            modifier = Modifier
                .padding(horizontal = 44.dp)
                .offset(y = (-16).dp)
                .fillMaxWidth()
                .height(150.dp)
                .rotate(4f)
                .background(palette.quietLift, ghostShape)
                .border(1.dp, palette.surfaceHairline, ghostShape),
        )
        Box(
            modifier = Modifier
                .padding(horizontal = 30.dp)
                .rotate(-3f)
                .shadow(30.dp, RoundedCornerShape(FitV3Geometry.panelRadius), clip = false),
        ) { card() }
    }
}

private fun Modifier.fitDashedOutline(color: Color, radius: Dp): Modifier = drawBehind {
    val r = radius.toPx()
    drawRoundRect(
        color = color,
        cornerRadius = CornerRadius(r, r),
        style = Stroke(
            width = 1.dp.toPx(),
            pathEffect = PathEffect.dashPathEffect(floatArrayOf(4.dp.toPx(), 3.dp.toPx())),
        ),
    )
}

/** `.cd-pf-focus` — a slow teal ring that says "your answer lands here". */
@Composable
private fun Modifier.fitBreathing(on: Boolean, shape: androidx.compose.ui.graphics.Shape): Modifier {
    if (!on) return this
    val transition = rememberInfiniteTransition(label = "breathe")
    val pulse by transition.animateFloat(
        initialValue = 0f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(tween(900, easing = FastOutSlowInEasing), RepeatMode.Reverse),
        label = "pulse",
    )
    val density = LocalDensity.current
    val color = LocalFitV3Palette.current.focusRing
    val ring = with(density) { 4.dp.toPx() } * pulse
    return drawBehind {
        if (ring > 0f) {
            val outline = shape.createOutline(
                size.copy(width = size.width + ring, height = size.height + ring),
                layoutDirection,
                this,
            )
            translate(-ring / 2, -ring / 2) {
                drawOutline(outline, color.copy(alpha = color.alpha * pulse), style = Stroke(width = ring))
            }
        }
    }
}

/** `.cd-sp-step` — the number of one how-it-works step, a teal ring around it. */
@Composable
fun FitStepBadge(number: Int, modifier: Modifier = Modifier) {
    Box(
        modifier = modifier
            .size(FitV3Geometry.stepBadge)
            .border(1.5.dp, FitV3Colors.stepBadgeRing, CircleShape),
        contentAlignment = Alignment.Center,
    ) {
        Text(text = number.toString(), style = FitV3Type.stepBadge, color = FitColors.Teal.t500)
    }
}
