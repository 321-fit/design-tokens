import SwiftUI

// MARK: - FitMaturityProgress
//
// "You're a new coach" progress block — leading icon plate + title +
// subtitle + checklist of graduation criteria + Learn-more link.
// Auto-hide policy is the caller's: render only while
// `reviewsCount < 1 OR sessionsCount < 3` (see memory
// `project_coach_maturity`).
// See `docs/components.md` § FitMaturityProgress.

public struct FitMaturityProgress: View {
    public struct Criterion {
        public let label: String
        public let done: Bool

        public init(label: String, done: Bool) {
            self.label = label
            self.done = done
        }
    }

    let title: String
    let subtitle: String
    let criteria: [Criterion]
    let learnMoreLabel: String
    let onLearnMore: (() -> Void)?

    @Environment(\.fitTheme) private var theme

    public init(
        title: String,
        subtitle: String,
        criteria: [Criterion],
        learnMoreLabel: String = "Learn more",
        onLearnMore: (() -> Void)? = nil
    ) {
        self.title = title
        self.subtitle = subtitle
        self.criteria = criteria
        self.learnMoreLabel = learnMoreLabel
        self.onLearnMore = onLearnMore
    }

    public var body: some View {
        VStack(alignment: .leading, spacing: 0) {
            HStack(spacing: 10) {
                FitIconPlate("star.fill", tone: .success, size: .md)
                Text(title)
                    .font(.custom(FitFont.family, size: 15).weight(.medium))
                    .foregroundColor(theme.textPrimary)
            }
            .padding(.bottom, FitSpacing.sp2)

            Text(subtitle)
                .font(.custom(FitFont.family, size: 13))
                .foregroundColor(theme.textSecondary)
                .fixedSize(horizontal: false, vertical: true)
                .padding(.bottom, 14)

            VStack(alignment: .leading, spacing: FitSpacing.sp2) {
                ForEach(Array(criteria.enumerated()), id: \.offset) { _, c in
                    criterionRow(c)
                }
            }
            .padding(.bottom, 14)

            if let onLearnMore {
                Button(action: onLearnMore) {
                    HStack(spacing: 4) {
                        Text(learnMoreLabel)
                            .font(.custom(FitFont.family, size: 13).weight(.medium))
                            .foregroundColor(FitColors.brandPrimary)
                        Image(systemName: "chevron.right")
                            .font(.system(size: 11, weight: .semibold))
                            .foregroundColor(FitColors.brandPrimary)
                    }
                }
                .buttonStyle(.plain)
            }
        }
        .padding(FitSpacing.sp4)
        .overlay(
            RoundedRectangle(cornerRadius: 14)
                .stroke(theme.divider, lineWidth: 1)
        )
    }

    @ViewBuilder
    private func criterionRow(_ c: Criterion) -> some View {
        HStack(spacing: 10) {
            check(done: c.done)
            Text(c.label)
                .font(.custom(FitFont.family, size: 13))
                .foregroundColor(c.done ? theme.textTertiary : theme.textSecondary)
                .strikethrough(c.done, color: theme.divider)
        }
    }

    @ViewBuilder
    private func check(done: Bool) -> some View {
        ZStack {
            if done {
                Circle().fill(FitColors.Teal.t500)
                Image(systemName: "checkmark")
                    .font(.system(size: 9, weight: .bold))
                    .foregroundColor(.white)
            } else {
                Circle().stroke(theme.divider, lineWidth: 1.5)
            }
        }
        .frame(width: 18, height: 18)
    }
}
