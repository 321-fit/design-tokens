import SwiftUI

// MARK: - FitParticipant
//
// List row for a participant — avatar + name + subtitle + optional
// remove button + paid/you states. Designed to nest in FitCard or
// standalone list. Swipe actions recommended via SwiftUI
// `.swipeActions` at the List level. See `docs/components.md`.

public enum FitParticipantPayment {
    case cash
    case card
    case none
}

public struct FitParticipant: View {
    let name: String
    let subtitle: String
    let avatarInitials: String
    let isRemovable: Bool
    let onRemove: (() -> Void)?
    let isPaid: Bool
    let payment: FitParticipantPayment
    let isYou: Bool

    @Environment(\.fitTheme) private var theme

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
        self.name = name
        self.subtitle = subtitle
        self.avatarInitials = avatarInitials
        self.isRemovable = isRemovable
        self.onRemove = onRemove
        self.isPaid = isPaid
        self.payment = payment
        self.isYou = isYou
    }

    public var body: some View {
        HStack(spacing: FitSpacing.sp3) {
            FitAvatar(initials: avatarInitials, size: .lg, isPaid: isPaid)

            VStack(alignment: .leading, spacing: 2) {
                Text(name)
                    .font(.custom(FitFont.family, size: 14).weight(.medium))
                    .foregroundColor(isPaid ? theme.textSecondary : theme.textPrimary)

                HStack(spacing: FitSpacing.sp1) {
                    Text(subtitle)
                        .font(FitFont.caption)
                        .foregroundColor(isYou ? FitColors.brandPrimary : theme.textSecondary)

                    if case .cash = payment {
                        FitBadge("Cash", style: .neutral)
                    } else if case .card = payment {
                        FitBadge("Card", style: .neutral)
                    }
                }
            }

            Spacer()

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
            }
        }
        .padding(.vertical, FitSpacing.sp3)
        .padding(.horizontal, isYou ? FitSpacing.sp3 : 0)
        .background(isYou ? FitColors.selectionGradient : nil)
        .clipShape(RoundedRectangle(cornerRadius: isYou ? FitRadius.md : 0))
    }
}
