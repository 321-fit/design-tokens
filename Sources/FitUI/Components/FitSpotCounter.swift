import SwiftUI

// MARK: - FitSpotCounter
//
// Group-training capacity bar — 12px tall fill bar with proportional
// teal fill and centered label "X of Y spots". Supports a compact
// label-less variant for tight cards. See `docs/components.md`
// § FitSpotCounter.

public struct FitSpotCounter: View {
    let available: Int
    let total: Int
    let showLabel: Bool
    let compact: Bool

    @Environment(\.fitTheme) private var theme

    public init(
        available: Int,
        total: Int,
        showLabel: Bool = true,
        compact: Bool = false
    ) {
        self.available = max(0, available)
        self.total = max(1, total)
        self.showLabel = showLabel
        self.compact = compact
    }

    public var body: some View {
        ZStack(alignment: .leading) {
            // Track
            RoundedRectangle(cornerRadius: FitRadius.md)
                .fill(theme.surfaceHigh)

            // Fill
            GeometryReader { proxy in
                let ratio = max(0, min(1, Double(available) / Double(total)))
                RoundedRectangle(cornerRadius: FitRadius.md)
                    .fill(FitColors.Teal.t500)
                    .frame(width: proxy.size.width * ratio)
            }

            if showLabel {
                Text(label)
                    .font(.custom(FitFont.family, size: 11).weight(.medium))
                    .foregroundColor(theme.textPrimary)
                    .frame(maxWidth: .infinity, alignment: .center)
            }
        }
        .frame(height: compact ? 8 : 12)
    }

    private var label: String {
        "\(available) of \(total) spots"
    }
}
