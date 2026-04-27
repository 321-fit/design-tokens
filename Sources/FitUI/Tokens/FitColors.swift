import SwiftUI

// MARK: - 321Fit Color System
// Source: design-tokens/tokens/color-palette.json + color-semantic.json
// Generated from Figma UI Kit via MCP

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

    // MARK: - Semantic (theme-aware)

    public struct Theme {
        // Surfaces
        public let screenBg: Color
        public let surfaceLow: Color
        public let surfaceDefault: Color
        public let surfaceHigh: Color
        public let surfaceHigher: Color
        // Text
        public let textPrimary: Color
        public let textSecondary: Color
        public let textTertiary: Color
        public let textPlaceholder: Color
        public let textDisabled: Color
        public let textOnBrand: Color
        public let textError: Color
        // Borders
        public let divider: Color
        public let borderFocus: Color
        public let borderError: Color
        // Backgrounds — disabled + status tints
        public let bgDisabled: Color
        public let bgErrorSubtle: Color
        public let bgErrorTinted: Color
        public let bgSuccessSubtle: Color
        public let bgSuccessTinted: Color
        public let bgWarningSubtle: Color
        public let bgWarningTinted: Color
        public let bgInfoSubtle: Color
        public let bgInfoTinted: Color
        public let bgBrandSubtle: Color
        public let bgBrandTinted: Color
        // Destructive
        public let destructiveBgSubtle: Color
        public let destructiveBgTinted: Color
        // Compatibility aliases — pre-existing names retained so existing FitUI components keep working
        public var inputBg: Color { surfaceLow }
        public var cardBg: Color { surfaceDefault }
        public var focusBorder: Color { borderFocus }

        public static let dark = Theme(
            screenBg: Gray.g900,
            surfaceLow: Gray.black,
            surfaceDefault: Gray.g800,
            surfaceHigh: Gray.g800,
            surfaceHigher: Gray.g700,
            textPrimary: Gray.white,
            textSecondary: Gray.g200,
            textTertiary: Gray.g400,
            textPlaceholder: Gray.g500,
            textDisabled: Gray.g600,
            textOnBrand: Gray.white,
            textError: Red.r400,
            divider: Gray.g700,
            borderFocus: Blue.b500,
            borderError: Red.r400,
            bgDisabled: Gray.g700,
            bgErrorSubtle: Color(red: 240/255, green: 92/255, blue: 91/255).opacity(0.12),
            bgErrorTinted: Color(red: 240/255, green: 92/255, blue: 91/255).opacity(0.18),
            bgSuccessSubtle: Color(red: 5/255, green: 224/255, blue: 166/255).opacity(0.12),
            bgSuccessTinted: Color(red: 5/255, green: 224/255, blue: 166/255).opacity(0.18),
            bgWarningSubtle: Color(red: 247/255, green: 201/255, blue: 72/255).opacity(0.12),
            bgWarningTinted: Color(red: 247/255, green: 201/255, blue: 72/255).opacity(0.20),
            bgInfoSubtle: Color(red: 3/255, green: 178/255, blue: 226/255).opacity(0.12),
            bgInfoTinted: Color(red: 3/255, green: 178/255, blue: 226/255).opacity(0.18),
            bgBrandSubtle: Color(red: 5/255, green: 224/255, blue: 166/255).opacity(0.12),
            bgBrandTinted: Color(red: 5/255, green: 224/255, blue: 166/255).opacity(0.18),
            destructiveBgSubtle: Color(red: 240/255, green: 92/255, blue: 91/255).opacity(0.12),
            destructiveBgTinted: Color(red: 240/255, green: 92/255, blue: 91/255).opacity(0.18)
        )

        public static let light = Theme(
            screenBg: Gray.g50,
            surfaceLow: Gray.g100,
            surfaceDefault: Gray.white,
            surfaceHigh: Gray.white,
            surfaceHigher: Gray.g50,
            textPrimary: Gray.g900,
            textSecondary: Gray.g500,
            textTertiary: Gray.g500,
            textPlaceholder: Gray.g400,
            textDisabled: Gray.g300,
            textOnBrand: Blue.b700,
            textError: Red.r700,
            divider: Gray.g100,
            borderFocus: Blue.b600,
            borderError: Red.r700,
            bgDisabled: Gray.g200,
            bgErrorSubtle: Color(red: 240/255, green: 92/255, blue: 91/255).opacity(0.08),
            bgErrorTinted: Color(red: 240/255, green: 92/255, blue: 91/255).opacity(0.12),
            bgSuccessSubtle: Color(red: 5/255, green: 224/255, blue: 166/255).opacity(0.08),
            bgSuccessTinted: Color(red: 5/255, green: 224/255, blue: 166/255).opacity(0.12),
            bgWarningSubtle: Color(red: 247/255, green: 201/255, blue: 72/255).opacity(0.10),
            bgWarningTinted: Color(red: 247/255, green: 201/255, blue: 72/255).opacity(0.16),
            bgInfoSubtle: Color(red: 3/255, green: 178/255, blue: 226/255).opacity(0.08),
            bgInfoTinted: Color(red: 3/255, green: 178/255, blue: 226/255).opacity(0.12),
            bgBrandSubtle: Color(red: 5/255, green: 224/255, blue: 166/255).opacity(0.08),
            bgBrandTinted: Color(red: 5/255, green: 224/255, blue: 166/255).opacity(0.12),
            destructiveBgSubtle: Color(red: 240/255, green: 92/255, blue: 91/255).opacity(0.08),
            destructiveBgTinted: Color(red: 240/255, green: 92/255, blue: 91/255).opacity(0.12)
        )
    }

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
