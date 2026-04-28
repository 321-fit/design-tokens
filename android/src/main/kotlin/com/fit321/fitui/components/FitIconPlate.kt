package com.fit321.fitui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import com.fit321.fitui.theme.LocalFitTheme
import com.fit321.fitui.tokens.FitColors
import com.fit321.fitui.tokens.FitRadius

/**
 * Decorative leading icon container — rounded square with tinted
 * background + centered icon. Mirrors Swift `FitIconPlate`. Non-interactive;
 * for tappable plates use `FitIconBtn` with `tintedBg = true`.
 * See `docs/components.md` § FitIconPlate.
 */
enum class FitIconPlateTone { Info, Success, Warning, Error, Brand, Neutral }

enum class FitIconPlateSize(val plateDp: Int, val iconDp: Int) {
    Sm(24, 12),
    Md(32, 16),
    Lg(40, 20)
}

@Composable
fun FitIconPlate(
    icon: ImageVector,
    tone: FitIconPlateTone = FitIconPlateTone.Neutral,
    size: FitIconPlateSize = FitIconPlateSize.Md,
    modifier: Modifier = Modifier
) {
    val theme = LocalFitTheme.current
    val shape = RoundedCornerShape(FitRadius.sm)

    val (bg, fg) = when (tone) {
        FitIconPlateTone.Info    -> theme.bgInfoSubtle    to FitColors.Blue.b500
        FitIconPlateTone.Success -> theme.bgSuccessSubtle to FitColors.Teal.t500
        FitIconPlateTone.Warning -> theme.bgWarningSubtle to FitColors.Yellow.y400
        FitIconPlateTone.Error   -> theme.bgErrorSubtle   to FitColors.Red.r400
        FitIconPlateTone.Brand   -> theme.bgBrandSubtle   to FitColors.brandPrimary
        FitIconPlateTone.Neutral -> theme.surfaceHigher   to theme.textSecondary
    }

    Box(
        modifier = modifier
            .size(size.plateDp.dp)
            .background(bg, shape),
        contentAlignment = Alignment.Center
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = fg,
            modifier = Modifier.size(size.iconDp.dp)
        )
    }
}
