import SwiftUI

// MARK: - 321Fit Typography System
// Font: Rubik (Google Fonts)
// Source: design-tokens/tokens/typography.json
//
// Each style references the **PostScript name** of the bundled Rubik cut
// directly (Rubik-Regular / Rubik-Medium / Rubik-SemiBold / Rubik-Bold)
// rather than `Font.custom("Rubik", …).weight(.medium)`, because the latter
// asks SwiftUI to synthesize a weight on the family — which doesn't always
// pick up the actual cut and can fall back to system bold-faking.
//
// Auto-registration happens on first use of `fitTheme()` modifier
// (see `FitFontRegister.swift`).

public enum FitFont {
    /// Family name kept for compatibility with any callsite that bypasses the
    /// per-style helpers. New code should prefer the explicit per-style values
    /// below so weight selection is deterministic.
    public static let family = "Rubik"

    // PostScript names of the bundled cuts.
    public static let regularName = "Rubik-Regular"
    public static let mediumName = "Rubik-Medium"
    public static let semiBoldName = "Rubik-SemiBold"
    public static let boldName = "Rubik-Bold"

    // Text styles
    public static let h1 = Font.custom(regularName, size: 22)
    public static let h2 = Font.custom(mediumName, size: 20)
    public static let button1 = Font.custom(mediumName, size: 18)
    public static let button2 = Font.custom(mediumName, size: 16)
    public static let body1 = Font.custom(regularName, size: 16)
    public static let body2 = Font.custom(regularName, size: 14)
    public static let caption = Font.custom(regularName, size: 12)

    // Section title (settings, lists)
    public static let sectionTitle = Font.custom(regularName, size: 16)
}
