package com.fit321.fitui.tokens

import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color

/**
 * 321Fit Color System — Kotlin Compose mirror of Swift FitColors.
 * Source: design-tokens/tokens/color-palette.json + color-semantic.json + gradient.json
 */
object FitColors {

    // ==== Palette: gray ====
    object Gray {
        val white = Color(0xFFFFFFFF)
        val g50   = Color(0xFFF7F7F8)
        val g100  = Color(0xFFE4E6E7)
        val g200  = Color(0xFFCCCFD2)
        val g300  = Color(0xFFACB1B6)
        val g400  = Color(0xFF979EA5)
        val g500  = Color(0xFF757E87)
        val g600  = Color(0xFF5B6269)
        val g700  = Color(0xFF3B4044)
        val g800  = Color(0xFF2B2E31)
        val g900  = Color(0xFF1F2123)
        val black = Color(0xFF111213)
    }

    object Blue {
        val b300 = Color(0xFF4FDDFC)
        val b400 = Color(0xFF22CCF7)
        val b500 = Color(0xFF03B2E2)
        val b600 = Color(0xFF0A93BD)
        val b700 = Color(0xFF06789D)
        val b900 = Color(0xFF00334D)
    }

    object Teal {
        val t200 = Color(0xFF87F7CC)
        val t400 = Color(0xFF22F7B7)
        val t500 = Color(0xFF05E0A6)
        val t600 = Color(0xFF08B48F)
    }

    object Red {
        val r50  = Color(0xFFFFF0F0)
        val r400 = Color(0xFFF05C5B)
        val r700 = Color(0xFFAB081F)
        val r900 = Color(0xFF610315)
    }

    object Yellow {
        val y50  = Color(0xFFFFF8DB)
        val y400 = Color(0xFFF7C948)
        val y600 = Color(0xFFDE911D)
        val y900 = Color(0xFF8D2B0A)
    }

    object Green {
        val g50  = Color(0xFFEAFBEB)
        val g400 = Color(0xFF51CA58)
        val g500 = Color(0xFF36B03D)
        val g900 = Color(0xFF004806)
    }

    // ==== Brand ====
    val brandPrimary     = Blue.b500
    val brandSecondary   = Teal.t500
    val selectionBorder  = Teal.t600

    val brandGradient: Brush = Brush.horizontalGradient(
        colors = listOf(Blue.b500, Teal.t500)
    )

    val selectionGradient: Brush = Brush.horizontalGradient(
        colors = listOf(
            Blue.b600.copy(alpha = 0.2f),
            Teal.t500.copy(alpha = 0.2f)
        )
    )

    // ==== Semantic ====
    val error   = Red.r400
    val warning = Yellow.y400
    val success = Teal.t500

    // ==== Theme data class ====
    data class Theme(
        val screenBg: Color,
        val surfaceLow: Color,
        val surfaceHigh: Color,
        val surfaceHigher: Color,
        val textPrimary: Color,
        val textSecondary: Color,
        val textTertiary: Color,
        val textPlaceholder: Color,
        val divider: Color,
        val textOnBrand: Color,
        val cardBg: Color,
        val focusBorder: Color
    ) {
        companion object {
            val dark = Theme(
                screenBg = Gray.g900,
                surfaceLow = Gray.black,
                surfaceHigh = Gray.g800,
                surfaceHigher = Gray.g700,
                textPrimary = Gray.white,
                textSecondary = Gray.g200,
                textTertiary = Gray.g400,
                textPlaceholder = Gray.g500,
                divider = Gray.g700,
                textOnBrand = Gray.white,
                cardBg = Gray.g800,
                focusBorder = Gray.g600
            )

            val light = Theme(
                screenBg = Color(0xFFF2F2F7),
                surfaceLow = Gray.g100,
                surfaceHigh = Gray.white,
                surfaceHigher = Gray.g50,
                textPrimary = Gray.g900,
                textSecondary = Gray.g500,
                textTertiary = Gray.g400,
                textPlaceholder = Gray.g400,
                divider = Gray.g100,
                textOnBrand = Blue.b700,
                cardBg = Gray.white,
                focusBorder = Gray.g300
            )
        }
    }
}
