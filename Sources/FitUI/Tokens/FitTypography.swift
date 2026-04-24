import SwiftUI

// MARK: - 321Fit Typography System
// Font: Rubik (Google Fonts)
// Source: design-tokens/tokens/typography.json

public enum FitFont {
    public static let family = "Rubik"

    // Text styles
    public static let h1 = Font.custom(family, size: 22).weight(.regular)
    public static let h2 = Font.custom(family, size: 20).weight(.medium)
    public static let button1 = Font.custom(family, size: 18).weight(.medium)
    public static let button2 = Font.custom(family, size: 16).weight(.medium)
    public static let body1 = Font.custom(family, size: 16).weight(.regular)
    public static let body2 = Font.custom(family, size: 14).weight(.regular)
    public static let caption = Font.custom(family, size: 12).weight(.regular)

    // Section title (settings, lists)
    public static let sectionTitle = Font.custom(family, size: 16).weight(.regular)
}
