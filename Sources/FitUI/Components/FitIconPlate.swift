import SwiftUI

// MARK: - FitIconPlate
//
// Decorative leading icon container: rounded square with tinted
// background + centered SF Symbol icon. Non-interactive — for tappable
// plates use FitIconBtn with tintedBg: true.
// See `docs/components.md` § FitIconPlate.

public struct FitIconPlate: View {
    public enum Tone {
        case info, success, warning, error, brand, neutral
    }

    public enum Size {
        case sm, md, lg

        var plate: CGFloat {
            switch self {
            case .sm: return 24
            case .md: return 32
            case .lg: return 40
            }
        }

        var icon: CGFloat {
            switch self {
            case .sm: return 12
            case .md: return 16
            case .lg: return 20
            }
        }
    }

    let systemName: String
    let tone: Tone
    let size: Size

    @Environment(\.fitTheme) private var theme

    public init(
        _ systemName: String,
        tone: Tone = .neutral,
        size: Size = .md
    ) {
        self.systemName = systemName
        self.tone = tone
        self.size = size
    }

    public var body: some View {
        ZStack {
            RoundedRectangle(cornerRadius: FitRadius.sm)
                .fill(background)
            Image(systemName: systemName)
                .font(.system(size: size.icon, weight: .medium))
                .foregroundColor(foreground)
        }
        .frame(width: size.plate, height: size.plate)
    }

    private var background: Color {
        switch tone {
        case .info:    return theme.bgInfoSubtle
        case .success: return theme.bgSuccessSubtle
        case .warning: return theme.bgWarningSubtle
        case .error:   return theme.bgErrorSubtle
        case .brand:   return theme.bgBrandSubtle
        case .neutral: return theme.surfaceHigher
        }
    }

    private var foreground: Color {
        switch tone {
        case .info:    return FitColors.Blue.b500
        case .success: return FitColors.Teal.t500
        case .warning: return FitColors.Yellow.y400
        case .error:   return FitColors.Red.r400
        case .brand:   return FitColors.brandPrimary
        case .neutral: return theme.textSecondary
        }
    }
}
