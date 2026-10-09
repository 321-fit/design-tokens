package com.fit321.fitui.v3.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.fillMaxWidth
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.sp
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.fit321.designtokens.R
import com.fit321.fitui.v3.theme.LocalFitV3Palette
import com.fit321.fitui.v3.tokens.FitV3Geometry
import com.fit321.fitui.v3.tokens.FitV3Type

@Composable
fun FitTileGrid(
    modifier: Modifier = Modifier,
    content: @Composable RowScope.() -> Unit,
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(FitV3Geometry.tileGap),
        content = content,
    )
}

/**
 * A tile whose content is its own — a review, where the name, the stars and the words are three
 * different kinds of thing and none of them is a title with a subtitle under it.
 */
@Composable
fun FitTileCard(
    modifier: Modifier = Modifier,
    onClick: (() -> Unit)? = null,
    content: @Composable ColumnScope.() -> Unit,
) {
    val shape = RoundedCornerShape(FitV3Geometry.tileRadius)
    Column(
        modifier = modifier
            .fitV3Surface(shape)
            .clip(shape)
            .then(if (onClick != null) Modifier.clickable { onClick() } else Modifier)
            .padding(FitV3Geometry.tilePadding),
        content = content,
    )
}

@Composable
fun FitTile(
    title: String,
    modifier: Modifier = Modifier,
    subtitle: String? = null,
    onClick: (() -> Unit)? = null,
    head: (@Composable () -> Unit)? = null,
) {
    val palette = LocalFitV3Palette.current
    val shape = RoundedCornerShape(FitV3Geometry.tileRadius)
    Column(
        modifier = modifier
            .fitV3Surface(shape)
            .clip(shape)
            .then(if (onClick != null) Modifier.clickable { onClick() } else Modifier)
            .padding(FitV3Geometry.tilePadding),
    ) {
        if (head != null) {
            Box(modifier = Modifier.padding(bottom = 10.dp)) { head() }
        }
        Text(
            text = title,
            style = FitV3Type.rowValue,
            color = palette.textPrimary,
        )
        if (subtitle != null) {
            Text(
                text = subtitle,
                style = FitV3Type.rowSub,
                color = palette.textTertiary,
                modifier = Modifier.padding(top = 3.dp),
            )
        }
    }
}

@Composable
fun FitFaceStack(
    initials: List<String>,
    modifier: Modifier = Modifier,
    size: Dp = FitV3Geometry.smallPlate,
    /** Set alongside a larger [size]: the ink does not scale with the circle on its own. */
    fontSize: TextUnit = 11.sp,
) {
    val palette = LocalFitV3Palette.current
    Row(
        modifier = modifier,
        // The overlap is a quarter of the face, not a constant: `.cd-j-faces` shifts 9 of a
        // 34px face and `.cd-gr-faces` 14 of a 56px one, so a fixed value leaves the big
        // stack standing almost edge to edge.
        horizontalArrangement = Arrangement.spacedBy(-size * FACE_OVERLAP),
    ) {
        initials.forEach { value ->
            Box(
                modifier = Modifier
                    .size(size)
                    // Ring outside the face, as `box-shadow` has it: a border would shrink the
                    // circle instead and the overlap with its neighbour would close up.
                    .background(palette.stackedFaceRing, CircleShape)
                    .padding(2.dp)
                    .background(palette.stackedFace, CircleShape),
                contentAlignment = Alignment.Center,
            ) {
                Text(
                    text = value,
                    style = FitV3Type.statLabelSmall.copy(
                        fontSize = fontSize,
                        fontWeight = FontWeight.SemiBold,
                    ),
                    color = palette.textSecondary,
                )
            }
        }
    }
}

@Composable
fun FitTipCard(
    title: String,
    modifier: Modifier = Modifier,
    subtitle: String? = null,
    actionLabel: String? = null,
    onClick: (() -> Unit)? = null,
    onDismiss: (() -> Unit)? = null,
    leading: (@Composable () -> Unit)? = null,
) {
    val palette = LocalFitV3Palette.current
    val shape = RoundedCornerShape(FitV3Geometry.tileRadius)
    Box(modifier = modifier.fillMaxWidth()) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clip(shape)
                .border(1.dp, palette.divider, shape)
                .then(if (onClick != null) Modifier.clickable { onClick() } else Modifier)
                .padding(start = 14.dp, end = 40.dp, top = 14.dp, bottom = 14.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            verticalAlignment = Alignment.Top,
        ) {
            leading?.invoke()
            Column(modifier = Modifier.weight(1f)) {
                Text(text = title, style = FitV3Type.rowValue, color = palette.textPrimary)
                if (subtitle != null) {
                    Text(
                        text = subtitle,
                        style = FitV3Type.rowSub,
                        color = palette.textTertiary,
                        modifier = Modifier.padding(top = 3.dp),
                    )
                }
                if (actionLabel != null) {
                    Text(
                        text = actionLabel,
                        style = FitV3Type.compactStats,
                        color = palette.textPrimary,
                        modifier = Modifier.padding(top = 10.dp),
                    )
                }
            }
        }
        if (onDismiss != null) {
            Box(
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .padding(6.dp)
                    .size(32.dp)
                    .clip(CircleShape)
                    .clickable { onDismiss() },
                contentAlignment = Alignment.Center,
            ) {
                CompositionLocalProvider(LocalContentColor provides palette.textTertiary) {
                    Icon(
                        painter = painterResource(R.drawable.ic_fit_close),
                        contentDescription = null,
                        modifier = Modifier.size(14.dp),
                    )
                }
            }
        }
    }
}

enum class FitBadgeTrend { Up, Down, Flat }

@Composable
fun FitAccentBadge(
    text: String,
    modifier: Modifier = Modifier,
    trend: FitBadgeTrend = FitBadgeTrend.Up,
    leading: (@Composable () -> Unit)? = null,
) {
    val palette = LocalFitV3Palette.current
    val fill = when (trend) {
        FitBadgeTrend.Up -> palette.accentBadgeFill
        FitBadgeTrend.Down -> palette.textError.copy(alpha = 0.15f)
        FitBadgeTrend.Flat -> palette.raised
    }
    val ink = when (trend) {
        FitBadgeTrend.Up -> palette.accentBadgeInk
        FitBadgeTrend.Down -> palette.textError
        FitBadgeTrend.Flat -> palette.textTertiary
    }
    Row(
        modifier = modifier
            .background(fill, CircleShape)
            .padding(horizontal = 8.dp, vertical = 3.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(2.dp),
    ) {
        if (leading != null) {
            CompositionLocalProvider(LocalContentColor provides ink) { leading() }
        }
        Text(text = text, style = FitV3Type.rowValueSub, color = ink)
    }
}

private const val FACE_OVERLAP = 0.26f
