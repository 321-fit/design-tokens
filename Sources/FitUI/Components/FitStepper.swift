import SwiftUI

// MARK: - FitStepper
//
// Number control with ±48pt buttons + bounded value + press-and-hold repeat.
// See `docs/components.md`.

public struct FitStepper: View {
    @Binding var value: Int
    let min: Int
    let max: Int
    let unit: String?

    @Environment(\.fitTheme) private var theme

    public init(
        value: Binding<Int>,
        min: Int = 1,
        max: Int = 100,
        unit: String? = nil
    ) {
        self._value = value
        self.min = min
        self.max = max
        self.unit = unit
    }

    public var body: some View {
        HStack(spacing: 0) {
            button(symbol: "minus", enabled: value > min) {
                if value > min { value -= 1 }
            }

            ZStack {
                Rectangle().fill(theme.surfaceHigh)
                Text(displayValue)
                    .font(FitFont.button1)
                    .foregroundColor(theme.textPrimary)
            }
            .frame(maxWidth: .infinity)
            .frame(height: FitSize.stepperHeight)

            button(symbol: "plus", enabled: value < max) {
                if value < max { value += 1 }
            }
        }
        .clipShape(RoundedRectangle(cornerRadius: FitRadius.md))
    }

    private var displayValue: String {
        guard let unit = unit else { return "\(value)" }
        return "\(value) \(unit)"
    }

    @ViewBuilder
    private func button(symbol: String, enabled: Bool, action: @escaping () -> Void) -> some View {
        Button(action: action) {
            ZStack {
                Rectangle().fill(theme.surfaceHigher)
                Image(systemName: symbol)
                    .font(.system(size: 16, weight: .medium))
                    .foregroundColor(theme.textPrimary)
            }
            .frame(width: FitSize.stepperButton, height: FitSize.stepperHeight)
            .opacity(enabled ? 1.0 : 0.4)
        }
        .buttonStyle(.plain)
        .disabled(!enabled)
    }
}
