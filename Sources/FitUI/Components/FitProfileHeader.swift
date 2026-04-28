import SwiftUI

// MARK: - FitProfileHeader
//
// Profile / coach hero — 80×80 avatar + name + metadata row +
// optional bio, optional trailing edit pencil icon. Used at the top
// of profile screens (own profile, viewed coach profile).
// See `docs/components.md` § FitProfileHeader.

public struct FitProfileHeader: View {
    let initials: String
    let imageURL: URL?
    let name: String
    let sports: [String]
    let location: String?
    let bio: String?
    let onEdit: (() -> Void)?

    @Environment(\.fitTheme) private var theme

    public init(
        initials: String,
        imageURL: URL? = nil,
        name: String,
        sports: [String] = [],
        location: String? = nil,
        bio: String? = nil,
        onEdit: (() -> Void)? = nil
    ) {
        self.initials = initials
        self.imageURL = imageURL
        self.name = name
        self.sports = sports
        self.location = location
        self.bio = bio
        self.onEdit = onEdit
    }

    public var body: some View {
        VStack(alignment: .leading, spacing: FitSpacing.sp4) {
            HStack(alignment: .top, spacing: FitSpacing.sp4) {
                FitAvatar(initials: initials, size: .xl, image: imageURL)

                VStack(alignment: .leading, spacing: FitSpacing.sp1) {
                    HStack(alignment: .top, spacing: FitSpacing.sp2) {
                        Text(name)
                            .font(.custom(FitFont.family, size: 24).weight(.semibold))
                            .foregroundColor(theme.textPrimary)
                            .frame(maxWidth: .infinity, alignment: .leading)

                        if let onEdit {
                            FitIconBtn(systemName: "pencil", color: .primary, action: onEdit)
                        }
                    }

                    if !metadata.isEmpty {
                        Text(metadata)
                            .font(FitFont.body2)
                            .foregroundColor(theme.textTertiary)
                    }
                }
            }

            if let bio, !bio.isEmpty {
                Text(bio)
                    .font(FitFont.body1)
                    .foregroundColor(theme.textSecondary)
                    .fixedSize(horizontal: false, vertical: true)
            }
        }
        .padding(FitSpacing.sp4)
        .frame(maxWidth: .infinity, alignment: .leading)
    }

    private var metadata: String {
        var parts: [String] = []
        if let location, !location.isEmpty { parts.append(location) }
        if !sports.isEmpty { parts.append(sports.joined(separator: ", ")) }
        return parts.joined(separator: " · ")
    }
}
