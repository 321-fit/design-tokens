package com.fit321.fitui.v3.components

import androidx.compose.ui.graphics.drawscope.translate
import androidx.compose.ui.graphics.drawscope.scale
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.draw.drawBehind
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.fit321.designtokens.R
import com.fit321.fitui.tokens.FitColors
import com.fit321.fitui.v3.theme.LocalFitV3Palette
import com.fit321.fitui.v3.tokens.FitV3Geometry
import com.fit321.fitui.v3.tokens.FitV3Type

/**
 * `.cd-m-cover` — the one thing a screen leads with, as a picture with its words on it: a kicker,
 * a title, a line under it, and the action in the corner ("Start ›").
 *
 * [attention] is the card's perimeter, the way a request paints [FitNextSessionCard] — never a
 * red pill — and the kicker takes the same warning ink. [empty] is the nothing-here face
 * (`.cd-s-hero-empty`): a pale wash with a hairline, shorter, no picture.
 *
 * With no [media] the cover shows the prototype's stand-in picture, for things that carry no
 * image of their own. With no [action] it is an introduction rather than a door — the buttons
 * that act on it sit under it.
 */
@Composable
fun FitCoverCard(
    title: String,
    action: String?,
    onClick: (() -> Unit)?,
    modifier: Modifier = Modifier,
    kicker: String? = null,
    sub: String? = null,
    attention: Boolean = false,
    empty: Boolean = false,
    media: (@Composable BoxScope.() -> Unit)? = null,
) {
    val palette = LocalFitV3Palette.current
    val shape = RoundedCornerShape(FitV3Geometry.panelRadius)
    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(if (empty) COVER_EMPTY_HEIGHT else COVER_HEIGHT)
            .clip(shape)
            .then(if (empty) Modifier.background(coverEmptyWash) else Modifier.drawBehind { drawCoverStandIn() })
            .then(
                when {
                    attention -> Modifier.border(1.dp, palette.perimeterAttention, shape)
                    empty -> Modifier.border(1.dp, coverEmptyEdge, shape)
                    else -> Modifier
                },
            )
            .then(if (onClick != null) Modifier.clickable(onClick = onClick) else Modifier),
    ) {
        if (!empty) {
            media?.invoke(this)
            Box(modifier = Modifier.fillMaxSize().background(coverScrim))
        }
        Column(
            modifier = Modifier
                .align(Alignment.TopStart)
                .padding(start = 16.dp, end = 16.dp, top = 16.dp),
        ) {
            kicker?.let {
                Text(
                    text = it.uppercase(),
                    style = FitV3Type.coverKicker,
                    color = if (attention) FitColors.Yellow.y400 else Color.White.copy(alpha = 0.9f),
                )
            }
            Text(
                text = title,
                style = FitV3Type.coverTitle,
                color = Color.White,
                modifier = Modifier.padding(top = 3.dp),
            )
            sub?.let {
                Text(
                    text = it,
                    style = FitV3Type.coverSub,
                    color = Color.White.copy(alpha = 0.86f),
                    modifier = Modifier.padding(top = 2.dp),
                )
            }
        }
        if (action != null) Row(
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(end = 16.dp, bottom = 14.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(2.dp),
        ) {
            Text(text = action, style = FitV3Type.coverGo, color = Color.White)
            Icon(
                painter = painterResource(R.drawable.ic_fit_chevron_right),
                contentDescription = null,
                tint = Color.White,
                modifier = Modifier.size(16.dp),
            )
        }
    }
}

private val COVER_HEIGHT = 178.dp
private val COVER_EMPTY_HEIGHT = 160.dp

// `--cd-img-gym`: the prototype's stand-in picture, two stops of its gradient.
/**
 * `--cd-img-tennis`, the prototype's stand-in picture: a teal diagonal under four soft blobs
 * (`feGaussianBlur 16` at 0.9). Drawn in its own 320×180 box and stretched, as `cover` does.
 */
private fun DrawScope.drawCoverStandIn() {
    // `center/cover`: one scale for both axes, large enough to fill, the overflow centred away.
    val k = maxOf(size.width / 320f, size.height / 180f)
    val dx = (size.width - 320f * k) / 2f
    val dy = (size.height - 180f * k) / 2f
    drawRect(
        Brush.linearGradient(
            listOf(Color(0xFF0D3B46), Color(0xFF0A6F6A)),
            start = Offset.Zero,
            end = Offset(size.width, size.height),
        ),
    )
    translate(dx, dy) { scale(k, k, pivot = Offset.Zero) {
        blob(Offset(70f, 40f), 70f, 50f, Color(0xFF18C9A3), 0.55f)
        blob(Offset(250f, 140f), 80f, 80f, Color(0xFF0A3550), 1f)
        blob(Offset(190f, 55f), 95f, 38f, Color(0xFFA8F0DD), 0.28f)
        blob(Offset(120f, 150f), 45f, 45f, Color(0xFF063B3F), 0.8f)
    } }
}

/**
 * A blurred ellipse: a disc under a Gaussian blur fades along the error function, from full
 * ink two sigmas inside its edge to nothing two sigmas outside — so there is no rim to see.
 */
private fun DrawScope.blob(center: Offset, rx: Float, ry: Float, color: Color, alpha: Float) {
    val ink = color.copy(alpha = alpha * BLOB_OPACITY)
    val outer = rx + 2 * BLOB_BLUR
    val stop = { r: Float -> (r / outer).coerceIn(0f, 1f) }
    scale(1f, ry / rx, pivot = center) {
        drawCircle(
            brush = Brush.radialGradient(
                0f to ink,
                stop(rx - 2 * BLOB_BLUR) to ink,
                stop(rx - BLOB_BLUR) to ink.copy(alpha = ink.alpha * 0.84f),
                stop(rx) to ink.copy(alpha = ink.alpha * 0.5f),
                stop(rx + BLOB_BLUR) to ink.copy(alpha = ink.alpha * 0.16f),
                1f to ink.copy(alpha = 0f),
                center = center,
                radius = outer,
            ),
            radius = outer,
            center = center,
        )
    }
}

// Wider than the SVG's 16: a gradient spread reads harder than a real blur at the same width.
private const val BLOB_BLUR = 24f
private const val BLOB_OPACITY = 0.9f

// `.cd-m-scrim` — keeps the words readable on whatever picture is under them.
private val coverScrim = Brush.verticalGradient(
    0.22f to Color.Black.copy(alpha = 0.05f),
    1f to Color.Black.copy(alpha = 0.78f),
)

// `.fit-dark.k-alpha .cd-s-hero-empty`.
private val coverEmptyWash = Brush.linearGradient(listOf(Color.White.copy(alpha = 0.10f), Color.White.copy(alpha = 0.03f)))
private val coverEmptyEdge = Color.White.copy(alpha = 0.10f)
