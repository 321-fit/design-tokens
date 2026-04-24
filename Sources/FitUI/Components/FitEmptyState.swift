import SwiftUI

// MARK: - FitEmptyState
//
// Centered placeholder for empty lists — illustration + title +
// subtitle + optional CTA. See `docs/components.md`.

public struct FitEmptyState: View {
    let title: String
    let subtitle: String
    let systemImage: String?
    let customImage: Image?
    let actionLabel: String?
    let action: (() -> Void)?

    @Environment(\.fitTheme) private var theme

    public init(
        title: String,
        subtitle: String,
        systemImage: String,
        actionLabel: String? = nil,
        action: (() -> Void)? = nil
    ) {
        self.title = title
        self.subtitle = subtitle
        self.systemImage = systemImage
        self.customImage = nil
        self.actionLabel = actionLabel
        self.action = action
    }

    public init(
        title: String,
        subtitle: String,
        image: Image,
        actionLabel: String? = nil,
        action: (() -> Void)? = nil
    ) {
        self.title = title
        self.subtitle = subtitle
        self.systemImage = nil
        self.customImage = image
        self.actionLabel = actionLabel
        self.action = action
    }

    public var body: some View {
        VStack(spacing: 0) {
            illustration
                .frame(width: 40, height: 40)
                .foregroundColor(theme.textTertiary)
                .padding(.bottom, FitSpacing.sp4)

            Text(title)
                .font(.custom(FitFont.family, size: 16).weight(.medium))
                .foregroundColor(theme.textSecondary)
                .multilineTextAlignment(.center)
                .padding(.bottom, FitSpacing.sp2)

            Text(subtitle)
                .font(FitFont.body2)
                .foregroundColor(theme.textTertiary)
                .multilineTextAlignment(.center)
                .padding(.bottom, FitSpacing.sp5)

            if let label = actionLabel, let action = action {
                FitButton(label, style: .primary, size: .md, action: action)
                    .fixedSize(horizontal: true, vertical: false)
            }
        }
        .padding(.horizontal, FitSpacing.sp7)
        .padding(.vertical, FitSpacing.sp7)
        .frame(maxWidth: .infinity)
    }

    @ViewBuilder
    private var illustration: some View {
        if let name = systemImage {
            Image(systemName: name)
                .resizable()
                .scaledToFit()
        } else if let custom = customImage {
            custom
                .resizable()
                .scaledToFit()
        }
    }
}
