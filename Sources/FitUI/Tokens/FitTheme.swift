import SwiftUI

// MARK: - 321Fit Theme System
//
// Usage:
//   Static (default = dark/coach):  .background(FitTheme.bgErrorTinted)
//   Explicit variant:                .foregroundStyle(FitTheme.light.textPrimary)
//   Raw palette color:               .foregroundStyle(FitColors.Gray.g600)
//   Screen background:               .fitBackground()

public struct FitTheme {

    // MARK: - Surfaces

    public let screenBg: Color
    public let surfaceLow: Color
    public let surfaceDefault: Color
    public let surfaceHigh: Color
    public let surfaceHigher: Color

    // MARK: - Text

    public let textPrimary: Color
    public let textSecondary: Color
    public let textTertiary: Color
    public let textPlaceholder: Color
    public let textDisabled: Color
    public let textOnBrand: Color
    public let textError: Color

    // MARK: - Borders

    public let divider: Color
    public let borderFocus: Color
    public let borderError: Color

    // MARK: - Backgrounds — disabled + status tints

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

    // MARK: - Destructive

    public let destructiveBgSubtle: Color
    public let destructiveBgTinted: Color

    // MARK: - Compatibility aliases

    public var inputBg: Color { surfaceLow }
    public var cardBg: Color { surfaceDefault }
    public var focusBorder: Color { borderFocus }

    // MARK: - Presets

    public static let dark = FitTheme(
        screenBg: FitColors.Gray.g900,
        surfaceLow: FitColors.Gray.black,
        surfaceDefault: FitColors.Gray.g800,
        surfaceHigh: FitColors.Gray.g800,
        surfaceHigher: FitColors.Gray.g700,
        textPrimary: FitColors.Gray.white,
        textSecondary: FitColors.Gray.g200,
        textTertiary: FitColors.Gray.g400,
        textPlaceholder: FitColors.Gray.g500,
        textDisabled: FitColors.Gray.g600,
        textOnBrand: FitColors.Gray.white,
        textError: FitColors.Red.r400,
        divider: FitColors.Gray.g700,
        borderFocus: FitColors.Blue.b500,
        borderError: FitColors.Red.r400,
        bgDisabled: FitColors.Gray.g700,
        bgErrorSubtle: FitColors.Red.r400.opacity(0.12),
        bgErrorTinted: FitColors.Red.r400.opacity(0.18),
        bgSuccessSubtle: FitColors.Teal.t500.opacity(0.12),
        bgSuccessTinted: FitColors.Teal.t500.opacity(0.18),
        bgWarningSubtle: FitColors.Yellow.y400.opacity(0.12),
        bgWarningTinted: FitColors.Yellow.y400.opacity(0.20),
        bgInfoSubtle: FitColors.Blue.b500.opacity(0.12),
        bgInfoTinted: FitColors.Blue.b500.opacity(0.18),
        bgBrandSubtle: FitColors.Teal.t500.opacity(0.12),
        bgBrandTinted: FitColors.Teal.t500.opacity(0.18),
        destructiveBgSubtle: FitColors.Red.r400.opacity(0.12),
        destructiveBgTinted: FitColors.Red.r400.opacity(0.18)
    )

    public static let light = FitTheme(
        screenBg: FitColors.Gray.g50,
        surfaceLow: FitColors.Gray.g100,
        surfaceDefault: FitColors.Gray.white,
        surfaceHigh: FitColors.Gray.white,
        surfaceHigher: FitColors.Gray.g50,
        textPrimary: FitColors.Gray.g900,
        textSecondary: FitColors.Gray.g500,
        textTertiary: FitColors.Gray.g500,
        textPlaceholder: FitColors.Gray.g400,
        textDisabled: FitColors.Gray.g300,
        textOnBrand: FitColors.Blue.b700,
        textError: FitColors.Red.r700,
        divider: FitColors.Gray.g100,
        borderFocus: FitColors.Blue.b600,
        borderError: FitColors.Red.r700,
        bgDisabled: FitColors.Gray.g200,
        bgErrorSubtle: FitColors.Red.r400.opacity(0.08),
        bgErrorTinted: FitColors.Red.r400.opacity(0.12),
        bgSuccessSubtle: FitColors.Teal.t500.opacity(0.08),
        bgSuccessTinted: FitColors.Teal.t500.opacity(0.12),
        bgWarningSubtle: FitColors.Yellow.y400.opacity(0.10),
        bgWarningTinted: FitColors.Yellow.y400.opacity(0.16),
        bgInfoSubtle: FitColors.Blue.b500.opacity(0.08),
        bgInfoTinted: FitColors.Blue.b500.opacity(0.12),
        bgBrandSubtle: FitColors.Teal.t500.opacity(0.08),
        bgBrandTinted: FitColors.Teal.t500.opacity(0.12),
        destructiveBgSubtle: FitColors.Red.r400.opacity(0.08),
        destructiveBgTinted: FitColors.Red.r400.opacity(0.12)
    )
}

// MARK: - Static convenience (defaults to dark/coach)

extension FitTheme {
    public static var screenBg: Color { dark.screenBg }
    public static var surfaceLow: Color { dark.surfaceLow }
    public static var surfaceDefault: Color { dark.surfaceDefault }
    public static var surfaceHigh: Color { dark.surfaceHigh }
    public static var surfaceHigher: Color { dark.surfaceHigher }

    public static var textPrimary: Color { dark.textPrimary }
    public static var textSecondary: Color { dark.textSecondary }
    public static var textTertiary: Color { dark.textTertiary }
    public static var textPlaceholder: Color { dark.textPlaceholder }
    public static var textDisabled: Color { dark.textDisabled }
    public static var textOnBrand: Color { dark.textOnBrand }
    public static var textError: Color { dark.textError }

    public static var divider: Color { dark.divider }
    public static var borderFocus: Color { dark.borderFocus }
    public static var borderError: Color { dark.borderError }

    public static var bgDisabled: Color { dark.bgDisabled }
    public static var bgErrorSubtle: Color { dark.bgErrorSubtle }
    public static var bgErrorTinted: Color { dark.bgErrorTinted }
    public static var bgSuccessSubtle: Color { dark.bgSuccessSubtle }
    public static var bgSuccessTinted: Color { dark.bgSuccessTinted }
    public static var bgWarningSubtle: Color { dark.bgWarningSubtle }
    public static var bgWarningTinted: Color { dark.bgWarningTinted }
    public static var bgInfoSubtle: Color { dark.bgInfoSubtle }
    public static var bgInfoTinted: Color { dark.bgInfoTinted }
    public static var bgBrandSubtle: Color { dark.bgBrandSubtle }
    public static var bgBrandTinted: Color { dark.bgBrandTinted }

    public static var destructiveBgSubtle: Color { dark.destructiveBgSubtle }
    public static var destructiveBgTinted: Color { dark.destructiveBgTinted }

    public static var inputBg: Color { dark.inputBg }
    public static var cardBg: Color { dark.cardBg }
    public static var focusBorder: Color { dark.focusBorder }
}

// MARK: - Role

public enum FitRole {
    case coach
    case athlete

    public var theme: FitTheme {
        switch self {
        case .coach: return .dark
        case .athlete: return .light
        }
    }
}

// MARK: - Environment (used internally by FitUI components)

private struct FitThemeKey: EnvironmentKey {
    static let defaultValue: FitTheme = .dark
}

extension EnvironmentValues {
    public var fitTheme: FitTheme {
        get { self[FitThemeKey.self] }
        set { self[FitThemeKey.self] = newValue }
    }
}

// MARK: - View modifiers

extension View {
    public func fitTheme(_ role: FitRole) -> some View {
        FitFontRegister.ensure()
        return self
            .environment(\.fitTheme, role.theme)
            .background(role.theme.screenBg)
    }

    /// Applies themed background filling behind safe area.
    /// Usage: `.fitBackground()` or `.fitBackground(.light, edges: .bottom)`
    public func fitBackground(
        _ theme: FitTheme = .dark,
        edges: Edge.Set = .all
    ) -> some View {
        FitFontRegister.ensure()
        return self
            .environment(\.fitTheme, theme)
            .background(theme.screenBg.ignoresSafeArea(.container, edges: edges))
    }
}
