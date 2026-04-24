import SwiftUI

// MARK: - FitSkeleton
//
// Shimmer-animated placeholders for list/content loading. Family
// of compositional views — Card, Row, Line, Circle, Block, Btn,
// Strip — that can be combined to match any card shape.
// See `docs/components.md`.

// MARK: - Shimmer modifier

public struct FitSkeletonShimmer: ViewModifier {
    @State private var phase: CGFloat = -1
    @Environment(\.fitTheme) private var theme

    public func body(content: Content) -> some View {
        content
            .overlay(
                LinearGradient(
                    colors: [
                        shimmerBase,
                        shimmerHighlight,
                        shimmerBase
                    ],
                    startPoint: UnitPoint(x: phase, y: 0.5),
                    endPoint: UnitPoint(x: phase + 2, y: 0.5)
                )
                .mask(content)
            )
            .onAppear {
                withAnimation(.easeInOut(duration: 1.4).repeatForever(autoreverses: false)) {
                    phase = 1
                }
            }
    }

    private var shimmerBase: Color {
        theme.surfaceHigher
    }

    private var shimmerHighlight: Color {
        theme.surfaceHigh
    }
}

public extension View {
    func fitShimmer() -> some View { modifier(FitSkeletonShimmer()) }
}

// MARK: - Primitive skeletons

public struct FitSkeletonCard<Content: View>: View {
    let compact: Bool
    let content: () -> Content

    @Environment(\.fitTheme) private var theme

    public init(compact: Bool = false, @ViewBuilder content: @escaping () -> Content) {
        self.compact = compact
        self.content = content
    }

    public var body: some View {
        VStack(spacing: FitSpacing.sp3, content: content)
            .padding(compact ? FitSpacing.sp3 : FitSpacing.sp4)
            .frame(maxWidth: .infinity)
            .background(theme.surfaceHigh)
            .clipShape(RoundedRectangle(cornerRadius: compact ? FitRadius.md : FitRadius.lg))
    }
}

public struct FitSkeletonLine: View {
    let width: CGFloat?   // nil = fill
    let short: Bool
    @Environment(\.fitTheme) private var theme

    public init(width: CGFloat? = nil, short: Bool = false) {
        self.width = width
        self.short = short
    }

    public var body: some View {
        RoundedRectangle(cornerRadius: 6)
            .fill(theme.surfaceHigher)
            .frame(width: width, height: short ? 10 : 12)
            .fitShimmer()
    }
}

public struct FitSkeletonCircle: View {
    let size: CGFloat
    let isSquare: Bool
    @Environment(\.fitTheme) private var theme

    public init(size: CGFloat = 44, isSquare: Bool = false) {
        self.size = size
        self.isSquare = isSquare
    }

    public var body: some View {
        Group {
            if isSquare {
                RoundedRectangle(cornerRadius: 10).fill(theme.surfaceHigher)
            } else {
                Circle().fill(theme.surfaceHigher)
            }
        }
        .frame(width: size, height: size)
        .fitShimmer()
    }
}

public struct FitSkeletonBlock: View {
    let width: CGFloat?
    let height: CGFloat
    @Environment(\.fitTheme) private var theme

    public init(width: CGFloat? = nil, height: CGFloat = 14) {
        self.width = width
        self.height = height
    }

    public var body: some View {
        RoundedRectangle(cornerRadius: 7)
            .fill(theme.surfaceHigher)
            .frame(width: width, height: height)
            .fitShimmer()
    }
}

public struct FitSkeletonBtn: View {
    @Environment(\.fitTheme) private var theme

    public init() {}

    public var body: some View {
        Capsule()
            .fill(theme.surfaceHigher)
            .frame(height: 40)
            .fitShimmer()
    }
}

public struct FitSkeletonStrip: View {
    @Environment(\.fitTheme) private var theme

    public init() {}

    public var body: some View {
        Rectangle()
            .fill(Color.clear)
            .frame(height: 36)
            .overlay(
                Rectangle()
                    .fill(theme.divider)
                    .frame(height: 1),
                alignment: .top
            )
    }
}

public struct FitSkeletonRow<Content: View>: View {
    let content: () -> Content

    public init(@ViewBuilder content: @escaping () -> Content) {
        self.content = content
    }

    public var body: some View {
        HStack(spacing: FitSpacing.sp3, content: content)
    }
}
