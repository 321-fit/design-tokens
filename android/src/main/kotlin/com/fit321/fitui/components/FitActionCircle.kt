package com.fit321.fitui.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.fit321.fitui.theme.LocalFitTheme
import com.fit321.fitui.tokens.FitColors

/**
 * A row of peer actions on an object — a sheet, a drawer, a request card.
 * 64dp circle with the label beneath it. Mirrors Swift `FitActionCircle`.
 * See `docs/components.md` § FitActionCircle.
 *
 * **When to use it, and when not.** Peer actions *on an object* take circles.
 * A question with an escape — "Disconnect / Cancel", "Cancel / Save" — takes
 * words: there the labels are the meaning and one of the two is the way out of
 * the question. Count does not decide it. Two peers are still peers, which is
 * why an Awaiting row is two circles while Disconnect / Cancel stays two buttons.
 *
 * **Colour is rationed**, and that is the rule rather than a preference: exactly
 * one [FitActionCircleRole.Primary] (the expected answer, under the thumb), at
 * most one [FitActionCircleRole.Danger] (destructive, farthest from it). A third
 * fill turns a row of actions into a row of warnings. Positions are fixed so the
 * row is muscle memory rather than a read: refusal left, neutral middle, expected
 * answer right.
 */
enum class FitActionCircleRole {
    /** Neutral peer — an OUTLINE, not a fill. Fill means an answer; outline means
     *  not-an-answer, which is what Reschedule is: it proposes rather than resolves.
     *  It is also the only thing that works in both themes with one value — a grey
     *  fill must sit one step "away" from its background, and away is lighter on dark
     *  but darker on light. No grey fill clears 3:1 on white anyway (the darkest
     *  reaches 1.57); the outline does — gray-500 measures 4.13 on white, 3.7 on the
     *  light page, 3.92 on dark. */
    Neutral,

    /** The expected answer. One per row. */
    Primary,

    /** Destructive. FILLED red with a white glyph — it must read as destructive
     *  before the icon is identified, which is the whole job of that colour. White
     *  on `#F05C5B` measures 3.29:1: under the 4.5 a label would need, over the 3.0
     *  a glyph needs, and a glyph is what sits inside. The word lives beneath the
     *  circle on the page background, where it has full contrast. */
    Danger,

    /** Deliberation rather than a decision — outline only, reserved for the
     *  assistant. A third fill colour would flatten the row's hierarchy. */
    Ask,
}

@Composable
fun RowScope.FitActionCircle(
    icon: ImageVector,
    label: String,
    role: FitActionCircleRole = FitActionCircleRole.Neutral,
    enabled: Boolean = true,
    modifier: Modifier = Modifier,
    onClick: () -> Unit,
) {
    val theme = LocalFitTheme.current

    val glyph: Color = when (role) {
        FitActionCircleRole.Primary, FitActionCircleRole.Danger -> FitColors.Gray.white
        FitActionCircleRole.Ask -> FitColors.Teal.t500
        FitActionCircleRole.Neutral -> theme.textPrimary
    }
    val caption: Color = when (role) {
        FitActionCircleRole.Primary -> FitColors.brandPrimary
        FitActionCircleRole.Danger -> FitColors.Red.r400
        else -> theme.textPrimary
    }

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(9.dp),
        modifier = modifier
            .weight(1f)
            .alpha(if (enabled) 1f else 0.4f)
            .clickable(enabled = enabled, onClick = onClick)
            .semantics { contentDescription = label }
            .padding(vertical = 4.dp),
    ) {
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier
                .size(64.dp)
                .then(
                    when (role) {
                        FitActionCircleRole.Primary ->
                            Modifier.background(FitColors.brandGradient, CircleShape)
                        FitActionCircleRole.Danger ->
                            Modifier.background(FitColors.Red.r400, CircleShape)
                        FitActionCircleRole.Ask ->
                            Modifier.border(BorderStroke(1.5.dp, FitColors.Teal.t500), CircleShape)
                        FitActionCircleRole.Neutral ->
                            Modifier.border(BorderStroke(1.5.dp, FitColors.Gray.g500), CircleShape)
                    }
                ),
        ) {
            Icon(imageVector = icon, contentDescription = null, tint = glyph, modifier = Modifier.size(30.dp))
        }
        Text(text = label, fontSize = 13.sp, fontWeight = FontWeight.SemiBold, color = caption)
    }
}

/**
 * The row. Exists so the spacing and the equal widths are not re-typed at every
 * call site, and so a row that grows a fourth action needs no layout rethink.
 */
@Composable
fun FitActionCircles(
    modifier: Modifier = Modifier,
    content: @Composable RowScope.() -> Unit,
) {
    Row(
        horizontalArrangement = Arrangement.spacedBy(6.dp),
        verticalAlignment = Alignment.Top,
        modifier = modifier.padding(top = 4.dp, bottom = 2.dp),
        content = content,
    )
}
