import SwiftUI

// MARK: - Theme Environment
// Coach = dark, Athlete = light (always)
//
// For FitUI internal components that need dynamic theme switching.
// App-level code can use static access instead: FitTheme.bgErrorTinted

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

private struct FitThemeKey: EnvironmentKey {
    static let defaultValue: FitTheme = .dark
}

extension EnvironmentValues {
    public var fitTheme: FitTheme {
        get { self[FitThemeKey.self] }
        set { self[FitThemeKey.self] = newValue }
    }
}

extension View {
    public func fitTheme(_ role: FitRole) -> some View {
        // Side-effect: register the bundled Rubik TTFs the first time any
        // FitUI-themed view appears. `FitFontRegister.ensure()` is idempotent
        // and thread-safe (lazy `let` static), so calling it on every modifier
        // application is free after the first call.
        FitFontRegister.ensure()
        return self
            .environment(\.fitTheme, role.theme)
            .background(role.theme.screenBg)
    }
}
