package com.fit321.fitui.v3.components

import com.fit321.designtokens.R
import androidx.compose.foundation.layout.size
import androidx.compose.ui.res.painterResource
import androidx.compose.material3.Icon
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.fit321.fitui.tokens.FitColors
import com.fit321.fitui.v3.theme.LocalFitV3Palette
import com.fit321.fitui.v3.tokens.FitV3Geometry
import com.fit321.fitui.v3.tokens.FitV3Type

data class FitProfileStat(
    val value: String,
    val label: String,
    val accent: Boolean = false,
)

private val coverFallback = Brush.radialGradient(
    colorStops = arrayOf(
        0f to Color(0xFF1F5360),
        0.55f to Color(0xFF0B2A33),
        1f to Color(0xFF061419),
    ),
)

@Composable
fun FitProfileCover(
    modifier: Modifier = Modifier,
    caption: String? = null,
    captionSub: String? = null,
    action: (@Composable () -> Unit)? = null,
    onClick: (() -> Unit)? = null,
    /** True where the cover is a video the coach can play — the badge sits dead centre. */
    playable: Boolean = false,
    media: (@Composable BoxScope.() -> Unit)? = null,
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .aspectRatio(16f / 9f)
            .background(coverFallback)
            .then(if (onClick != null) Modifier.clickable { onClick() } else Modifier),
    ) {
        media?.invoke(this)
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.BottomCenter)
                .fillMaxHeight(0.55f)
                .background(
                    Brush.verticalGradient(
                        listOf(Color.Transparent, Color(0x8C000000)),
                    ),
                ),
        )
        if (playable) {
            Box(
                modifier = Modifier
                    .align(Alignment.Center)
                    .size(56.dp)
                    .background(Color.White.copy(alpha = 0.92f), CircleShape),
                contentAlignment = Alignment.Center,
            ) {
                Icon(
                    painter = painterResource(R.drawable.ic_fit_play),
                    contentDescription = null,
                    tint = Color(0xFF0B2A2F),
                    modifier = Modifier.size(22.dp).padding(start = 3.dp),
                )
            }
        }
        if (caption != null) {
            Column(
                modifier = Modifier
                    .align(Alignment.BottomStart)
                    .padding(start = 16.dp, end = 64.dp, bottom = 12.dp),
            ) {
                Text(
                    // Uppercased here rather than at the call site: it is the cover's own
                    // treatment, and every caller spelling it in caps would put the shouting
                    // into the string resources.
                    text = caption.uppercase(),
                    style = FitV3Type.coverCaption,
                    color = Color.White,
                )
                if (captionSub != null) {
                    Text(
                        text = captionSub,
                        style = FitV3Type.statLabel,
                        color = Color.White.copy(alpha = 0.78f),
                    )
                }
            }
        }
        if (action != null) {
            Box(
                modifier = Modifier
                    .align(Alignment.BottomEnd)
                    .padding(end = 12.dp, bottom = 10.dp),
            ) {
                action()
            }
        }
    }
}

@Composable
fun FitProfileHeader(
    name: String,
    modifier: Modifier = Modifier,
    initials: String = "",
    avatarUrl: String? = null,
    place: String? = null,
    placeIcon: (@Composable () -> Unit)? = null,
    stats: List<FitProfileStat> = emptyList(),
    onIdentityClick: (() -> Unit)? = null,
    trailing: (@Composable () -> Unit)? = null,
    cover: (@Composable () -> Unit)? = null,
) {
    val palette = LocalFitV3Palette.current
    Column(modifier = modifier.fillMaxWidth()) {
        cover?.invoke()
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .then(if (onIdentityClick != null) Modifier.clickable { onIdentityClick() } else Modifier)
                .padding(start = 16.dp, end = 16.dp, top = 18.dp, bottom = 14.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(16.dp),
        ) {
            FitV3Avatar(
                initials = initials,
                size = FitV3Geometry.profileAvatar,
                imageUrl = avatarUrl,
                plate = FitV3AvatarPlate.Brand,
                modifier = Modifier.border(3.dp, palette.avatarRing, CircleShape),
            )
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = name,
                    style = FitV3Type.profileName,
                    color = palette.textPrimary,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                )
                if (place != null) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(4.dp),
                        modifier = Modifier.padding(top = 4.dp),
                    ) {
                        placeIcon?.invoke()
                        Text(
                            text = place,
                            style = FitV3Type.identityContext,
                            color = palette.textSecondary,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis,
                        )
                    }
                }
            }
            trailing?.invoke()
        }
        if (stats.isNotEmpty()) {
            FitProfileStats(
                stats = stats,
                modifier = Modifier.padding(horizontal = FitV3Geometry.screenInset),
            )
        }
    }
}

@Composable
fun FitProfileStats(
    stats: List<FitProfileStat>,
    modifier: Modifier = Modifier,
) {
    val palette = LocalFitV3Palette.current
    val shape = RoundedCornerShape(FitV3Geometry.panelRadius)
    Row(
        modifier = modifier
            .fillMaxWidth()
            .fitV3Surface(shape)
            .clip(shape)
            .padding(vertical = 12.dp, horizontal = 4.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        stats.forEachIndexed { index, stat ->
            if (index > 0) {
                Box(
                    modifier = Modifier
                        .width(1.dp)
                        .height(28.dp)
                        .background(palette.divider),
                )
            }
            Column(
                modifier = Modifier.weight(1f),
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                Text(
                    text = stat.value,
                    style = FitV3Type.statValue,
                    color = if (stat.accent) FitColors.Teal.t500 else palette.textPrimary,
                    textAlign = TextAlign.Center,
                )
                Text(
                    text = stat.label,
                    style = FitV3Type.statLabel,
                    color = palette.textTertiary,
                    textAlign = TextAlign.Center,
                )
            }
        }
    }
}

@Composable
fun FitProfileCard(
    name: String,
    modifier: Modifier = Modifier,
    initials: String = "",
    avatarUrl: String? = null,
    place: String? = null,
    placeIcon: (@Composable () -> Unit)? = null,
    priceLine: String? = null,
    sports: (@Composable ColumnScope.() -> Unit)? = null,
) {
    val palette = LocalFitV3Palette.current
    val shape = RoundedCornerShape(FitV3Geometry.panelRadius)
    Column(
        modifier = modifier
            .fillMaxWidth()
            .fitV3Surface(shape)
            .clip(shape)
            .padding(20.dp),
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(16.dp),
            modifier = Modifier.fillMaxWidth(),
        ) {
            FitV3Avatar(
                initials = initials,
                size = FitV3Geometry.profileCompactAvatar,
                imageUrl = avatarUrl,
                plate = FitV3AvatarPlate.Brand,
            )
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = name,
                    style = FitV3Type.compactName,
                    color = palette.textPrimary,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                )
                if (place != null) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(4.dp),
                        modifier = Modifier.padding(top = 5.dp),
                    ) {
                        placeIcon?.invoke()
                        Text(
                            text = place,
                            style = FitV3Type.identityContext,
                            color = palette.textSecondary,
                        )
                    }
                }
            }
        }
        if (sports != null) {
            Column(modifier = Modifier.padding(top = 16.dp)) { sports() }
        }
        if (priceLine != null) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 16.dp)
                    .height(1.dp)
                    .background(palette.divider),
            )
            Text(
                text = priceLine,
                style = FitV3Type.rowValue,
                color = palette.textPrimary,
                modifier = Modifier.padding(top = 14.dp),
            )
        }
    }
}
