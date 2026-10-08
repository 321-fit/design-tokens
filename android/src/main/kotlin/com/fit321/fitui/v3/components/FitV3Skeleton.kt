package com.fit321.fitui.v3.components

import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
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
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.fit321.fitui.v3.theme.LocalFitV3Palette
import com.fit321.fitui.v3.tokens.FitV3Geometry

private const val SHIMMER_MS = 1400
private const val SHIMMER_SPAN = 600f

/**
 * What a list looks like while it is still arriving — `fit-ui.css .sk-*`.
 *
 * A list with nothing in it yet and a list with nothing in it are different facts, and only one
 * of them is worth telling the coach. Rows in the shape of the rows that are coming say "a
 * moment"; an empty panel says "you have no clients", which for two seconds is a lie.
 */
@Composable
fun FitRowSkeleton(
    modifier: Modifier = Modifier,
    rows: Int = 3,
    /** Square where the rows that are coming lead with a plate rather than a face. */
    square: Boolean = false,
) {
    FitPanel(modifier = modifier) {
        repeat(rows) { index ->
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(FitV3Geometry.rowPadding),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(FitV3Geometry.rowGap),
            ) {
                FitShimmer(
                    modifier = Modifier.size(FitV3Geometry.leadingPlate),
                    shape = if (square) {
                        RoundedCornerShape(FitV3Geometry.leadingPlateRadius)
                    } else {
                        CircleShape
                    },
                )
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    verticalArrangement = Arrangement.spacedBy(8.dp),
                ) {
                    FitShimmer(modifier = Modifier.fillMaxWidth().height(12.dp))
                    // The second line is short and its length varies, so the block reads as
                    // text rather than as a loading bar.
                    FitShimmer(
                        modifier = Modifier
                            .fillMaxWidth(if (index % 2 == 0) 0.5f else 0.62f)
                            .height(10.dp),
                    )
                }
            }
        }
    }
}

/** One shimmering block. Sized and shaped by the caller — it knows what is coming. */
@Composable
fun FitShimmer(
    modifier: Modifier = Modifier,
    shape: Shape = RoundedCornerShape(6.dp),
) {
    val palette = LocalFitV3Palette.current
    val transition = rememberInfiniteTransition(label = "shimmer")
    val shift by transition.animateFloat(
        initialValue = -SHIMMER_SPAN,
        targetValue = SHIMMER_SPAN * 2,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = SHIMMER_MS),
            repeatMode = RepeatMode.Restart,
        ),
        label = "shimmerShift",
    )
    val brush = Brush.linearGradient(
        colors = listOf(palette.raised, palette.pressed, palette.raised),
        start = Offset(shift, 0f),
        end = Offset(shift + SHIMMER_SPAN, 0f),
    )
    Box(modifier = modifier.clip(shape).background(brush))
}

/** A shimmering line of a given height, for skeletons that are not rows. */
/**
 * The shape an identity block leaves behind while it loads — the face, the name, the line
 * under it. Without it a screen that opens on an identity shows an empty canvas and then
 * snaps the whole thing into place.
 */
@Composable
fun FitIdentitySkeleton(modifier: Modifier = Modifier) {
    Column(
        modifier = modifier.fillMaxWidth().padding(top = 6.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        FitShimmer(modifier = Modifier.size(FitV3Geometry.identityAvatar), shape = CircleShape)
        FitShimmerLine(
            modifier = Modifier.padding(top = 14.dp).width(IDENTITY_NAME),
            height = 20.dp,
        )
        FitShimmerLine(
            modifier = Modifier.padding(top = 9.dp).width(IDENTITY_SUB),
            height = 13.dp,
        )
    }
}

private val IDENTITY_NAME = 168.dp
private val IDENTITY_SUB = 108.dp

@Composable
fun FitShimmerLine(
    modifier: Modifier = Modifier,
    height: Dp = 12.dp,
) {
    FitShimmer(modifier = modifier.height(height))
}
