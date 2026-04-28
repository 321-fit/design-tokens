package com.fit321.fitui.components

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.fit321.fitui.theme.LocalFitTheme
import com.fit321.fitui.tokens.FitFont
import com.fit321.fitui.tokens.FitSpacing

// ============================================================================
// FitProfileHeader — coach / athlete profile hero.
// Mirrors Swift FitProfileHeader. See `docs/components.md`
// § FitProfileHeader.
// ============================================================================

@Composable
fun FitProfileHeader(
    initials: String,
    name: String,
    sports: List<String> = emptyList(),
    location: String? = null,
    bio: String? = null,
    onEdit: (() -> Unit)? = null,
    modifier: Modifier = Modifier
) {
    val theme = LocalFitTheme.current
    val metadataParts = mutableListOf<String>()
    if (!location.isNullOrEmpty()) metadataParts.add(location)
    if (sports.isNotEmpty()) metadataParts.add(sports.joinToString(", "))
    val metadata = metadataParts.joinToString(" · ")

    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(FitSpacing.sp4),
        verticalArrangement = Arrangement.spacedBy(FitSpacing.sp4)
    ) {
        Row(
            verticalAlignment = Alignment.Top,
            horizontalArrangement = Arrangement.spacedBy(FitSpacing.sp4)
        ) {
            FitAvatar(initials = initials, size = FitAvatarSize.Xl)

            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(FitSpacing.sp1)
            ) {
                Row(
                    verticalAlignment = Alignment.Top,
                    horizontalArrangement = Arrangement.spacedBy(FitSpacing.sp2)
                ) {
                    Text(
                        name,
                        style = FitFont.heading1.copy(fontSize = 24.sp, fontWeight = FontWeight.SemiBold),
                        color = theme.textPrimary,
                        modifier = Modifier.weight(1f)
                    )
                    if (onEdit != null) {
                        FitIconBtn(
                            icon = Icons.Default.Edit,
                            color = FitIconBtnColor.Primary,
                            onClick = onEdit
                        )
                    }
                }
                if (metadata.isNotEmpty()) {
                    Text(metadata, style = FitFont.body2, color = theme.textTertiary)
                }
            }
        }

        if (!bio.isNullOrEmpty()) {
            Text(bio, style = FitFont.body1, color = theme.textSecondary)
        }
    }
}
