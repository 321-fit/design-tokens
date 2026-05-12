import SwiftUI

// MARK: - FitInviteRow
//
// Referral / invite list row — avatar + name + when. No trailing pill
// in MVP (share-link path can't track "pending" so pills would suggest
// capability we don't have). Optional `trailing` slot for Phase 2 pills
// or chevrons.
// See `docs/components.md` § FitInviteRow.

public struct FitInviteRow<Trailing: View>: View {
    let initials: String
    let name: String
    let when: String
    let onTap: (() -> Void)?
    let trailing: Trailing

    @Environment(\.fitTheme) private var theme

    public init(
        initials: String,
        name: String,
        when: String,
        onTap: (() -> Void)? = nil,
        @ViewBuilder trailing: () -> Trailing
    ) {
        self.initials = initials
        self.name = name
        self.when = when
        self.onTap = onTap
        self.trailing = trailing()
    }

    public var body: some View {
        let row = HStack(spacing: 12) {
            FitAvatar(initials: initials, size: .md)

            VStack(alignment: .leading, spacing: 2) {
                Text(name)
                    .font(.custom(FitFont.family, size: 15).weight(.medium))
                    .foregroundColor(theme.textPrimary)
                    .lineLimit(1)
                Text(when)
                    .font(.custom(FitFont.family, size: 13))
                    .foregroundColor(theme.textTertiary)
                    .lineLimit(1)
            }
            .frame(maxWidth: .infinity, alignment: .leading)

            trailing
        }
        .padding(12)
        .background(theme.surfaceHigh)
        .clipShape(RoundedRectangle(cornerRadius: 12))

        if let onTap {
            Button(action: onTap) { row }.buttonStyle(.plain)
        } else {
            row
        }
    }
}

// Convenience overload — most callers don't want a trailing slot.
public extension FitInviteRow where Trailing == EmptyView {
    init(
        initials: String,
        name: String,
        when: String,
        onTap: (() -> Void)? = nil
    ) {
        self.init(initials: initials, name: name, when: when, onTap: onTap) {
            EmptyView()
        }
    }
}
