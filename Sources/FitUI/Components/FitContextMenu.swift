import SwiftUI

// MARK: - FitContextMenu
//
// iOS-style floating popover menu. For native SwiftUI, prefer the
// built-in `.contextMenu` modifier; this component provides a
// programmatic trigger equivalent. See `docs/components.md`.

public struct FitContextMenuItem: Identifiable {
    public let id = UUID()
    public let title: String
    public let systemImage: String?
    public let destructive: Bool
    public let action: () -> Void

    public init(
        title: String,
        systemImage: String? = nil,
        destructive: Bool = false,
        action: @escaping () -> Void
    ) {
        self.title = title
        self.systemImage = systemImage
        self.destructive = destructive
        self.action = action
    }
}

/// Trigger view — attach a menu to any content view.
///
/// Usage:
/// ```
/// FitContextMenuTrigger(items: [
///   .init(title: "View profile", systemImage: "person.fill") { ... },
///   .init(title: "Delete", systemImage: "trash", destructive: true) { ... },
/// ]) {
///   FitIconBtn(systemName: "ellipsis") { }
/// }
/// ```
public struct FitContextMenuTrigger<Label: View>: View {
    let items: [FitContextMenuItem]
    let label: () -> Label

    public init(
        items: [FitContextMenuItem],
        @ViewBuilder label: @escaping () -> Label
    ) {
        self.items = items
        self.label = label
    }

    public var body: some View {
        Menu(content: {
            ForEach(items) { item in
                Button(role: item.destructive ? .destructive : nil, action: item.action) {
                    if let icon = item.systemImage {
                        Label(item.title, systemImage: icon)
                    } else {
                        Text(item.title)
                    }
                }
            }
        }, label: label)
    }
}
