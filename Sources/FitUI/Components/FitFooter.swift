import SwiftUI

// MARK: - FitFooter
//
// Sticky bottom area wrapper — holds either a single CTA or a navbar.
// Provides consistent padding + safe-area handling. See `docs/components.md`.

public struct FitFooter<Content: View>: View {
    let content: () -> Content
    let topPadding: CGFloat

    @Environment(\.fitTheme) private var theme

    public init(
        topPadding: CGFloat = 12,
        @ViewBuilder content: @escaping () -> Content
    ) {
        self.topPadding = topPadding
        self.content = content
    }

    public var body: some View {
        VStack(spacing: 0) {
            content()
                .padding(.horizontal, FitSpacing.sp4)
                .padding(.top, topPadding)
                .padding(.bottom, FitSpacing.sp8)   // 40pt safe area
                .frame(maxWidth: .infinity)
                .background(theme.screenBg)
        }
    }
}
