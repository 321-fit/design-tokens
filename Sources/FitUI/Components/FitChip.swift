import SwiftUI

// MARK: - FitChip
//
// Tag-like selectable button. Single or multi-select mode.
// See `docs/components.md`.

public struct FitChip: View {
    let label: String
    let systemImage: String?
    @Binding var isSelected: Bool
    let action: () -> Void

    @Environment(\.fitTheme) private var theme

    public init(
        _ label: String,
        systemImage: String? = nil,
        isSelected: Binding<Bool>,
        action: @escaping () -> Void
    ) {
        self.label = label
        self.systemImage = systemImage
        self._isSelected = isSelected
        self.action = action
    }

    public var body: some View {
        Button(action: {
            isSelected.toggle()
            action()
        }) {
            HStack(spacing: FitSpacing.sp2) {
                if let icon = systemImage {
                    Image(systemName: icon)
                        .font(.system(size: FitSize.iconMd))
                }
                Text(label)
                    .font(FitFont.body1)
            }
            .foregroundColor(theme.textPrimary)
            .padding(.horizontal, FitSpacing.sp3)
            .frame(height: 48)
            .frame(maxWidth: .infinity)
            .background(chipBackground)
            .overlay(chipBorder)
            .clipShape(RoundedRectangle(cornerRadius: FitRadius.md))
        }
        .buttonStyle(.plain)
    }

    @ViewBuilder
    private var chipBackground: some View {
        if isSelected {
            FitColors.selectionGradient
        } else {
            theme.surfaceHigh
        }
    }

    @ViewBuilder
    private var chipBorder: some View {
        RoundedRectangle(cornerRadius: FitRadius.md)
            .stroke(isSelected ? FitColors.selectionBorder : Color.clear, lineWidth: 1)
    }
}
