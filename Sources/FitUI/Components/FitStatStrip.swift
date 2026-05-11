import SwiftUI

// MARK: - FitStatStrip
//
// Horizontal 4-column readout used on coach profile (athlete-view and
// coach-view). Columns are uniformly spaced with vertical dividers.
// One column may be visually accented (e.g. price) via `.accent` tone.
// See `docs/components.md` § FitStatStrip.

public struct FitStatStrip: View {
    public struct Item {
        public let value: String
        public let label: String
        public let accent: Bool

        public init(value: String, label: String, accent: Bool = false) {
            self.value = value
            self.label = label
            self.accent = accent
        }
    }

    let items: [Item]

    @Environment(\.fitTheme) private var theme

    public init(_ items: [Item]) {
        self.items = items
    }

    public var body: some View {
        HStack(spacing: 0) {
            ForEach(Array(items.enumerated()), id: \.offset) { index, item in
                column(item)
                    .frame(maxWidth: .infinity)

                if index < items.count - 1 {
                    Rectangle()
                        .fill(theme.divider)
                        .frame(width: 1)
                }
            }
        }
        .padding(.horizontal, FitSpacing.sp4)
        .padding(.vertical, FitSpacing.sp3)
        .background(
            RoundedRectangle(cornerRadius: 12)
                .fill(theme.surfaceHigh)
        )
    }

    @ViewBuilder
    private func column(_ item: Item) -> some View {
        VStack(spacing: 2) {
            Text(item.value)
                .font(.custom(FitFont.family, size: 18).weight(.semibold))
                .foregroundColor(item.accent ? FitColors.Teal.t500 : theme.textPrimary)
                .lineLimit(1)

            Text(item.label)
                .font(FitFont.caption)
                .foregroundColor(theme.textTertiary)
                .lineLimit(1)
        }
    }
}
