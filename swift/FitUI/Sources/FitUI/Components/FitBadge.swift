import SwiftUI

// MARK: - FitBadge
// PaddingX: 10, PaddingY: 3, Radius: 6, Font: 12px

public enum FitBadgeStyle {
    case group      // cyan bg
    case personal   // surface high bg
    case full       // red bg
    case joined     // green bg
    case pending    // yellow bg
    case special    // cyan light bg
    case error      // red text, subtle bg
}

public struct FitBadge: View {
    let text: String
    let style: FitBadgeStyle

    @Environment(\.fitTheme) private var theme

    public init(_ text: String, style: FitBadgeStyle) {
        self.text = text
        self.style = style
    }

    public var body: some View {
        Text(text)
            .font(FitFont.caption)
            .fontWeight(.medium)
            .foregroundColor(foregroundColor)
            .padding(.horizontal, 10)
            .padding(.vertical, 3)
            .background(backgroundColor)
            .clipShape(RoundedRectangle(cornerRadius: FitRadius.badge))
    }

    private var foregroundColor: Color {
        switch style {
        case .group: return FitColors.Blue.b500
        case .personal: return theme.textSecondary
        case .full: return FitColors.error
        case .joined: return FitColors.Green.g500
        case .pending: return FitColors.Yellow.y400
        case .special: return FitColors.Blue.b500
        case .error: return FitColors.error
        }
    }

    private var backgroundColor: Color {
        switch style {
        case .group: return FitColors.Blue.b500.opacity(0.15)
        case .personal: return theme.surfaceHigh
        case .full: return FitColors.error.opacity(0.12)
        case .joined: return FitColors.success.opacity(0.12)
        case .pending: return FitColors.warning.opacity(0.12)
        case .special: return FitColors.Blue.b500.opacity(0.12)
        case .error: return FitColors.error.opacity(0.12)
        }
    }
}
