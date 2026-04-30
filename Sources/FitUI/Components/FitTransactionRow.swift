import SwiftUI

// MARK: - FitTransactionRow
//
// Balance / payouts list row — leading FitIconPlate keyed to
// transaction type, title + subtitle, trailing amount in a
// type-driven color. See `docs/components.md` § FitTransactionRow.

public enum FitTransactionType {
    /// Athlete top-up / coach earning credit (incoming).
    case earning
    /// Coach payout (outgoing to bank).
    case payout
    /// Refund to athlete (out for coach, in for athlete).
    case refund
}

public struct FitTransactionRow: View {
    let type: FitTransactionType
    let title: String
    let subtitle: String
    let amount: String
    let date: String?
    let isPending: Bool
    let onTap: (() -> Void)?

    @Environment(\.fitTheme) private var theme

    public init(
        type: FitTransactionType,
        title: String,
        subtitle: String,
        amount: String,
        date: String? = nil,
        isPending: Bool = false,
        onTap: (() -> Void)? = nil
    ) {
        self.type = type
        self.title = title
        self.subtitle = subtitle
        self.amount = amount
        self.date = date
        self.isPending = isPending
        self.onTap = onTap
    }

    public var body: some View {
        let row = HStack(spacing: FitSpacing.sp3) {
            FitIconPlate(iconName, tone: iconTone, size: .md)

            VStack(alignment: .leading, spacing: 2) {
                Text(title)
                    .font(.custom(FitFont.family, size: 15).weight(.medium))
                    .foregroundColor(theme.textPrimary)
                    .lineLimit(1)

                Text(subtitle)
                    .font(.custom(FitFont.family, size: 13))
                    .foregroundColor(theme.textTertiary)
                    .lineLimit(1)
            }
            .frame(maxWidth: .infinity, alignment: .leading)

            VStack(alignment: .trailing, spacing: 2) {
                Text(amount)
                    .font(.custom(FitFont.family, size: 15).weight(.medium))
                    .foregroundColor(amountColor)
                if let date {
                    Text(date)
                        .font(.custom(FitFont.family, size: 11))
                        .foregroundColor(theme.textTertiary)
                }
            }
        }
        .padding(.vertical, FitSpacing.sp3)

        if let onTap {
            Button(action: onTap) { row }.buttonStyle(.plain)
        } else {
            row
        }
    }

    // MARK: - Tokens

    private var iconName: String {
        switch type {
        case .earning: return "arrow.up.right"
        case .payout:  return "arrow.right"
        case .refund:  return "arrow.uturn.left"
        }
    }

    private var iconTone: FitIconPlate.Tone {
        switch type {
        case .earning, .payout: return .success
        case .refund:           return .error
        }
    }

    private var amountColor: Color {
        if isPending { return theme.textTertiary }
        switch type {
        case .earning, .payout: return FitColors.Teal.t500
        case .refund:           return FitColors.Red.r400
        }
    }
}
