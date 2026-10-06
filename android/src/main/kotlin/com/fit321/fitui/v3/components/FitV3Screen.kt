package com.fit321.fitui.v3.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.fit321.designtokens.R
import com.fit321.fitui.v3.theme.LocalFitV3Palette
import com.fit321.fitui.v3.tokens.FitV3Geometry
import com.fit321.fitui.v3.tokens.FitV3Type

@Composable
fun FitV3Screen(
    modifier: Modifier = Modifier,
    header: (@Composable () -> Unit)? = null,
    footer: (@Composable () -> Unit)? = null,
    overlay: (@Composable BoxScope.() -> Unit)? = null,
    content: @Composable ColumnScope.() -> Unit,
) {
    FitV3Canvas(modifier = modifier) {
        Column(modifier = Modifier.fillMaxSize()) {
            if (header != null) {
                Box(modifier = Modifier.statusBarsPadding()) { header() }
            }
            Column(modifier = Modifier.weight(1f)) { content() }
            footer?.invoke()
        }
        overlay?.invoke(this)
    }
}

@Composable
fun FitV3Header(
    modifier: Modifier = Modifier,
    title: String? = null,
    onBack: (() -> Unit)? = null,
    leading: (@Composable () -> Unit)? = null,
    trailing: (@Composable RowScope.() -> Unit)? = null,
) {
    val palette = LocalFitV3Palette.current
    Box(
        modifier = modifier
            .fillMaxWidth()
            .heightIn(min = 48.dp)
            .padding(horizontal = FitV3Geometry.screenInset, vertical = 8.dp),
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            if (onBack != null) {
                FitV3HeaderCircle(onClick = onBack) {
                    Icon(
                        painter = painterResource(R.drawable.ic_fit_chevron_left),
                        contentDescription = null,
                        modifier = Modifier.size(18.dp),
                    )
                }
            }
            leading?.invoke()
            Box(modifier = Modifier.weight(1f))
            trailing?.invoke(this)
        }
        if (title != null) {
            Text(
                text = title,
                style = FitV3Type.nextWhen,
                color = palette.textPrimary,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                modifier = Modifier
                    .align(Alignment.Center)
                    .padding(horizontal = 72.dp),
            )
        }
    }
}

@Composable
fun FitV3HeaderCircle(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    badge: String? = null,
    /**
     * A word beside the glyph turns the circle into a pill of the same height — `.cd-pr-role`,
     * the role the coach is currently wearing. Null keeps it round.
     */
    label: String? = null,
    content: @Composable () -> Unit,
) {
    val palette = LocalFitV3Palette.current
    Box(modifier = modifier) {
        Row(
            modifier = Modifier
                .then(
                    if (label == null) {
                        Modifier.size(FitV3Geometry.leadingPlate)
                    } else {
                        Modifier
                            .height(FitV3Geometry.leadingPlate)
                    },
                )
                .clip(CircleShape)
                .background(palette.headerCircleFill, CircleShape)
                .border(1.dp, palette.headerCircleBorder, CircleShape)
                .clickable { onClick() }
                .then(
                    if (label == null) {
                        Modifier
                    } else {
                        Modifier.padding(start = 14.dp, end = 12.dp)
                    },
                ),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(6.dp, Alignment.CenterHorizontally),
        ) {
            if (label != null) {
                Text(text = label, style = FitV3Type.roleChip, color = palette.textPrimary)
            }
            CompositionLocalProvider(LocalContentColor provides palette.textPrimary) { content() }
        }
        if (badge != null) {
            // Sits proud of the circle, as `.cd-d-hbn` does — a count clipped to the plate
            // reads as part of the glyph.
            FitCountBadge(
                text = badge,
                modifier = Modifier.align(Alignment.TopEnd).offset(x = 3.dp, y = (-3).dp),
            )
        }
    }
}

@Composable
fun FitV3Footer(
    modifier: Modifier = Modifier,
    content: @Composable ColumnScope.() -> Unit,
) {
    val palette = LocalFitV3Palette.current
    Column(
        modifier = modifier
            .fillMaxWidth()
            .background(palette.chrome)
            .navigationBarsPadding()
            .padding(
                start = FitV3Geometry.footerCtaSide,
                end = FitV3Geometry.footerCtaSide,
                top = 12.dp,
                bottom = FitV3Geometry.footerCtaBottom,
            ),
        verticalArrangement = Arrangement.spacedBy(8.dp),
        content = content,
    )
}

@Composable
fun FitV3Bar(
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit,
) {
    val palette = LocalFitV3Palette.current
    Box(modifier = modifier.fillMaxWidth().background(palette.bar)) { content() }
}
