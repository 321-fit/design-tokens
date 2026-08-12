import SwiftUI

// MARK: - FitProgressBar
//
// Bare determinate bar, no label. `FitSpotCounter` is the capacity variant
// built on top of it — it owns the centred "X of Y spots" label, which is
// exactly why it cannot be dropped into a dense row that already carries its
// own counter line. See `docs/components.md` § FitProgressBar.

/// Tone carries the meaning, as it does on badges: `.brand` is ordinary
/// progress, `.warning` says the measured thing is running out and someone
/// should act — a pack down to its last credits, not merely a nearly-full bar.
public enum FitProgressTone {
    case brand
    case warning
    case neutral
}

/// What the empty part of the bar is drawn in — and it is not cosmetic.
///
/// `.surface` is for a bar sitting directly on the screen background (a capacity bar on
/// a card); `.divider` is for one inside a plain list row, where `surfaceHigh` is so
/// close to the background that an empty bar vanishes and "0 of 5 used" loses its picture.
public enum FitProgressTrack {
    case surface
    case divider
}

public struct FitProgressBar: View {
    let progress: Double
    let tone: FitProgressTone
    let track: FitProgressTrack
    let height: CGFloat

    @Environment(\.fitTheme) private var theme

    public init(
        progress: Double,
        tone: FitProgressTone = .brand,
        track: FitProgressTrack = .surface,
        height: CGFloat = 4
    ) {
        self.progress = min(max(progress, 0), 1)
        self.tone = tone
        self.track = track
        self.height = height
    }

    public var body: some View {
        ZStack(alignment: .leading) {
            RoundedRectangle(cornerRadius: FitRadius.md)
                .fill(trackColor)

            // A zero-width capsule still paints its rounded caps, which reads as
            // "a little bit done" on an empty bar — so draw nothing until there is.
            if progress > 0 {
                GeometryReader { proxy in
                    RoundedRectangle(cornerRadius: FitRadius.md)
                        .fill(fillColor)
                        .frame(width: proxy.size.width * progress)
                }
            }
        }
        .frame(height: height)
    }

    private var trackColor: Color {
        switch track {
        case .surface: return theme.surfaceHigh
        case .divider: return theme.divider
        }
    }

    private var fillColor: Color {
        switch tone {
        case .brand: return FitColors.Teal.t500
        case .warning: return FitColors.warning
        case .neutral: return theme.textTertiary
        }
    }
}
