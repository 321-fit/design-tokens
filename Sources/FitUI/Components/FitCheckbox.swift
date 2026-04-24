import SwiftUI

// MARK: - FitCheckbox
//
// Square 28×28 checkbox with on/off toggle. See `docs/components.md`.

public struct FitCheckbox: View {
    @Binding var checked: Bool
    let label: String?
    let disabled: Bool

    @Environment(\.fitTheme) private var theme

    public init(
        checked: Binding<Bool>,
        label: String? = nil,
        disabled: Bool = false
    ) {
        self._checked = checked
        self.label = label
        self.disabled = disabled
    }

    public var body: some View {
        Button(action: { if !disabled { checked.toggle() } }) {
            HStack(spacing: FitSpacing.sp3) {
                ZStack {
                    RoundedRectangle(cornerRadius: 6)
                        .fill(checked ? FitColors.Teal.t600 : Color.clear)
                    RoundedRectangle(cornerRadius: 6)
                        .stroke(checked ? FitColors.Teal.t600 : theme.textTertiary, lineWidth: 2)
                    if checked {
                        Image(systemName: "checkmark")
                            .font(.system(size: 14, weight: .bold))
                            .foregroundColor(.white)
                    }
                }
                .frame(width: FitSize.checkboxSize, height: FitSize.checkboxSize)
                .opacity(disabled ? 0.5 : 1.0)

                if let label = label {
                    Text(label)
                        .font(FitFont.body1)
                        .foregroundColor(theme.textPrimary)
                        .opacity(disabled ? 0.5 : 1.0)
                }
            }
        }
        .buttonStyle(.plain)
        .disabled(disabled)
    }
}
