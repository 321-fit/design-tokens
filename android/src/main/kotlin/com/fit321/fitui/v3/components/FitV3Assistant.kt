package com.fit321.fitui.v3.components

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
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
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CircularProgressIndicator
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
import androidx.compose.ui.draw.scale
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.fit321.designtokens.R
import com.fit321.fitui.tokens.FitColors
import com.fit321.fitui.v3.theme.LocalFitV3Palette
import com.fit321.fitui.v3.tokens.FitV3Type

/**
 * `.ai-orb` — the assistant's mark: one gradient orb. It is the only gradient on these screens,
 * so it stays the assistant's signature rather than reading as a button.
 */
@Composable
fun FitAssistantOrb(
    modifier: Modifier = Modifier,
    size: Dp = 72.dp,
    icon: @Composable () -> Unit,
) {
    Box(
        modifier = modifier
            .size(size)
            .shadow(if (size > 40.dp) 18.dp else 0.dp, CircleShape, ambientColor = ORB_GLOW, spotColor = ORB_GLOW)
            .clip(CircleShape)
            .drawBehind {
                drawCircle(
                    Brush.radialGradient(
                        0f to ORB_LIGHT,
                        0.28f to FitColors.Teal.t500,
                        0.7f to ORB_BLUE,
                        1f to ORB_DEEP,
                        center = Offset(this.size.width * 0.32f, this.size.height * 0.28f),
                        radius = this.size.minDimension,
                    ),
                )
            },
        contentAlignment = Alignment.Center,
    ) {
        CompositionLocalProvider(LocalContentColor provides Color.White) { icon() }
    }
}

/** `.ai-wave` — five bars that rise and fall while someone is talking. */
@Composable
fun FitVoiceWave(modifier: Modifier = Modifier, color: Color = LocalContentColor.current) {
    val transition = rememberInfiniteTransition(label = "wave")
    Row(
        modifier = modifier.height(20.dp),
        horizontalArrangement = Arrangement.spacedBy(3.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        WAVE_HEIGHTS.forEachIndexed { index, height ->
            val scale by transition.animateFloat(
                initialValue = 0.4f,
                targetValue = 1f,
                animationSpec = infiniteRepeatable(
                    tween(500, delayMillis = index * 100, easing = FastOutSlowInEasing),
                    RepeatMode.Reverse,
                ),
                label = "bar$index",
            )
            Box(
                modifier = Modifier
                    .width(3.dp)
                    .height(height * scale)
                    .background(color, RoundedCornerShape(2.dp)),
            )
        }
    }
}

/**
 * `.ai-mic` — the 64dp talk button in the CTA's own fill. [live] pulses it (listening, speaking)
 * and, while listening, rings it so the screen shows it is taking sound.
 */
@Composable
fun FitVoiceMic(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    live: Boolean = false,
    ringed: Boolean = false,
    icon: @Composable () -> Unit,
) {
    val palette = LocalFitV3Palette.current
    val transition = rememberInfiniteTransition(label = "mic")
    val pulse by transition.animateFloat(
        initialValue = 1f,
        targetValue = if (live) 1.05f else 1f,
        animationSpec = infiniteRepeatable(tween(800, easing = FastOutSlowInEasing), RepeatMode.Reverse),
        label = "pulse",
    )
    Box(
        modifier = modifier
            .size(64.dp)
            .scale(pulse)
            .drawBehind {
                if (ringed) {
                    drawCircle(RING_OUTER, radius = size.minDimension / 2 + 14.dp.toPx())
                    drawCircle(RING_INNER, radius = size.minDimension / 2 + 7.dp.toPx())
                }
            }
            .shadow(16.dp, CircleShape, ambientColor = palette.ctaShadow, spotColor = palette.ctaShadow)
            .clip(CircleShape)
            .background(Brush.verticalGradient(colorStops = palette.ctaStops.toTypedArray()))
            .clickable { onClick() },
        contentAlignment = Alignment.Center,
    ) {
        CompositionLocalProvider(LocalContentColor provides palette.ctaLabel) { icon() }
    }
}

/** `.ai-dots` — the assistant is writing. */
@Composable
fun FitTypingDots(modifier: Modifier = Modifier) {
    val palette = LocalFitV3Palette.current
    val transition = rememberInfiniteTransition(label = "dots")
    Row(
        modifier = modifier
            .background(palette.surface, RoundedCornerShape(18.dp, 18.dp, 18.dp, 6.dp))
            .padding(horizontal = 14.dp, vertical = 13.dp),
        horizontalArrangement = Arrangement.spacedBy(4.dp),
    ) {
        repeat(3) { index ->
            val lift by transition.animateFloat(
                initialValue = 0f,
                targetValue = 1f,
                animationSpec = infiniteRepeatable(tween(600, delayMillis = index * 200), RepeatMode.Reverse),
                label = "dot$index",
            )
            Box(
                modifier = Modifier
                    .padding(top = (3 * (1 - lift)).dp)
                    .size(6.dp)
                    .alpha(0.3f + 0.7f * lift)
                    .background(palette.textTertiary, CircleShape),
            )
        }
    }
}

/**
 * `.ai-done` — a decision that went through: one tinted line with the check, what happened and
 * a door to it. [neutral] is the grey line a declined or superseded card collapses to.
 */
@Composable
fun FitResultLine(
    title: String,
    modifier: Modifier = Modifier,
    subtitle: String? = null,
    neutral: Boolean = false,
    doorLabel: String? = null,
    onDoor: (() -> Unit)? = null,
) {
    val palette = LocalFitV3Palette.current
    val accent = palette.accentBadgeInk
    Row(
        modifier = modifier
            .fillMaxWidth()
            .background(if (neutral) palette.quietLift else accent.copy(alpha = 0.10f), RoundedCornerShape(16.dp))
            .padding(horizontal = 12.dp, vertical = 10.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(10.dp),
    ) {
        if (!neutral) {
            Box(
                modifier = Modifier.size(26.dp).background(accent, CircleShape),
                contentAlignment = Alignment.Center,
            ) {
                Icon(
                    painter = painterResource(R.drawable.ic_fit_check),
                    contentDescription = null,
                    tint = DONE_INK,
                    modifier = Modifier.size(14.dp),
                )
            }
        }
        Column(modifier = Modifier.weight(1f)) {
            Text(text = title, style = FitV3Type.kvLabel, color = if (neutral) palette.textSecondary else palette.textPrimary)
            if (subtitle != null) {
                Text(text = subtitle, style = FitV3Type.rowValueSub, color = palette.textTertiary)
            }
        }
        if (doorLabel != null && onDoor != null) {
            Text(
                text = doorLabel,
                style = FitV3Type.rowSubStrong,
                color = accent,
                modifier = Modifier.clickable { onDoor() }.padding(4.dp),
            )
        }
    }
}

/** `.ai-sys` — a turn the assistant could not answer: dashed, says nothing changed, offers a retry. */
@Composable
fun FitFailedLine(
    text: String,
    modifier: Modifier = Modifier,
    actionLabel: String? = null,
    onAction: (() -> Unit)? = null,
    icon: @Composable () -> Unit,
) {
    val palette = LocalFitV3Palette.current
    Row(
        modifier = modifier
            .fillMaxWidth()
            .drawBehind {
                val r = 16.dp.toPx()
                drawRoundRect(
                    color = palette.dashed,
                    cornerRadius = CornerRadius(r, r),
                    style = Stroke(
                        width = 1.dp.toPx(),
                        pathEffect = PathEffect.dashPathEffect(floatArrayOf(5.dp.toPx(), 4.dp.toPx())),
                    ),
                )
            }
            .padding(horizontal = 12.dp, vertical = 10.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(10.dp),
    ) {
        CompositionLocalProvider(LocalContentColor provides palette.textTertiary) { icon() }
        Text(text = text, style = FitV3Type.kvLabel, color = palette.textSecondary, modifier = Modifier.weight(1f))
        if (actionLabel != null && onAction != null) {
            Text(
                text = actionLabel,
                style = FitV3Type.rowSubStrong,
                color = palette.textPrimary,
                modifier = Modifier
                    .clip(CircleShape)
                    .background(palette.secondaryFill)
                    .clickable { onAction() }
                    .padding(horizontal = 12.dp, vertical = 6.dp),
            )
        }
    }
}

/** `.ai-effect` — what a change does besides itself: money, a notice, a rule. [warning] is amber. */
@Composable
fun FitEffectLine(
    text: String,
    modifier: Modifier = Modifier,
    warning: Boolean = false,
    error: Boolean = false,
    icon: @Composable () -> Unit,
) {
    val palette = LocalFitV3Palette.current
    val ink = when {
        error -> palette.textError
        warning -> palette.perimeterAttention
        else -> palette.textSecondary
    }
    Column(modifier = modifier.fillMaxWidth()) {
        Box(modifier = Modifier.fillMaxWidth().height(1.dp).background(palette.divider))
        Row(
            modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            CompositionLocalProvider(LocalContentColor provides if (warning || error) ink else palette.textTertiary) {
                Box(modifier = Modifier.padding(top = 1.dp)) { icon() }
            }
            Text(text = text, style = FitV3Type.rowSub, color = ink)
        }
    }
}

/** `.ai-yn` — answer a request in place: decline outlined, accept filled. */
@Composable
fun FitAnswerCircles(
    onDecline: () -> Unit,
    onAccept: () -> Unit,
    modifier: Modifier = Modifier,
    busy: Boolean = false,
    declineIcon: @Composable () -> Unit,
    acceptIcon: @Composable () -> Unit,
) {
    val palette = LocalFitV3Palette.current
    Row(modifier = modifier, horizontalArrangement = Arrangement.spacedBy(8.dp)) {
        if (busy) {
            CircularProgressIndicator(modifier = Modifier.size(22.dp), color = palette.textSecondary, strokeWidth = 2.dp)
            return@Row
        }
        Box(
            modifier = Modifier
                .size(36.dp)
                .clip(CircleShape)
                .border(1.dp, palette.circleOutline, CircleShape)
                .clickable { onDecline() },
            contentAlignment = Alignment.Center,
        ) { CompositionLocalProvider(LocalContentColor provides palette.textPrimary) { declineIcon() } }
        Box(
            modifier = Modifier
                .size(36.dp)
                .clip(CircleShape)
                .background(Brush.verticalGradient(colorStops = palette.ctaStops.toTypedArray()))
                .clickable { onAccept() },
            contentAlignment = Alignment.Center,
        ) { CompositionLocalProvider(LocalContentColor provides palette.ctaLabel) { acceptIcon() } }
    }
}

private val WAVE_HEIGHTS = listOf(7.dp, 15.dp, 20.dp, 12.dp, 6.dp)
private val ORB_LIGHT = Color(0xFF5FF0CF)
private val ORB_BLUE = Color(0xFF00A7D0)
private val ORB_DEEP = Color(0xFF0A6E8C)
private val ORB_GLOW = Color(0x5905E0A6)
private val RING_INNER = Color(0x29FFFFFF)
private val RING_OUTER = Color(0x12FFFFFF)
private val DONE_INK = Color(0xFF04130D)
