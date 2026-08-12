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
            // The bar itself is FitProgressBar: this component adds capacity semantics
            // and the centred label on top, and must not carry a second copy of it.
            FitProgressBar(
                progress: Double(available) / Double(total),
                height: compact ? 8 : 12
            )

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
