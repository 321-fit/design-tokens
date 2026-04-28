import SwiftUI

// MARK: - FitSettingsCard
//
// Settings row with leading icon plate + title + optional subtitle + trailing
// chevron / "Default" badge. The `context` enum styles the leading icon
// container (settings = bare icon; location/space = neutral icon plate so
// rows in non-settings sections share the visual weight of FitParticipant).
// See `docs/components.md` § FitSettingsCard.

public enum FitSettingsCardContext {
    /// Stock Settings list — bare icon, theme.cardBg row.
    case settings
    /// Location entry (e.g. saved address). Same visual as `.settings`,
    /// but reserves an `addressOrSubtitle` slot for ellipsised single-line
    /// subtitle and may show a "Default" badge.
    case location
    /// Space (e.g. workout space, gym). Mirrors `.location`.
    case space
}

public struct FitSettingsCard<Icon: View>: View {
    let title: String
    let subtitle: String?
    let addressOrSubtitle: String?
    let icon: Icon
    let context: FitSettingsCardContext
    let isDefault: Bool
    let showChevron: Bool
    let action: (() -> Void)?

    @Environment(\.fitTheme) private var theme

    public init(
        _ title: String,
        subtitle: String? = nil,
        addressOrSubtitle: String? = nil,
        context: FitSettingsCardContext = .settings,
        isDefault: Bool = false,
        showChevron: Bool = true,
        @ViewBuilder icon: () -> Icon,
        action: (() -> Void)? = nil
    ) {
        self.title = title
        self.subtitle = subtitle
        self.addressOrSubtitle = addressOrSubtitle
        self.context = context
        self.isDefault = isDefault
        self.showChevron = showChevron
        self.icon = icon()
        self.action = action
    }

    public var body: some View {
        Button(action: { action?() }) {
            HStack(spacing: FitSpacing.sp3) {
                iconContainer

                VStack(alignment: .leading, spacing: FitSpacing.sp1) {
                    HStack(spacing: FitSpacing.sp2) {
                        Text(title)
                            .font(FitFont.body1)
                            .foregroundColor(theme.textPrimary)
                            .lineLimit(1)

                        if isDefault {
                            FitBadge("Default", style: .accent)
                        }
                    }

                    if let line = effectiveSubtitle {
                        Text(line)
                            .font(FitFont.body2)
                            .foregroundColor(theme.textSecondary)
                            .lineLimit(context == .settings ? 2 : 1)
                            .truncationMode(.tail)
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

    private var effectiveSubtitle: String? {
        switch context {
        case .location, .space: return addressOrSubtitle ?? subtitle
        case .settings:         return subtitle
        }
    }

    @ViewBuilder
    private var iconContainer: some View {
        switch context {
        case .settings:
            icon
                .frame(width: FitSize.settingsCardIcon, height: FitSize.settingsCardIcon)
                .foregroundColor(theme.textPrimary)
        case .location, .space:
            ZStack {
                RoundedRectangle(cornerRadius: FitRadius.sm)
                    .fill(theme.surfaceHigher)
                icon
                    .foregroundColor(theme.textPrimary)
            }
            .frame(width: 40, height: 40)
        }
    }
}
