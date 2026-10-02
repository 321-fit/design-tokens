package com.fit321.fitui.v3.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.fit321.fitui.v3.theme.LocalFitV3Palette
import com.fit321.fitui.v3.tokens.FitV3Geometry
import com.fit321.fitui.v3.tokens.FitV3Type

@Composable
fun FitIdentity(
    name: String,
    modifier: Modifier = Modifier,
    initials: String = "",
    imageUrl: String? = null,
    context: String? = null,
    avatar: (@Composable () -> Unit)? = null,
) {
    val palette = LocalFitV3Palette.current
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(
                start = FitV3Geometry.screenInset,
                end = FitV3Geometry.screenInset,
                top = 6.dp,
                bottom = 2.dp,
            ),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        if (avatar != null) {
            avatar()
        } else {
            FitV3Avatar(
                initials = initials,
                size = FitV3Geometry.identityAvatar,
                imageUrl = imageUrl,
                plate = FitV3AvatarPlate.OnCanvas,
            )
        }
        Text(
            text = name,
            style = FitV3Type.identityName,
            color = palette.textPrimary,
            textAlign = TextAlign.Center,
            maxLines = 2,
            overflow = TextOverflow.Ellipsis,
            modifier = Modifier.padding(top = 12.dp),
        )
        if (context != null) {
            Text(
                text = context,
                style = FitV3Type.identityContext,
                color = palette.textSecondary,
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(top = 5.dp),
            )
        }
    }
}

@Composable
fun FitIdentityCompact(
    name: String,
    modifier: Modifier = Modifier,
    initials: String = "",
    imageUrl: String? = null,
    stats: String? = null,
    avatarSize: Dp = FitV3Geometry.identityStack,
    avatar: (@Composable () -> Unit)? = null,
    trailing: (@Composable () -> Unit)? = null,
) {
    val palette = LocalFitV3Palette.current
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(FitV3Geometry.rowPadding),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(FitV3Geometry.rowGap),
    ) {
        if (avatar != null) {
            avatar()
        } else {
            FitV3Avatar(
                initials = initials,
                size = avatarSize,
                imageUrl = imageUrl,
                plate = FitV3AvatarPlate.OnSurface,
            )
        }
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = name,
                style = FitV3Type.compactName,
                color = palette.textPrimary,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
            )
            if (stats != null) {
                Text(
                    text = stats,
                    style = FitV3Type.compactStats,
                    color = palette.textSecondary,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier.padding(top = 4.dp),
                )
            }
        }
        trailing?.invoke()
    }
}
