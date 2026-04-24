import SwiftUI

// MARK: - FitNavbar
//
// Floating 5-tab bottom navigation bar (glass pill). Active tab gets
// selection-gradient background. See `docs/components.md`.
//
// ONLY renders on 5 root tab screens (Dashboard, Clients, Calendar,
// Messages, Settings) per feedback_navbar_visibility rule.

public enum FitNavTab: String, CaseIterable {
    case dashboard
    case clients
    case calendar
    case messages
    case settings

    var systemIcon: String {
        switch self {
        case .dashboard: return "house"
        case .clients:   return "person.2"
        case .calendar:  return "calendar"
        case .messages:  return "bubble.left"
        case .settings:  return "person.crop.circle"
        }
    }
}

public struct FitNavbar: View {
    @Binding var activeTab: FitNavTab
    let onTabChange: (FitNavTab) -> Void

    @Environment(\.fitTheme) private var theme

    public init(
        activeTab: Binding<FitNavTab>,
        onTabChange: @escaping (FitNavTab) -> Void
    ) {
        self._activeTab = activeTab
        self.onTabChange = onTabChange
    }

    public var body: some View {
        HStack(spacing: FitSpacing.sp3) {
            ForEach(FitNavTab.allCases, id: \.rawValue) { tab in
                tabButton(tab)
            }
        }
        .padding(FitSpacing.sp1)
        .background(navbarBackground)
        .clipShape(Capsule())
        .padding(.horizontal, FitSpacing.sp4)
        .padding(.bottom, FitSpacing.sp4)
    }

    @ViewBuilder
    private func tabButton(_ tab: FitNavTab) -> some View {
        Button(action: {
            activeTab = tab
            onTabChange(tab)
        }) {
            ZStack {
                if activeTab == tab {
                    Capsule().fill(FitColors.selectionGradient)
                }
                Image(systemName: tab.systemIcon)
                    .font(.system(size: FitSize.iconLg, weight: .regular))
                    .foregroundColor(activeTab == tab ? theme.textPrimary : FitColors.Gray.g300)
            }
            .frame(width: FitSize.navbarItemSize, height: FitSize.navbarItemSize)
        }
        .buttonStyle(.plain)
    }

    @ViewBuilder
    private var navbarBackground: some View {
        // Glass effect on iOS 15+; fall back to translucent surface
        theme.surfaceHigh.opacity(0.85)
            .background(.ultraThinMaterial)
    }
}
