import SwiftUI

// MARK: - FitCalEvent
//
// Colored block representing an event on a timeline. Personal vs
// Group vs External vs Cross-role × 6 statuses, with adaptive 3-tier
// layout based on tile height. See `docs/components.md`.
//
// Layout: positioned absolutely on parent timeline with
// `top = startMinute * pxPerMin`, `height = durationMin * pxPerMin`.
// A 3px hairline gap is reserved at the bottom of every tile so
// back-to-back events don't visually merge (Apple Calendar style).

public enum FitCalEventType {
    case personal
    case group
    case external
    case crossRole(FitRole)     // user's own event from the OTHER role
}

public enum FitCalEventStatus {
    case planned
    case request
    case awaiting
    case review
    case missed
    case finished
}

/// Layout density derived from tile height.
public enum FitCalEventTier {
    case tiny       // ≤30pt — title · start-time inline
    case compact    // 31-45pt — title / recipient · time
    case standard   // ≥46pt — title / recipient · time / location

    public static func from(height: CGFloat) -> FitCalEventTier {
        if height <= 30 { return .tiny }
        if height <= 45 { return .compact }
        return .standard
    }
}

public struct FitCalEvent: View {
    let title: String
    let recipient: String?
    let time: String
    let location: String?
    let type: FitCalEventType
    let status: FitCalEventStatus
    let height: CGFloat
    let onTap: () -> Void

    @Environment(\.fitTheme) private var theme

    public init(
        title: String,
        recipient: String? = nil,
        time: String,
        location: String? = nil,
        type: FitCalEventType,
        status: FitCalEventStatus = .planned,
        height: CGFloat,
        onTap: @escaping () -> Void = {}
    ) {
        self.title = title
        self.recipient = recipient
        self.time = time
        self.location = location
        self.type = type
        self.status = status
        self.height = height
        self.onTap = onTap
    }

    private var tier: FitCalEventTier { .from(height: height) }
    private var isCrossRole: Bool {
        if case .crossRole = type { return true }
        return false
    }

    public var body: some View {
        // Outer container keeps the full inline height; visible card sits inside
        // with `.padding(.bottom, 3)` to reserve the hairline gap.
        ZStack(alignment: .topLeading) {
            card
                .padding(.bottom, 3)

            // Cross-role role-tag anchored bottom-right of the OUTER (so it
            // sits inside the inset visible card area, not over the gap).
            if case let .crossRole(role) = type, tier != .tiny {
                FitRoleTag(role: role)
                    .padding(.trailing, 8)
                    .padding(.bottom, 9)  // 3 (gap) + 6 (visual inset)
                    .frame(maxWidth: .infinity, maxHeight: .infinity, alignment: .bottomTrailing)
            }
        }
        .frame(height: height)
        .opacity(outerOpacity)
        .contentShape(Rectangle())
        .onTapGesture(perform: onTap)
    }

    // MARK: - Visible card

    @ViewBuilder
    private var card: some View {
        ZStack(alignment: .topLeading) {
            // Left accent stripe (3pt). Dashed for cross-role; solid for others.
            stripe

            // Content
            content
                .padding(.horizontal, 12)
                .padding(.vertical, tier == .tiny ? 4 : 8)
                .padding(.leading, 4) // breathing room past the 3pt stripe
        }
        .frame(maxWidth: .infinity, maxHeight: .infinity, alignment: .topLeading)
        .background(cardBackground)
        .overlay(borderOverlay)
        .clipShape(RoundedRectangle(cornerRadius: FitRadius.md))
    }

    @ViewBuilder
    private var stripe: some View {
        switch type {
        case .crossRole:
            // Dashed vertical line, 3pt wide
            DashedVerticalStripe(color: theme.textTertiary)
                .frame(width: 3)
                .frame(maxHeight: .infinity)
        default:
            Rectangle()
                .fill(leftAccentColor)
                .frame(width: 3)
                .frame(maxHeight: .infinity)
        }
    }

    @ViewBuilder
    private var content: some View {
        switch tier {
        case .tiny:
            HStack(spacing: 6) {
                Text(title)
                    .font(.custom(FitFont.family, size: 10).weight(.medium))
                    .foregroundColor(titleColor)
                    .lineLimit(1)
                    .truncationMode(.tail)
                Text("·")
                    .font(.custom(FitFont.family, size: 10))
                    .foregroundColor(theme.textTertiary)
                Text(time)
                    .font(.custom(FitFont.family, size: 10))
                    .foregroundColor(theme.textTertiary)
                    .lineLimit(1)
                Spacer(minLength: 0)
                if let pillText = pillText, let pillStatus = pillStatus {
                    FitCalEventPill(text: pillText, status: pillStatus)
                }
            }
        case .compact:
            VStack(alignment: .leading, spacing: 2) {
                titleRow
                Text(metaText)
                    .font(.custom(FitFont.family, size: 12))
                    .foregroundColor(theme.textSecondary)
                    .lineLimit(1)
            }
        case .standard:
            VStack(alignment: .leading, spacing: 2) {
                titleRow
                Text(metaText)
                    .font(.custom(FitFont.family, size: 12))
                    .foregroundColor(theme.textSecondary)
                    .lineLimit(1)
                if let location = location {
                    HStack(spacing: 4) {
                        Image(systemName: "mappin.and.ellipse")
                            .font(.system(size: 10))
                            .foregroundColor(theme.textTertiary)
                        Text(location)
                            .font(.custom(FitFont.family, size: 11))
                            .foregroundColor(theme.textTertiary)
                            .lineLimit(1)
                    }
                }
            }
        }
    }

    @ViewBuilder
    private var titleRow: some View {
        HStack(alignment: .center, spacing: 6) {
            Text(title)
                .font(.custom(FitFont.family, size: 12).weight(.medium))
                .foregroundColor(titleColor)
                .lineLimit(1)
                .truncationMode(.tail)
            if let pillText = pillText, let pillStatus = pillStatus {
                FitCalEventPill(text: pillText, status: pillStatus)
            }
            Spacer(minLength: 0)
        }
    }

    // MARK: - Visual derivations

    private var metaText: String {
        if let recipient = recipient {
            return "\(recipient) · \(time)"
        }
        return time
    }

    private var outerOpacity: Double {
        if status == .finished { return 0.5 }
        switch type {
        case .external:    return 0.7
        case .crossRole:   return 0.75
        default:           return 1.0
        }
    }

    private var titleColor: Color {
        switch type {
        case .external, .crossRole: return theme.textPrimary
        default:                    return theme.textPrimary
        }
    }

    private var leftAccentColor: Color {
        switch type {
        case .personal:    return FitColors.Teal.t500
        case .group:       return FitColors.brandPrimary
        case .external:    return theme.textTertiary
        case .crossRole:   return theme.textTertiary    // dashed, color used by DashedVerticalStripe
        }
    }

    private var cardBackground: Color {
        switch (type, status) {
        case (_, .request):           return FitColors.Yellow.y600.opacity(0.10)
        case (_, .review):            return FitColors.Yellow.y600.opacity(0.10)
        case (_, .missed):            return FitColors.error.opacity(0.10)
        case (.external, _):          return theme.surfaceHigher
        case (.crossRole, _):         return theme.surfaceHigh
        case (.group, _):             return FitColors.brandPrimary.opacity(0.12)
        case (.personal, _):          return theme.surfaceHigh
        }
    }

    @ViewBuilder
    private var borderOverlay: some View {
        let color: Color = {
            switch status {
            case .request, .review: return FitColors.Yellow.y600
            case .awaiting:         return theme.textTertiary
            case .missed:           return FitColors.error
            default:                return Color.clear
            }
        }()
        if color != .clear {
            RoundedRectangle(cornerRadius: FitRadius.md)
                .stroke(color, lineWidth: 1)
        }
    }

    private var pillText: String? {
        // Cross-role tiles hide status pill — actions belong to the other role.
        if case .crossRole = type { return nil }
        switch status {
        case .request:  return "Request"
        case .review:   return "Review"
        case .awaiting: return "Awaiting"
        case .missed:   return "Missed"
        default:        return nil
        }
    }

    private var pillStatus: FitCalEventPillStatus? {
        if case .crossRole = type { return nil }
        switch status {
        case .request:  return .request
        case .review:   return .review
        case .awaiting: return .awaiting
        case .missed:   return .missed
        default:        return nil
        }
    }
}

// MARK: - Dashed stripe helper

private struct DashedVerticalStripe: View {
    let color: Color
    var body: some View {
        GeometryReader { geo in
            Path { p in
                p.move(to: CGPoint(x: 1.5, y: 0))
                p.addLine(to: CGPoint(x: 1.5, y: geo.size.height))
            }
            .stroke(
                color,
                style: StrokeStyle(lineWidth: 3, lineCap: .butt, dash: [3, 3])
            )
        }
    }
}
