package com.fit321.fitui.tokens

import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.fit321.designtokens.R

/**
 * 321Fit typography system — mirrors Swift FitFont.
 * Font: Rubik.
 * Source: design-tokens/tokens/typography.json
 *
 * Rubik is bundled directly in the design-system module
 * (`android/src/main/res/font/rubik_*.ttf`) and resolved from those
 * resources — no runtime download, no Google Play Services, no per-app
 * wiring. Every consumer of FitUI renders Rubik out of the box, offline,
 * with no first-paint fallback flash.
 */
object FitFont {

    val family: FontFamily = FontFamily(
        Font(R.font.rubik_regular, FontWeight.Normal),
        Font(R.font.rubik_medium, FontWeight.Medium),
        Font(R.font.rubik_semibold, FontWeight.SemiBold),
        Font(R.font.rubik_bold, FontWeight.Bold),
    )

    val headline     get() = TextStyle(fontFamily = family, fontWeight = FontWeight.SemiBold, fontSize = 28.sp, lineHeight = 34.sp)
    val heading1     get() = TextStyle(fontFamily = family, fontWeight = FontWeight.SemiBold, fontSize = 24.sp, lineHeight = 29.sp)
    val heading2     get() = TextStyle(fontFamily = family, fontWeight = FontWeight.SemiBold, fontSize = 22.sp, lineHeight = 27.5.sp)
    val heading3     get() = TextStyle(fontFamily = family, fontWeight = FontWeight.Medium,   fontSize = 20.sp, lineHeight = 26.sp)
    val navTitle     get() = TextStyle(fontFamily = family, fontWeight = FontWeight.SemiBold, fontSize = 17.sp, lineHeight = 22.sp)
    val button1      get() = TextStyle(fontFamily = family, fontWeight = FontWeight.Medium,   fontSize = 18.sp, lineHeight = 23.sp)
    val button2      get() = TextStyle(fontFamily = family, fontWeight = FontWeight.Medium,   fontSize = 16.sp, lineHeight = 22.sp)
    val body1        get() = TextStyle(fontFamily = family, fontWeight = FontWeight.Normal,   fontSize = 16.sp, lineHeight = 22.sp)
    val body2        get() = TextStyle(fontFamily = family, fontWeight = FontWeight.Normal,   fontSize = 14.sp, lineHeight = 20.sp)
    val footnote     get() = TextStyle(fontFamily = family, fontWeight = FontWeight.Normal,   fontSize = 13.sp, lineHeight = 18.sp)
    val caption      get() = TextStyle(fontFamily = family, fontWeight = FontWeight.Normal,   fontSize = 12.sp, lineHeight = 17.sp)
    val captionMicro get() = TextStyle(fontFamily = family, fontWeight = FontWeight.Medium,   fontSize = 10.sp, lineHeight = 12.sp, letterSpacing = 0.5.sp)
    val pill         get() = TextStyle(fontFamily = family, fontWeight = FontWeight.Medium,   fontSize = 11.sp, lineHeight = 13.sp)
}
