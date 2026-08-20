import SwiftUI

// MARK: - FitActionCircle
//
// A row of peer actions on an object — a sheet, a drawer, a request card.
// 64pt circle with the label beneath it. See `docs/components.md` § FitActionCircle.
//
// WHEN TO USE IT, AND WHEN NOT. Peer actions *on an object* take circles. A
// question with an escape — "Disconnect / Cancel", "Cancel / Save" — takes words,
// because there the labels are the meaning and one of the two is the way out of
// the question. Count does not decide it: two peers are still peers, which is why
// an Awaiting row is two circles while Disconnect / Cancel stays two buttons.
//
// COLOUR IS RATIONED, and that is the rule rather than a preference: exactly one
// `.primary` (the expected answer, under the thumb), at most one `.danger`
// (destructive, farthest from it). A third fill turns a row of actions into a row
// of warnings. Positions are fixed so the row becomes muscle memory instead of a
// read: refusal left, neutral middle, expected answer right.

public enum FitActionCircleRole {
    /// Neutral peer — the plate sits one surface step above whatever it is on, so
    /// it stays a plate on a card as well as on the page.
    case neutral
    /// The expected answer. One per row.
    case primary
    /// Destructive. FILLED red with a white glyph — it has to read as destructive
    /// before the icon is identified, which is the whole job of that colour. White
    /// on `#F05C5B` measures 3.29:1 — under the 4.5 a label would need, over the
    /// 3.0 a glyph needs, and a glyph is what sits inside. The word lives beneath
    /// the circle, on the page background, where it has full contrast.
    case danger
    /// Deliberation rather than a decision — outline only, reserved for the
    /// assistant. A third fill colour would flatten the row's hierarchy.
    case ask
}

public struct FitActionCircle: View {
    let systemName: String
    let label: String
    let role: FitActionCircleRole
    let isEnabled: Bool
    let action: () -> Void

    @Environment(\.fitTheme) private var theme

    public init(
        systemName: String,
        label: String,
        role: FitActionCircleRole = .neutral,
        isEnabled: Bool = true,
        action: @escaping () -> Void
    ) {
        self.systemName = systemName
        self.label = label
        self.role = role
        self.isEnabled = isEnabled
        self.action = action
    }

    public var body: some View {
        Button(action: action) {
            VStack(spacing: 9) {
                ZStack {
                    plate
                    Image(systemName: systemName)
                        .font(.system(size: 26, weight: .medium))
                        .foregroundColor(glyph)
                }
                .frame(width: 64, height: 64)

                Text(label)
                    .font(.system(size: 13, weight: .semibold))
                    .foregroundColor(caption)
            }
            .frame(maxWidth: .infinity)
        }
        .buttonStyle(.plain)
        .disabled(!isEnabled)
        .opacity(isEnabled ? 1 : 0.4)
        .accessibilityLabel(label)
    }

    @ViewBuilder private var plate: some View {
        switch role {
        case .primary:
            Circle().fill(FitColors.brandGradient)
        case .danger:
            Circle().fill(FitColors.Red.r400)
        case .ask:
            Circle().strokeBorder(FitColors.Teal.t500, lineWidth: 1.5)
        case .neutral:
            Circle().fill(theme.surfaceHigher)
        }
    }

    private var glyph: Color {
        switch role {
        case .primary, .danger: return FitColors.Gray.white
        case .ask:              return FitColors.Teal.t500
        case .neutral:          return theme.textPrimary
        }
    }

    private var caption: Color {
        switch role {
        case .primary: return FitColors.brandPrimary
        case .danger:  return FitColors.Red.r400
        default:       return theme.textPrimary
        }
    }
}

// MARK: - Row
//
// The container exists so the spacing and the equal widths are not re-typed at
// every call site — and so a row that grows a fourth action does not need its
// layout rethought.

public struct FitActionCircles<Content: View>: View {
    let content: Content

    public init(@ViewBuilder content: () -> Content) {
        self.content = content()
    }

    public var body: some View {
        HStack(spacing: 6) {
            content
        }
        .padding(.top, 4)
        .padding(.bottom, 2)
    }
}
