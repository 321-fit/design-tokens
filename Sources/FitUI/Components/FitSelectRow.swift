import SwiftUI

// MARK: - FitSelectRow
// Full-width tappable row with selection gradient + checkmark
// Used for: calendar toggle, sport selection
// Padding: 10px, Radius: 8px, Dot: 10px, Check: 22px

public struct FitSelectRow: View {
    let title: String
    var dotColor: Color? = nil
    let isSelected: Bool
    let action: () -> Void

    @Environment(\.fitTheme) private var theme

    public init(_ title: String, dotColor: Color? = nil, isSelected: Bool, action: @escaping () -> Void) {
        self.title = title
        self.dotColor = dotColor
        self.isSelected = isSelected
        self.action = action
    }

    public var body: some View {
        Button(action: action) {
            HStack(spacing: FitSpacing.sp3) {
                if let dotColor {
                    Circle()
                        .fill(dotColor)
                        .frame(width: FitSize.selectRowDot, height: FitSize.selectRowDot)
                }

                Text(title)
                    .font(.custom(FitFont.family, size: 15))
                    .foregroundColor(theme.textPrimary)
                    .frame(maxWidth: .infinity, alignment: .leading)

                // Checkmark
                if isSelected {
                    RoundedRectangle(cornerRadius: FitRadius.badge)
                        .fill(FitColors.selectionBorder)
                        .frame(width: FitSize.selectRowCheck, height: FitSize.selectRowCheck)
                        .overlay(
                            Image(systemName: "checkmark")
                                .font(.system(size: 10, weight: .bold))
                                .foregroundColor(.white)
                        )
                }
            }
            .padding(FitSpacing.sp3)
            .background(
                isSelected
                    ? AnyShapeStyle(FitColors.selectionGradient)
                    : AnyShapeStyle(theme.surfaceHigher)
            )
            .clipShape(RoundedRectangle(cornerRadius: FitRadius.selectRow))
            .overlay(
                RoundedRectangle(cornerRadius: FitRadius.selectRow)
                    .stroke(isSelected ? FitColors.selectionBorder : .clear, lineWidth: 1)
            )
        }
        .buttonStyle(.plain)
    }
}
