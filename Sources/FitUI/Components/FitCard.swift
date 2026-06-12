import SwiftUI

// MARK: - FitCard
//
// Content container for grouped information. Optional header with
// edit action. Edge: light = soft drop shadow (no border); dark = surface
// bg on a darker screen, tonal contrast only (no border). Keeping dark
// borderless avoids a hard hairline flashing on theme toggle.
// See `docs/components.md`.

public struct FitCard<Content: View>: View {
    let title: String?
    let onEdit: (() -> Void)?
    let content: () -> Content

    @Environment(\.fitTheme) private var theme

    public init(
        title: String? = nil,
        onEdit: (() -> Void)? = nil,
        @ViewBuilder content: @escaping () -> Content
    ) {
        self.title = title
        self.onEdit = onEdit
        self.content = content
    }

    public var body: some View {
        VStack(alignment: .leading, spacing: FitSpacing.sp3) {
            if let title = title {
                HStack {
                    Text(title)
                        .font(.custom(FitFont.family, size: 18).weight(.medium))
                        .foregroundColor(theme.textPrimary)
                    Spacer()
                    if let edit = onEdit {
                        Button(action: edit) {
                            Image(systemName: "pencil")
                                .font(.system(size: FitSize.iconMd))
                                .foregroundColor(theme.textTertiary)
                        }
                        .buttonStyle(.plain)
                    }
                }
            }
            content()
        }
        .padding(FitSpacing.sp5)
        .frame(maxWidth: .infinity, alignment: .leading)
        .background(theme.cardBg)
        .clipShape(RoundedRectangle(cornerRadius: FitRadius.card))
        .shadow(color: theme.screenBg == Color(hex: "F2F2F7") ? Color.black.opacity(0.07) : .clear,
                radius: 12, x: 0, y: 0)
    }
}

// MARK: - FitCardRow — single info row with icon + text

public struct FitCardRow: View {
    let icon: String
    let text: String

    @Environment(\.fitTheme) private var theme

    public init(icon: String, text: String) {
        self.icon = icon
        self.text = text
    }

    public var body: some View {
        HStack(spacing: FitSpacing.sp2) {
            Image(systemName: icon)
                .font(.system(size: FitSize.iconMd))
                .foregroundColor(theme.textSecondary)
                .frame(width: 16, height: 16)

            Text(text)
                .font(FitFont.body2)
                .foregroundColor(theme.textSecondary)
        }
    }
}
