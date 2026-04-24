import SwiftUI

// MARK: - FitAvatar
//
// User representation circle — initials or image — in 5 sizes.
// See `docs/components.md` FitAvatar.

public enum FitAvatarSize {
    case xs   // 24 — inline chip
    case sm   // 32 — compact list
    case md   // 40 — standard list (default)
    case lg   // 48 — event sheet / card
    case xl   // 80 — profile hero

    var px: CGFloat {
        switch self {
        case .xs: return FitSize.avatarXs
        case .sm: return FitSize.avatarSm
        case .md: return FitSize.avatarMd
        case .lg: return FitSize.avatarLg
        case .xl: return FitSize.avatarXl
        }
    }

    var fontSize: CGFloat {
        switch self {
        case .xs: return 10
        case .sm: return 12
        case .md: return 14
        case .lg: return 16
        case .xl: return 28
        }
    }
}

public enum FitAvatarBg {
    case brand          // brand gradient — default for active
    case gray           // surface-higher — muted (deleted, archived)
    case custom(Color)  // override (e.g. sport type color)
}

public enum FitAvatarShape {
    case circle       // default
    case rect10       // rounded rect (icon placeholders in skeletons, session templates)
}

public struct FitAvatar: View {
    let initials: String
    let size: FitAvatarSize
    let bg: FitAvatarBg
    let shape: FitAvatarShape
    let image: URL?
    let isPaid: Bool

    @Environment(\.fitTheme) private var theme

    public init(
        initials: String,
        size: FitAvatarSize = .md,
        bg: FitAvatarBg = .brand,
        shape: FitAvatarShape = .circle,
        image: URL? = nil,
        isPaid: Bool = false
    ) {
        self.initials = String(initials.prefix(2)).uppercased()
        self.size = size
        self.bg = bg
        self.shape = shape
        self.image = image
        self.isPaid = isPaid
    }

    public var body: some View {
        content
            .frame(width: size.px, height: size.px)
            .background(background)
            .clipShape(shapeView)
            .opacity(isPaid ? 0.5 : 1.0)
    }

    @ViewBuilder
    private var content: some View {
        if let url = image {
            AsyncImage(url: url) { phase in
                switch phase {
                case .success(let img):
                    img.resizable().scaledToFill()
                default:
                    initialsLabel
                }
            }
        } else {
            initialsLabel
        }
    }

    private var initialsLabel: some View {
        Text(initials)
            .font(.custom(FitFont.family, size: size.fontSize).weight(.medium))
            .foregroundColor(.white)
    }

    @ViewBuilder
    private var background: some View {
        switch bg {
        case .brand:            FitColors.brandGradient
        case .gray:             theme.surfaceHigher
        case .custom(let c):    c
        }
    }

    @ViewBuilder
    private var shapeView: some Shape {
        switch shape {
        case .circle:   AnyShape(Circle())
        case .rect10:   AnyShape(RoundedRectangle(cornerRadius: 10))
        }
    }
}

// AnyShape wrapper (for Shape type erasure pre-iOS 16; SwiftUI has native AnyShape in iOS 16+)
private struct AnyShape: Shape {
    private let pathFn: (CGRect) -> Path
    init<S: Shape>(_ shape: S) { pathFn = { shape.path(in: $0) } }
    func path(in rect: CGRect) -> Path { pathFn(rect) }
}
