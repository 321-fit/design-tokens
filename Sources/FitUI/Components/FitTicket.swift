import SwiftUI

// MARK: - FitTicket
//
// Compact event summary card — title, time, coach, price, optional
// meta chips. See `docs/components.md`.

public struct FitTicket: View {
    let title: String
    let time: String
    let coachName: String
    let coachInitials: String
    let price: String
    let meta: [String]
    let onTap: (() -> Void)?

    @Environment(\.fitTheme) private var theme

    public init(
        title: String,
        time: String,
        coachName: String,
        coachInitials: String,
        price: String,
        meta: [String] = [],
        onTap: (() -> Void)? = nil
    ) {
        self.title = title
        self.time = time
        self.coachName = coachName
        self.coachInitials = coachInitials
        self.price = price
        self.meta = meta
        self.onTap = onTap
    }

    public var body: some View {
        Button(action: { onTap?() }) {
            VStack(alignment: .leading, spacing: FitSpacing.sp3) {
                HStack(alignment: .top) {
                    Text(title)
                        .font(FitFont.button1)
                        .foregroundColor(theme.textPrimary)
                        .multilineTextAlignment(.leading)
                        .lineLimit(2)
                    Spacer()
                    Image(systemName: "chevron.right")
                        .font(.system(size: FitSize.iconMd))
                        .foregroundColor(theme.textTertiary)
                }

                HStack(spacing: FitSpacing.sp2) {
                    Image(systemName: "clock")
                        .font(.system(size: 13))
                        .foregroundColor(theme.textSecondary)
                    Text(time)
                        .font(FitFont.body2)
                        .foregroundColor(theme.textSecondary)
                }

                if !meta.isEmpty {
                    HStack(spacing: FitSpacing.sp2) {
                        ForEach(meta, id: \.self) { m in
                            Text(m)
                                .font(FitFont.caption)
                                .foregroundColor(theme.textTertiary)
                        }
                    }
                }

                Divider().background(theme.divider)

                HStack(spacing: FitSpacing.sp3) {
                    FitAvatar(initials: coachInitials, size: .sm)
                    VStack(alignment: .leading, spacing: 2) {
                        Text(coachName)
                            .font(.custom(FitFont.family, size: 14).weight(.medium))
                            .foregroundColor(theme.textPrimary)
                    }
                    Spacer()
                    Text(price)
                        .font(FitFont.button2)
                        .foregroundColor(FitColors.Teal.t500)
                }
            }
            .padding(FitSpacing.sp4)
            .frame(maxWidth: .infinity, alignment: .leading)
            .background(theme.cardBg)
            .clipShape(RoundedRectangle(cornerRadius: FitRadius.md))
        }
        .buttonStyle(.plain)
    }
}
