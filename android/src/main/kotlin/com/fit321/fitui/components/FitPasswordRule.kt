package com.fit321.fitui.components

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Circle
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.fit321.fitui.theme.LocalFitTheme
import com.fit321.fitui.tokens.FitColors
import com.fit321.fitui.tokens.FitFont
import com.fit321.fitui.tokens.FitSpacing

// ============================================================================
// FitPasswordRule — vertical list of password validation rules.
// Mirrors Swift FitPasswordRule. See `docs/components.md` § FitPasswordRule.
// ============================================================================

data class FitPasswordRuleItem(val label: String, val isMet: Boolean)

@Composable
fun FitPasswordRule(
    rules: List<FitPasswordRuleItem>,
    modifier: Modifier = Modifier
) {
    val theme = LocalFitTheme.current
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(6.dp)
    ) {
        rules.forEach { rule ->
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(FitSpacing.sp2)
            ) {
                Icon(
                    imageVector = if (rule.isMet) Icons.Default.Check else Icons.Default.Circle,
                    contentDescription = null,
                    tint = if (rule.isMet) FitColors.Teal.t500 else theme.textTertiary,
                    modifier = Modifier.size(12.dp)
                )
                Text(
                    rule.label,
                    style = FitFont.body2,
                    color = if (rule.isMet) theme.textSecondary else theme.textTertiary
                )
            }
        }
    }
}
