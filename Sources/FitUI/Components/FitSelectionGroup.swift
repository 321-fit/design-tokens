import SwiftUI

// MARK: - FitSelectionGroup
//
// Equal-width chip group with single-select (radio-like) or multi-select mode.
// Used for: Personal/Group toggle, Recurring/One-off toggle, payment method
// (Cash/Card), online provider (Zoom/Meet/Custom), etc.
//
// Visual: 48pt height chips, equal flex width, gap sp-2, radius-md, brand
// selection gradient on selected, surfaceHigh on unselected, 150ms transition.
//
// See `docs/components.md` and prototype:
// project-spec/prototypes/flows/coach/settings.html (`data-fit-selection`).

public struct FitSelectionGroup<Option: Hashable>: View {

    // MARK: Selection binding (private — disambiguates init by binding shape)

    private enum SelectionBinding {
        case single(Binding<Option?>)
        case multi(Binding<Set<Option>>)
    }

    // MARK: Stored

    private let options: [Option]
    private let label: (Option) -> String
    private let selectionBinding: SelectionBinding

    @Environment(\.fitTheme) private var theme

    // MARK: Init — single-select

    public init(
        options: [Option],
        selection: Binding<Option?>,
        label: @escaping (Option) -> String
    ) {
        self.options = options
        self.label = label
        self.selectionBinding = .single(selection)
    }

    // MARK: Init — multi-select

    public init(
        options: [Option],
        selection: Binding<Set<Option>>,
        label: @escaping (Option) -> String
    ) {
        self.options = options
        self.label = label
        self.selectionBinding = .multi(selection)
    }

    // MARK: Body

    public var body: some View {
        HStack(spacing: FitSpacing.sp2) {
            ForEach(options, id: \.self) { option in
                chip(for: option)
            }
        }
    }

    // MARK: Chip

    @ViewBuilder
    private func chip(for option: Option) -> some View {
        let selected = isSelected(option)
        Button(action: { toggle(option) }) {
            Text(label(option))
                .font(FitFont.body1)
                .foregroundColor(theme.textPrimary)
                .frame(maxWidth: .infinity)
                .frame(height: 48)
                .background(chipBackground(selected: selected))
                .overlay(chipBorder(selected: selected))
                .clipShape(RoundedRectangle(cornerRadius: FitRadius.md))
                .animation(.easeInOut(duration: 0.15), value: selected)
        }
        .buttonStyle(.plain)
    }

    @ViewBuilder
    private func chipBackground(selected: Bool) -> some View {
        if selected {
            FitColors.selectionGradient
        } else {
            theme.surfaceHigh
        }
    }

    @ViewBuilder
    private func chipBorder(selected: Bool) -> some View {
        RoundedRectangle(cornerRadius: FitRadius.md)
            .stroke(
                selected ? FitColors.selectionBorder : Color.clear,
                lineWidth: 1
            )
    }

    // MARK: Selection helpers

    private func isSelected(_ option: Option) -> Bool {
        switch selectionBinding {
        case .single(let binding):
            return binding.wrappedValue == option
        case .multi(let binding):
            return binding.wrappedValue.contains(option)
        }
    }

    private func toggle(_ option: Option) {
        switch selectionBinding {
        case .single(let binding):
            binding.wrappedValue = option
        case .multi(let binding):
            if binding.wrappedValue.contains(option) {
                binding.wrappedValue.remove(option)
            } else {
                binding.wrappedValue.insert(option)
            }
        }
    }
}
