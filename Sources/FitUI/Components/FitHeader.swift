import SwiftUI

// MARK: - FitHeader
// Back button (glass circle) + title + optional trailing action
// Used on every screen

public struct FitHeader<Trailing: View>: View {
    let title: String
    var onBack: (() -> Void)? = nil
    let subtitle: String?
    let onTitleClick: (() -> Void)?
    let leading: AnyView?
    let transparent: Bool
    let trailing: Trailing

    @Environment(\.fitTheme) private var theme
    @Environment(\.dismiss) private var dismiss

    public init(
        _ title: String,
        onBack: (() -> Void)? = nil,
        subtitle: String? = nil,
        onTitleClick: (() -> Void)? = nil,
        leading: AnyView? = nil,
        transparent: Bool = false,
        @ViewBuilder trailing: () -> Trailing = { EmptyView() }
    ) {
        self.title = title
        self.onBack = onBack
        self.subtitle = subtitle
        self.onTitleClick = onTitleClick
        self.leading = leading
        self.transparent = transparent
        self.trailing = trailing()
    }

    public var body: some View {
        HStack(spacing: FitSpacing.sp3) {
            // A caller-built leading wins over the back button: a header starts with a close,
            // a cancel or a step counter often enough that screens were rebuilding the whole
            // bar to change one corner.
            if let leading = leading {
                leading
            } else {
                backButton
            }
            // The title is a block rather than a line: a chat header carries who you are
            // talking to and whether they are online, and a tap that opens a profile has to
            // cover both lines.
            titleBlock
                .frame(maxWidth: .infinity, alignment: .leading)

            // Trailing action (e.g., trash icon)
            trailing
        }
        .padding(.horizontal, FitSpacing.sp5)
        .padding(.vertical, FitSpacing.sp2)
        // Transparent for a header that floats over media — a cover photo, a video. The
        // opaque plate is right everywhere else and stays the default.
        .background(transparent ? Color.clear : theme.screenBg)
    }

    @ViewBuilder
    private var titleBlock: some View {
        let block = VStack(alignment: .leading, spacing: 2) {
            Text(title)
                .font(FitFont.h2)
                .foregroundColor(theme.textPrimary)
            if let subtitle = subtitle, !subtitle.isEmpty {
                Text(subtitle)
                    .font(FitFont.caption)
                    .foregroundColor(theme.textTertiary)
            }
        }
        if let onTitleClick = onTitleClick {
            Button(action: onTitleClick) { block }.buttonStyle(.plain)
        } else {
            block
        }
    }

    private var backButton: some View {
        Button(action: { onBack?() ?? dismiss() }) {
            Image(systemName: "chevron.left")
                .font(.system(size: 14, weight: .medium))
                .foregroundColor(theme.textPrimary)
                .frame(width: 32, height: 32)
                .background(.ultraThinMaterial)
                .clipShape(Circle())
        }
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
