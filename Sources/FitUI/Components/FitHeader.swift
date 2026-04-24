import SwiftUI

// MARK: - FitHeader
// Back button (glass circle) + title + optional trailing action
// Used on every screen

public struct FitHeader<Trailing: View>: View {
    let title: String
    var onBack: (() -> Void)? = nil
    let trailing: Trailing

    @Environment(\.fitTheme) private var theme
    @Environment(\.dismiss) private var dismiss

    public init(_ title: String, onBack: (() -> Void)? = nil, @ViewBuilder trailing: () -> Trailing = { EmptyView() }) {
        self.title = title
        self.onBack = onBack
        self.trailing = trailing()
    }

    public var body: some View {
        HStack(spacing: FitSpacing.sp3) {
            // Back button
            Button(action: { onBack?() ?? dismiss() }) {
                Image(systemName: "chevron.left")
                    .font(.system(size: 14, weight: .medium))
                    .foregroundColor(theme.textPrimary)
                    .frame(width: 32, height: 32)
                    .background(.ultraThinMaterial)
                    .clipShape(Circle())
            }

            // Title
            Text(title)
                .font(FitFont.h2)
                .foregroundColor(theme.textPrimary)
                .frame(maxWidth: .infinity, alignment: .leading)

            // Trailing action (e.g., trash icon)
            trailing
        }
        .padding(.horizontal, FitSpacing.sp5)
        .padding(.vertical, FitSpacing.sp2)
    }
}

// MARK: - Destructive header button (trash icon)

public struct FitDestructiveHeaderButton: View {
    let action: () -> Void

    public init(action: @escaping () -> Void) {
        self.action = action
    }

    public var body: some View {
        Button(action: action) {
            Image(systemName: "trash")
                .font(.system(size: 14))
                .foregroundColor(FitColors.error)
                .frame(width: 32, height: 32)
                .background(FitColors.error.opacity(0.1))
                .clipShape(Circle())
        }
    }
}
