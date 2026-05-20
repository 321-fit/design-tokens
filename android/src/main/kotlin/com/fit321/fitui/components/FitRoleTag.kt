package com.fit321.fitui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.DirectionsRun
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.fit321.fitui.theme.LocalFitTheme
import com.fit321.fitui.tokens.FitFont

// ============================================================================
// FitRole — user role concept shared across FitUI (matches Swift `FitRole`).
// ============================================================================

enum class FitRole { Athlete, Coach }

// ============================================================================
// FitRoleTag — small corner badge labelling the user role context.
//
// Used by FitCalEvent in cross-role state — placed at the bottom-right of the
// tile to signal "this event lives on your OTHER role profile."
// 18dp tall, 10sp font, capsule. Subtle wash, deliberately unobtrusive.
// ============================================================================

@Composable
fun FitRoleTag(
    role: FitRole,
    modifier: Modifier = Modifier
) {
    val theme = LocalFitTheme.current
    val tagBg = Color.Black.copy(alpha = 0.05f)   // subtle on both themes

    Row(
        modifier = modifier
            .clip(CircleShape)
            .background(tagBg)
            .padding(horizontal = 8.dp)
            .height(18.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        Icon(
            imageVector = when (role) {
                FitRole.Athlete -> Icons.Filled.DirectionsRun
                FitRole.Coach   -> Icons.Filled.Person
            },
            contentDescription = null,
            tint = theme.textTertiary,
            modifier = Modifier.size(10.dp)
        )
        Text(
            text = when (role) {
                FitRole.Athlete -> "Athlete"
                FitRole.Coach   -> "Coach"
            },
            style = FitFont.caption.copy(fontSize = 10.sp, fontWeight = FontWeight.Medium),
            color = theme.textTertiary
        )
    }
}
