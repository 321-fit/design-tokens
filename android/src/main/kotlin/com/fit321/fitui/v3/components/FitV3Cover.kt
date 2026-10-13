package com.fit321.fitui.v3.components

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
 * image of their own.
 */
@Composable
fun FitCoverCard(
    title: String,
    action: String,
    onClick: () -> Unit,
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
            .background(if (empty) coverEmptyWash else coverStandIn)
            .then(
                when {
                    attention -> Modifier.border(1.dp, palette.perimeterAttention, shape)
                    empty -> Modifier.border(1.dp, coverEmptyEdge, shape)
                    else -> Modifier
                },
            )
            .clickable(onClick = onClick),
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
        Row(
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
private val coverStandIn = Brush.linearGradient(listOf(Color(0xFF2A1A24), Color(0xFF5C2B25)))

// `.cd-m-scrim` — keeps the words readable on whatever picture is under them.
private val coverScrim = Brush.verticalGradient(
    0.22f to Color.Black.copy(alpha = 0.05f),
    1f to Color.Black.copy(alpha = 0.78f),
)

// `.fit-dark.k-alpha .cd-s-hero-empty`.
private val coverEmptyWash = Brush.linearGradient(listOf(Color.White.copy(alpha = 0.10f), Color.White.copy(alpha = 0.03f)))
private val coverEmptyEdge = Color.White.copy(alpha = 0.10f)
