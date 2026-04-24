import SwiftUI

// MARK: - FitSnackbar
//
// Bottom pill notification with optional action. Auto-dismiss timer.
// See `docs/components.md`.

public struct FitSnackbar: View {
    @Binding var isVisible: Bool
    let message: String
    let actionLabel: String?
    let actionCallback: (() -> Void)?
    let duration: Double
    let showDot: Bool

    public init(
        isVisible: Binding<Bool>,
        message: String,
        actionLabel: String? = nil,
        actionCallback: (() -> Void)? = nil,
        duration: Double = 4.0,
        showDot: Bool = false
    ) {
        self._isVisible = isVisible
        self.message = message
        self.actionLabel = actionLabel
        self.actionCallback = actionCallback
        self.duration = duration
        self.showDot = showDot
    }

    public var body: some View {
        if isVisible {
            HStack(spacing: FitSpacing.sp3) {
                if showDot {
                    Circle()
                        .fill(FitColors.Yellow.y400)
                        .frame(width: FitSize.snackbarDot, height: FitSize.snackbarDot)
                }

                Text(message)
                    .font(FitFont.body2)
                    .foregroundColor(.white)
                    .lineLimit(2)

                if let label = actionLabel, let cb = actionCallback {
                    Spacer(minLength: FitSpacing.sp2)
                    Button(label, action: cb)
                        .font(.custom(FitFont.family, size: 14).weight(.medium))
                        .foregroundColor(FitColors.Teal.t500)
                        .buttonStyle(.plain)
                }
            }
            .padding(.horizontal, FitSpacing.sp4)
            .padding(.vertical, 10)
            .background(FitColors.Gray.black)
            .overlay(
                Capsule().stroke(Color.white.opacity(0.08), lineWidth: 1)
            )
            .clipShape(Capsule())
            .transition(.asymmetric(
                insertion: .opacity.combined(with: .offset(y: 8)),
                removal: .opacity
            ))
            .task {
                try? await Task.sleep(nanoseconds: UInt64(duration * 1_000_000_000))
                withAnimation(.easeInOut(duration: 0.2)) { isVisible = false }
            }
        }
    }
}
