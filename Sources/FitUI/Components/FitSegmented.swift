import SwiftUI

// MARK: - FitSegmented
//
// iOS-native segmented control / tab switcher. Equal-width buttons inside a
// "selection well" container; tapping a button slides selection to it.
//
// Use for navigation-style switching of screen modes / filters (e.g. Archived
// vs Blocked tabs, or any 2-3 way view filter). Always single-select. For
// form-input chip groups use `FitSelectionGroup` instead — the visual
// contract differs (chip border on selection vs slid pill in a well).
//
// See `docs/components.md` § FitSegmented and prototype:
// project-spec/prototypes/flows/coach/clients.html (`#s-archived`).

public struct FitSegmented<Option: Hashable>: View {

    // MARK: Stored

    private let options: [Option]
    private let label: (Option) -> String
    private let count: ((Option) -> Int?)?
    @Binding private var selection: Option

    // MARK: Init

    public init(
        options: [Option],
        selection: Binding<Option>,
        label: @escaping (Option) -> String,
        count: ((Option) -> Int?)? = nil
    ) {
        self.options = options
        self._selection = selection
        self.label = label
        self.count = count
    }

    // MARK: Body

    public var body: some View {
        HStack(spacing: 0) {
            ForEach(options, id: \.self) { option in
                tab(for: option)
            }
        }
        .padding(FitSpacing.sp1)
        .background(FitTheme.surfaceHigh)
        .clipShape(RoundedRectangle(cornerRadius: FitRadius.md))
    }

    // MARK: Tab

    @ViewBuilder
    private func tab(for option: Option) -> some View {
        let selected = (selection == option)
        Button(action: { selection = option }) {
            Text(displayText(for: option))
                .font(FitFont.button2)
                .foregroundStyle(selected ? FitTheme.textOnBrand : FitTheme.textSecondary)
                .frame(maxWidth: .infinity)
                .padding(.vertical, FitSpacing.sp2)
                .background(tabBackground(selected: selected))
                .clipShape(RoundedRectangle(cornerRadius: FitRadius.sm))
                .animation(.easeInOut(duration: 0.15), value: selected)
        }
        .buttonStyle(.plain)
    }

    @ViewBuilder
    private func tabBackground(selected: Bool) -> some View {
        if selected {
            FitColors.selectionGradient
        } else {
            Color.clear
        }
    }

    // MARK: Helpers

    private func displayText(for option: Option) -> String {
        guard let count, let n = count(option) else {
            return label(option)
        }
        return "\(label(option)) (\(n))"
    }
}
