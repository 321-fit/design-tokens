import SwiftUI

// MARK: - FitPasswordRule
//
// Vertical list of password validation rules — 12×12 check icon
// (tertiary if unmet, teal if met) + 14pt rule text.
// See `docs/components.md` § FitPasswordRule.

public struct FitPasswordRuleItem: Identifiable, Hashable {
    public let id: UUID
    public let label: String
    public let isMet: Bool

    public init(id: UUID = UUID(), label: String, isMet: Bool) {
        self.id = id
        self.label = label
        self.isMet = isMet
    }
}

public struct FitPasswordRule: View {
    let rules: [FitPasswordRuleItem]

    @Environment(\.fitTheme) private var theme

    public init(rules: [FitPasswordRuleItem]) {
        self.rules = rules
    }

    public var body: some View {
        VStack(alignment: .leading, spacing: 6) {
            ForEach(rules) { rule in
                HStack(spacing: FitSpacing.sp2) {
                    Image(systemName: rule.isMet ? "checkmark" : "circle")
                        .font(.system(size: 10, weight: .bold))
                        .foregroundColor(rule.isMet ? FitColors.Teal.t500 : theme.textTertiary)
                        .frame(width: 12, height: 12)

                    Text(rule.label)
                        .font(.custom(FitFont.family, size: 14))
                        .foregroundColor(rule.isMet ? theme.textSecondary : theme.textTertiary)
                }
            }
        }
    }
}
