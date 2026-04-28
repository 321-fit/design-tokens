import SwiftUI

// MARK: - FitDayStrip
//
// Horizontal scrollable day chips with today highlight + event dots.
// 50×62 chips, 10pt uppercase weekday name + 16pt day number.
// See `docs/components.md` § FitDayStrip.

public enum FitDayEventType: Hashable {
    case personal
    case group
    case external
}

public enum FitDayStripMode {
    /// Calendar navigation — chips drive the active day on a connected
    /// timeline / list. Default for the strip in calendar.html.
    case nav
    /// Selection — chips read as toggleable "pick a day" affordances
    /// (invite flows, group session schedule preview).
    case select
}

public struct FitDayStrip: View {
    let year: Int
    let month: Int
    let days: Int
    @Binding var selectedDay: Int
    let todayDay: Int?
    let events: [Int: Set<FitDayEventType>]
    let mode: FitDayStripMode
    let onDaySelect: (Int) -> Void

    @Environment(\.fitTheme) private var theme

    public init(
        year: Int,
        month: Int,
        days: Int,
        selectedDay: Binding<Int>,
        todayDay: Int? = nil,
        events: [Int: Set<FitDayEventType>] = [:],
        mode: FitDayStripMode = .nav,
        onDaySelect: @escaping (Int) -> Void = { _ in }
    ) {
        self.year = year
        self.month = month
        self.days = days
        self._selectedDay = selectedDay
        self.todayDay = todayDay
        self.events = events
        self.mode = mode
        self.onDaySelect = onDaySelect
    }

    public var body: some View {
        ScrollViewReader { proxy in
            ScrollView(.horizontal, showsIndicators: false) {
                HStack(spacing: 0) {
                    ForEach(1...days, id: \.self) { day in
                        FitDayButton(
                            year: year,
                            month: month,
                            day: day,
                            isSelected: selectedDay == day,
                            isToday: todayDay == day,
                            events: events[day] ?? []
                        ) {
                            selectedDay = day
                            onDaySelect(day)
                            withAnimation(.easeInOut(duration: 0.3)) {
                                proxy.scrollTo(day, anchor: .center)
                            }
                        }
                        .id(day)
                    }
                }
                .padding(.horizontal, FitSpacing.sp4)
            }
        }
    }
}

// MARK: - FitDayButton (standalone sub-component)
//
// Public so invite / select flows that don't render an entire month
// strip can compose a row of buttons directly. Mirrors `.day-btn` /
// `.mg-day` patterns in the prototype.

public struct FitDayButton: View {
    let year: Int
    let month: Int
    let day: Int
    let isSelected: Bool
    let isToday: Bool
    let events: Set<FitDayEventType>
    let onTap: () -> Void

    @Environment(\.fitTheme) private var theme

    public init(
        year: Int,
        month: Int,
        day: Int,
        isSelected: Bool,
        isToday: Bool = false,
        events: Set<FitDayEventType> = [],
        onTap: @escaping () -> Void
    ) {
        self.year = year
        self.month = month
        self.day = day
        self.isSelected = isSelected
        self.isToday = isToday
        self.events = events
        self.onTap = onTap
    }

    public var body: some View {
        Button(action: onTap) {
            VStack(spacing: FitSpacing.sp1) {
                Text(weekdayAbbreviation.uppercased())
                    .font(.custom(FitFont.family, size: 10).weight(.medium))
                    .foregroundColor(isSelected ? theme.textPrimary : theme.textTertiary)

                Text("\(day)")
                    .font(.custom(FitFont.family, size: 16).weight(isSelected ? .semibold : .medium))
                    .foregroundColor(dayNumberColor)

                dotsRow
            }
            .frame(width: 50, height: 62)
            .background(isSelected ? AnyView(FitColors.selectionGradient) : AnyView(Color.clear))
            .overlay(
                RoundedRectangle(cornerRadius: FitRadius.lg)
                    .stroke(isSelected ? FitColors.selectionBorder : Color.clear, lineWidth: 1)
            )
            .clipShape(RoundedRectangle(cornerRadius: FitRadius.lg))
        }
        .buttonStyle(.plain)
    }

    @ViewBuilder
    private var dotsRow: some View {
        HStack(spacing: 3) {
            if events.contains(.personal) { dot(FitColors.Teal.t500) }
            if events.contains(.group)    { dot(FitColors.brandPrimary) }
            if events.contains(.external) { dot(theme.textTertiary) }
        }
        .frame(height: 4)
    }

    private func dot(_ color: Color) -> some View {
        Circle().fill(color).frame(width: 4, height: 4)
    }

    private var dayNumberColor: Color {
        if isSelected { return theme.textPrimary }
        if isToday    { return FitColors.brandPrimary }
        return theme.textSecondary
    }

    private var weekdayAbbreviation: String {
        var components = DateComponents()
        components.year = year
        components.month = month
        components.day = day
        let date = Calendar.current.date(from: components) ?? Date()
        let formatter = DateFormatter()
        formatter.dateFormat = "EEE"
        formatter.locale = Locale(identifier: "en_US")
        return formatter.string(from: date)
    }
}
