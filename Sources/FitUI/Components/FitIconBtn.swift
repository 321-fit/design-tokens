import SwiftUI

// MARK: - FitIconBtn
//
// Circular icon-only button (32pt). Used in headers, toolbars,
// inline actions. See `docs/components.md` FitIconBtn.

public enum FitIconBtnColor {
    case primary   // text-secondary icon on surface-high bg (default)
    case brand     // brand-primary blue icon
    case error     // red-400 icon; with tintedBg = subtle red background (header trash pattern)
    case success   // green-500 icon
}

public struct FitIconBtn: View {
    let systemName: String?
    let imageName: String?
    let color: FitIconBtnColor
    let tintedBg: Bool
    let action: () -> Void

    @Environment(\.fitTheme) private var theme

    public init(
        systemName: String,
        color: FitIconBtnColor = .primary,
        tintedBg: Bool = false,
        action: @escaping () -> Void
    ) {
        self.systemName = systemName
        self.imageName = nil
        self.color = color
        self.tintedBg = tintedBg
        self.action = action
    }

    public init(
        image: String,
        color: FitIconBtnColor = .primary,
        tintedBg: Bool = false,
        action: @escaping () -> Void
    ) {
        self.systemName = nil
        self.imageName = image
        self.color = color
        self.tintedBg = tintedBg
        self.action = action
    }

    public var body: some View {
        Button(action: action) {
            iconView
                .frame(width: FitSize.iconBtnSize, height: FitSize.iconBtnSize)
                .background(backgroundColor)
                .clipShape(Circle())
        }
        .buttonStyle(.plain)
    }

    @ViewBuilder
    private var iconView: some View {
        if let systemName = systemName {
            Image(systemName: systemName)
                .font(.system(size: FitSize.iconMd, weight: .regular))
                .foregroundColor(iconColor)
        } else if let imageName = imageName {
            Image(imageName)
                .resizable()
                .renderingMode(.template)
                .scaledToFit()
                .frame(width: FitSize.iconMd, height: FitSize.iconMd)
                .foregroundColor(iconColor)
        }
    }

    private var iconColor: Color {
        switch color {
        case .primary: return theme.textSecondary
        case .brand:   return FitColors.brandPrimary
        case .error:   return FitColors.error
        case .success: return FitColors.success
        }
    }

    private var backgroundColor: Color {
        if tintedBg {
            switch color {
            case .primary: return theme.surfaceHigh
            case .brand:   return FitColors.brandPrimary.opacity(0.10)
            case .error:   return FitColors.error.opacity(0.10)
            case .success: return FitColors.success.opacity(0.12)
            }
        }
        return theme.surfaceHigh
    }
}
