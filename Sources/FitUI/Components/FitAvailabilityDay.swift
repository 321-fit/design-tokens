import SwiftUI

// MARK: - FitAvailabilityDay
//
// Coach availability row for one day-of-week — header (FitCheckbox +
// day name) + collapsible list of time intervals + optional add /
// remove buttons + optional validation error text. Used in coach
// onboarding (availability step) and Settings → Availability.
// See `docs/components.md` § FitAvailabilityDay.

public enum FitDayOfWeek: Int, CaseIterable, Identifiable, Hashable {
    case monday = 1, tuesday, wednesday, thursday, friday, saturday, sunday

    public var id: Int { rawValue }

    public var label: String {
        switch self {
        case .monday: return "Monday"
        case .tuesday: return "Tuesday"
        case .wednesday: return "Wednesday"
        case .thursday: return "Thursday"
        case .friday: return "Friday"
        case .saturday: return "Saturday"
        case .sunday: return "Sunday"
        }
    }
}

/// Free-form interval so callers can use whatever time type fits
/// (`Date`, `(Int,Int)`, etc.); the component just renders strings
/// supplied by the caller. Keeps the component decoupled from
/// platform-specific Date types.
public struct FitAvailabilityInterval: Hashable {
    public var start: String
    public var end: String

    public init(start: String, end: String) {
        self.start = start
        self.end = end
    }
}

public struct FitAvailabilityDay: View {
    let day: FitDayOfWeek
    @Binding var isActive: Bool
    @Binding var intervals: [FitAvailabilityInterval]
    let onAddInterval: (() -> Void)?
    let onRemoveInterval: ((Int) -> Void)?
    let onIntervalEdit: ((Int) -> Void)?
    let validationError: String?

    @Environment(\.fitTheme) private var theme

    public init(
        day: FitDayOfWeek,
        isActive: Binding<Bool>,
        intervals: Binding<[FitAvailabilityInterval]>,
        onAddInterval: (() -> Void)? = nil,
        onRemoveInterval: ((Int) -> Void)? = nil,
        onIntervalEdit: ((Int) -> Void)? = nil,
        validationError: String? = nil
    ) {
        self.day = day
        self._isActive = isActive
        self._intervals = intervals
        self.onAddInterval = onAddInterval
        self.onRemoveInterval = onRemoveInterval
        self.onIntervalEdit = onIntervalEdit
        self.validationError = validationError
    }

    public var body: some View {
        VStack(alignment: .leading, spacing: FitSpacing.sp3) {
            // Header
            HStack(spacing: FitSpacing.sp3) {
                FitCheckbox(checked: $isActive)
                Text(day.label)
                    .font(.custom(FitFont.family, size: 16).weight(.medium))
                    .foregroundColor(isActive ? theme.textPrimary : theme.textTertiary)
                Spacer()
            }

            // Intervals (visible only when active)
            if isActive {
                VStack(alignment: .leading, spacing: FitSpacing.sp2) {
                    ForEach(intervals.indices, id: \.self) { idx in
                        intervalRow(idx)
                    }

                    if let onAddInterval {
                        Button(action: onAddInterval) {
                            HStack(spacing: FitSpacing.sp2) {
                                Image(systemName: "plus")
                                    .font(.system(size: 14, weight: .medium))
                                Text("Add interval")
                                    .font(FitFont.body2)
                            }
                            .foregroundColor(FitColors.brandPrimary)
                        }
                        .buttonStyle(.plain)
                    }
                }
                .padding(.leading, FitSize.checkboxSize + FitSpacing.sp3)
            }

            if let validationError {
                Text(validationError)
                    .font(.custom(FitFont.family, size: 13))
                    .foregroundColor(FitColors.Red.r400)
                    .padding(.leading, FitSize.checkboxSize + FitSpacing.sp3)
            }
        }
    }

    @ViewBuilder
    private func intervalRow(_ idx: Int) -> some View {
        HStack(spacing: FitSpacing.sp2) {
            timeInputBox(intervals[idx].start, idx: idx)

            Text("to")
                .font(FitFont.body2)
                .foregroundColor(theme.textSecondary)

            timeInputBox(intervals[idx].end, idx: idx)

            Spacer(minLength: 0)

            if let onRemoveInterval {
                Button(action: { onRemoveInterval(idx) }) {
                    Image(systemName: "xmark")
                        .font(.system(size: 12, weight: .semibold))
                        .foregroundColor(FitColors.error)
                        .frame(width: 28, height: 28)
                        .background(FitColors.error.opacity(0.1))
                        .clipShape(Circle())
                }
                .buttonStyle(.plain)
            }
        }
    }

    @ViewBuilder
    private func timeInputBox(_ value: String, idx: Int) -> some View {
        Button(action: { onIntervalEdit?(idx) }) {
            Text(value)
                .font(.custom(FitFont.family, size: 14).weight(.medium))
                .foregroundColor(theme.textPrimary)
                .frame(width: 56, height: 56)
                .background(theme.surfaceLow)
                .clipShape(RoundedRectangle(cornerRadius: FitRadius.input))
        }
        .buttonStyle(.plain)
    }
}
