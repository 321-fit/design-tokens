package com.fit321.fitui.v3.components

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.sp
import com.fit321.fitui.components.FitAvatar
import com.fit321.fitui.tokens.FitColors
import com.fit321.fitui.v3.theme.LocalFitV3Palette
import com.fit321.fitui.v3.tokens.FitV3Geometry

enum class FitV3AvatarPlate { OnCanvas, OnSurface, Brand }

@Composable
fun FitV3Avatar(
    initials: String,
    size: Dp = FitV3Geometry.leadingPlate,
    modifier: Modifier = Modifier,
    imageUrl: String? = null,
    plate: FitV3AvatarPlate = FitV3AvatarPlate.OnSurface,
    fontSize: TextUnit = (size.value / 3f).sp,
) {
    val palette = LocalFitV3Palette.current
    val background = when (plate) {
        FitV3AvatarPlate.OnCanvas -> SolidColor(palette.raisedOnCanvas)
        FitV3AvatarPlate.OnSurface -> SolidColor(palette.raised)
        FitV3AvatarPlate.Brand -> FitColors.brandGradient
    }
    val ink = when (plate) {
        FitV3AvatarPlate.Brand -> FitColors.Gray.white
        else -> palette.textPrimary
    }
    FitAvatar(
        initials = initials,
        size = size,
        bg = background,
        imageUrl = imageUrl,
        textColor = ink,
        fontWeight = FontWeight.SemiBold,
        fontSize = fontSize,
        modifier = modifier,
    )
}
