package com.fit321.fitui.v3.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.foundation.background
import com.fit321.fitui.v3.theme.LocalFitV3Palette
import com.fit321.fitui.v3.tokens.FitV3Geometry
import com.fit321.fitui.v3.tokens.FitV3Type

/**
 * Who this person is, as one panel: face, name, one line about them, and the relationship in
 * three numbers under a rule.
 *
 * This is layout A of the client detail — the panel, not the bare centred block. A client who
 * has no history yet carries no stats row at all rather than three zeroes, which is why the
 * list is a parameter and an empty one is a legal answer.
 */
@Composable
fun FitIdentityPanel(
    name: String,
    modifier: Modifier = Modifier,
    sub: String? = null,
    stats: List<FitProfileStat> = emptyList(),
    badge: (@Composable () -> Unit)? = null,
    avatar: (@Composable () -> Unit)? = null,
) {
    val palette = LocalFitV3Palette.current
    val shape = RoundedCornerShape(FitV3Geometry.panelRadius)
    Column(
        modifier = modifier
            .fillMaxWidth()
            .fitV3Surface(shape)
            .clip(shape)
            .padding(start = 16.dp, end = 16.dp, top = 16.dp, bottom = 14.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        if (avatar != null) {
            Box(modifier = Modifier.padding(bottom = 10.dp)) { avatar() }
        }
        Text(
            text = name,
            style = FitV3Type.compactName,
            color = palette.textPrimary,
            textAlign = TextAlign.Center,
        )
        if (sub != null) {
            Text(
                text = sub,
                style = FitV3Type.nextWhat,
                color = palette.textSecondary,
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(top = 4.dp),
            )
        }
        if (badge != null) {
            Box(modifier = Modifier.padding(top = 8.dp)) { badge() }
        }
        if (stats.isNotEmpty()) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 12.dp)
                    .height(1.dp)
                    .background(palette.divider),
            )
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 12.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
            ) {
                stats.forEach { stat ->
                    Column(
                        modifier = Modifier.weight(1f),
                        horizontalAlignment = Alignment.CenterHorizontally,
                    ) {
                        Text(
                            text = stat.value,
                            style = FitV3Type.compactStats,
                            color = palette.textPrimary,
                            maxLines = 1,
                        )
                        Text(
                            text = stat.label,
                            style = FitV3Type.statLabelSmall,
                            color = palette.textTertiary,
                            textAlign = TextAlign.Center,
                            maxLines = 1,
                        )
                    }
                }
            }
        }
    }
}
