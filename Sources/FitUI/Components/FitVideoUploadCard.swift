import SwiftUI

// MARK: - FitVideoUploadCard
//
// 16:9 state-aware video upload card. Owns the visual lifecycle of a
// direct-upload video integration (Mux is the primary consumer):
//   .idle       — dashed dropzone with camera icon + CTA
//   .uploading  — yellow-tinted card with linear progress + cancel ×
//   .processing — yellow-tinted card with spinner
//   .ready      — solid card with thumbnail + play overlay + ⋯ menu
//   .errored    — red-tinted card with alert + retry CTA
//   .pending    — same visual as .idle + "last upload didn't finish" hint
//
// Owners pass platform integration (picker, upload, player) via callbacks.
// The kit component does NOT pick files, perform the PUT, or play video
// itself — that's app-level work that varies per platform.
//
// See `docs/components.md` § FitVideoUploadCard.

public struct FitVideoUploadCard: View {
    public enum State: Equatable {
        case idle
        case uploading(progress: Double, filename: String)
        case processing
        case ready(thumbnailURL: URL?)
        case errored(errorCode: String?)
        case pending
    }

    let state: State
    let onTap: () -> Void
    let onCancel: (() -> Void)?
    let onMore: (() -> Void)?

    @Environment(\.fitTheme) private var theme

    public init(
        state: State,
        onTap: @escaping () -> Void,
        onCancel: (() -> Void)? = nil,
        onMore: (() -> Void)? = nil
    ) {
        self.state = state
        self.onTap = onTap
        self.onCancel = onCancel
        self.onMore = onMore
    }

    public var body: some View {
        ZStack(alignment: .topTrailing) {
            content
                .frame(maxWidth: .infinity)
                .aspectRatio(16.0 / 9.0, contentMode: .fit)
                .background(backgroundFill)
                .overlay(borderOverlay)
                .clipShape(RoundedRectangle(cornerRadius: FitRadius.md))
                .contentShape(Rectangle())
                .onTapGesture(perform: handleTap)

            overlayControls
                .padding(.top, FitSpacing.sp2)
                .padding(.trailing, FitSpacing.sp2)
        }
    }

    // MARK: - Body content by state

    @ViewBuilder
    private var content: some View {
        switch state {
        case .idle, .pending:
            idleSlot
        case .uploading(let progress, let filename):
            uploadingSlot(progress: progress, filename: filename)
        case .processing:
            processingSlot
        case .ready(let thumbnailURL):
            readySlot(thumbnailURL: thumbnailURL)
        case .errored(let errorCode):
            erroredSlot(errorCode: errorCode)
        }
    }

    // MARK: - IDLE / PENDING

    private var idleSlot: some View {
        VStack(spacing: FitSpacing.sp2) {
            Image(systemName: "video")
                .font(.system(size: 24, weight: .regular))
                .foregroundStyle(theme.textSecondary.opacity(0.6))
                .frame(width: 32, height: 32)
            Text("Upload intro video")
                .font(.custom(FitFont.family, size: 15).weight(.medium))
                .foregroundStyle(theme.textPrimary)
            Text("Up to 200 MB · 2 min · mp4/mov")
                .font(.custom(FitFont.family, size: 12))
                .foregroundStyle(theme.textTertiary)
        }
        .multilineTextAlignment(.center)
        .padding(FitSpacing.sp3)
    }

    // MARK: - UPLOADING

    private func uploadingSlot(progress: Double, filename: String) -> some View {
        VStack(alignment: .leading, spacing: 10) {
            HStack(spacing: 6) {
                Image(systemName: "video.fill")
                    .font(.system(size: 12))
                Text(filename)
                    .font(.custom(FitFont.family, size: 13))
                    .lineLimit(1)
                    .truncationMode(.middle)
            }
            .foregroundStyle(theme.textPrimary)

            // Progress bar
            GeometryReader { proxy in
                ZStack(alignment: .leading) {
                    Capsule()
                        .fill(theme.surfaceHigh)
                    Capsule()
                        .fill(FitColors.warning)
                        .frame(width: max(0, min(1, progress)) * proxy.size.width)
                }
            }
            .frame(height: 6)

            Text("Uploading… \(Int(max(0, min(1, progress)) * 100))%")
                .font(.custom(FitFont.family, size: 12))
                .foregroundStyle(theme.textTertiary)
                .frame(maxWidth: .infinity, alignment: .trailing)
        }
        .padding(.horizontal, FitSpacing.sp4)
        .padding(.vertical, 14)
        .frame(maxWidth: .infinity, maxHeight: .infinity)
    }

    // MARK: - PROCESSING

    private var processingSlot: some View {
        VStack(spacing: 10) {
            ProgressView()
                .progressViewStyle(.circular)
                .tint(FitColors.warning)
                .scaleEffect(1.2)
                .frame(width: 28, height: 28)
            Text("Processing your video…")
                .font(.custom(FitFont.family, size: 14).weight(.medium))
                .foregroundStyle(FitColors.warning)
            Text("Usually 30-60 seconds")
                .font(.custom(FitFont.family, size: 12))
                .foregroundStyle(theme.textTertiary)
        }
        .multilineTextAlignment(.center)
        .padding(FitSpacing.sp3)
    }

    // MARK: - READY

    private func readySlot(thumbnailURL: URL?) -> some View {
        ZStack {
            if let thumbnailURL {
                AsyncImage(url: thumbnailURL) { phase in
                    switch phase {
                    case .success(let image):
                        image.resizable().scaledToFill()
                    default:
                        FitColors.brandGradient
                    }
                }
            } else {
                FitColors.brandGradient
            }

            // Play overlay
            Color.black.opacity(0.25)
            Image(systemName: "play.fill")
                .font(.system(size: 36))
                .foregroundStyle(.white)
                .shadow(color: .black.opacity(0.4), radius: 6, y: 2)
        }
        .frame(maxWidth: .infinity, maxHeight: .infinity)
    }

    // MARK: - ERRORED

    private func erroredSlot(errorCode: String?) -> some View {
        VStack(spacing: 6) {
            Image(systemName: "exclamationmark.triangle")
                .font(.system(size: 24, weight: .regular))
                .foregroundStyle(FitColors.error)
                .frame(width: 32, height: 32)
            Text("Couldn’t process this video")
                .font(.custom(FitFont.family, size: 14).weight(.medium))
                .foregroundStyle(theme.textPrimary)
            Text("Try a different file")
                .font(.custom(FitFont.family, size: 14).weight(.medium))
                .foregroundStyle(FitColors.error)
                .underline()
            if let errorCode {
                Text("code: \(errorCode)")
                    .font(.custom(FitFont.family, size: 11))
                    .foregroundStyle(theme.textTertiary)
                    .padding(.top, 2)
            }
        }
        .multilineTextAlignment(.center)
        .padding(FitSpacing.sp3)
    }

    // MARK: - Top-right controls (cancel × in uploading, ⋯ in ready)

    @ViewBuilder
    private var overlayControls: some View {
        switch state {
        case .uploading:
            if let onCancel {
                Button(action: onCancel) {
                    Image(systemName: "xmark")
                        .font(.system(size: 12, weight: .semibold))
                        .foregroundStyle(.white)
                        .frame(width: 28, height: 28)
                        .background(Color.black.opacity(0.6), in: Circle())
                }
                .buttonStyle(.plain)
            }
        case .ready:
            if let onMore {
                Button(action: onMore) {
                    Image(systemName: "ellipsis")
                        .font(.system(size: 14, weight: .semibold))
                        .foregroundStyle(.white)
                        .frame(width: 32, height: 32)
                        .background(Color.black.opacity(0.5), in: Circle())
                }
                .buttonStyle(.plain)
            }
        default:
            EmptyView()
        }
    }

    // MARK: - State-driven styling

    private var backgroundFill: Color {
        switch state {
        case .idle, .pending:
            return .clear
        case .uploading, .processing:
            return theme.bgWarningSubtle
        case .ready:
            return theme.surfaceHigher
        case .errored:
            return theme.bgErrorSubtle
        }
    }

    @ViewBuilder
    private var borderOverlay: some View {
        let shape = RoundedRectangle(cornerRadius: FitRadius.md)
        switch state {
        case .idle, .pending:
            shape.strokeBorder(theme.divider, style: StrokeStyle(lineWidth: 1.5, dash: [5, 4]))
        case .uploading, .processing:
            shape.strokeBorder(FitColors.warning, lineWidth: 1)
        case .ready:
            shape.strokeBorder(theme.divider, lineWidth: 1)
        case .errored:
            shape.strokeBorder(FitColors.error, lineWidth: 1)
        }
    }

    // MARK: - Tap routing

    private func handleTap() {
        switch state {
        case .idle, .pending, .errored, .ready:
            onTap()
        case .uploading, .processing:
            // Card is non-interactive; cancel × handles abort during upload.
            break
        }
    }
}

// MARK: - Preview

#if DEBUG
struct FitVideoUploadCard_Previews: PreviewProvider {
    static var previews: some View {
        ScrollView {
            VStack(spacing: 16) {
                FitVideoUploadCard(state: .idle, onTap: {})
                FitVideoUploadCard(state: .uploading(progress: 0.64, filename: "intro-2026.mp4"), onTap: {}, onCancel: {})
                FitVideoUploadCard(state: .processing, onTap: {})
                FitVideoUploadCard(state: .ready(thumbnailURL: nil), onTap: {}, onMore: {})
                FitVideoUploadCard(state: .errored(errorCode: "invalid_input"), onTap: {})
                FitVideoUploadCard(state: .pending, onTap: {})
            }
            .padding()
        }
        .background(Color.black)
        .fitTheme(.coach)
        .previewDisplayName("Coach (dark) — all states")
    }
}
#endif
