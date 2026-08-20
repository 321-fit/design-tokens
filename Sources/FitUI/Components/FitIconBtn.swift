import SwiftUI

// MARK: - FitIconBtn
//
// Circular icon-only button, 32pt by default and 60pt for a provider choice. Used in
// headers, toolbars, inline actions. See `docs/components.md` FitIconBtn.

public enum FitIconBtnColor {
    case primary   // text-secondary icon on surface-high bg (default)
    case brand     // brand-primary blue icon
    case error     // red-400 icon; with tintedBg = subtle red background (header trash pattern)
    case success   // green-500 icon
}

/// Plate style. `.filled` is the default 32pt circle on `surface.high`. `.ghost` drops the
/// plate — for buttons sitting on an already-busy surface (a sheet header next to a
/// descriptor and a status pill), where a filled circle competes with the content it
/// belongs to. Mirrors `.fit-sheet-menu-btn` in the prototype kit.
public enum FitIconBtnStyle {
    case filled
    case ghost
}

/// How large the circle is, and how large the glyph inside it. `.sm` is the header
/// affordance — back, menu, refresh. `.lg` is a choice the screen is asking for rather than
/// an action tucked into a corner: the provider circles on the auth screens are the case it
/// was added for. The glyph is not a fixed fraction of the plate — the small one carries a
/// heavier ratio so it stays legible at 32.
public enum FitIconBtnSize {
    case sm
    case lg

    var box: CGFloat {
        switch self {
        case .sm: return FitSize.iconBtnSize
        case .lg: return 60
        }
    }

    var glyph: CGFloat {
        switch self {
        case .sm: return FitSize.iconMd
        case .lg: return 26
        }
    }
}

public struct FitIconBtn: View {
    let systemName: String?
    let imageName: String?
    let color: FitIconBtnColor
    let tintedBg: Bool
    let style: FitIconBtnStyle
    let size: FitIconBtnSize
    let action: () -> Void

    @Environment(\.fitTheme) private var theme

    public init(
        systemName: String,
        color: FitIconBtnColor = .primary,
        tintedBg: Bool = false,
        style: FitIconBtnStyle = .filled,
        size: FitIconBtnSize = .sm,
        action: @escaping () -> Void
    ) {
        self.systemName = systemName
        self.imageName = nil
        self.color = color
        self.tintedBg = tintedBg
        self.style = style
        self.size = size
        self.action = action
    }

    public init(
        image: String,
        color: FitIconBtnColor = .primary,
        tintedBg: Bool = false,
        style: FitIconBtnStyle = .filled,
        size: FitIconBtnSize = .sm,
        action: @escaping () -> Void
    ) {
        self.systemName = nil
        self.imageName = image
        self.color = color
        self.tintedBg = tintedBg
        self.style = style
        self.size = size
        self.action = action
    }

    public var body: some View {
        Button(action: action) {
            iconView
                .frame(width: size.box, height: size.box)
                .background(backgroundColor)
                .clipShape(Circle())
        }
        .buttonStyle(.plain)
    }

    @ViewBuilder
    private var iconView: some View {
        if let systemName = systemName {
            Image(systemName: systemName)
                .font(.system(size: size.glyph, weight: .regular))
                .foregroundColor(iconColor)
        } else if let imageName = imageName {
            Image(imageName)
                .resizable()
                .renderingMode(.template)
                .scaledToFit()
                .frame(width: size.glyph, height: size.glyph)
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
        if style == .ghost { return .clear }
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
