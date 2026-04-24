package com.fit321.fitui.tokens

import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp

/**
 * 321Fit typography system — mirrors Swift FitFont.
 * Font: Rubik (add via Google Fonts plugin in consuming app).
 * Source: design-tokens/tokens/typography.json
 */
object FitFont {
    val family: FontFamily = FontFamily.Default   // TODO: wire Rubik via GoogleFont

    val headline    = TextStyle(fontFamily = family, fontWeight = FontWeight.SemiBold, fontSize = 28.sp, lineHeight = 34.sp)
    val heading1    = TextStyle(fontFamily = family, fontWeight = FontWeight.SemiBold, fontSize = 24.sp, lineHeight = 29.sp)
    val heading2    = TextStyle(fontFamily = family, fontWeight = FontWeight.SemiBold, fontSize = 22.sp, lineHeight = 27.5.sp)
    val heading3    = TextStyle(fontFamily = family, fontWeight = FontWeight.Medium,   fontSize = 20.sp, lineHeight = 26.sp)
    val navTitle    = TextStyle(fontFamily = family, fontWeight = FontWeight.SemiBold, fontSize = 17.sp, lineHeight = 22.sp)
    val button1     = TextStyle(fontFamily = family, fontWeight = FontWeight.Medium,   fontSize = 18.sp, lineHeight = 23.sp)
    val button2     = TextStyle(fontFamily = family, fontWeight = FontWeight.Medium,   fontSize = 16.sp, lineHeight = 22.sp)
    val body1       = TextStyle(fontFamily = family, fontWeight = FontWeight.Normal,   fontSize = 16.sp, lineHeight = 22.sp)
    val body2       = TextStyle(fontFamily = family, fontWeight = FontWeight.Normal,   fontSize = 14.sp, lineHeight = 20.sp)
    val footnote    = TextStyle(fontFamily = family, fontWeight = FontWeight.Normal,   fontSize = 13.sp, lineHeight = 18.sp)
    val caption     = TextStyle(fontFamily = family, fontWeight = FontWeight.Normal,   fontSize = 12.sp, lineHeight = 17.sp)
    val captionMicro = TextStyle(fontFamily = family, fontWeight = FontWeight.Medium,  fontSize = 10.sp, lineHeight = 12.sp, letterSpacing = 0.5.sp)
    val pill        = TextStyle(fontFamily = family, fontWeight = FontWeight.Medium,   fontSize = 11.sp, lineHeight = 13.sp)
}
