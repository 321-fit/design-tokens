import SwiftUI

// MARK: - FitCalEvent
//
// Colored block representing an event on a timeline. Personal vs
// Group vs External types × 6 statuses. See `docs/components.md`.
//
// Layout: positioned absolutely on parent timeline with
// `top = startMinute * pxPerMin`, `height = durationMin * pxPerMin`.

public enum FitCalEventType {
    case personal
    case group
    case external
}

public enum FitCalEventStatus {
    case planned
    case request
    case awaiting
    case review
    case missed
    case finished
}

public struct FitCalEvent: View {
    let title: String
    let time: String
    let type: FitCalEventType
    let status: FitCalEventStatus
    let isTiny: Bool
    let badge: String?
    let rescheduled: Bool
    let onTap: () -> Void

    @Environment(\.fitTheme) private var theme

    public init(
        title: String,
        time: String,
        type: FitCalEventType,
        status: FitCalEventStatus = .planned,
        isTiny: Bool = false,
        badge: String? = nil,
        rescheduled: Bool = false,
        onTap: @escaping () -> Void = {}
    ) {
        self.title = title
        self.time = time
        self.type = type
        self.status = status
        self.isTiny = isTiny
        self.badge = badge
        self.rescheduled = rescheduled
        self.onTap = onTap
    }

    public var body: some View {
        Button(action: onTap) {
            ZStack(alignment: .topLeading) {
                // Left accent stripe (3pt)
                Rectangle()
                    .fill(leftAccentColor)
                    .frame(width: 3)
                    .frame(maxHeight: .infinity)

                VStack(alignment: .leading, spacing: 2) {
                    HStack(alignment: .center, spacing: FitSpacing.sp1) {
                        Text(title)
                            .font(.custom(FitFont.family, size: isTiny ? 10 : 12).weight(.medium))
                            .foregroundColor(titleColor)
                            .lineLimit(1)
                            .truncationMode(.tail)

                        if rescheduled {
                            Image(systemName: "arrow.triangle.2.circlepath")
                                .font(.system(size: 9))
                                .foregroundColor(titleColor.opacity(0.8))
                        }

                        Spacer()

                        if let pillText = pillText {
                            FitCalEventPill(text: pillText, status: pillStatus)
                        }
                    }

                    if !isTiny {
                        Text(time)
                            .font(.custom(FitFont.family, size: 12))
                            .foregroundColor(theme.textSecondary)
                            .lineLimit(1)
                    }

                    if let badge = badge, !isTiny {
                        Text(badge)
                            .font(.custom(FitFont.family, size: 10).weight(.medium))
                            .foregroundColor(.white)
                            .padding(.horizontal, 6)
                            .padding(.vertical, 1)
                            .background(FitColors.brandPrimary)
                            .clipShape(Capsule())
                    }
                }
                .padding(.horizontal, isTiny ? 8 : 12)
                .padding(.vertical, isTiny ? 1 : 8)
                .padding(.leading, 8)   // space for left stripe
            }
            .background(backgroundColor)
            .overlay(borderOverlay)
            .clipShape(RoundedRectangle(cornerRadius: FitRadius.md))
        }
        .buttonStyle(.plain)
        .opacity(status == .finished ? 0.5 : (type == .external ? 0.7 : 1.0))
    }

    // MARK: - Status visual derivations

    private var titleColor: Color {
        switch type {
        case .external: return theme.textSecondary
        default:        return theme.textPrimary
        }
    }

    private var leftAccentColor: Color {
        switch (type, status) {
        case (.personal, _): return FitColors.Teal.t500
        case (.group, _):    return FitColors.brandPrimary
        case (.external, _): return theme.textTertiary
        }
    }

    private var backgroundColor: Color {
        switch (type, status) {
        case (_, .request):  return FitColors.Yellow.y600.opacity(0.10)
        case (_, .review):   return FitColors.Yellow.y600.opacity(0.10)
        case (_, .missed):   return FitColors.error.opacity(0.10)
        case (.external, _): return theme.surfaceHigher
        default:             return theme.surfaceHigh
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
        switch status {
        case .request:  return "Request"
        case .review:   return "Review"
        case .awaiting: return "Awaiting"
        case .missed:   return "Missed"
        default:        return nil
        }
    }

    private var pillStatus: FitCalEventPillStatus {
        switch status {
        case .request:  return .request
        case .review:   return .review
        case .awaiting: return .awaiting
        case .missed:   return .missed
        default:        return .request  // unreachable
        }
    }
}
