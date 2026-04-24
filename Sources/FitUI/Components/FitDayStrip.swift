import SwiftUI

// MARK: - FitDayStrip
//
// Horizontal scrollable day chips with today highlight + event dots.
// 50×62 chips, 10pt uppercase weekday name + 16pt day number.
// See `docs/components.md`.

public enum FitDayEventType: Hashable {
    case personal
    case group
    case external
}

public struct FitDayStrip: View {
    let year: Int
    let month: Int
    let days: Int
    @Binding var selectedDay: Int
    let todayDay: Int?
    let events: [Int: Set<FitDayEventType>]
    let onDaySelect: (Int) -> Void

    @Environment(\.fitTheme) private var theme

    public init(
        year: Int,
        month: Int,
        days: Int,
        selectedDay: Binding<Int>,
        todayDay: Int? = nil,
        events: [Int: Set<FitDayEventType>] = [:],
        onDaySelect: @escaping (Int) -> Void = { _ in }
    ) {
        self.year = year
        self.month = month
        self.days = days
        self._selectedDay = selectedDay
        self.todayDay = todayDay
        self.events = events
        self.onDaySelect = onDaySelect
    }

    public var body: some View {
        ScrollViewReader { proxy in
            ScrollView(.horizontal, showsIndicators: false) {
                HStack(spacing: 0) {
                    ForEach(1...days, id: \.self) { day in
                        dayChip(day)
                            .id(day)
                            .onTapGesture {
                                selectedDay = day
                                onDaySelect(day)
                                withAnimation(.easeInOut(duration: 0.3)) {
                                    proxy.scrollTo(day, anchor: .center)
                                }
                            }
                    }
                }
                .padding(.horizontal, FitSpacing.sp4)
            }
        }
    }

    @ViewBuilder
    private func dayChip(_ day: Int) -> some View {
        let isSelected = selectedDay == day
        let isToday = todayDay == day
        let weekdayName = weekdayAbbreviation(for: day)

        VStack(spacing: FitSpacing.sp1) {
            Text(weekdayName.uppercased())
                .font(.custom(FitFont.family, size: 10).weight(.medium))
                .foregroundColor(isSelected ? theme.textPrimary : theme.textTertiary)

            Text("\(day)")
                .font(.custom(FitFont.family, size: 16).weight(isSelected ? .semibold : .medium))
                .foregroundColor(dayNumberColor(selected: isSelected, today: isToday))

            dotsRow(day)
        }
        .frame(width: 50, height: 62)
        .background(isSelected ? AnyView(FitColors.selectionGradient) : AnyView(Color.clear))
        .overlay(
            RoundedRectangle(cornerRadius: FitRadius.lg)
                .stroke(isSelected ? FitColors.selectionBorder : Color.clear, lineWidth: 1)
        )
        .clipShape(RoundedRectangle(cornerRadius: FitRadius.lg))
    }

    @ViewBuilder
    private func dotsRow(_ day: Int) -> some View {
        let types = events[day] ?? []
        HStack(spacing: 3) {
            if types.contains(.personal) { dot(FitColors.Teal.t500) }
            if types.contains(.group)    { dot(FitColors.brandPrimary) }
            if types.contains(.external) { dot(theme.textTertiary) }
        }
        .frame(height: 4)
    }

    private func dot(_ color: Color) -> some View {
        Circle().fill(color).frame(width: 4, height: 4)
    }

    private func dayNumberColor(selected: Bool, today: Bool) -> Color {
        if selected { return theme.textPrimary }
        if today    { return FitColors.brandPrimary }
        return theme.textSecondary
    }

    private func weekdayAbbreviation(for day: Int) -> String {
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
