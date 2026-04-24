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
    case neutral    // surface-higher bg, text-tertiary (Cash/Card tags on prices)
    case crm        // teal-tinted pill (coach-created client without app account)
    case danger     // alias for full (kept for inventory semantics)
    case info       // alias for special (info banners)
    case success    // alias for joined (success states)
    case accent     // brand-primary tint (highlights)
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
        case .group:    return FitColors.Blue.b500
        case .personal: return theme.textSecondary
        case .full, .danger: return FitColors.error
        case .joined, .success: return FitColors.Green.g500
        case .pending:  return FitColors.Yellow.y400
        case .special, .info: return FitColors.Blue.b500
        case .error:    return FitColors.error
        case .neutral:  return theme.textTertiary
        case .crm:      return FitColors.Teal.t500
        case .accent:   return FitColors.brandPrimary
        }
    }

    private var backgroundColor: Color {
        switch style {
        case .group:    return FitColors.Blue.b500.opacity(0.15)
        case .personal: return theme.surfaceHigh
        case .full, .danger: return FitColors.error.opacity(0.12)
        case .joined, .success: return FitColors.success.opacity(0.12)
        case .pending:  return FitColors.warning.opacity(0.12)
        case .special, .info: return FitColors.Blue.b500.opacity(0.12)
        case .error:    return FitColors.error.opacity(0.12)
        case .neutral:  return theme.surfaceHigher
        case .crm:      return FitColors.Teal.t500.opacity(0.15)
        case .accent:   return FitColors.brandPrimary.opacity(0.12)
        }
    }
}
