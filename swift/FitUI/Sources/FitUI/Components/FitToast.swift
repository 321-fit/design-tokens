import SwiftUI

// MARK: - FitToast
// Top of screen, informational
// Radius: 12px, Padding: 12px, Font: 13px

public enum FitToastStyle {
    case success  // green icon + left border
    case error    // red icon + left border
    case info     // blue icon + left border
}

public struct FitToast: View {
    let message: String
    let style: FitToastStyle
    @Binding var isVisible: Bool

    @Environment(\.fitTheme) private var theme

    public init(_ message: String, style: FitToastStyle, isVisible: Binding<Bool>) {
        self.message = message
        self.style = style
        self._isVisible = isVisible
    }

    public var body: some View {
        if isVisible {
            HStack(spacing: FitSpacing.sp3) {
                Image(systemName: iconName)
                    .font(.system(size: FitSize.toastIcon))
                    .foregroundColor(accentColor)

                Text(message)
                    .font(FitFont.caption)
                    .foregroundColor(theme.textPrimary)
            }
            .padding(FitSpacing.sp3)
            .frame(maxWidth: .infinity, alignment: .leading)
            .background(theme.surfaceHigh)
            .clipShape(RoundedRectangle(cornerRadius: FitRadius.md))
            .overlay(
                HStack {
                    Rectangle()
                        .fill(accentColor)
                        .frame(width: 3)
                    Spacer()
                }
                .clipShape(RoundedRectangle(cornerRadius: FitRadius.md))
            )
            .shadow(color: .black.opacity(0.3), radius: 8, y: 4)
            .padding(.horizontal, FitSpacing.sp5)
            .transition(.move(edge: .top).combined(with: .opacity))
            .onAppear {
                DispatchQueue.main.asyncAfter(deadline: .now() + 3) {
                    withAnimation { isVisible = false }
                }
            }
        }
    }

    private var accentColor: Color {
        switch style {
        case .success: return FitColors.success
        case .error: return FitColors.error
        case .info: return FitColors.brandPrimary
        }
    }

    private var iconName: String {
        switch style {
        case .success: return "checkmark"
        case .error: return "xmark"
        case .info: return "info"
        }
    }
}
