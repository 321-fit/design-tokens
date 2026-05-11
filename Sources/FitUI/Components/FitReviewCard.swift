import SwiftUI

// MARK: - FitReviewCard
//
// Single review entry — 280pt-wide snap card with avatar + reviewer name +
// relative time + star rating + 4-line clamped body. Designed to live in a
// horizontal carousel; the trailing "Show all N reviews" tile is the
// `.showAll` variant.
// See `docs/components.md` § FitReviewCard.

public struct FitReviewCard: View {
    public enum Variant {
        case review(reviewer: String, initials: String, when: String, stars: Int, body: String)
        case showAll(total: Int)
    }

    let variant: Variant
    let onTap: (() -> Void)?

    @Environment(\.fitTheme) private var theme

    public init(_ variant: Variant, onTap: (() -> Void)? = nil) {
        self.variant = variant
        self.onTap = onTap
    }

    public var body: some View {
        let card = content
            .padding(14)
            .frame(width: 280, alignment: .leading)
            .background(theme.surfaceHigh)
            .overlay(
                RoundedRectangle(cornerRadius: 14)
                    .stroke(theme.divider, lineWidth: 1)
                    .opacity(0)
            )
            .clipShape(RoundedRectangle(cornerRadius: 14))

        if let onTap {
            Button(action: onTap) { card }.buttonStyle(.plain)
        } else {
            card
        }
    }

    @ViewBuilder
    private var content: some View {
        switch variant {
        case .review(let reviewer, let initials, let when, let stars, let body):
            VStack(alignment: .leading, spacing: 8) {
                HStack(spacing: 10) {
                    FitAvatar(initials: initials, size: .sm, bg: .gray)
                    VStack(alignment: .leading, spacing: 0) {
                        Text(reviewer)
                            .font(.custom(FitFont.family, size: 14).weight(.medium))
                            .foregroundColor(theme.textPrimary)
                        Text(when)
                            .font(FitFont.caption)
                            .foregroundColor(theme.textTertiary)
                    }
                }
                stars5(filled: stars)
                Text(body)
                    .font(FitFont.body2)
                    .foregroundColor(theme.textSecondary)
                    .lineLimit(4)
            }

        case .showAll(let total):
            VStack(spacing: 8) {
                Image(systemName: "arrow.right")
                    .font(.system(size: 24, weight: .light))
                    .foregroundColor(FitColors.brandPrimary)
                Text("Show all \(total) reviews")
                    .font(FitFont.button2)
                    .foregroundColor(FitColors.brandPrimary)
            }
            .frame(maxWidth: .infinity, maxHeight: .infinity)
        }
    }

    @ViewBuilder
    private func stars5(filled: Int) -> some View {
        HStack(spacing: 2) {
            ForEach(0..<5, id: \.self) { i in
                Image(systemName: "star.fill")
                    .font(.system(size: 12))
                    .foregroundColor(i < filled ? FitColors.Teal.t500 : theme.textTertiary)
            }
        }
    }
}

// MARK: - FitReviewCarousel
//
// Thin horizontal scroll wrapper that lays out a list of `FitReviewCard`
// values with snap behavior and consistent gaps. Use directly when a
// carousel is desired; otherwise embed `FitReviewCard` in a custom scroller.

public struct FitReviewCarousel: View {
    let cards: [FitReviewCard.Variant]
    let onCardTap: ((Int) -> Void)?

    public init(_ cards: [FitReviewCard.Variant], onCardTap: ((Int) -> Void)? = nil) {
        self.cards = cards
        self.onCardTap = onCardTap
    }

    public var body: some View {
        ScrollView(.horizontal, showsIndicators: false) {
            HStack(spacing: 10) {
                ForEach(Array(cards.enumerated()), id: \.offset) { index, variant in
                    FitReviewCard(variant, onTap: onCardTap.map { handler in { handler(index) } })
                }
            }
            .padding(.horizontal, FitSpacing.sp4)
        }
    }
}
