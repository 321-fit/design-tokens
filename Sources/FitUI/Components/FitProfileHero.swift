import SwiftUI

// MARK: - FitProfileHero
//
// 16:9 hero media block at the top of the coach profile. Renders in
// 3 fallback variants:
//   .video(url)    — preferred; native player
//   .cover(url)    — secondary; image fill
//   .initials(s)   — fallback; brand-gradient + 56pt initials
// Optional onEdit callback renders a camera-overlay button (top-right)
// that the caller wires to a media picker.
// See `docs/components.md` § FitProfileHero.

public struct FitProfileHero: View {
    public enum Media {
        case video(URL)
        case cover(URL)
        case initials(String)
    }

    let media: Media
    let onEdit: (() -> Void)?

    @Environment(\.fitTheme) private var theme

    public init(media: Media, onEdit: (() -> Void)? = nil) {
        self.media = media
        self.onEdit = onEdit
    }

    public var body: some View {
        ZStack(alignment: .topTrailing) {
            content
                .frame(maxWidth: .infinity)
                .aspectRatio(16.0 / 9.0, contentMode: .fit)
                .background(Color.black)
                .clipped()

            if let onEdit {
                Button(action: onEdit) {
                    Image(systemName: "camera.fill")
                        .font(.system(size: 16))
                        .foregroundColor(.white)
                        .frame(width: 36, height: 36)
                        .background(
                            Color.black.opacity(0.5),
                            in: Circle()
                        )
                }
                .buttonStyle(.plain)
                .padding(.top, 12)
                .padding(.trailing, 12)
            }
        }
    }

    @ViewBuilder
    private var content: some View {
        switch media {
        case .video(let url):
            // Real impl: native AVPlayer / AVPlayerLayer. Prototype-level
            // placeholder showing play glyph on dark gradient.
            ZStack {
                LinearGradient(
                    colors: [Color(red: 0.1, green: 0.1, blue: 0.18),
                             Color(red: 0.09, green: 0.13, blue: 0.24)],
                    startPoint: .topLeading, endPoint: .bottomTrailing
                )
                Image(systemName: "play.fill")
                    .font(.system(size: 48))
                    .foregroundColor(.white.opacity(0.8))
            }
        case .cover(let url):
            AsyncImage(url: url) { phase in
                switch phase {
                case .success(let image):
                    image.resizable().scaledToFill()
                default:
                    LinearGradient(
                        colors: [Color(red: 0.18, green: 0.35, blue: 0.24),
                                 Color(red: 0.29, green: 0.49, blue: 0.35)],
                        startPoint: .topLeading, endPoint: .bottomTrailing
                    )
                }
            }
        case .initials(let initials):
            ZStack {
                FitColors.brandGradient
                Text(initials.prefix(2).uppercased())
                    .font(.custom(FitFont.family, size: 56).weight(.semibold))
                    .foregroundColor(.white.opacity(0.95))
                    .tracking(2)
            }
        }
    }
}
