import SwiftUI

// MARK: - FitPaymentMethodCard
//
// Provider selector / connected-method card — leading 40×40 logo
// container + title + subtitle (with optional status dot) + trailing
// button or "Coming soon" badge. Used in payment / payout / balance
// settings. See `docs/components.md` § FitPaymentMethodCard.

public enum FitPaymentMethodStatus {
    case connected
    case actionRequired
    case notConnected
    case comingSoon
}

public struct FitPaymentMethodCard: View {
    let logoLetter: String
    let title: String
    let subtitle: String
    let status: FitPaymentMethodStatus
    let actionLabel: String?
    let onAction: (() -> Void)?
    let disabled: Bool
    let onTap: (() -> Void)?

    @Environment(\.fitTheme) private var theme

    public init(
        logoLetter: String,
        title: String,
        subtitle: String,
        status: FitPaymentMethodStatus = .notConnected,
        actionLabel: String? = nil,
        onAction: (() -> Void)? = nil,
        disabled: Bool = false,
        onTap: (() -> Void)? = nil
    ) {
        self.logoLetter = logoLetter
        self.title = title
        self.subtitle = subtitle
        self.status = status
        self.actionLabel = actionLabel
        self.onAction = onAction
        self.disabled = disabled
        self.onTap = onTap
    }

    public var body: some View {
        let row = HStack(spacing: FitSpacing.sp3) {
            // Logo container
            ZStack {
                RoundedRectangle(cornerRadius: FitRadius.sm)
                    .fill(theme.surfaceHigher)
                Text(String(logoLetter.prefix(1)).uppercased())
                    .font(.custom(FitFont.family, size: 18).weight(.semibold))
                    .foregroundColor(theme.textPrimary)
            }
            .frame(width: 40, height: 40)

            VStack(alignment: .leading, spacing: 2) {
                Text(title)
                    .font(.custom(FitFont.family, size: 15).weight(.medium))
                    .foregroundColor(theme.textPrimary)
                    .lineLimit(1)

                HStack(spacing: FitSpacing.sp1) {
                    if let dot = statusDotColor {
                        Circle()
                            .fill(dot)
                            .frame(width: 6, height: 6)
                    }
                    Text(subtitle)
                        .font(.custom(FitFont.family, size: 13))
                        .foregroundColor(theme.textTertiary)
                        .lineLimit(1)
                }
            }
            .frame(maxWidth: .infinity, alignment: .leading)

            trailing
        }
        .padding(.horizontal, 14)
        .padding(.vertical, FitSpacing.sp3)
        .background(theme.surfaceHigh)
        .clipShape(RoundedRectangle(cornerRadius: FitRadius.md))
        .opacity(disabled ? 0.6 : 1.0)

        if let onTap, !disabled {
            Button(action: onTap) { row }.buttonStyle(.plain)
        } else {
            row
        }
    }

    @ViewBuilder
    private var trailing: some View {
        switch status {
        case .comingSoon:
            FitBadge("Coming soon", style: .neutral)
        case .connected, .actionRequired, .notConnected:
            if let label = actionLabel, let onAction {
                FitButton(label, style: .secondary, size: .sm, action: onAction)
                    .fixedSize()
            }
        }
    }

    private var statusDotColor: Color? {
        switch status {
        case .connected:      return FitColors.Teal.t500
        case .actionRequired: return FitColors.Yellow.y400
        case .notConnected, .comingSoon: return nil
        }
    }
}
