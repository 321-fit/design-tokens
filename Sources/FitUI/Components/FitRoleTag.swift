import SwiftUI

// MARK: - FitRoleTag
//
// Compact corner badge labelling the user role context of an event:
// "Athlete" (dumbbell glyph) or "Coach" (whistle glyph). Used by
// FitCalEvent in cross-role state — placed at the bottom-right corner
// to signal "this event lives on your OTHER role profile."
//
// 18pt high, 10pt font, 99pt radius. Subtle bg (white 0.08 dark / black
// 0.05 light) — meant to be unobtrusive vs. the tile content.

public struct FitRoleTag: View {
    let role: FitRole

    @Environment(\.fitTheme) private var theme

    public init(role: FitRole) {
        self.role = role
    }

    public var body: some View {
        HStack(spacing: 4) {
            Image(systemName: iconName)
                .font(.system(size: 10, weight: .medium))
                .foregroundColor(theme.textTertiary)
            Text(label)
                .font(.custom(FitFont.family, size: 10).weight(.medium))
                .foregroundColor(theme.textTertiary)
        }
        .padding(.horizontal, 8)
        .frame(height: 18)
        .background(tagBackground)
        .clipShape(Capsule())
    }

    private var iconName: String {
        switch role {
        case .athlete: return "figure.run"
        case .coach:   return "person.fill"
        }
    }

    private var label: String {
        switch role {
        case .athlete: return "Athlete"
        case .coach:   return "Coach"
        }
    }

    /// Subtle wash — slightly different alpha per theme so it reads on both
    /// dark surface-high and light gray-100 cross-role tile backgrounds.
    private var tagBackground: Color {
        // White wash on dark / black wash on light.
        // Hardcoded alphas mirror the prototype's rgba(255,255,255,0.08) /
        // rgba(0,0,0,0.05).
        Color.primary.opacity(0.05)
    }
}
