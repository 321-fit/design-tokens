import SwiftUI

// MARK: - FitStatStrip
//
// Horizontal 4-column readout used on coach profile (athlete-view and
// coach-view). Columns are uniformly spaced with vertical dividers.
// One column may be visually accented (e.g. price) via `.accent` tone.
// See `docs/components.md` § FitStatStrip.

public struct FitStatStrip: View {
    /// Which way a column reads. `.accent` is the good number (a price, a total earned);
    /// `.warning` / `.danger` are the ones the reader owes something about — a debt in
    /// brand green says "well done" about money the coach has not been paid.
    public enum Tone {
        case neutral, accent, warning, danger
    }

    public struct Item {
        public let value: String
        public let label: String
        public let accent: Bool
        public let tone: Tone

        public init(value: String, label: String, accent: Bool = false, tone: Tone? = nil) {
            self.value = value
            self.label = label
            self.accent = accent
            self.tone = tone ?? (accent ? .accent : .neutral)
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
                .foregroundColor(toneColor(item.tone))
                .lineLimit(1)

            Text(item.label)
                .font(FitFont.caption)
                .foregroundColor(theme.textTertiary)
                .lineLimit(1)
        }
    }
}

private extension FitStatStrip {
    func toneColor(_ tone: Tone) -> Color {
        switch tone {
        case .accent: return FitColors.Teal.t500
        case .warning: return FitColors.Yellow.y400
        case .danger: return FitColors.Red.r400
        case .neutral: return theme.textPrimary
        }
    }
}
