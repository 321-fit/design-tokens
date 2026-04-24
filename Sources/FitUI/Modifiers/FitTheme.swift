import SwiftUI

// MARK: - Theme Environment
// Coach = dark, Athlete = light (always)

public enum FitRole {
    case coach
    case athlete

    public var theme: FitColors.Theme {
        switch self {
        case .coach: return .dark
        case .athlete: return .light
        }
    }
}

private struct FitThemeKey: EnvironmentKey {
    static let defaultValue: FitColors.Theme = .dark
}

extension EnvironmentValues {
    public var fitTheme: FitColors.Theme {
        get { self[FitThemeKey.self] }
        set { self[FitThemeKey.self] = newValue }
    }
}

extension View {
    public func fitTheme(_ role: FitRole) -> some View {
        self
            .environment(\.fitTheme, role.theme)
            .background(role.theme.screenBg)
    }
}
