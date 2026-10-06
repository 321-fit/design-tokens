package com.fit321.fitui.v3.tokens

import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import androidx.compose.ui.unit.TextUnit
import com.fit321.fitui.tokens.FitFont

object FitV3Type {

    private fun style(
        size: Int,
        weight: FontWeight,
        lineHeight: Int,
        letterSpacing: TextUnit = TextUnit.Unspecified,
    ) = TextStyle(
        fontFamily = FitFont.family,
        fontWeight = weight,
        fontSize = size.sp,
        lineHeight = lineHeight.sp,
        letterSpacing = letterSpacing,
    )

    val sectionTitle = style(15, FontWeight.SemiBold, 20)

    val rowTitle = style(16, FontWeight.Medium, 21)
    val rowSub = style(13, FontWeight.Normal, 18)
    val rowValue = style(15, FontWeight.Medium, 20)
    val rowValueSub = style(12, FontWeight.Normal, 16)

    val fieldLabel = style(13, FontWeight.SemiBold, 18)
    val fieldText = style(16, FontWeight.Normal, 21)
    val fieldHint = style(12, FontWeight.Normal, 16)

    val identityName = style(28, FontWeight.Bold, 34)
    val identityContext = style(14, FontWeight.Normal, 20)
    val compactName = style(22, FontWeight.Bold, 27)
    val compactStats = style(15, FontWeight.SemiBold, 20)

    val profileName = style(24, FontWeight.Bold, 29)
    val statValue = style(18, FontWeight.SemiBold, 23)
    val statLabel = style(12, FontWeight.Normal, 16)

    val actionLabel = style(12, FontWeight.Normal, 16)
    val chip = style(14, FontWeight.Medium, 19)
    val pillLabel = style(13, FontWeight.Medium, 18)

    val nextWhen = style(17, FontWeight.SemiBold, 22)
    val nextWhat = style(13, FontWeight.Normal, 18)

    val ringValue = style(34, FontWeight.Bold, 36)
    val ringTotal = style(18, FontWeight.SemiBold, 24)
    val rowValueBold = style(14, FontWeight.SemiBold, 19)

    val detailHero = style(32, FontWeight.SemiBold, 38, (-0.5).sp)
    val detailHeroSub = style(14, FontWeight.Normal, 20)
    val sheetTitle = style(18, FontWeight.Medium, 23)

    val clipDuration = style(12, FontWeight.SemiBold, 16)

    val statLabelSmall = style(11, FontWeight.Normal, 14)

    val moneyHeadline = style(24, FontWeight.Bold, 29)
    val moneyHero = style(48, FontWeight.Bold, 54)
    val moneyHeroCurrency = style(28, FontWeight.SemiBold, 34)
}
