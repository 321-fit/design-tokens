import SwiftUI

// MARK: - FitCalEventPill
//
// Inline status badge for calendar events — 11pt Caption 2 white text
// on colored bg. 4 states. See `docs/components.md`.

public enum FitCalEventPillStatus {
    case request    // yellow-600
    case review     // yellow-600 (same as request visually)
    case awaiting   // gray-500
    case missed     // red-400
}

public struct FitCalEventPill: View {
    let text: String
    let status: FitCalEventPillStatus

    public init(text: String, status: FitCalEventPillStatus) {
        self.text = text
        self.status = status
    }

    public var body: some View {
        Text(text)
            .font(.custom(FitFont.family, size: 11).weight(.medium))
            .foregroundColor(.white)
            .padding(.horizontal, 6)
            .padding(.vertical, 2)
            .background(background)
            .clipShape(Capsule())
    }

    private var background: Color {
        switch status {
        case .request, .review: return FitColors.Yellow.y600
        case .awaiting:         return FitColors.Gray.g500
        case .missed:           return FitColors.error
        }
    }
}

