import SwiftUI

// MARK: - FitSheet
//
// Bottom sheet modal — slides up from bottom, dismissible by swipe-down
// or overlay tap. Supports status header (descriptor + pill), variants
// (standard / compact), and state-aware footer slots for event sheets.
//
// See `docs/components.md` FitSheet.
//
// Usage:
//   FitSheet(isVisible: $isShown, variant: .standard) {
//     VStack(spacing: 28) {
//       FitSheetStatusHeader(descriptor: "Confirmed session")
//       // ... event info ...
//       HStack { FitButton("Accept", action: {}) }
//     }
//   }

public enum FitSheetVariant {
    case standard   // 40pt bottom safe area (full sheets)
    case compact    // 28pt bottom (quick action sheets)
}

public struct FitSheet<Content: View>: View {
    @Binding var isVisible: Bool
    let variant: FitSheetVariant
    let content: () -> Content

    @Environment(\.fitTheme) private var theme

    public init(
        isVisible: Binding<Bool>,
        variant: FitSheetVariant = .standard,
        @ViewBuilder content: @escaping () -> Content
    ) {
        self._isVisible = isVisible
        self.variant = variant
        self.content = content
    }

    public var body: some View {
        ZStack {
            if isVisible {
                // Overlay backdrop
                Color.black.opacity(0.5)
                    .ignoresSafeArea()
                    .transition(.opacity)
                    .onTapGesture { isVisible = false }

                // Sheet body
                VStack(spacing: 0) {
                    Spacer()

                    VStack(spacing: 0) {
                        // Handle
                        RoundedRectangle(cornerRadius: 2)
                            .fill(theme.divider)
                            .frame(width: 36, height: 4)
                            .padding(.top, FitSpacing.sp2)
                            .padding(.bottom, FitSpacing.sp4)

                        content()
                            .padding(.horizontal, FitSpacing.sp4)
                            .padding(.bottom, bottomPadding)
                    }
                    .frame(maxWidth: .infinity)
                    .background(theme.screenBg)
                    .clipShape(
                        RoundedRectangle(cornerRadius: FitRadius.lg, style: .continuous)
                            .path(in: CGRect(x: 0, y: 0, width: 1000, height: 2000))  // visual stub
                    )
                    .background(
                        theme.screenBg
                            .clipShape(SheetTopRoundedShape(radius: FitRadius.lg))
                    )
                    .transition(.move(edge: .bottom))
                }
                .ignoresSafeArea()
            }
        }
        .animation(.easeOut(duration: 0.25), value: isVisible)
    }

    private var bottomPadding: CGFloat {
        switch variant {
        case .standard: return FitSpacing.sp9   // 40pt
        case .compact:  return 28
        }
    }
}

// Custom shape rounding only the top corners of the sheet
private struct SheetTopRoundedShape: Shape {
    let radius: CGFloat
    func path(in rect: CGRect) -> Path {
        var p = Path()
        p.move(to: CGPoint(x: 0, y: rect.height))
        p.addLine(to: CGPoint(x: 0, y: radius))
        p.addQuadCurve(to: CGPoint(x: radius, y: 0),
                       control: CGPoint(x: 0, y: 0))
        p.addLine(to: CGPoint(x: rect.width - radius, y: 0))
        p.addQuadCurve(to: CGPoint(x: rect.width, y: radius),
                       control: CGPoint(x: rect.width, y: 0))
        p.addLine(to: CGPoint(x: rect.width, y: rect.height))
        p.closeSubpath()
        return p
    }
}

// MARK: - FitSheetStatusHeader
//
// Status declaration for event sheets: 18pt descriptor + optional inline pill.

public struct FitSheetStatusHeader: View {
    let descriptor: String
    let pill: FitCalEventPillData?

    @Environment(\.fitTheme) private var theme

    public init(descriptor: String, pill: FitCalEventPillData? = nil) {
        self.descriptor = descriptor
        self.pill = pill
    }

    public var body: some View {
        HStack(spacing: FitSpacing.sp3) {
            Text(descriptor)
                .font(.custom(FitFont.family, size: 18).weight(.medium))
                .foregroundColor(theme.textPrimary)
                .frame(maxWidth: .infinity, alignment: .leading)

            if let pill = pill {
                FitCalEventPill(text: pill.text, status: pill.status)
            }
        }
        .padding(.bottom, 28)
    }
}

public struct FitCalEventPillData {
    public let text: String
    public let status: FitCalEventPillStatus
    public init(text: String, status: FitCalEventPillStatus) {
        self.text = text
        self.status = status
    }
}
