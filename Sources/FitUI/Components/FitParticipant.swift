import SwiftUI

// MARK: - FitParticipant
//
// List row for a participant — leading (avatar/icon) + name + subtitle +
// trailing (chevron / edit / dot / swipe / none) + state-tinted appearance.
// See `docs/components.md` § FitParticipant.
//
// The original avatar + remove + isPaid + isYou + payment API is preserved
// via default values so existing call sites compile unchanged. New call
// sites should prefer the explicit `leading` / `trailing` / `state` enums.

public enum FitParticipantPayment {
    case cash
    case card

    /// Covered by a session-pack credit. Its own state rather than "paid": the coach has
    /// to be able to tell money in their hand from a credit that was spent.
    case pack
    case none
}

/// Leading visual. `.avatar` is the canonical participant case;
/// `.icon` is for non-user rows (calendars, integrations) reusing the
/// participant layout.
public enum FitParticipantLeading {
    case avatar(initials: String, bg: FitAvatarBg = .brand)
    case icon(systemName: String, tone: FitIconPlate.Tone = .neutral)
}

/// Trailing element. Defaults to `.chevron` so existing call sites that
/// don't specify a trailing element behave the same as before
/// (chevron previously not drawn at all → callers wired the row inside a
/// row with their own external chevron). For brand-new code we recommend
/// passing `.none` explicitly when no trailing affordance is needed.
public enum FitParticipantTrailing {
    case chevron
    case edit
    case dot(color: Color)
    /// Caller wires up SwiftUI `.swipeActions` at the List level.
    /// Provided here only for documentation/parity; the component
    /// itself doesn't render a swipe affordance.
    case swipe
    case none
}

/// Visual state. Drives row tint, name color, and (when relevant) a
/// trailing dot color. `.connected` / `.disconnected` map to the
/// account-link rows in Settings → Accounts.
public enum FitParticipantState {
    case `default`
    case connected
    case disconnected
    case disabled
    case destructive
}

public struct FitParticipant: View {
    let name: String
    let subtitle: String
    let leading: FitParticipantLeading
    let trailing: FitParticipantTrailing
    let state: FitParticipantState
    let isRemovable: Bool
    let onRemove: (() -> Void)?
    let isPaid: Bool
    let payment: FitParticipantPayment
    let isYou: Bool
    let onTap: (() -> Void)?

    @Environment(\.fitTheme) private var theme

    // MARK: - Designated initializer (new API)

    public init(
        name: String,
        subtitle: String,
        leading: FitParticipantLeading,
        trailing: FitParticipantTrailing = .chevron,
        state: FitParticipantState = .default,
        isRemovable: Bool = false,
        onRemove: (() -> Void)? = nil,
        isPaid: Bool = false,
        payment: FitParticipantPayment = .none,
        isYou: Bool = false,
        onTap: (() -> Void)? = nil
    ) {
        self.name = name
        self.subtitle = subtitle
        self.leading = leading
        self.trailing = trailing
        self.state = state
        self.isRemovable = isRemovable
        self.onRemove = onRemove
        self.isPaid = isPaid
        self.payment = payment
        self.isYou = isYou
        self.onTap = onTap
    }

    // MARK: - Legacy initializer (kept for source compatibility)

    /// Pre-extension call signature. Existing call sites continue to work
    /// unchanged: `FitParticipant(name:, subtitle:, avatarInitials:, …)`.
    public init(
        name: String,
        subtitle: String,
        avatarInitials: String,
        isRemovable: Bool = false,
        onRemove: (() -> Void)? = nil,
        isPaid: Bool = false,
        payment: FitParticipantPayment = .none,
        isYou: Bool = false
    ) {
        self.init(
            name: name,
            subtitle: subtitle,
            leading: .avatar(initials: avatarInitials),
            trailing: isRemovable ? .swipe : .none,
            state: .default,
            isRemovable: isRemovable,
            onRemove: onRemove,
            isPaid: isPaid,
            payment: payment,
            isYou: isYou,
            onTap: nil
        )
    }

    public var body: some View {
        let row = HStack(spacing: FitSpacing.sp3) {
            leadingView

            VStack(alignment: .leading, spacing: 2) {
                Text(name)
                    .font(.custom(FitFont.family, size: 14).weight(.medium))
                    .foregroundColor(nameColor)
                    .strikethrough(state == .disabled, color: theme.textTertiary)

                HStack(spacing: FitSpacing.sp1) {
                    Text(subtitle)
                        .font(FitFont.caption)
                        .foregroundColor(subtitleColor)

                    if case .cash = payment {
                        FitBadge("Cash", style: .neutral)
                    } else if case .card = payment {
                        FitBadge("Card", style: .neutral)
                    } else if case .pack = payment {
                        FitBadge("Pack", style: .neutral)
                    }
                }
            }

            Spacer(minLength: 0)

            trailingView
        }
        .padding(.vertical, FitSpacing.sp3)
        .padding(.horizontal, isYou ? FitSpacing.sp3 : 0)
        .background(rowBackground)
        .clipShape(RoundedRectangle(cornerRadius: rowCornerRadius))
        .opacity(state == .disabled ? 0.6 : 1.0)

        if let onTap = onTap, state != .disabled {
            Button(action: onTap) { row }.buttonStyle(.plain)
        } else {
            row
        }
    }

    // MARK: - Leading

    @ViewBuilder
    private var leadingView: some View {
        switch leading {
        case .avatar(let initials, let bg):
            FitAvatar(initials: initials, size: .lg, bg: bg, isPaid: isPaid)
        case .icon(let systemName, let tone):
            FitIconPlate(systemName, tone: tone, size: .lg)
        }
    }

    // MARK: - Trailing

    @ViewBuilder
    private var trailingView: some View {
        // Legacy path: explicit remove button takes precedence over trailing enum
        if isRemovable, let onRemove = onRemove {
            Button(action: onRemove) {
                ZStack {
                    Circle().fill(FitColors.error.opacity(0.1))
                    Image(systemName: "xmark")
                        .font(.system(size: 12, weight: .semibold))
                        .foregroundColor(FitColors.error)
                }
                .frame(width: 28, height: 28)
            }
            .buttonStyle(.plain)
        } else {
            switch trailing {
            case .chevron:
                Image(systemName: "chevron.right")
                    .font(.system(size: 12, weight: .medium))
                    .foregroundColor(theme.textTertiary)
            case .edit:
                Image(systemName: "pencil")
                    .font(.system(size: 14, weight: .medium))
                    .foregroundColor(theme.textTertiary)
            case .dot(let color):
                Circle()
                    .fill(color)
                    .frame(width: 8, height: 8)
            case .swipe, .none:
                EmptyView()
            }
        }
    }

    // MARK: - Tint

    private var nameColor: Color {
        if isPaid { return theme.textSecondary }
        switch state {
        case .destructive: return FitColors.error
        case .disabled:    return theme.textTertiary
        default:           return theme.textPrimary
        }
    }

    private var subtitleColor: Color {
        if isYou { return FitColors.brandPrimary }
        switch state {
        case .destructive: return FitColors.error.opacity(0.85)
        default:           return theme.textSecondary
        }
    }

    @ViewBuilder
    private var rowBackground: some View {
        if isYou {
            FitColors.selectionGradient
        } else {
            switch state {
            case .connected:    theme.bgSuccessSubtle
            case .disconnected: theme.bgWarningSubtle
            case .destructive:  theme.bgErrorSubtle
            default:            Color.clear
            }
        }
    }

    private var rowCornerRadius: CGFloat {
        if isYou { return FitRadius.md }
        switch state {
        case .connected, .disconnected, .destructive: return FitRadius.md
        default: return 0
        }
    }
}
