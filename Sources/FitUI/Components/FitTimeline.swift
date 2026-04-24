import SwiftUI

// MARK: - FitTimeline
//
// 24-hour vertical grid container — hour labels on left, events
// positioned absolutely, "now" line indicator, unavailable zones.
// See `docs/components.md`.

public struct FitTimelineEvent: Identifiable {
    public let id = UUID()
    public let startMinute: Int   // 0-1439 (minutes from 00:00)
    public let durationMin: Int
    public let view: AnyView

    public init<V: View>(startMinute: Int, durationMin: Int, @ViewBuilder view: () -> V) {
        self.startMinute = startMinute
        self.durationMin = durationMin
        self.view = AnyView(view())
    }
}

public struct FitTimeline: View {
    let events: [FitTimelineEvent]
    let currentMinute: Int?     // nil = no "now" line
    let unavailableHours: [Int] // hours with diagonal-stripe overlay
    let hourHeight: CGFloat

    @Environment(\.fitTheme) private var theme

    public init(
        events: [FitTimelineEvent] = [],
        currentMinute: Int? = nil,
        unavailableHours: [Int] = [],
        hourHeight: CGFloat = 96
    ) {
        self.events = events
        self.currentMinute = currentMinute
        self.unavailableHours = unavailableHours
        self.hourHeight = hourHeight
    }

    public var body: some View {
        ZStack(alignment: .topLeading) {
            VStack(spacing: 0) {
                ForEach(0..<24, id: \.self) { hour in
                    HStack(alignment: .top, spacing: 0) {
                        Text(String(format: "%02d:00", hour))
                            .font(.custom(FitFont.family, size: 10))
                            .foregroundColor(theme.textTertiary)
                            .frame(width: 40, alignment: .trailing)
                            .padding(.top, 4)
                            .padding(.trailing, 8)

                        Rectangle()
                            .fill(theme.divider)
                            .frame(height: 1)
                            .frame(maxWidth: .infinity)
                    }
                    .frame(height: hourHeight, alignment: .top)
                    .background(unavailableHours.contains(hour) ? unavailableOverlay : nil)
                }
            }

            ForEach(events) { event in
                event.view
                    .padding(.leading, 48)
                    .padding(.trailing, 8)
                    .frame(height: max(24, CGFloat(event.durationMin) * (hourHeight / 60)))
                    .offset(y: CGFloat(event.startMinute) * (hourHeight / 60))
            }

            if let now = currentMinute {
                nowLine(minute: now)
            }
        }
    }

    private var unavailableOverlay: some View {
        // Diagonal stripe pattern (simplified for MVP — flat tint)
        FitColors.error.opacity(0.08)
    }

    @ViewBuilder
    private func nowLine(minute: Int) -> some View {
        let y = CGFloat(minute) * (hourHeight / 60)
        HStack(spacing: 0) {
            Circle()
                .fill(FitColors.brandPrimary)
                .frame(width: 8, height: 8)
                .padding(.leading, 44)
            Rectangle()
                .fill(FitColors.brandPrimary)
                .frame(height: 2)
        }
        .offset(y: y - 1)
    }
}
