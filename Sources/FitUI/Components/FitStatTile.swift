import SwiftUI

// MARK: - FitStatTile
//
// Card-level stat row — leading FitIconPlate + title + subtitle +
// optional chevron. Used pervasively on dashboards (`.dash-action`)
// and balance / earnings summary blocks.
// See `docs/components.md` § FitStatTile.

public struct FitStatTile: View {
    public typealias Tone = FitIconPlate.Tone

    let title: String
    let subtitle: String
    let systemImage: String?
    let tone: Tone
    let showChevron: Bool
    let onTap: (() -> Void)?

    @Environment(\.fitTheme) private var theme

    public init(
        title: String,
        subtitle: String,
        systemImage: String? = nil,
        tone: Tone = .neutral,
        showChevron: Bool = true,
        onTap: (() -> Void)? = nil
    ) {
        self.title = title
        self.subtitle = subtitle
        self.systemImage = systemImage
        self.tone = tone
        self.showChevron = showChevron
        self.onTap = onTap
    }

    public var body: some View {
        let row = HStack(spacing: FitSpacing.sp3) {
            if let systemImage {
                FitIconPlate(systemImage, tone: tone, size: .md)
            }

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

            if showChevron {
                Image(systemName: "chevron.right")
                    .font(.system(size: 12, weight: .medium))
                    .foregroundColor(theme.textTertiary)
                    .frame(width: 18, height: 18)
            }
        }
        .padding(.horizontal, 14)
        .padding(.vertical, FitSpacing.sp3)
        .background(theme.surfaceHigh)
        .overlay(
            RoundedRectangle(cornerRadius: FitRadius.lg)
                .stroke(theme.divider, lineWidth: 1)
        )
        .clipShape(RoundedRectangle(cornerRadius: FitRadius.lg))

        if let onTap {
            Button(action: onTap) { row }.buttonStyle(.plain)
        } else {
            row
        }
    }
}
