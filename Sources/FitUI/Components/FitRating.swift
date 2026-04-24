import SwiftUI

// MARK: - FitRating
//
// 5-star rating control — tap to rate. 3 sizes. Read-only mode for display.
// See `docs/components.md`.

public enum FitRatingSize {
    case small   // 28pt
    case medium  // 36pt (default)
    case large   // 48pt

    var px: CGFloat {
        switch self {
        case .small:  return 28
        case .medium: return 36
        case .large:  return 48
        }
    }
}

public struct FitRating: View {
    @Binding var rating: Int
    let size: FitRatingSize
    let readOnly: Bool

    public init(
        rating: Binding<Int>,
        size: FitRatingSize = .medium,
        readOnly: Bool = false
    ) {
        self._rating = rating
        self.size = size
        self.readOnly = readOnly
    }

    public var body: some View {
        HStack(spacing: 10) {
            ForEach(1...5, id: \.self) { index in
                Button(action: {
                    if !readOnly { rating = index }
                }) {
                    Image(systemName: index <= rating ? "star.fill" : "star")
                        .font(.system(size: size.px, weight: .regular))
                        .foregroundColor(FitColors.Yellow.y400)
                }
                .buttonStyle(.plain)
                .disabled(readOnly)
            }
        }
    }
}
