import SwiftUI

// MARK: - 321Fit Color System
// Source: design-tokens/tokens/color-palette.json + color-semantic.json
// Generated from Figma UI Kit via MCP
//
// Usage: .foregroundStyle(FitColors.Gray.g600)
// For theme-aware tokens, use FitTheme instead.

public enum FitColors {

    // MARK: - Palette

    public enum Gray {
        public static let white = Color(hex: "FFFFFF")
        public static let g50 = Color(hex: "F7F7F8")
        public static let g100 = Color(hex: "E4E6E7")
        public static let g200 = Color(hex: "CCCFD2")
        public static let g300 = Color(hex: "ACB1B6")
        public static let g400 = Color(hex: "979EA5")
        public static let g500 = Color(hex: "757E87")
        public static let g600 = Color(hex: "5B6269")
        public static let g700 = Color(hex: "3B4044")
        public static let g800 = Color(hex: "2B2E31")
        public static let g900 = Color(hex: "1F2123")
        public static let black = Color(hex: "111213")
    }

    public enum Blue {
        public static let b300 = Color(hex: "4FDDFC")
        public static let b400 = Color(hex: "22CCF7")
        public static let b500 = Color(hex: "03B2E2")
        public static let b600 = Color(hex: "0A93BD")
        public static let b700 = Color(hex: "06789D")
        public static let b900 = Color(hex: "00334D")
    }

    public enum Teal {
        public static let t200 = Color(hex: "87F7CC")
        public static let t400 = Color(hex: "22F7B7")
        public static let t500 = Color(hex: "05E0A6")
        public static let t600 = Color(hex: "08B48F")
    }

    public enum Red {
        public static let r50 = Color(hex: "FFF0F0")
        public static let r400 = Color(hex: "F05C5B")
        public static let r700 = Color(hex: "AB081F")
        public static let r900 = Color(hex: "610315")
    }

    public enum Yellow {
        public static let y50 = Color(hex: "FFF8DB")
        public static let y400 = Color(hex: "F7C948")
        public static let y600 = Color(hex: "DE911D")
        public static let y900 = Color(hex: "8D2B0A")
    }

    public enum Green {
        public static let g50 = Color(hex: "EAFBEB")
        public static let g400 = Color(hex: "51CA58")
        public static let g500 = Color(hex: "36B03D")
        public static let g900 = Color(hex: "004806")
    }

    // MARK: - Brand

    public static let brandPrimary = Blue.b500
    public static let brandSecondary = Teal.t500
    public static let selectionBorder = Teal.t600

    public static let brandGradient = LinearGradient(
        colors: [Blue.b500, Teal.t500],
        startPoint: .leading,
        endPoint: .trailing
    )

    public static let selectionGradient = LinearGradient(
        colors: [Blue.b600.opacity(0.2), Teal.t500.opacity(0.2)],
        startPoint: .leading,
        endPoint: .trailing
    )

    // MARK: - Semantic Colors (theme-agnostic accents)

    public static let error = Red.r400
    public static let warning = Yellow.y400
    public static let success = Teal.t500
    public static let destructive = Red.r400
}

// MARK: - Color hex initializer

extension Color {
    init(hex: String) {
        let scanner = Scanner(string: hex)
        var rgbValue: UInt64 = 0
        scanner.scanHexInt64(&rgbValue)
        self.init(
            red: Double((rgbValue & 0xFF0000) >> 16) / 255.0,
            green: Double((rgbValue & 0x00FF00) >> 8) / 255.0,
            blue: Double(rgbValue & 0x0000FF) / 255.0
        )
    }
}
