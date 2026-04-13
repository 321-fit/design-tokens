import SwiftUI

// MARK: - FitButton
// Primary (gradient), Secondary (outlined), Destructive (red), Disabled
// Height: 56px, Radius: 99px (pill), Font: 18px Rubik Medium

public enum FitButtonStyle {
    case primary
    case secondary
    case destructive
    case disabled
}

public struct FitButton: View {
    let title: String
    let style: FitButtonStyle
    let isSmall: Bool
    let action: () -> Void

    @Environment(\.fitTheme) private var theme

    public init(_ title: String, style: FitButtonStyle = .primary, isSmall: Bool = false, action: @escaping () -> Void) {
        self.title = title
        self.style = style
        self.isSmall = isSmall
        self.action = action
    }

    public var body: some View {
        Button(action: action) {
            Text(title)
                .font(isSmall ? FitFont.body2 : FitFont.button1)
                .frame(maxWidth: .infinity)
                .frame(height: isSmall ? FitSize.buttonSmHeight : FitSize.buttonHeight)
                .foregroundColor(foregroundColor)
                .background(background)
                .clipShape(Capsule())
                .overlay(overlayBorder)
        }
        .disabled(style == .disabled)
        .opacity(style == .disabled ? 0.7 : 1.0)
    }

    private var foregroundColor: Color {
        switch style {
        case .primary: return .white
        case .secondary: return theme.textPrimary
        case .destructive: return FitColors.error
        case .disabled: return theme.textTertiary
        }
    }

    @ViewBuilder
    private var background: some View {
        switch style {
        case .primary: FitColors.brandGradient
        case .secondary: theme.surfaceHigh
        case .destructive: FitColors.error.opacity(0.12)
        case .disabled: theme.surfaceLow
        }
    }

    @ViewBuilder
    private var overlayBorder: some View {
        if style == .secondary {
            Capsule().stroke(theme.divider, lineWidth: 1)
        } else {
            EmptyView()
        }
    }
}
