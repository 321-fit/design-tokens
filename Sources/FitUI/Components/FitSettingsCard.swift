import SwiftUI

// MARK: - FitSettingsCard
// Padding: 12px, Radius: 16px, Icon: 24px, Chevron: 16px
// Dark bg: #2B2E31, Light bg: #FFFFFF

public struct FitSettingsCard<Icon: View>: View {
    let title: String
    var subtitle: String? = nil
    let icon: Icon
    var showChevron: Bool = true
    var action: (() -> Void)? = nil

    @Environment(\.fitTheme) private var theme

    public init(
        _ title: String,
        subtitle: String? = nil,
        showChevron: Bool = true,
        @ViewBuilder icon: () -> Icon,
        action: (() -> Void)? = nil
    ) {
        self.title = title
        self.subtitle = subtitle
        self.showChevron = showChevron
        self.icon = icon()
        self.action = action
    }

    public var body: some View {
        Button(action: { action?() }) {
            HStack(spacing: FitSpacing.sp3) {
                icon
                    .frame(width: FitSize.settingsCardIcon, height: FitSize.settingsCardIcon)
                    .foregroundColor(theme.textPrimary)

                VStack(alignment: .leading, spacing: FitSpacing.sp1) {
                    Text(title)
                        .font(FitFont.body1)
                        .foregroundColor(theme.textPrimary)
                        .lineLimit(1)

                    if let subtitle {
                        Text(subtitle)
                            .font(FitFont.body2)
                            .foregroundColor(theme.textSecondary)
                            .lineLimit(2)
                    }
                }
                .frame(maxWidth: .infinity, alignment: .leading)

                if showChevron {
                    Image(systemName: "chevron.right")
                        .font(.system(size: 12, weight: .medium))
                        .foregroundColor(theme.textTertiary)
                }
            }
            .padding(FitSpacing.sp3)
            .background(theme.cardBg)
            .clipShape(RoundedRectangle(cornerRadius: FitRadius.settingsCard))
        }
        .buttonStyle(.plain)
    }
}
