import SwiftUI

// MARK: - FitButton
//
// Call-to-action button — 4-tier severity system (primary / secondary /
// destructive) + size variants. See `docs/components.md` FitButton.
//
// Pick the destructive tier by impact, not by appearance:
//   .destructive         → Medium (tinted red)   — cancel confirmed session, decline request
//   .destructiveHigh     → High (filled red)     — delete account, remove template forever
//   .destructiveLow      → Low (outlined red)    — retract pending invite (your own)
//   .destructiveMinimal  → Minimal (text red)    — secondary "Cancel" in confirm dialogs
//
// Size: .lg (h=50, footer CTA) | .md (h=44, card-level) | .sm (h=40, compact)

public enum FitButtonStyle {
    case primary
    case secondary
    case destructive          // Medium — tinted red bg + red text (default)
    case destructiveHigh      // High — filled red bg + white text
    case destructiveLow       // Low — outlined red (transparent bg, red border, red text)
    case destructiveMinimal   // Minimal — red text only (no bg, no border)
    case disabled
}

public enum FitButtonSize {
    case lg   // 50pt — footer primary CTA (Apple HIG)
    case md   // 44pt — card-level CTA (tap target minimum)
    case sm   // 40pt — compact in sheets / cards
}

public struct FitButton: View {
    let title: String
    let style: FitButtonStyle
    let size: FitButtonSize
    /// A button the screen is not ready for yet — a form CTA before the form is valid. It
    /// renders the disabled grammar and swallows the tap.
    let enabled: Bool
    let action: () -> Void

    @Environment(\.fitTheme) private var theme

    public init(
        _ title: String,
        style: FitButtonStyle = .primary,
        size: FitButtonSize = .lg,
        enabled: Bool = true,
        action: @escaping () -> Void
    ) {
        self.title = title
        self.enabled = enabled
        self.style = style
        self.size = size
        self.action = action
    }

    private var effectiveStyle: FitButtonStyle { enabled ? style : .disabled }

    public var body: some View {
        Button(action: action) {
            Text(title)
                .font(fontForSize)
                .frame(maxWidth: .infinity)
                .frame(height: heightForSize)
                .foregroundColor(foregroundColor)
                .background(background)
                .clipShape(Capsule())
                .overlay(overlayBorder)
        }
        .disabled(effectiveStyle == .disabled)
    }

    // MARK: - Size mapping

    private var heightForSize: CGFloat {
        switch size {
        case .lg: return FitSize.buttonLgHeight
        case .md: return FitSize.buttonMdHeight
        case .sm: return FitSize.buttonSmHeight
        }
    }

    private var fontForSize: Font {
        switch size {
        case .lg: return FitFont.button1   // 18pt
        case .md: return FitFont.button2   // 16pt
        case .sm: return FitFont.body2     // 14pt
        }
    }

    // MARK: - Style mapping

    private var foregroundColor: Color {
        switch effectiveStyle {
        case .primary:             return .white
        case .secondary:           return theme.textPrimary
        case .destructive,
             .destructiveLow,
             .destructiveMinimal:  return FitColors.error
        case .destructiveHigh:     return .white
        case .disabled:            return theme.textTertiary
        }
    }

    @ViewBuilder
    private var background: some View {
        switch effectiveStyle {
        case .primary:            FitColors.brandGradient
        case .secondary:          theme.surfaceHigh
        case .destructive:        FitColors.error.opacity(0.15)
        case .destructiveHigh:    FitColors.error
        case .destructiveLow,
             .destructiveMinimal: Color.clear
        // Inactive is the outline grammar, not a filled plate — see `.fit-btn-disabled`.
        case .disabled:           Color.clear
        }
    }

    @ViewBuilder
    private var overlayBorder: some View {
        switch effectiveStyle {
        case .secondary:
            Capsule().stroke(theme.divider, lineWidth: 1)
        case .destructiveLow:
            Capsule().stroke(FitColors.error, lineWidth: 1)
        default:
            EmptyView()
        }
    }
}
