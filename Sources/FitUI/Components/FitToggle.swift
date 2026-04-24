import SwiftUI

// MARK: - FitToggle
//
// iOS-style on/off switch (48×28). Teal-500 when on. See `docs/components.md`.

public struct FitToggle: View {
    @Binding var isOn: Bool
    let label: String?
    let disabled: Bool

    @Environment(\.fitTheme) private var theme

    public init(
        isOn: Binding<Bool>,
        label: String? = nil,
        disabled: Bool = false
    ) {
        self._isOn = isOn
        self.label = label
        self.disabled = disabled
    }

    public var body: some View {
        HStack(spacing: FitSpacing.sp3) {
            if let label = label {
                Text(label)
                    .font(FitFont.body1)
                    .foregroundColor(theme.textPrimary)
                Spacer()
            }
            track
        }
        .opacity(disabled ? 0.6 : 1.0)
        .contentShape(Rectangle())
        .onTapGesture {
            if !disabled {
                withAnimation(.easeInOut(duration: 0.2)) { isOn.toggle() }
            }
        }
    }

    private var track: some View {
        ZStack(alignment: isOn ? .trailing : .leading) {
            Capsule()
                .fill(isOn ? FitColors.Teal.t500 : theme.surfaceHigher)
                .frame(width: FitSize.toggleWidth, height: FitSize.toggleHeight)

            Circle()
                .fill(Color.white)
                .frame(width: FitSize.toggleThumb, height: FitSize.toggleThumb)
                .padding(3)
        }
    }
}
