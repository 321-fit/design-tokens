import SwiftUI

// MARK: - FitAvatar
//
// User representation circle — initials or image — in 5 sizes, with an optional
// corner badge. See `docs/components.md` FitAvatar.

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

// What the chip in an avatar's bottom corner offers. A closed set rather than a content
// slot — every screen that hand-rolled this pattern wanted one of these two.
public enum FitAvatarBadge {
    case none
    case edit
    case add
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
    let badge: FitAvatarBadge

    @Environment(\.fitTheme) private var theme

    public init(
        initials: String,
        size: FitAvatarSize = .md,
        bg: FitAvatarBg = .brand,
        shape: FitAvatarShape = .circle,
        image: URL? = nil,
        isPaid: Bool = false,
        badge: FitAvatarBadge = .none
    ) {
        self.initials = String(initials.prefix(2)).uppercased()
        self.size = size
        self.bg = bg
        self.shape = shape
        self.image = image
        self.isPaid = isPaid
        self.badge = badge
    }

    public var body: some View {
        // The badge sits outside the clip shape: inside it, the circle would shave the
        // chip's corner off.
        ZStack(alignment: .bottomTrailing) {
            content
                .frame(width: size.px, height: size.px)
                .background(background)
                .clipShape(shapeView)
                .opacity(isPaid ? 0.5 : 1.0)
            badgeChip
        }
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
    private var badgeChip: some View {
        switch badge {
        case .none:
            EmptyView()
        case .edit, .add:
            ZStack {
                Circle().fill(theme.screenBg)
                Circle().fill(theme.surfaceHigh).padding(2)
                Image(systemName: badge == .edit ? "pencil" : "plus")
                    .font(.system(size: 12, weight: .medium))
                    .foregroundColor(theme.textPrimary)
            }
            .frame(width: 28, height: 28)
        }
    }

    private var shapeView: some Shape {
        // Both branches type-erase to AnyShape so the opaque return type is
        // satisfied. @ViewBuilder cannot apply here — Shape isn't a View,
        // and the implicit `_ConditionalContent` it would synthesise won't
        // conform to Shape. Explicit `return` keeps Swift 5.7-compatible.
        switch shape {
        case .circle:
            return AnyShape(Circle())
        case .rect10:
            return AnyShape(RoundedRectangle(cornerRadius: 10))
        }
    }
}

// AnyShape wrapper (for Shape type erasure pre-iOS 16; SwiftUI has native AnyShape in iOS 16+)
private struct AnyShape: Shape {
    private let pathFn: (CGRect) -> Path
    init<S: Shape>(_ shape: S) { pathFn = { shape.path(in: $0) } }
    func path(in rect: CGRect) -> Path { pathFn(rect) }
}
