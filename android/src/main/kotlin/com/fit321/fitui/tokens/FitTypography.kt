package com.fit321.fitui.tokens

import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.googlefonts.Font as GoogleFontFont
import androidx.compose.ui.text.googlefonts.GoogleFont
import androidx.compose.ui.unit.sp

/**
 * 321Fit typography system — mirrors Swift FitFont.
 * Font: Rubik (Google Fonts).
 * Source: design-tokens/tokens/typography.json
 *
 * The fonts are fetched at runtime via the Compose Downloadable Fonts API
 * (`androidx.compose.ui:ui-text-google-fonts`), which uses Google Play
 * Services as the font provider. No bundled binaries — the OS caches the
 * font once, shared across apps on the device.
 *
 * Cert array note:
 * The standard Google Fonts provider needs a `certificates` int-array
 * resource. We can't easily wire the platform-default
 * `R.array.com_google_android_gms_fonts_certs` from inside a library module
 * (it lives in the consuming app's package), so we fall back to
 * `FontFamily.Default` if the provider hasn't been supplied.
 *
 * TODO: pass `GoogleFont.Provider` from app via `CompositionLocal` so the
 * provider lives in the app module (where the cert array is reachable) and
 * is propagated down to FitUI components. Tracked alongside the iOS Rubik
 * registration work — see Phase 1 plan.
 */
object FitFont {

    private val rubik = GoogleFont("Rubik")

    /**
     * Optional provider — set this from the consuming app once at startup
     * (e.g. inside `FitTheme(...)` initialization in the app shell). When
     * `null`, all FitFont styles fall back to `FontFamily.Default` so
     * components keep rendering instead of crashing.
     */
    @Volatile
    var provider: GoogleFont.Provider? = null

    val family: FontFamily
        get() = provider?.let { p ->
            FontFamily(
                GoogleFontFont(googleFont = rubik, fontProvider = p, weight = FontWeight.Normal,   style = FontStyle.Normal),
                GoogleFontFont(googleFont = rubik, fontProvider = p, weight = FontWeight.Medium,   style = FontStyle.Normal),
                GoogleFontFont(googleFont = rubik, fontProvider = p, weight = FontWeight.SemiBold, style = FontStyle.Normal),
                GoogleFontFont(googleFont = rubik, fontProvider = p, weight = FontWeight.Bold,     style = FontStyle.Normal)
            )
        } ?: FontFamily.Default

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
