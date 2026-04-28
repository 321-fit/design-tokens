import SwiftUI

// MARK: - FitChip
//
// Tag-like selectable button. Single or multi-select mode.
// See `docs/components.md` § FitChip.

public enum FitChipSize {
    case sm   // h=40 — compact, e.g. inline filter row
    case md   // h=48 — default form chip
    case lg   // h=56 — large prominent toggle (rare; equal to FitInput height)

    var height: CGFloat {
        switch self {
        case .sm: return 40
        case .md: return 48
        case .lg: return 56
        }
    }
}

public struct FitChip: View {
    let label: String
    let systemImage: String?
    let size: FitChipSize
    @Binding var isSelected: Bool
    let action: () -> Void

    @Environment(\.fitTheme) private var theme

    public init(
        _ label: String,
        systemImage: String? = nil,
        size: FitChipSize = .md,
        isSelected: Binding<Bool>,
        action: @escaping () -> Void
    ) {
        self.label = label
        self.systemImage = systemImage
        self.size = size
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
                    .font(font)
            }
            .foregroundColor(theme.textPrimary)
            .padding(.horizontal, FitSpacing.sp3)
            .frame(height: size.height)
            .frame(maxWidth: .infinity)
            .background(chipBackground)
            .overlay(chipBorder)
            .clipShape(RoundedRectangle(cornerRadius: FitRadius.md))
        }
        .buttonStyle(.plain)
    }

    private var font: Font {
        switch size {
        case .sm: return FitFont.body2
        case .md: return FitFont.body1
        case .lg: return FitFont.body1
        }
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
