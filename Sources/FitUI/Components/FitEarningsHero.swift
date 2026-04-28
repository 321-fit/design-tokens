import SwiftUI

// MARK: - FitEarningsHero
//
// Balance / earnings hero card — large amount + period label,
// optional trend pill, optional 60/40 breakdown grid, optional
// "planned" footer line, and an empty-state shape. Used at the
// top of balance.html and the coach earnings dashboard.
// See `docs/components.md` § FitEarningsHero.

public enum FitEarningsTrend {
    case up(percent: Double)
    case down(percent: Double)
    case flat
}

public struct FitEarningsHeroBreakdown {
    public let label: String
    public let value: String

    public init(label: String, value: String) {
        self.label = label
        self.value = value
    }
}

public struct FitEarningsHero: View {
    let amount: String
    let period: String
    let trend: FitEarningsTrend?
    let breakdown: [FitEarningsHeroBreakdown]
    let planned: String?
    let isEmpty: Bool

    @Environment(\.fitTheme) private var theme

    public init(
        amount: String,
        period: String,
        trend: FitEarningsTrend? = nil,
        breakdown: [FitEarningsHeroBreakdown] = [],
        planned: String? = nil,
        isEmpty: Bool = false
    ) {
        self.amount = amount
        self.period = period
        self.trend = trend
        self.breakdown = breakdown
        self.planned = planned
        self.isEmpty = isEmpty
    }

    public var body: some View {
        VStack(alignment: .leading, spacing: FitSpacing.sp3) {
            // Header
            HStack(alignment: .firstTextBaseline) {
                VStack(alignment: .leading, spacing: 2) {
                    Text(isEmpty ? "—" : amount)
                        .font(.custom(FitFont.family, size: 28).weight(.semibold))
                        .foregroundColor(theme.textPrimary)
                    Text(period)
                        .font(.custom(FitFont.family, size: 13))
                        .foregroundColor(theme.textTertiary)
                }
                Spacer()
                if let trend, !isEmpty {
                    trendPill(trend)
                }
            }

            if !breakdown.isEmpty && !isEmpty {
                Rectangle()
                    .fill(theme.divider)
                    .frame(height: 1)

                HStack(alignment: .top, spacing: FitSpacing.sp3) {
                    if breakdown.count >= 1 {
                        breakdownColumn(breakdown[0])
                            .frame(maxWidth: .infinity, alignment: .leading)
                            .layoutPriority(60)
                    }
                    if breakdown.count >= 2 {
                        breakdownColumn(breakdown[1])
                            .frame(maxWidth: .infinity, alignment: .leading)
                            .layoutPriority(40)
                    }
                }
            }

            if let planned, !isEmpty {
                Text(planned)
                    .font(.custom(FitFont.family, size: 13))
                    .foregroundColor(theme.textTertiary)
            }
        }
        .padding(FitSpacing.sp4)
        .frame(maxWidth: .infinity, alignment: .leading)
        .background(theme.surfaceHigh)
        .clipShape(RoundedRectangle(cornerRadius: FitRadius.lg))
    }

    private func breakdownColumn(_ row: FitEarningsHeroBreakdown) -> some View {
        VStack(alignment: .leading, spacing: 2) {
            Text(row.label)
                .font(.custom(FitFont.family, size: 13))
                .foregroundColor(theme.textTertiary)
            Text(row.value)
                .font(.custom(FitFont.family, size: 16).weight(.medium))
                .foregroundColor(theme.textPrimary)
        }
    }

    @ViewBuilder
    private func trendPill(_ trend: FitEarningsTrend) -> some View {
        switch trend {
        case .up(let pct):
            FitBadge(format(pct, prefix: "↑ "), style: .success)
        case .down(let pct):
            FitBadge(format(pct, prefix: "↓ "), style: .danger)
        case .flat:
            FitBadge("—", style: .neutral)
        }
    }

    private func format(_ pct: Double, prefix: String) -> String {
        let abs = abs(pct)
        let str = String(format: "%.0f", abs)
        return "\(prefix)\(str)%"
    }
}
