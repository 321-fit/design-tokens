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
        // Surfaces
        val screenBg: Color,
        val surfaceLow: Color,
        val surfaceDefault: Color,
        val surfaceHigh: Color,
        val surfaceHigher: Color,
        // Text
        val textPrimary: Color,
        val textSecondary: Color,
        val textTertiary: Color,
        val textPlaceholder: Color,
        val textDisabled: Color,
        val textOnBrand: Color,
        val textError: Color,
        // Borders
        val divider: Color,
        val borderFocus: Color,
        val borderError: Color,
        // Backgrounds — disabled + status tints
        val bgDisabled: Color,
        val bgErrorSubtle: Color,
        val bgErrorTinted: Color,
        val bgSuccessSubtle: Color,
        val bgSuccessTinted: Color,
        val bgWarningSubtle: Color,
        val bgWarningTinted: Color,
        val bgInfoSubtle: Color,
        val bgInfoTinted: Color,
        val bgBrandSubtle: Color,
        val bgBrandTinted: Color,
        // Destructive
        val destructiveBgSubtle: Color,
        val destructiveBgTinted: Color
    ) {
        // Compatibility aliases — pre-existing names retained so existing FitUI components keep working
        val cardBg: Color get() = surfaceDefault
        val focusBorder: Color get() = borderFocus

        companion object {
            val dark = Theme(
                screenBg = Gray.g900,
                surfaceLow = Gray.black,
                surfaceDefault = Gray.g800,
                surfaceHigh = Gray.g800,
                surfaceHigher = Gray.g700,
                textPrimary = Gray.white,
                textSecondary = Gray.g200,
                textTertiary = Gray.g400,
                textPlaceholder = Gray.g500,
                textDisabled = Gray.g600,
                textOnBrand = Gray.white,
                textError = Red.r400,
                divider = Gray.g700,
                borderFocus = Blue.b500,
                borderError = Red.r400,
                bgDisabled = Gray.g700,
                bgErrorSubtle    = Color(0xFFF05C5B).copy(alpha = 0.12f),
                bgErrorTinted    = Color(0xFFF05C5B).copy(alpha = 0.18f),
                bgSuccessSubtle  = Color(0xFF05E0A6).copy(alpha = 0.12f),
                bgSuccessTinted  = Color(0xFF05E0A6).copy(alpha = 0.18f),
                bgWarningSubtle  = Color(0xFFF7C948).copy(alpha = 0.12f),
                bgWarningTinted  = Color(0xFFF7C948).copy(alpha = 0.20f),
                bgInfoSubtle     = Color(0xFF03B2E2).copy(alpha = 0.12f),
                bgInfoTinted     = Color(0xFF03B2E2).copy(alpha = 0.18f),
                bgBrandSubtle    = Color(0xFF05E0A6).copy(alpha = 0.12f),
                bgBrandTinted    = Color(0xFF05E0A6).copy(alpha = 0.18f),
                destructiveBgSubtle = Color(0xFFF05C5B).copy(alpha = 0.12f),
                destructiveBgTinted = Color(0xFFF05C5B).copy(alpha = 0.18f)
            )

            val light = Theme(
                screenBg = Color(0xFFF2F2F7),
                surfaceLow = Gray.g100,
                surfaceDefault = Gray.white,
                surfaceHigh = Gray.white,
                surfaceHigher = Gray.g50,
                textPrimary = Gray.g900,
                textSecondary = Gray.g500,
                textTertiary = Gray.g500,
                textPlaceholder = Gray.g400,
                textDisabled = Gray.g300,
                textOnBrand = Blue.b700,
                textError = Red.r700,
                divider = Gray.g100,
                borderFocus = Blue.b600,
                borderError = Red.r700,
                bgDisabled = Gray.g200,
                bgErrorSubtle    = Color(0xFFF05C5B).copy(alpha = 0.08f),
                bgErrorTinted    = Color(0xFFF05C5B).copy(alpha = 0.12f),
                bgSuccessSubtle  = Color(0xFF05E0A6).copy(alpha = 0.08f),
                bgSuccessTinted  = Color(0xFF05E0A6).copy(alpha = 0.12f),
                bgWarningSubtle  = Color(0xFFF7C948).copy(alpha = 0.10f),
                bgWarningTinted  = Color(0xFFF7C948).copy(alpha = 0.16f),
                bgInfoSubtle     = Color(0xFF03B2E2).copy(alpha = 0.08f),
                bgInfoTinted     = Color(0xFF03B2E2).copy(alpha = 0.12f),
                bgBrandSubtle    = Color(0xFF05E0A6).copy(alpha = 0.08f),
                bgBrandTinted    = Color(0xFF05E0A6).copy(alpha = 0.12f),
                destructiveBgSubtle = Color(0xFFF05C5B).copy(alpha = 0.08f),
                destructiveBgTinted = Color(0xFFF05C5B).copy(alpha = 0.12f)
            )
        }
    }
}
