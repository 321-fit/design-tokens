package com.fit321.fitui.v3.tokens

import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import androidx.compose.ui.unit.TextUnit
import com.fit321.fitui.tokens.FitFont

object FitV3Type {

    private fun style(
        size: Int,
        weight: FontWeight,
        lineHeight: Int,
        letterSpacing: TextUnit = TextUnit.Unspecified,
    ) = TextStyle(
        fontFamily = FitFont.family,
        fontWeight = weight,
        fontSize = size.sp,
        lineHeight = lineHeight.sp,
        letterSpacing = letterSpacing,
    )

    val sectionTitle = style(15, FontWeight.SemiBold, 20)

    val rowTitle = style(16, FontWeight.Medium, 21)
    val rowSub = style(13, FontWeight.Normal, 18)
    val rowSubStrong = style(13, FontWeight.Medium, 18)
    val sectionCount = style(13, FontWeight.Medium, 18)
    val rowValue = style(15, FontWeight.Medium, 20)
    val rowValueSub = style(12, FontWeight.Normal, 16)

    val fieldLabel = style(13, FontWeight.SemiBold, 18)
    /** `.cd-f-label span` — "optional", "needed for review": a note beside the label. */
    val fieldLabelNote = style(13, FontWeight.Normal, 18)
    val fieldText = style(16, FontWeight.Normal, 21)
    val fieldHint = style(12, FontWeight.Normal, 16)

    val identityName = style(28, FontWeight.Bold, 34)
    val identityContext = style(14, FontWeight.Normal, 20)
    val compactName = style(22, FontWeight.Bold, 27)
    val compactStats = style(15, FontWeight.SemiBold, 20)

    val profileName = style(24, FontWeight.Bold, 29)
    val statValue = style(18, FontWeight.SemiBold, 23)
    val statLabel = style(12, FontWeight.Normal, 16)

    val actionLabel = style(12, FontWeight.SemiBold, 16)
    val chip = style(14, FontWeight.Medium, 19)
    val pillLabel = style(13, FontWeight.Medium, 18)
    val badgeLabel = style(12, FontWeight.Medium, 16)
    val countBadge = style(12, FontWeight.SemiBold, 16)
    val dateBlockDay = style(18, FontWeight.Bold, 18)
    val panelEmpty = style(14, FontWeight.Normal, 20)

    val nextWhen = style(17, FontWeight.SemiBold, 22)
    val nextWhat = style(13, FontWeight.Normal, 18)
    /** `.cd-i-next--hero .cd-i-when` — the hero card's time, 20 where the plain card is 17. */
    val nextWhenHero = style(20, FontWeight.SemiBold, 25)
    /** `.cd-i-loc` — the place line under the hero card's what. */
    val nextPlace = style(14, FontWeight.Medium, 19)

    val ringValue = style(34, FontWeight.Bold, 36)
    val ringTotal = style(18, FontWeight.SemiBold, 24)
    val rowValueBold = style(14, FontWeight.SemiBold, 19)

    val detailHero = style(32, FontWeight.SemiBold, 38, (-0.5).sp)
    val detailHeroSub = style(14, FontWeight.Normal, 20)
    val sheetTitle = style(18, FontWeight.Medium, 23)
    val menuItem = style(15, FontWeight.Normal, 20)
    val sectionTitleStrong = style(16, FontWeight.Medium, 21)
    val kvLabel = style(14, FontWeight.Normal, 19)
    val kvValue = style(14, FontWeight.Medium, 19)
    val selectionChip = style(16, FontWeight.Normal, 21)
    val coverCaption = style(11, FontWeight.SemiBold, 14, 0.8.sp)
    /** `.cd-m-cover` — a card that is a picture with words on it. */
    val coverKicker = style(11, FontWeight.SemiBold, 14, 1.4.sp)
    val coverTitle = style(21, FontWeight.SemiBold, 26)
    val coverSub = style(13, FontWeight.Normal, 18)
    val coverGo = style(15, FontWeight.SemiBold, 20)
    val roleChip = style(14, FontWeight.SemiBold, 19)
    val stripeMark = style(16, FontWeight.Bold, 20)

    val clipDuration = style(12, FontWeight.SemiBold, 16)

    val statLabelSmall = style(11, FontWeight.Normal, 14)
    val rowTag = style(11, FontWeight.Medium, 14)
    val bubble = style(15, FontWeight.Normal, 21)
    val chatSeparator = style(12, FontWeight.Normal, 16)
    val chatUnread = style(11, FontWeight.SemiBold, 14, 0.5.sp)
    val senderName = style(11, FontWeight.Normal, 14)

    val moneyHeadline = style(24, FontWeight.Bold, 29)
    /** `.cd-mc-big` — the money card's total. */
    val moneyCardTotal = style(28, FontWeight.Bold, 34, (-0.5).sp)
    /** `.cd-mc-v` — one split cell's amount. */
    val moneyCardValue = style(20, FontWeight.Bold, 25, (-0.3).sp)
    /** `.fit-fab-label` — a speed-dial action's word. */
    val fabLabel = style(15, FontWeight.SemiBold, 20)
    val moneyHero = style(48, FontWeight.Bold, 54)
    val moneyHeroCurrency = style(28, FontWeight.SemiBold, 34)

    /** `.ob-q` — the one question a setup step asks. */
    val stepQuestion = style(24, FontWeight.Bold, 29, (-0.4).sp)
    /** `.ob-hint` — why we ask, at most one line of it per step. */
    val stepHint = style(13, FontWeight.Normal, 19)
    val stepCount = style(12, FontWeight.SemiBold, 16)
    val welcomeTitle = style(28, FontWeight.Bold, 32, (-0.5).sp)
    val welcomeLead = style(17, FontWeight.Normal, 25)
    val draftSlot = style(12, FontWeight.Medium, 16)
    val link = style(14, FontWeight.SemiBold, 19)
    val stripTitle = style(12, FontWeight.SemiBold, 16)
    val stripSub = style(12, FontWeight.Normal, 16)
    /** `.sp-card-label` — 14px at the regular weight: a choice, not a heading. */
    val tileLabel = style(14, FontWeight.Normal, 17)
}
