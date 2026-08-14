# FitUI Component Inventory

> **Status:** Phase 2 spec (2026-04-24)
> **Scope:** coach flows + shared modules; native targets (SwiftUI + Compose)
> **Skip:** FitPhoneShell, FitStatusBar (prototype-only)

Every component lists **purpose · required props · optional props · variants · states · sub-elements · where used · iOS/Android notes**. The prototype at `project-spec/prototypes/lib/fit-ui.css` + `flows/` is the canonical visual reference.

---

## PRIMITIVES

### FitButton
**Purpose:** Call-to-action button — 4 tier severity system (primary / secondary / destructive) + size variants.

**Required props:**
- `title: String`
- `action: () -> Void`
- `style: enum { primary, secondary, destructive, destructiveHigh, destructiveLow, destructiveMinimal }`

**Optional props:**
- `size: enum { md (h=50, default), sm (h=40) }`
- `leadingIcon: Icon?`

**Variants (visual tier):**
- `primary` — brand gradient bg, white text, 99px radius
- `secondary` — surface-high bg, text-primary, 1px divider border
- `destructive` (Medium) — `rgba(240,92,91,0.15)` bg, red-400 text
- `destructiveHigh` — red-400 solid bg, white text (irreversible: delete account, template)
- `destructiveLow` — transparent bg, 1px red-400 border, red-400 text (retract own pending)
- `destructiveMinimal` — transparent, red-400 text only (secondary "Cancel" in dialogs)

**States:** default, pressed (opacity 0.85), disabled (opacity 0.7, cursor: not-allowed)

**Used:** sheet footer buttons, screen footer CTA, action row buttons. Extensive coverage across calendar.html / clients.html / settings.html / balance.html / account-access.html.

**iOS/Android notes:**
- iOS: pill shape + UIImpactFeedbackGenerator on tap
- Android: corresponding Material ripple
- Rubik 500 (medium), full width by default; flex:1 when inline siblings

**Status:** ✅ Swift exists; needs refactor to add 4-tier destructive. Compose: to build.

---

### FitIconBtn
**Purpose:** Circular icon-only button (32px) for header right-slot actions, toolbar, inline actions. Visual weight matches `.fit-header-back` so left + right of header read as a single group.

**Required props:**
- `icon: Icon`
- `action: () -> Void`

**Optional props:**
- `color: enum { primary (default), brand, error, success }`
- `tintedBg: Bool = false` — adds background tint matching color
- `style: enum { filled (default), ghost }` — `ghost` drops the plate entirely (transparent bg)
- `contentDescription: String?` — accessibility label (Compose)

**Variants:**
- **default** — translucent blur bg (`rgba(117,126,135,0.3)` + 4px blur) in dark, subtle `surface-high` solid bg in light. Icon color = `text-primary` (matching back chevron). SVG stroke `1.8` (UI rule per `feedback_icon_system`; back chevron uses `2` for nav).
- `tintedBg: true` with `color: error` — used for header trash icons (10% red bg + red stroke).
- `style: ghost` — no plate. For buttons sitting **inside** an already-busy surface, where a filled circle competes with the content it belongs to: the sheet-header action slot (Message, `⋯`) next to a descriptor and a status pill. CSS equivalent: `.fit-sheet-menu-btn` (transparent, `surface-high` only on `:active`).

**States:** default, pressed (scale 0.95)

**Used:** headers (menu, bell, +, trash, share), balance account card actions, notification inbox `⋯` menu trigger.

**iOS/Android notes:** 32×32 container, 16×16 SVG, min 44pt tap target (iOS) / 48dp (Android). On iOS, replicate the dark-theme blur via `.background(.ultraThinMaterial)` masked to a circle; on Compose, use a translucent surface tint.

**Status:** ✅ Swift (`Sources/FitUI/Components/FitIconBtn.swift`), ✅ Compose (`components/FitPrimitives.kt`), ✅ CSS. ⚠️ Both native impls currently use the old spec (`textSecondary` icon, flat `surfaceHigh` bg) — sync to the new spec above is tracked separately.

---

**Icon slot (Android).** As with [FitChip], an overload takes `@Composable (tint: Color, size: Dp) -> Unit`
in place of the `ImageVector`, for hosts that ship their own drawables.

**`background` (Android).** A plate colour stated by the caller wins over `tintedBg`. The
tinted variants here are computed alphas (`error.copy(alpha = .10f)`), which this repo's own
rule says belong in `bg.<status>-subtle` tokens — a caller holding that token should be able
to pass it rather than inherit an alpha that reads differently on white and on gray.800.

### FitIconPlate
**Purpose:** Decorative leading icon container — rounded square with tinted background + centered icon. Used wherever a row / card needs a visual category accent (Dashboard action cards, Settings rows, empty-state CTAs, list category accents). Non-interactive — for tappable plates use `FitIconBtn` with `tintedBg: true`.

**Required props:**
- `icon: Icon` (SF Symbol on iOS / `ImageVector` on Compose / SVG `<symbol>` reference on web)

**Optional props:**
- `tone: enum { info, success, warning, error, brand, neutral } = .neutral`
- `size: enum { sm (24), md (32, default), lg (40) }`

**Sub-elements:** none — pure leaf.

**States:** default. (Decorative; no pressed / hover.)

**Sizing:**

| Size | Container | Icon | Use case |
|---|---|---|---|
| sm | 24×24 | 12 | inline rows, compact lists |
| md | 32×32 | 16 | dashboard action cards, settings row leading |
| lg | 40×40 | 20 | empty-state CTAs, hero rows |

**Custom sizes (feature-driven, Android):**

| Size | Container | Icon | Use case |
|---|---|---|---|
| mdLg | 36×36 | 18 | Coach Dashboard V2 HintCard (`FitHintCard`) |

**Tone → tokens** (read from current FitTheme):

| Tone | Background | Foreground |
|---|---|---|
| info | `theme.bgInfoSubtle` | `FitColors.Blue.b500` |
| success | `theme.bgSuccessSubtle` | `FitColors.Teal.t500` |
| warning | `theme.bgWarningSubtle` | `FitColors.Yellow.y400` |
| error | `theme.bgErrorSubtle` | `FitColors.Red.r400` |
| brand | `theme.bgBrandSubtle` | `FitColors.brandPrimary` |
| neutral | `theme.surfaceHigher` | `theme.textSecondary` |

**Shape:** `RoundedRectangle(cornerRadius: FitRadius.sm)` (8 px).

**Used:** Dashboard action cards (requests=info, cash=success, review=warning), Settings rows (legacy `FitSettingsCard.icon` callsites should migrate), empty-state CTAs.

**iOS/Android/Web notes:** Identical visual API across platforms. Web uses CSS class `.fit-icon-plate` with modifiers `.fit-icon-plate--{tone}` and `.fit-icon-plate--{size}` (`prototypes/lib/fit-ui.css`).

**Status:** ✅ Swift, ✅ Compose, ✅ CSS.

---

**Icon slot (Android).** Alongside the `ImageVector` signature there is an overload taking
`@Composable (tint: Color, size: Dp) -> Unit`, so a host can pass its own drawable. The tint
and size are handed in rather than left to the caller: a plate whose background says "error"
with a grey glyph inside is worse than no plate.

### FitBadge
**Purpose:** Tag/status pill — 12px font, pill shape, color variants.

**Required props:**
- `text: String`
- `style: enum { group, personal, full, joined, pending, special, neutral, success, danger, info, accent, cash, card }`

**Optional props (Android):**
- `icon: ImageVector? = null` — leading glyph, tinted to the style color (icon + text row)
- `bordered: Boolean = false` — 1px border in the style color at 0.3 alpha (outlined pills like CRM)
- `compact: Boolean = false` — dense row tag (10sp + 0.3 letter-spacing, padding 2×6, radius 4); default is the standard pill (12sp, padding 3×10, radius 6). CRM / Deleted / payment-method tags are compact; status pills like "€ owed" stay standard.

Text is single-line (`maxLines = 1, softWrap = false`) — a squeezed badge truncates, never wraps per-letter.

**States:** default (non-interactive). Paired with avatars, event cards, payment chips.

**Used:** "Cash" / "Card" tag on prices, "Group" / "Personal" on events, "CRM" / "Deleted" / "Archived" on client cards.

**iOS/Android notes:** Inline-flex, padding 3px 10px, 6px radius.

**Status:** ✅ Swift exists. Compose: to build.

---

### FitAvatar
**Purpose:** User representation circle — initials or image, 5 sizes.

**Required props:**
- `initials: String` (2 chars)

**Optional props:**
- `size: enum { xs (24), sm (32), md (40, default), lg (48), xl (80) }`
- `bg: enum { brand (gradient), gray, surfaceHigher }`
- `image: URL?` (iOS) / `imageUrl: String?` (Android) — the photo; initials stay underneath
  as the placeholder while it loads and the fallback if it never does
- `textColor` / `fontWeight` (Android) — initials colour and weight; a `bg` that is not the
  brand gradient usually needs the first, a large avatar sometimes the second
- `shape: enum { circle (default), rect10 }` — rect10 for session/template icons

**Off-scale sizes (Android).** `size` also accepts a raw `Dp`. The enum is the scale screens
are drawn against and stays the default choice, but layouts do land between its steps (44, 56,
64), and the alternative to offering them is a consumer rebuilding the avatar to change one
number — after which the initials, the photo fallback and the paid state drift apart. Initials
default to 0.36 of the diameter: the scale's own steps run 0.42 · 0.38 · 0.35 · 0.33 · 0.35,
which averages 0.365, so one rounded ratio fits the scale better than any single step's does.

**States:** default; `paid` variant (opacity 0.5) when participant has paid cash.

**Payment badge** (`FitParticipantPayment`): `cash` · `card` · **`pack`** · `none`. `pack`
is its own state and never folded into "paid": a coach settling up has to tell money in
their hand from a credit that was spent, or the total they are chasing counts seats nobody
owes for.

**Used:** client cards, event sheet avatar, review queue, clients list, coach profile hero.

**iOS/Android notes:** Text is Rubik 500, centered. Image fallback to initials if URL load fails.

**Status:** ❌ Swift missing. Compose: to build.

---

### FitInput
**Purpose:** Text input with label, placeholder, error, secure mode.

**Required props:**
- `label: String`
- `text: Binding<String>`

**Optional props:**
- `placeholder: String?`
- `isSecure: Bool = false`
- `isError: Bool = false`
- `errorText: String?`
- `keyboardType: enum { default, number, email, url, phonePad }`
- `submitLabel: enum { done, next, return }`

**States:** default, focus (1px divider border), error (red border + text), disabled (opacity 0.6).

**Used:** create client, edit profile, create session, invite.

**iOS/Android notes:** Height 56px, radius 12px, font 16px. Password has eye toggle to reveal.

**Status:** ✅ Swift exists. Compose: to build.

---

### FitCheckbox
**Purpose:** Square checkbox 28×28 toggle.

**Required props:**
- `checked: Binding<Bool>`

**Optional props:**
- `label: String?`
- `disabled: Bool = false`

**States:** unchecked (2px text-tertiary border), checked (teal-600 bg + white check), disabled.

**Used:** payment confirmation, "Mark as paid", opt-in toggles in sheets.

**iOS/Android notes:** 6px radius, SVG check 14×14 stroke 3.

**Status:** ❌ Swift missing. Compose: to build.

---

### FitToggle
**Purpose:** iOS-style on/off switch 48×28.

**Required props:**
- `isOn: Binding<Bool>`

**Optional props:**
- `label: String?`
- `disabled: Bool = false`

**States:** off (surface-higher bg, thumb left), on (teal-500 bg, thumb right).

**Used:** settings (notifications, availability, recurring, 2FA).

**iOS/Android notes:** Haptic on change (iOS UIImpactFeedbackGenerator). Animation 0.2s.

**Status:** ❌ Swift missing. Compose: to build (Material 3 `Switch`).

---

### FitStepper
**Purpose:** Number input with ± buttons, min/max bounds, press-and-hold repeat.

**Required props:**
- `value: Binding<Int>`
- `min: Int`
- `max: Int`

**Optional props:**
- `unit: String?` (e.g., "athletes")

**States:** disabled at bounds (min / max), otherwise interactive.

**Used:** invite flow (attendee count), settings (group size).

**iOS/Android notes:** 48×48 buttons, 48px total height. Hold: first tap immediate, then repeat every 100ms after 500ms delay.

**Status:** ❌ Swift missing. Compose: to build.

---

## LAYOUT

### FitHeader
**Purpose:** Screen header — centered title + optional back button + optional right actions.

**Required props:**
- `title: String`

**Optional props:**
- `showBack: Bool = true` (Android; the button still needs an `onBack` to appear)
- `onBack: (() -> Void)?`
- `rightActions: [HeaderAction] = []`
- `trailing` (iOS `@ViewBuilder`, Android `(@Composable () -> Unit)?`) — an arbitrary end
  control. Takes precedence over `rightActions`: a header ends in a menu, a badge or a text
  action at least as often as in a row of glyphs
- `maxLines` / `overflow` / `backTestTag` (Android)

**Sub-elements:**
- `FitHeaderBack` — 32×32 circular back button, 16×16 chevron-left SVG (Android ships the
  drawable as `ic_fit_chevron_left`; it is not a Material glyph)
- Title — reserves 48dp on both sides so a long one cannot slide under the buttons
- Title — 17px semibold (Apple HIG Headline token)

**States:** default.

**Used:** every screen with navigation.

**iOS/Android notes:** Title absolutely centered; right-action group auto-margin-left. Height 48px + padding.

**Status:** ✅ Swift exists. Compose: to build.

---

### FitFooter
**Purpose:** Sticky bottom area — wrapper for either a single CTA or a navbar.

**Variants:**
- **CTA footer** — `padding: 12px 16px 32px` (safe area), single FitButton full-width
- **Navbar footer** — wraps FitNavbar

**Used:** every root tab screen (navbar variant), flow screens (CTA variant).

**Status:** ❌ Swift missing. Compose: to build.

---

### FitNavbar
**Purpose:** Floating 5-tab bottom bar (glassmorphism, pill shape).

**Required props:**
- `tabs: [NavTab]`
- `activeTab: NavTab`
- `onTabChange: (NavTab) -> Void`

**Tabs:** Dashboard, Clients, Calendar, Messages (TBD), Settings.

**Sub-elements:**
- `NavItem` — 56×56 circle, 24×24 stroke SVG icon

**States:** active (selection-gradient bg, white icon), inactive (gray-300).

**Used:** on 5 root tab screens ONLY — per navbar visibility rule.

**iOS/Android notes:** Floating with `margin 0 16px 16px`. Backdrop blur 10px (glassmorphism). Shadow `0 0 24px rgba(0,0,0,0.4)` on dark.

**Compose API note:** Compose `FitNavbar` accepts `items: List<FitNavbarItem>` where each item carries `tab` + `icon: ImageVector` — icon source is decoupled from the component so consumers can supply custom (non-Material) glyphs per tab without changing the library. iOS resolves icons internally via SF Symbols on the `FitNavTab` enum.

**Status:** ✅ Swift, ✅ Compose.

---

### FitCard
**Purpose:** Content container for grouped information.

**Required props:**
- `content: @ViewBuilder View`

**Optional props:**
- `header: CardHeader?` — (title: String, editAction: () -> Void?)
- `footer: View?`

**Sub-elements:**
- `CardHeader` — title 18px 500, optional edit icon right
- `CardRow` — flex align-center, gap 8px, 14px text-secondary, 16×16 icon
- `CardTitle` — 18px 500
- `CardEdit` — chevron-right or edit icon

**States:** default.

**Used:** profile sections, event summary cards, settings groups.

**iOS/Android notes:** 20px padding, 16px radius. **Edge rule (updated 2026-06-12):** Dark = `surfaceDefault` only, tonal contrast (no border, no shadow). Light = `surfaceDefault` + soft drop shadow (`elevation/2` = `0 0 12px rgba(0,0,0,0.07)`), **no border**. The old 1px `divider` border on light was dropped — a hard hairline flashed against the borderless dark card on theme toggle; shadow-on-light + clean-dark keeps toggling seamless. Never a brand/teal edge (reads as selection).

**Compose API addition:** optional `onClick: (() -> Unit)?` for tappable cards (e.g. `Next session`). Same default styling regardless of clickability.

**Status:** ✅ Swift, ✅ Compose.

---

### FitTicket
**Purpose:** Compact event summary card for session lists.

**Required props:**
- `title: String`
- `time: String`
- `coachName: String`
- `coachInitials: String`
- `price: String`

**Optional props:**
- `meta: [String] = []`
- `status: enum? { upcoming, completed, cancelled }`
- `onTap: (() -> Void)?`

**Sub-elements:**
- TicketTop — title + chevron
- TicketMeta — small icons + text
- TicketCoach — 36×36 avatar + name
- TicketPrice — teal-500 color

**States:** default, status variants tint borders/chips.

**Used:** calendar session details, athlete schedule.

**iOS/Android notes:** Padding 16px, radius 12px. Coach avatar is 36×36 with brand gradient.

**Status:** ❌ Swift missing. Compose: to build.

---

## OVERLAYS

### FitSheet
**Purpose:** Bottom sheet modal — slides up, dismissible by swipe-down or overlay tap.

**Required props:**
- `isVisible: Binding<Bool>`
- `content: @ViewBuilder View`

**Optional props:**
- `title: String?`
- `subtitle: String?`
- `statusHeader: SheetStatusHeader?` — (descriptor: String, pill: Badge?, actions: slot?)
- `variant: enum { standard (pad-bottom 40), compact (pad-bottom 28) }`
- `footerVariant: String?` — for event sheets, selects footer from `ev-planned/ev-request/ev-awaiting/ev-review/ev-missed/ev-finished`

**Sub-elements:**
- `SheetHandle` — 36×4 rounded bar
- `SheetStatusHeader` — descriptor (18px 500) + optional FitCalEventPill + optional trailing **actions slot**
- `SheetTitle` — 18px 500
- `SheetItem` — flex row with divider, 16px font
- `SheetWarning` — icon + text, colored bg
- `SheetFooterVariant` — state-selected footer button row

**Event sheet footer variants (by state):**
| State | Footer |
|---|---|
| planned | Message icon + Reschedule (secondary) + Cancel (destructive) |
| request | Decline (destructive) + Accept (primary) |
| awaiting | Cancel request (destructiveLow) |
| review | Complete training (primary) |
| missed | Reschedule (secondary) |
| finished | View history (secondary) |

**States:** hidden, visible (slide-up animation 250ms).

**Used:** everywhere — event detail, fab actions, cancel confirm, delete confirm, time picker, etc.

**iOS/Android notes:**
- iOS: `.sheet()` or custom ModalPresentationStyle with `.medium/.large` detent
- Android: `ModalBottomSheet` (Material 3) with skipPartiallyExpanded
- Swipe-down > 80px dismisses
- Safe area bottom 40px (standard) or 28px (compact)

**Status:** ❌ Swift missing (JS helper exists — `FitUI.openEventSheet()`). Compose: to build.

---

### FitSheetOverlay
**Purpose:** Backdrop for FitSheet — darkens underlying content, tap to dismiss.

**Required props:**
- `isVisible: Binding<Bool>`
- `onDismiss: () -> Void`

**States:** hidden, visible (rgba(0,0,0,0.5)).

**Used:** paired with every FitSheet.

**iOS/Android notes:** Z-index below sheet (50 vs 50+1); on iOS usually provided by `.sheet()`; on Android by `ModalBottomSheet`.

**Status:** ❌ Swift: bundled with FitSheet. Compose: bundled.

---

### FitSnackbar
**Purpose:** Bottom pill notification with optional action link.

**Required props:**
- `message: String`
- `isVisible: Binding<Bool>`

**Optional props:**
- `action: (label: String, callback: () -> Void)?`
- `duration: Int = 4000` (ms)

**States:** hidden (opacity 0, translateY 8px), visible.

**Used:** "Request handled", "Session created", "Undo" prompts.

**iOS/Android notes:**
- iOS: custom overlay view at bottom 100pt, pill shape (99px radius)
- Android: `SnackbarHost` with custom composable, or MD3 Snackbar
- Black bg + 1px white-alpha border

**Status:** ❌ Swift missing. Compose: to build.

---

### FitToast
**Purpose:** Top notification banner (success / error / info). Used for async events, foreground push surfaces, and short non-blocking confirmations.

**Required props:**
- `message: String`
- `isVisible: Binding<Bool>`
- `type: enum { success, error, info }`

**Optional props:**
- `action: (label: String, callback: () -> Void)?` — adds a trailing tap-action button next to the message. Used for "View" / "Fix it" / "Retry" CTAs that route the user from a foreground push toast to the relevant screen.

**Sub-elements:**
- Leading icon (color matches `type`)
- Message text
- **Trailing action button** (when `action` is set) — teal-400 text, no background, mirrors `.fit-snackbar .snack-action`

**States:**
- hidden (opacity 0, translateY -10)
- visible — auto-dismiss **3s** without action / **5s** with action
- Tap body (anywhere except action) → dismiss
- Tap action → fire callback + dismiss

**Used:** "Signed out", "Google Calendar connected", "Password changed", "Your intro video is live · View", "Couldn't process your video · Fix it".

**iOS/Android notes:** Top-anchored, 60pt from top. Radius 12, 3px left border (matching type color). When `action` is set, button sits at the right side with `margin-left: auto`. See [project-spec/specs/notifications.md § Foreground push handling](https://github.com/321-fit/project-spec/blob/main/specs/notifications.md) for the foreground-push → toast conversion contract.

**Status:** ✅ Swift exists (action variant: to extend). Compose: to build.

---

### FitContextMenu
**Purpose:** iOS-style floating popover menu (2–5 options with optional divider).

**Required props:**
- `isVisible: Binding<Bool>`
- `items: [ContextMenuItem]`
- `anchor: View.position`

**Sub-elements:**
- `MenuItem` — icon 18×18 + label 15px + optional destructive tint
- `MenuDivider` — 1px gray-700 line with 4px/8px margin

**States:** hidden, visible (fadeIn + scale 0.96→1 in 120ms).

**Used:** event ⋯ menu (reschedule/cancel/note), client ⋯ menu (profile/history/block/archive).

**iOS/Android notes:**
- iOS: `ContextMenu` modifier works natively for Apple-style look
- Android: `DropdownMenu` composable

**Status:** ❌ Swift missing (HTML prototype has it). Compose: to build (native `DropdownMenu`).

---

### FitEmptyState
**Purpose:** Placeholder for empty lists — illustration + title + subtitle + CTA.

**Required props:**
- `title: String`
- `subtitle: String`
- `illustration: Image` (or system icon)

**Optional props:**
- `action: (label: String, callback: () -> Void)?`

**States:** default (centered, text-center).

**Used:** "No clients yet", "Inbox zero", "No pending requests", review queue "All reviews done".

**iOS/Android notes:** 40×40 illustration (gray-600), 16px title (text-secondary), 14px sub (text-tertiary), 20px margin-bottom before CTA.

**Status:** ❌ Swift missing. Compose: to build.

---

### FitTipCard
**Purpose:** Dismissable suggestion card with teal-stripe accent, lightbulb icon, body text, inline CTA link, and a top-right close button. The kit form of the dashboard `.dash-tip` prototype pattern (Tier 2 onboarding tips on Coach Dashboard V2).

**Required props:**
- `title: String`
- `subtitle: String`
- `cta: String` (link label — the trailing `›` glyph is appended by the component)
- `icon: ImageVector` (typically a lightbulb)
- `onTap: () -> Unit`
- `onDismiss: () -> Unit`

**Optional props:** none.

**Sub-elements:** 3px Teal-500 left stripe (full height); 16×16 icon tinted `FitColors.Yellow.y400`; title 14pt 500 text-primary; subtitle 13pt text-tertiary; CTA 13pt 500 blue-500 with trailing `›`; absolute 24×24 round dismiss button (top-right, 14×14 `Icons.Default.Close` text-tertiary).

**States:** default, pressed.

**Used:** Coach Dashboard V2 Tier 2 tips (Stripe / Hours / Video / Bio onboarding suggestions).

**Notes:** 14dp padding (start/top/bottom), 40dp padding-end (reserves space for the dismiss button); `RoundedCornerShape(14dp)`; `theme.surfaceHigh` background; `1.dp theme.divider` outer border (gives the `var(--fit-gray-100)` outline on light theme, subtle in dark). The whole card is clickable (`onTap`), and the CTA text is independently clickable with the same callback for easier tap targeting on the link.

**Status:** ✅ Compose.

---

### FitHintCard
**Purpose:** Dashed-border onboarding/tip card with leading icon plate + title + subtitle + chevron action. The kit form of the dashboard `.dash-hint` prototype pattern. Used to surface a suggested next-step or contextual hint inside a feed of cards, visually distinct from solid action cards.

**Required props:**
- `title: String`
- `subtitle: String`
- `icon: ImageVector`
- `onClick: () -> Unit`

**Optional props:** none.

**Sub-elements:** leading `FitIconPlate(tone = Info, size = MdLg)` (36×36 plate, 18px icon, blue-500 tint); title 15pt 500 text-primary; subtitle 13pt text-tertiary; trailing 18×18 chevron text-tertiary.

**States:** default, pressed.

**Used:** Coach Dashboard V2 Ready state — "Already coaching offline? Add your existing clients" hint card linking to Recent clients.

**Notes:** 14px padding; 12px row gap; 14px corner radius; `theme.surfaceHigh` background; 1px dashed border in `theme.divider` (Compose: drawn via `drawBehind` + `PathEffect.dashPathEffect(6dp, 4dp)` since Compose `Modifier.border` doesn't natively support dashed strokes on `RoundedCornerShape`).

**Status:** ✅ Compose.

---

## MEDIA

**Icon slot (Android).** Alongside the `ImageVector` signature there is an overload taking
`@Composable (tint: Color, size: Dp) -> Unit`, so a host can pass its own drawable. The tint
and size are handed in rather than left to the caller: a plate whose background says "error"
with a grey glyph inside is worse than no plate.

### FitVideoUploadCard
**Purpose:** 16:9 state-aware video upload card. Owns the full lifecycle of a direct-upload video integration (Mux): picker entry, upload progress, server-side processing, ready-with-thumbnail, error recovery. Coach intro video on Personal Data is the primary consumer; reusable for any place that needs "pick from device → direct-upload → wait for processing → play" in our app.

**Required props:**
- `state: enum { idle, uploading, processing, ready, errored, pending }`
- `onTap: () -> Void` — fired in `idle` / `pending` / `errored` states (opens picker / retry)

**Optional props (per state):**
- `progress: Double` (0…1) — used in `uploading`
- `filename: String` — used in `uploading`
- `thumbnailURL: URL?` — used in `ready` (defaults to Mux auto-thumbnail at `time=2s`)
- `onCancel: () -> Void` — used in `uploading` (× button top-right)
- `onMore: () -> Void` — used in `ready` (⋯ button top-right opens [FitContextMenu](#fitcontextmenu) with Preview / Replace / Remove)
- `errorCode: String?` — used in `errored` (small muted code line)
- `helpText: String?` — caption below the card, state-aware

**States:**
| State | Visual | Tap target |
|---|---|---|
| **idle** | Dashed 1.5px divider border, transparent bg, camera icon (32px, opacity 0.6) + CTA text + meta sub | opens native picker |
| **uploading** | Yellow border + yellow-tinted bg, filename row + linear progress bar + percent label; floating × cancel button top-right | Cancel × aborts |
| **processing** | Yellow border + yellow-tinted bg, spinner (28px) + "Processing…" + sub-line | non-tappable |
| **ready** | Solid divider border, thumbnail fills 16:9 (background-image), centered play overlay 48px white, ⋯ button top-right | Tap plays preview · ⋯ opens menu |
| **errored** | Red border + red-tinted bg, alert-triangle (32px) + title + retry link + optional error code | Tap = Retry (opens picker) |
| **pending** | Same visual as idle + muted "Last upload didn't finish" line | opens picker |

**Sub-elements:**
- `FitVUCIcon` — 32px monochrome icon (camera for idle, alert-triangle for errored)
- `FitVUCSpinner` — 28px circular yellow spinner (0.8s linear infinite)
- `FitVUCProgress` — 6px linear bar with yellow fill
- `FitVUCPlayOverlay` — full-bleed 25% black scrim + centered play SVG
- `FitVUCMoreButton` — 32px circular semi-transparent black with ⋯ icon

**Used:** Coach intro video upload on Personal Data screen.

**iOS/Android notes:**
- Width 100%, `aspect-ratio: 16/9`, radius 12.
- iOS: `PHPickerViewController` for picker (video filter, max 1); background upload via `URLSession.shared.uploadTask` so PUT survives app suspend. Playback uses **Mux Player Swift SDK** (`MuxPlayerSwift` SPM) — owner replaces the kit's Ready slot with `MuxPlayerView` in-place on `onTap`; fullscreen via the player's own corner control (system fullscreen handoff). Preply-style inline playback — no sheet, no push.
- Android: `ActivityResultContracts.PickVisualMedia(VideoOnly)`; `WorkManager` + OkHttp resumable PUT (256KB chunks). Playback uses **mux-player-android** Gradle dependency — same Preply-style inline pattern.
- The kit component itself is **visual-only** (poster + play overlay + ⋯ menu). Playback integration lives at the app level (owner wires the Mux Player view to replace the Ready slot on tap).
- Backend orchestration documented at [project-spec/architecture/mux-integration.md](https://github.com/321-fit/project-spec/blob/main/architecture/mux-integration.md).
- Client-side limits: 200 MB / 5-120 s / mp4-mov-m4v / 480p min. Enforced before requesting direct-upload URL.

**Status:** ✅ HTML/CSS in `prototypes/lib/fit-ui.css` (`.fit-vuc-*` classes). ✅ Swift in `Sources/FitUI/Components/FitVideoUploadCard.swift` (xcodebuild iOS Simulator: BUILD SUCCEEDED). ⏳ Compose in `android/src/main/kotlin/.../components/FitVideoUploadCard.kt` (authored; Gradle verification pending JDK 17 install). Dashed border on Idle / Pending falls back to solid in Compose v1 (Modifier.border has no native dashed support on RoundedCornerShape).

---

## CALENDAR

### FitDayStrip
**Purpose:** Horizontal scrollable day chips with today-highlight + event dots.

**Required props:**
- `month: Int (1-12)`
- `year: Int`
- `selectedDay: Binding<Int>`

**Optional props:**
- `todayDay: Int?` (highlight current day)
- `events: [Int: [EventType]]` (day → [personal, group, external])
- `mode: enum { nav, select } = .nav` — `nav` drives a connected timeline (calendar.html); `select` is the "pick a day" affordance for invite / group preview flows

**Sub-elements:**
- `FitDayButton` — **public sub-component**, 50×62 rounded card, 16px radius. Composable standalone (outside the strip) for invite / select flows that render a small set of explicitly-chosen days rather than a full month.
- `DayChipName` — 10px uppercase tertiary (Mon, Tue…)
- `DayChipNum` — 16px 500 (or 600 selected)
- `DayChipDots` — up to 3 colored dots (4×4 each)

**States:** default, today (brand-primary number), selected (selection-gradient bg + teal-600 border).

**Used:** calendar.html main view, invite.html time selection, group session schedule preview.

**iOS/Android notes:** Horizontal `ScrollView` (iOS) / `LazyRow` (Compose) with snap-to-center. Smooth scroll 300ms on tap. `FitDayButton` exposed publicly as `FitDayButton(year:month:day:isSelected:isToday:events:onTap)` (Swift) / `FitDayButton(year, month, day, isSelected, isToday, events, onClick)` (Compose) — same API shape as the strip cell.

**Status:** ✅ Swift, ✅ Compose, ✅ CSS.

---

### FitCalEvent
**Purpose:** Colored block representing an event on a timeline. Adaptive 3-tier layout (tiny / compact / standard) derived from tile height + cross-role variant for users with both roles active.

**Required props:**
- `title: String`
- `time: String`
- `type: enum { personal, group, external, crossRole(FitRole) }`
- `height: CGFloat` / `Dp` (drives tier derivation)

**Optional props:**
- `recipient: String?` — counterparty / group ratio. Examples:
    - Personal coach view: `"Anna K."`
    - Personal athlete view: `"with Coach Mark"`
    - Group (any view): `"7/10 athletes"`
    - Cross-role: counterparty in the OTHER role
- `location: String?` — rendered only on Standard tier
- `status: enum { planned, request, awaiting, review, missed, finished } = .planned`
- `overlapped: Bool = false` — marks the tile as overlapping with another event in the same time window. Adds red-tinted gradient overlay (#705959 → #BB7F7F, matches iOS `Theme.Gradient.overlappedEvent`) + 8pt corner dot. Works additively on top of any type/status — coach can still see the underlying tile color underneath. Use when client overlap detection (interval intersection between events on the same day) flags a conflict, typically between a 321Fit event and an external Google/Apple event pulled in post-factum.

**Tier derivation:**
- `tiny`: height ≤ 30pt — 1 row: `{title} · {start-time}` inline
- `compact`: height ≤ 45pt — 2 rows: title / `{recipient} · {time}`
- `standard`: height ≥ 46pt — 3 rows: title / `{recipient} · {time}` / `📍 {location}`

**Variants (type × status combos)** — two axes per [event-statuses.md § 5b](../../project-spec/specs/event-statuses.md#5b-calendar-visual-language-updated-2026-07-24): **fill/tint = TYPE**, **border = ACTION**. All tints are theme-aware (read the `bg.*-{subtle,tinted}` tokens — a light tint is the darker accent step at higher alpha, not the same bright accent):
- personal — **teal tint (`bg.brand-subtle`, both themes)** + 3pt teal-500 solid stripe
- group — **blue tint (`bg.info-subtle`)** + 3pt blue-500 solid stripe
- external — gray-700 / gray-200 bg, opacity 0.7, 3pt text-tertiary solid stripe (neutral surface = not-a-training)
- crossRole(role) — surface-high bg, opacity 0.75, 3pt **dashed** text-tertiary stripe, role-tag badge anchored bottom-right (no status pill — actions belong to the other role)
- custom — surface-high bg, opacity 1.0 (your own event — no muting), 3pt **solid** text-tertiary stripe, no role tag, **no status pill** (custom events are stateless — they don't participate in the 6-state lifecycle per event-statuses.md). Default title "My time" when caller passes empty title.
- Actionable / terminal statuses override the type fill (act-on-it = own tint + solid perimeter); awaiting has NO fill (dashed border alone = you-wait); planned / finished keep the type tint:
    - request / review — `bg.cal-action-subtle` fill + 1pt yellow-600 solid perimeter
    - awaiting — **1pt yellow-600 DASHED perimeter + `bg.cal-tentative` fill** (transparent in dark, 70% white in light)
    - missed — `bg.error-subtle` fill + red-400 perimeter
    - finished — opacity 0.5

**Why the calendar has its own two tint tokens** (`bgCalActionSubtle`, `bgCalTentative`) instead of reusing `bg.warning-subtle` / `surface-high`:
- `bg.warning-subtle` is the general heads-up surface (banners, alerts) and sits on **yellow.400** in dark — brighter than a calendar tile should read next to a teal or blue one. The calendar tint stays on **yellow.600** in both themes: `0.10` dark / `0.20` light, matching `.fit-cal-event.request` in the kit.
- Awaiting is a *tentative card*, so it cannot use a surface fill: dark = **transparent**, letting the dashed perimeter float over the grid; light = **70% white**, because on the `#F2F2F7` canvas a transparent card dissolves into the background entirely. `surface-high` gave a solid card in both themes, which read as an ordinary booked tile.

**Left accent stripe:** the 3pt accent is painted as the **left slice of a rounded-rect stroke**, not as a rectangle inside the clipped card — so the colour follows the top-left and bottom-left corner arcs, exactly as `border-left: 3px` + `border-radius` renders in CSS. A plain rectangle gets bitten off by the corner clip and reads as a bar pasted onto the tile. Cross-role keeps its dashed vertical bar.

**Sub-elements:**
- title row — title (12pt 500, 10pt on tiny) + optional FitCalEventPill (hidden on cross-role)
- meta row — 12pt secondary, `{recipient} · {time}` or just `{time}`
- location row — 11pt tertiary + pin glyph, only on standard tier
- FitRoleTag — bottom-right corner badge on cross-role tiles (tier ≥ compact)

**Drag states:**
- `dragging` — lifted: raised z-order + drop shadow, no colour change
- `dragging` + `invalid` — hovering a target that refuses the drop (occupied, blocked, or outside available hours): red-tinted fill (`bg.error-subtle`), 1pt red-400 perimeter, red-400 title and meta. Same grammar as the booking grid's invalid selection block, so "red block = it can't go there" is learned once. The reason is named by the snackbar, not by the tile.

**3pt hairline gap (Apple Calendar style):**
Every tile reserves a 3pt transparent strip at the bottom so back-to-back events don't visually merge. The visible card lives inside the outer container with `padding(.bottom, 3)` — outer still occupies the full inline height so neighbours don't reflow.

**Used:** calendar timeline main content (coach + athlete).

**iOS/Android notes:** Outer is `.frame(height:)` on iOS / `.height()` on Compose. Tier is derived from `height` parameter — caller doesn't pass tier directly.

**Status:** ✅ Swift implemented. ✅ Compose implemented (2026-05-20 rewrite — recipient + location + crossRole + 3-tier).

---

### FitRoleTag
**Purpose:** Compact corner badge labelling the user role context of an event ("Athlete" or "Coach"). Used by FitCalEvent in cross-role state — placed at the bottom-right of the tile to signal "this event lives on your OTHER role profile."

**Required props:**
- `role: FitRole` (athlete | coach)

**Visual:**
- 18pt tall capsule, 10pt 500 font, 8pt horizontal padding
- Subtle wash background (rgba(0,0,0,0.05) — reads on both light gray-100 and dark surface-high cross-role tile bgs)
- Icon: `figure.run` (SF Symbol) / `DirectionsRun` (Material) for athlete, `person.fill` / `Person` for coach
- Color: text-tertiary

**Used:** FitCalEvent cross-role variant. Could be reused for other places where role context needs an inline label (currently only calendar event tiles).

**Status:** ✅ Swift implemented. ✅ Compose implemented (new 2026-05-20).

---

### FitCalEventPill
**Purpose:** Inline badge next to event title or in sheet status header (4 status colors).

**Required props:**
- `status: enum { request, review, awaiting, missed }`

**States:** fill weight carries *"is this on me?"*, not just which state:
- request / review — **filled** yellow-600, white text. Someone owes an answer.
- awaiting — **outlined**: transparent bg, 1pt yellow-600 border, yellow-600 text. Same hue as request, opposite weight — you are only waiting. Horizontal/vertical padding drops 1px (5/1 instead of 6/2) so the border sits inside the same overall pill size and a row of pills keeps one baseline. Mirrors the tile's dashed perimeter.
- missed — **filled** red-400, white text.

The earlier all-filled version (#17) is retired: it made a passive wait shout as loudly as a request, which is the one distinction this pill exists to draw. Canon: `.fit-cal-event-pill--*` in `fit-ui.css` + event-statuses.md § status pills.

**Used:** inside FitCalEvent title row, inside FitSheet status header.

**iOS/Android notes:** 11px 500 font (documented exception — Apple Caption 2), 2/6 padding filled · 1/5 outlined, 99px radius.

**Status:** ✅ Compose (`components/FitOverlays.kt`), ✅ CSS. ⚠️ Swift still on the old spec (grey `awaiting` fill) — iOS is paused, so parity is a tracked follow-up, not a gap in this change.

---

### FitTimeline
**Purpose:** 24-hour vertical grid container for calendar view. See [event-statuses.md § 5b](../../project-spec/specs/event-statuses.md#5b-calendar-visual-language-updated-2026-07-24) for the reasoning behind everything below.

**Required props:**
- `events: [CalEvent]`
- `onEventTap: (CalEvent) -> Void`

**Optional props:**
- `currentTime: Time?` (draws "now" line)
- `offHours: [TimeRange]` — outside the coach's published availability
- `blocked: [TimeRange]` — taken *inside* working hours (external calendar event, time off)
- `dragging: Bool = false` — the grid is in drag-targeting mode

**The grid is ALWAYS 00:00–24:00.** Day height must never depend on availability — a grid that starts at the first available hour changes shape per coach and per day. On appear, scroll to the now-line when it falls inside the working band, otherwise to the first available hour; that is what makes a full 24h grid free of cost.

**Sub-elements:**
- Hour — 96pt tall, border-top divider, hour label 10pt tertiary. The rule runs to the **trailing screen edge** (leading is offset by the time gutter, Apple Calendar convention): the ruler is chrome, not content.
- NowLine — 2pt brand-primary with 8×8 dot at the leading edge
- **OffHoursBand** — outside availability. Flat tonal wash (`rgba(0,0,0,0.35)` dark / `rgba(60,60,67,0.10)` light), **full-bleed past the container's horizontal padding to the true screen edge, no corner radius** — a background belongs to the surface, only content respects the gutter. Inside the band the hour rules fade to ~35% of the divider and both edges carry an inner shadow, so the band reads as a **recessed well** rather than a differently-coloured strip: "disabled" is loss of definition plus depth, not a hue. A 12pt 500 tertiary label ("Outside your hours") is centred on **each contiguous band** — hidden when the band is under 32pt. Hour labels sit *under* the band (they recede with it) at opacity 0.75 dark / 0.6 light, raised to pay for the wash above them.
  - Optional enhancement: ≤ 1pt backdrop blur under the band. **Never load-bearing** — the faded rules and recessed edges carry the meaning, so absence on Android < API 31 (`Modifier.blur`) costs nothing. Do not exceed 1pt: at 1.6pt a 10pt hour label smudges into an unreadable blob, and those labels are how the user navigates.
- **BlockedBand** — taken inside working hours. Diagonal hatch (gray-700 dark / gray-300 light at 0.3), **inset and rounded like a card**. Hatch means "busy" and nothing else; shape carries the difference from OffHoursBand (full-bleed band = a state of the day, inset rounded block = an object occupying a slot).

**States:**
- default — events re-layout on selected day change
- `dragging` — off-hours bands deepen and drop their labels (a tile parked over a band would otherwise have the caption reading through its semi-transparent fill). The bands **never turn red**: flooding half the day with an error colour is alarm without information and competes with the tile the user is looking at. The verdict rides on the dragged FitCalEvent instead (see its `invalid` state).

**Used:** calendar main view (coach + athlete). **Athlete calendars pass no `offHours`** — an athlete has no availability, so there is nothing to grey out; the bands belong to the coach calendar and to booking grids, where the counterparty's hours constrain the choice.

**iOS/Android notes:** Each event positioned absolutely; hour height 96pt. The full-bleed band needs to escape the container padding — negative horizontal insets on iOS, `layout` / negative offset on Compose. Legend of every state: [calendar-legend.html](https://321-fit.github.io/project-spec/prototypes/flows/shared/calendar-legend.html).

**Status:** ❌ Swift missing. Compose: to build.

---

## LOADING

### FitSkeleton (family)
**Purpose:** Shimmer-animated placeholders for async content.

**Family members:**
- `FitSkeletonCard` — 16px padding, 12px radius, surface-high bg
- `FitSkeletonRow` — horizontal flex with avatar + lines slot
- `FitSkeletonCircle` — default 44×44 round; `.sm` 40×40; `.sq` 10px radius (icon)
- `FitSkeletonLine` — 12px height, 6px radius; `.short` 50% width
- `FitSkeletonBlock` — 14px height, 7px radius
- `FitSkeletonBtn` — 40px height, 99px radius
- `FitSkeletonStrip` — 36px height with top border
- `FitSkeletonShimmer` — applies the shimmer animation to any of the above

**States:** animated shimmer (1400ms ease-in-out infinite).

**Used:** list loading states (clients, dashboard review queue, invite templates, settings sessions).

**iOS/Android notes:** Linear gradient animation from gray-800 to gray-700 (dark) or gray-200/100 (light). Animation-duration matches token.

**Status:** ❌ Swift missing. Compose: to build (use `Modifier.placeholder` + custom shimmer).

---

## LISTS

### FitSelectRow
**Purpose:** Single-select list item with optional icon, label, chevron or checkbox.

**Required props:**
- `label: String`
- `isSelected: Bool`
- `onSelect: () -> Void`

**Optional props:**
- `icon: Icon?`
- `trailing: enum { chevron, toggle, check, none }`

**States:** unselected (surface-higher bg, transparent border), selected (selection-gradient bg + teal-600 border).

**Used:** calendar selection sheets, language/timezone pickers in settings.

**iOS/Android notes:** 48px min height, 10-12px padding, 12px radius. Check SVG 22×22.

**Status:** ✅ Swift exists. Compose: to build.

---

**Icon slot (Android).** Alongside the `ImageVector` signature there is an overload taking
`@Composable (tint: Color, size: Dp) -> Unit`, so a host can pass its own drawable. The tint
and size are handed in rather than left to the caller: a plate whose background says "error"
with a grey glyph inside is worse than no plate.

### FitSettingsCard
**Purpose:** Settings / location / space row with icon + title + subtitle + chevron + optional "Default" badge.

**Required props:**
- `icon: Icon`
- `title: String`

**Optional props:**
- `subtitle: String?`
- `addressOrSubtitle: String?` — single-line ellipsised subtitle for `.location` / `.space` contexts (overrides `subtitle` when present)
- `context: enum { settings, location, space } = .settings` — drives the leading container styling
- `isDefault: Bool = false` — when true, shows a trailing "Default" `FitBadge(.accent)` next to the title
- `trailing: enum { chevron, toggle(Binding<Bool>), value(String), none }`
- `destructive: Bool = false`
- `onTap: (() -> Void)?`

**Variants (`context`):**
- `settings` — bare 24×24 icon at full theme.textPrimary contrast (default Settings list)
- `location` / `space` — 40×40 rounded square (`surfaceHigher` bg, `radius-sm`) wrapping the icon — visually echoes `FitParticipant` so location / saved-space rows read at the same weight as participant rows in mixed lists

**States:** default, pressed, with-default-badge, with-address (ellipsised), destructive.

**Used:** settings screens (profile, notifications, language, accounts, etc.); location settings (Saved Locations); workout-space management.

**iOS/Android notes:** 12px padding, 16px radius. Settings icon is 24×24 bare; location/space icon container is 40×40 with `surfaceHigher` bg. `addressOrSubtitle` always single-line ellipsis; `subtitle` in `.settings` context allows up to 2 lines. Light theme has box-shadow for elevation.

**Status:** ✅ Swift, ✅ Compose, ✅ CSS.

---

**Icon slot (Android).** Alongside the `ImageVector` signature there is an overload taking
`@Composable (tint: Color, size: Dp) -> Unit`, so a host can pass its own drawable. The tint
and size are handed in rather than left to the caller: a plate whose background says "error"
with a grey glyph inside is worse than no plate.

### FitParticipant
**Purpose:** User-or-entity row with leading avatar/icon + name + subtitle + trailing affordance + optional state tint. Pervasive across event sheets, clients lists, accounts list, integration providers, balance cash toggle.

**Required props:**
- `name: String`
- `subtitle: String`
- `leading: enum { avatar(initials, bg), icon(systemName, tone) } = .avatar(...)` — `.icon` reuses the row layout for non-user entries (calendar provider rows, integrations)

**Optional props:**
- `trailing: enum { chevron, edit, dot(color), swipe, none } = .chevron` — `.swipe` is informational; the swipe gesture is wired by the caller (`.swipeActions` on iOS, `SwipeToDismissBox` on Compose)
- `state: enum { default, connected, disconnected, disabled, destructive } = .default` — tints the row background (`bg.success-subtle` / `bg.warning-subtle` / `bg.error-subtle`), the name color, and (for `.disabled`) reduces overall opacity
- `isRemovable: Bool = false` + `onRemove: (() -> Void)?` — legacy explicit remove button (red ⊗) — when set, takes precedence over the trailing enum
- `isPaid: Bool = false` — dims avatar + uses `text-secondary` for the name
- `payment: enum { cash, card, none } = .none` — appends a `FitBadge(.neutral)` Cash/Card pill next to the subtitle
- `isYou: Bool = false` — selection-gradient row highlight, `text-on-brand` subtitle
- `onTap: (() -> Void)?`

**States:** default, paid (dimmed name + avatar), you (selection-gradient bg), connected (success tint), disconnected (warning tint), disabled (60% opacity, strikethrough name, no tap), destructive (error tint, red name), swipe-left (remove action), swipe-right (mark paid).

**Used:** event sheet participants, clients list (swipe remove), balance cash toggle, accounts list (Settings → Accounts) where each row maps to a connected provider with status, integrations.

**iOS/Android notes:**
- iOS: SwiftUI `.swipeActions` for native feel; new `leading` / `trailing` enums replace the old implicit chevron-then-X pattern; the **legacy initializer** `init(name:, subtitle:, avatarInitials:, isRemovable:, onRemove:, isPaid:, payment:, isYou:)` is preserved so existing call sites compile unchanged.
- Android: `SwipeToDismissBox` from Material 3; `leading: FitParticipantLeading` is sealed with `Avatar` / `IconPlate` cases.

**Status:** ✅ Swift, ✅ Compose, ✅ CSS.

---

### FitChip
**Purpose:** Single, standalone tag-like selectable button (toggleable on/off). For grouped exclusive or multi-select choices use `FitSelectionGroup` instead.

**Required props:**
- `label: String`
- `isSelected: Binding<Bool>` (iOS) / `isSelected: Boolean` + `onClick` (Android)

**Optional props:**
- `icon: Icon?` (iOS `systemImage`) / `icon: ImageVector?` (Android)
- `size: enum { sm (h=40), md (h=48, default), lg (h=56) } = .md`

**Icon slot.** Android also takes `leading: @Composable () -> Unit` instead of `icon`, the
counterpart of the `@ViewBuilder` slots the SwiftUI side already uses. A host that ships its
own drawables cannot express them as an `ImageVector` and would otherwise rebuild the chip to
change one glyph. The slot is required in that overload — with a default the two signatures
are ambiguous at every call that passes neither.

**Variants (`size`):**
- `sm` — 40px height, `body2` font (14pt), used inline in compact filter rows
- `md` — 48px height, `body1` font (16pt), default form chip
- `lg` — 56px height, `body1` font (16pt), prominent toggle (matches `FitInput` height for tall layouts; rare)

**States:** unselected (surface-high bg, no border), selected (selection-gradient bg, selection-border).

**Used:** standalone toggleable tags. Most form-style chip groups should use `FitSelectionGroup` instead.

**iOS/Android notes:** Per `size`. Always `radius-md`, `sp-2` gap inside. Selection animation 150ms ease-in-out.

**Status:** ✅ Swift, ✅ Compose, ✅ CSS.

---

### FitSelectionGroup
**Purpose:** Equal-width chip group with single-select (radio-like) or multi-select mode. Replaces the need for separate FitRadio component — a single API serves both modes via the binding shape.

**Required props:**
- `options: [Option]` — generic over any `Hashable` value type
- `label: (Option) -> String` — human-readable label per option
- One of:
  - `selection: Binding<Option?>` (iOS) / `selectedSingle + onSelectSingle` (Android) — **single-select**
  - `selection: Binding<Set<Option>>` (iOS) / `selectedMulti + onSelectMulti` (Android) — **multi-select**

**Variants:**
- Single mode — tapping a chip selects it and deselects others (radio behavior)
- Multi mode — tapping toggles each chip independently

**States:**
- Unselected chip: `surfaceHigh` bg, `textPrimary` text, no border
- Selected chip: `selectionGradient` bg, 1px `selectionBorder`, `textPrimary` text
- Transition: 150ms ease-in-out on selection change

**Used:** Personal/Group toggle, Recurring/One-off toggle, payment method (Cash/Card), online provider (Zoom/Meet/Custom). Whenever a form needs a small fixed set of mutually-aware choices.

**iOS/Android notes:** 48px height per chip, equal-width via `flex: 1` (iOS `frame(maxWidth: .infinity)` per chip; Android `Modifier.weight(1f)`). Gap `sp-2` between chips, no horizontal padding on the group itself (caller adds container padding).

**Status:** ✅ Swift `FitSelectionGroup.swift`. Compose `FitLists.kt`.

---

### FitSegmented
**Purpose:** iOS-native segmented control / tab switcher. Equal-width buttons inside a "selection well" container; tapping a button slides selection to it. Use for **navigation-style switching of screen modes / filters** (e.g. Archived / Blocked tabs on the Archived & Blocked screen, or any 2–3 way exclusive view filter). **Not a form input** — for form-style mutually-aware option choice use `FitSelectionGroup` instead.

**Why separate from `FitSelectionGroup`:** different UX semantics + different visual contract. `FitSelectionGroup` is a chip group answering "which option are you choosing in this form?" (each chip stands alone, has its own border on selection). `FitSegmented` is a slid-pill container answering "which view / filter mode am I in?" (single grouped well with a moving selection pill, no border, brand gradient on the active tab).

**Required props:**
- `options: [Option]` — generic over any `Hashable` value type, typically an enum with 2–3 cases
- `selection: Binding<Option>` (iOS) / `selected: Option` + `onSelectedChange: (Option) -> Unit` (Android) — **always single-select**, no nullable / multi-select variant by design
- `label: (Option) -> String` — human-readable label per option

**Optional props:**
- `count: ((Option) -> Int?)?` — when set, appended to the label as ` (N)` for tabs that show a counter (e.g. "Archived (3) / Blocked (2)"). `nil` returned for an option means "no counter for this one".

**Variants:** none — always single-select pill switcher. Container is full-width by default (caller wraps in horizontal padding).

**States:**
- Container: `surfaceHigh` bg, `radius-md` (12pt), `sp-1` (4pt) padding all sides
- Selected button: `selectionGradient` bg, `textOnBrand` color, `radius-sm` (8pt), no border
- Unselected button: transparent bg, `textSecondary` color, `radius-sm` (8pt), no border
- Vertical button padding `sp-2` (8pt). Equal-width via `flex: 1` (iOS `frame(maxWidth: .infinity)`; Android `Modifier.weight(1f)`).
- Selection transition 150ms ease-in-out on bg + color crossfade.
- Font: `button2` (medium 16) — slightly larger than the 14pt rendered in the prototype, but stays within the existing token set; do not introduce a `medium-14` font token for one component.

**Used:**
- `s-archived` (Clients module): Archived / Blocked tabs with counters
- Future: any screen-mode tab switcher (e.g. coach profile tabs if introduced; any 2–3 way filter row at top of a list)

**Not used for:** form input options (Cash/Card, Personal/Group, Recurring/One-off) — those stay on `FitSelectionGroup` because they answer "what choice?" not "what mode?".

**iOS/Android notes:** Container is `padding(sp-1)` + `background(surfaceHigh).clipShape(roundedRectangle(radius-md))`. Buttons are `frame(maxWidth: .infinity)` + vertical padding `sp-2` + `clipShape(roundedRectangle(radius-sm))`. Animation `easeInOut(0.15)` on the binding change (matches `FitSelectionGroup`).

**Status:** ✅ Swift `FitSegmented.swift`. Compose `FitLists.kt` (alongside FitSelectionGroup). CSS `fit-ui.css` `.fit-segmented` / `.fit-segmented-tab`.

---

## SOCIAL

### FitRating
**Purpose:** Interactive 5-star rating display with tap-to-rate.

**Required props:**
- `rating: Binding<Int>` (0-5)

**Optional props:**
- `size: enum { small (28), medium (36, default), large (48) }`
- `readOnly: Bool = false`

**States:** default, filled per rating, hover (scale 1.1), active (scale 1.2).

**Used:** reviews on coach profile, post-session athlete rating.

**iOS/Android notes:** Stroke-style stars (1.8 stroke); fill on tap. Gap 10px.

**Status:** ❌ Swift missing. Compose: to build.

---

### FitProfileHeader
**Purpose:** Profile / coach hero — large avatar + name + metadata row + optional bio + optional trailing edit pencil. Used at the top of own-profile, viewed-coach, viewed-athlete screens.

**Required props:**
- `initials: String` (or `imageURL: URL?` on iOS for the loaded photo)
- `name: String`

**Optional props:**
- `sports: [String] = []` — joined with ", " into the metadata row
- `location: String?` — first segment of the metadata row
- `bio: String?` — body1 text below the header row, multi-line
- `onEdit: (() -> Void)?` — when set, renders a trailing `FitIconBtn` with the edit pencil

**Sub-elements:** `FitAvatar(.xl)` leading, name (24pt 600), metadata row (`body2`, text-tertiary, `loc · sports` joined by `·`), optional bio block (`body1`, text-secondary).

**States:** default, with-edit (trailing icon button visible), text-only (no avatar image, initials).

**Used:** profile.html (own + viewed), coach detail, athlete detail.

**iOS/Android notes:** 80×80 avatar (`FitSize.avatarXl`); 24pt 600 name; metadata row joined by ` · `. Edit affordance is `FitIconBtn(systemName: "pencil")` (iOS) / `FitIconBtn(icon: Icons.Default.Edit)` (Compose).

**Status:** ✅ Swift, ✅ Compose, ✅ CSS.

---

### FitStatStrip
**Purpose:** Horizontal 4-column readout (Rating / Reviews / Sessions / Price from) used on coach profile in both athlete-view (`s-coach-v2`) and coach-view (`s-coach-profile`). Read-only, system-computed values. One column may be visually accented (e.g. price-from in teal).

**Required props:**
- `items: [Item]` — variable-length but typically 4 columns. Each `Item` carries `value: String`, `label: String`, `accent: Bool = false`, and an optional `tone`.

**Tone:** `neutral` (default) · `accent` (teal — the good number: a price, a total earned) · `warning` (yellow) · `danger` (red — money owed, an overdue count). `accent: true` still resolves to `.accent`, so existing call sites are unchanged. A debt rendered in brand green reads as praise for money the coach has not been paid — that is what the tone exists to prevent.

**Sub-elements:** flex row with equal-width columns + 1px vertical dividers (`theme.divider`). Each column: value (18pt 600 `theme.textPrimary`, or `Teal.t500` if `accent`) + label (12pt `theme.textTertiary`). Container: 12px×16px padding (vertical × horizontal), 12px corner radius, `theme.surfaceHigh` background. CSS class `.fit-stat-strip` + `-col`, `-value`, `-value--accent`, `-label`, `-divider`.

**States:** default (real values), zero-state (`—` / `0`s with the price keeping `accent: true` so the column still reads as the "from" price).

**Used:** coach profile (athlete-view + coach-view); future athlete dashboards may reuse.

**iOS/Android notes:** Use `FitStatStripItem` (Swift `Item` / Compose data class) with `FitStatStripTone` / `FitStatStrip.Tone`. Compose enforces a 32dp tall divider column (vertically centered) to match the CSS divider height; iOS expands the `Rectangle` to fill row height. Accent color is `FitColors.Teal.t500` on both platforms — not theme-aware (price is a brand-accent signal, deliberate).

**Status:** ✅ Swift (`Sources/FitUI/Components/FitStatStrip.swift`), ✅ Compose (`components/FitStatStrip.kt`), ✅ CSS (`.fit-stat-strip` + sub-elements).

---

### FitMaturityProgress
**Purpose:** "You're a new coach" graduation block on the coach-side profile. Leading icon plate + title + sub-copy + check-list of graduation criteria + optional Learn-more link. Frames the new-coach period as benefits, not burden (search-rank boost on graduation per memory `project_coach_maturity`).

**Required props:**
- `title: String`
- `subtitle: String`
- `criteria: [Criterion]` — each `Criterion` carries `label: String`, `done: Bool`

**Optional props:**
- `learnMoreLabel: String = "Learn more"`
- `onLearnMore: (() -> Void)?` — when omitted, the chevron link is hidden

**Visibility policy:** caller-controlled. Render only while `reviews_count < 1 OR sessions_count < 3` (auto-hide after graduation). The component itself does not gate.

**Sub-elements:** outer card 14px radius, 1px `theme.divider` border, 16px padding. Header row = `FitIconPlate(success, .md)` star + 15pt 500 title. Sub = 13pt `theme.textSecondary`. Each criterion row = 18dp circle (`Teal.t500` filled when `done`, outlined `theme.divider` otherwise) + label (13pt; strike-through + `textTertiary` when `done`). Learn-more = brand-color text + 13dp chevron.

**States:** default (some criteria pending), partial (some done — strike-through applied), all-done (caller should stop rendering — this is the post-graduation state, no UI fallback inside the component).

**Used:** coach profile (`s-coach-profile`), new-coach states only.

**iOS/Android notes:** Use `FitMaturityCriterion(label, done)` data class. Rule per memory `project_coach_maturity`: threshold `reviews >= 1 AND sessions >= 3` for graduation; component is agnostic, caller computes done state per criterion.

**Status:** ✅ Swift (`Sources/FitUI/Components/FitMaturityProgress.swift`), ✅ Compose (`components/FitMaturityProgress.kt`), ✅ CSS (`.fit-maturity-progress` + sub-elements).

---

### FitReviewCard
**Purpose:** 280pt-wide snap card for a single review entry — reviewer avatar + name + relative time + 5-star rating + 4-line clamped body. Plus a trailing "Show all N reviews" variant that closes the carousel.

**Variants:**
- `.review(reviewer, initials, when, stars, body)` — default content card
- `.showAll(total)` — terminal CTA card with arrow + "Show all N reviews"

**Optional props:**
- `onTap: (() -> Void)?` — tap routing (review → expanded sheet; showAll → all-reviews push screen)

**Sub-elements:** 280pt fixed width, 14px radius, 14px padding. Head row = `FitAvatar(.sm, gray)` + reviewer name (14pt 500) + when (12pt `theme.textTertiary`). Stars row = 5 × 12pt stars (`Teal.t500` filled, `theme.textTertiary` empty), 2px gap. Body = 14pt `theme.textSecondary`, 4-line `lineClamp` with ellipsis. CSS classes `.fit-review-card`, `.fit-review-card-head`, `-name`, `-when`, `-stars`, `-body`, `--show-all`.

**Reviewer avatar bg** is fixed `Gray.g600` (not theme-aware) — visual hierarchy intent: reviewers are subordinate to the coach themselves and shouldn't compete with the brand-gradient main avatar.

**States:** default; truncated (>4 lines, "Show more" link can be applied at carousel level on tap).

**Used:** coach profile reviews carousel (athlete-view + coach-view), All Reviews screen (single-column list reuses the same card).

**iOS/Android notes:** Use `FitReviewCardData` sealed/enum hierarchy with `.Review` + `.ShowAll` cases. Compose carousel scrolls horizontally via `horizontalScroll(rememberScrollState())`; iOS uses `ScrollView(.horizontal, showsIndicators: false)`. Snap behavior preserved via scroll-padding alignment.

**Status:** ✅ Swift (`Sources/FitUI/Components/FitReviewCard.swift` — includes `FitReviewCarousel`), ✅ Compose (`components/FitReviewCard.kt` — includes `FitReviewCarousel`), ✅ CSS (`.fit-review-card` + `.fit-review-carousel`).

---

### FitProfileHero
**Purpose:** 16:9 hero media block at the top of the coach profile (both athlete-side view and coach-side preview). Renders one of three media variants with a fallback chain: video → cover image → brand-gradient + initials. Optional camera-overlay edit affordance (top-right) wired to a media picker.

**Required props:**
- `media: enum { video(URL), cover(URL), initials(String) }`

**Optional props:**
- `onEdit: (() -> Void)?` — when set, renders the 36×36 camera button in the top-right corner. When nil, hero is read-only (e.g., athlete viewing a coach).

**Sub-elements:** `.fit-profile-hero` 16:9 wrapper, three `.fit-profile-hero-media--*` sibling divs (only one shown via parent `.has-video` / `.has-cover` state class, default fallback otherwise), `.fit-profile-hero-edit` overlay button (36×36 with `rgba(0,0,0,0.5)` + blur), `.fit-profile-hero-initials` for fallback rendering.

**States:** has-video / has-cover / default fallback.

**Used:** coach-side `s-coach-profile`, athlete-side `s-coach-v2`. Future: athlete profile (their own).

**iOS/Android notes:** Video renders via native player (AVPlayer on iOS, ExoPlayer on Android) in production — prototype + Swift component show a play-glyph placeholder. Cover image via `AsyncImage` (iOS) / `coil-compose` (Android) with the gradient as loading/error fallback. Initials use `FitColors.brandGradient` background.

**Status:** ✅ Swift (`Sources/FitUI/Components/FitProfileHero.swift`), ✅ Compose (`components/FitProfileHero.kt`), ✅ CSS (`.fit-profile-hero` + sub-elements). Prototype page-local `.cp-hero*` classes in `flows/coach/profile.html` to be refactored to use the kit class in a follow-up.

---

### FitInviteRow
**Purpose:** Referral / invite list row — avatar + name + when (e.g. "Joined 1 week ago"). Optional trailing slot for Phase 2 status pills / chevrons. Used in `invite-coach` flow (coach-to-coach referral) on both main screen inline list and All Invites push.

**Required props:**
- `initials: String`
- `name: String`
- `when: String` — relative timestamp text rendered as-is (no formatting inside the component)

**Optional props:**
- `onTap: (() -> Void)?` — tap routing (e.g. open invitee detail in Phase 2)
- `trailing: View?` — optional trailing slot. MVP omits (no pill). Phase 2: status pill (Pending / Joined / Active) or chevron.

**Sub-elements:** `.fit-invite-row` 12px-padded card, `.fit-invite-row-body` flex column with `.fit-invite-row-name` (15pt 500) + `.fit-invite-row-when` (13pt tertiary), `.fit-invite-row-trailing` flex-shrink slot. Reuses `FitAvatar(.md, .brand)` as leading.

**States:** default, pressed (if `onTap` provided).

**Used:** `flows/coach/invite-coach.html` main screen inline list (3 rows) + push screen (12 rows). Future: athlete-side referral list (mirror).

**iOS/Android notes:** Compose API exposes `trailing` as `(@Composable () -> Unit)?`; Swift uses ViewBuilder generic `Trailing: View` with `EmptyView` convenience overload. Surface: `theme.surfaceHigh` on dark, `Gray.white` + `theme.divider` 1px border on light.

**Status:** ✅ Swift (`Sources/FitUI/Components/FitInviteRow.swift`), ✅ Compose (`components/FitInviteRow.kt`), ✅ CSS (`.fit-invite-row` + sub-elements). Prototype page-local `.ic-invite-row*` in `flows/coach/invite-coach.html` to be refactored to use the kit class in a follow-up.

---

### FitSectionTitle—warm variant (`--md`)
**Purpose:** 16pt medium normal-case section header. Profile / settings sections use this **warm** variant rather than the kit's default `.fit-section-title` (12pt UPPERCASE bold), which is too cold for human-facing sections like "My Sports" / "About Me" on the coach profile.

**Class composition:**
- `.fit-section-title--md` — standalone label
- `.fit-section-title--md-row` — wrapper for label + optional trailing edit pencil
- `.fit-section-title--md-row.tap` — whole-row tappable cursor + hover

**No native widget — typography-only.** Swift uses `Font.custom(FitFont.family, size: 16).weight(.medium)` with `.foregroundColor(theme.textPrimary)`, no `.uppercase()` modifier. Compose uses `FitFont.body1.copy(fontSize = 16.sp, fontWeight = FontWeight.Medium)`.

**Used:** coach-profile sections (My Sports, About Me, Reviews), settings.html row headings, invite-coach hero + section labels.

**Status:** ✅ CSS (`.fit-section-title--md` + `--md-row`). Native: typography token references already present in `FitFont`. Document here so consumers know to NOT call `.uppercase()` / `.textTransform: uppercase` on these labels.

---

## DASHBOARD / STATS

### FitStatTile
**Purpose:** Card-level stat row — leading `FitIconPlate` + title + optional subtitle + optional chevron. The kit form of the dashboard `.dash-action` pattern (26 prototype callsites). The `Large` size variant also covers the prototype `.boost-card` pattern (optional onboarding boost suggestions).

**Required props:**
- `title: String`

**Optional props:**
- `subtitle: String?` — rendered only when non-null; with null the row collapses to title-only height (used when backend omits the secondary metric, e.g. `oldestAtHours` in `RequestsAction`)
- `icon: Icon?` (SF Symbol / ImageVector)
- `tone: FitIconPlate.Tone = .neutral` — `.info / .success / .warning / .error / .brand / .neutral` — drives the icon plate background + color
- `showChevron: Bool = true`
- `onTap: (() -> Void)?`
- `size: FitStatTileSize = .Default` — `Default` for `.dash-action`-style rows (32 icon, 12dp vertical padding); `Large` for `.boost-card`-style rows (36 icon, 14dp vertical padding)

**Sub-elements:**

| Size | Icon plate | Vertical padding |
|---|---|---|
| Default | `FitIconPlate(size = Md)` — 32×32, 16px icon | 12dp |
| Large | `FitIconPlate(size = MdLg)` — 36×36, 18px icon | 14dp |

Title 15pt 500 text-primary single-line; subtitle 13pt text-tertiary single-line (omitted entirely when null); trailing 18×18 chevron text-tertiary.

**States:** default, pressed.

**Used:** coach dashboard action cards (Pending requests, Awaiting reviews, Today's earnings — Default size), Tier 1 / UnderReview optional boost cards (Stripe / Hours / Video — Large size), athlete home stat cards, balance summary blocks.

**iOS/Android notes:** 14px horizontal padding (both sizes); `FitRadius.lg` corner; `theme.surfaceHigh` bg with 1px `theme.divider` border. On light theme the border is the visible affordance (no shadow needed). Full-width within container; row gap `sp-3`. **Android:** `subtitle: String? = null` + `size: FitStatTileSize` enum.

**Status:** ✅ Compose (subtitle nullable + size variants).

---

### FitTransactionRow
**Purpose:** Balance / payouts list row — leading `FitIconPlate` keyed to txn type, title + subtitle, trailing semantic-colored amount. The kit form of the balance.html `.aa-row` / txn pattern (92 prototype callsites).

**Required props:**
- `type: enum { earning, payout, refund }`
- `title: String`
- `subtitle: String`
- `amount: String` (already-formatted, e.g. `"+€42.00"` / `"−€10.50"`)

**Optional props:**
- `date: String?` — small footnote below the amount (right column)
- `isPending: Bool = false` — switches the amount color to `text-tertiary` (e.g. clearing payouts)
- `onTap: (() -> Void)?`

**Variants (`type`):**
- `earning` — `FitIconPlate(.success, "arrow.up.right")`, amount `Teal.t500`
- `payout` — `FitIconPlate(.success, "arrow.right")`, amount `Teal.t500`
- `refund` — `FitIconPlate(.error, "arrow.uturn.left")`, amount `Red.r400`
- `isPending` (any type) — amount renders in `text-tertiary` regardless

**States:** default, pressed.

**Used:** balance.html transaction list, coach payouts history, athlete top-up history.

**iOS/Android notes:** 32×32 leading icon plate; title 15pt 500 single-line; subtitle 13pt text-tertiary single-line; amount 15pt 500 right-aligned with optional 11pt date below. Padding `sp-3` vertical, no horizontal padding (caller adds container padding).

**Status:** ✅ Swift, ✅ Compose, ✅ CSS.

---

### FitNotificationRow
**Purpose:** Notification inbox feed row — leading `FitIconPlate` keyed to notification type, title + 2-line subtitle, trailing relative timestamp, optional unread brand dot. Specifically distinct from `FitTransactionRow` (which has semantic-colored amount in trailing slot and payment-typed variants); notifications need timestamp trailing + unread-state visual cue.

**Required props:**
- `type: enum { request, reschedule, approved, cancelled, declined, expired, onboardingDone }`
- `title: String`
- `subtitle: String`
- `time: String` (already-formatted relative or short absolute, e.g. `"2h ago"` / `"Apr 14"`)

**Optional props:**
- `isUnread: Bool = false` — renders a 6×6 brand-primary dot at the row's left edge; title also de-emphasizes (weight + color) when read
- `onTap: (() -> Void)?` — fires before navigation; caller handles optimistic mark-read + routing per `TargetData`

**Variants (`type` → leading `FitIconPlate` color/icon):**
- `request` — `FitIconPlate(.brand, "calendar.badge.plus")` — for `*CreatedTrainingRequest` events
- `reschedule` — `FitIconPlate(.warning, "clock")` — for `*RescheduledTraining` events
- `approved` — `FitIconPlate(.success, "checkmark.circle")` — for `trainingRequestApproved`
- `cancelled` — `FitIconPlate(.error, "xmark.circle")` — for `trainingEventCancelled`
- `declined` — `FitIconPlate(.neutral, "xmark.circle")` — for `trainingRequestDeclined` (low-severity, neutral not error)
- `expired` — `FitIconPlate(.neutral, "clock.badge.xmark")` — for `pendingRequestAutoDeclined`
- `onboardingDone` — `FitIconPlate(.success, "person.crop.circle.badge.checkmark")` — for `athleteOnboardingCompleted`

**States:** default, pressed (background `surface-high` on tap), unread (`isUnread = true`)

**Used:** `s-notifications` (notifications inbox in coach/athlete dashboard module). Kit form of the `flows/coach/dashboard.html` `.notif-row` pattern.

**iOS/Android notes:**
- 32×32 leading icon plate via existing `FitIconPlate`; title 15pt 500 single-line ellipsis (de-emphasizes to 400 / `text-secondary` when read); time 13pt `text-tertiary` flex-shrink-0; subtitle 13pt `text-tertiary` 2-line clamp; vertical padding `sp-3`, left padding `22px` to clear unread dot
- Unread dot: 6×6 circle, `brand.primary` fill, absolute-positioned at left edge (8px from row leading), vertically centered to first line of title
- Tap: `withAnimation(.fitFast)` strip the unread dot + de-emphasize title BEFORE navigation fires (optimistic). On iOS use `UIImpactFeedbackGenerator` light tap on row press
- Decoupled from notification taxonomy: kit owns `type` enum and visual mapping; backend `TargetRoute` → kit `type` translation lives at the call site (a single helper enum on each platform)

**Status:** ❌ Swift missing, ❌ Compose missing, ✅ CSS (`flows/coach/dashboard.html` `.notif-row`).

---

### FitEarningsHero
**Purpose:** Balance / earnings hero card — large amount + period label, optional trend pill, optional 60/40 breakdown grid, optional planned-line. The kit form of the balance.html hero (33 prototype callsites).

**Required props:**
- `amount: String`
- `period: String`

**Optional props:**
- `trend: enum { up(percent), down(percent), flat }?` — renders as a `FitBadge` (`.success` for up, `.danger` for down, `.neutral` for flat) in the header trailing slot
- `breakdown: [FitEarningsHeroBreakdown] = []` — up to 2 entries (60/40 split row below the amount)
- `planned: String?` — small footnote line at the bottom (e.g. "+€80 planned this week")
- `isEmpty: Bool = false` — replaces the amount with `—` and hides trend / breakdown / planned (empty state)

**Sub-elements:** amount (28pt 600 text-primary), period (13pt text-tertiary), divider, breakdown columns (each: 13pt label text-tertiary + 16pt 500 value text-primary), planned (13pt text-tertiary).

**States:** default, with-trend, with-breakdown, with-planned, empty (`isEmpty: true`).

**Used:** balance.html top hero, coach earnings dashboard hero.

**iOS/Android notes:** 16px padding, `FitRadius.lg`, `theme.surfaceHigh` bg. Header row uses `firstTextBaseline` alignment so trend pill aligns with amount baseline. Breakdown columns split `0.6 : 0.4` weight.

**Status:** ✅ Swift, ✅ Compose, ✅ CSS.

---

### FitPaymentMethodCard
**Purpose:** Provider selector / connected-method card — leading 40×40 logo container + title + subtitle (with optional status dot) + trailing button or "Coming soon" badge. Used in payment / payout / balance settings (7 prototype callsites).

**Required props:**
- `logoLetter: String` — first letter of provider name (e.g. "S" for Stripe). Replaced with `logo: Image?` once we have proper provider artwork.
- `title: String`
- `subtitle: String`

**Optional props:**
- `status: enum { connected, actionRequired, notConnected, comingSoon } = .notConnected`
- `actionLabel: String?` — label for the trailing button (e.g. "Connect", "Manage")
- `onAction: (() -> Void)?`
- `disabled: Bool = false` — entire card at 60% opacity, no tap
- `onTap: (() -> Void)?`

**Variants (`status`):**
- `connected` — leading 6×6 success dot before subtitle, optional `actionLabel` button (e.g. "Manage")
- `actionRequired` — leading 6×6 warning dot before subtitle, optional `actionLabel` button (e.g. "Resolve")
- `notConnected` — no dot, optional `actionLabel` button (e.g. "Connect")
- `comingSoon` — no dot, trailing `FitBadge("Coming soon", .neutral)` instead of a button

**States:** default, disabled (60% opacity, no tap).

**Used:** Settings → Payments / Payouts; balance.html account list.

**iOS/Android notes:** 14px × 12px padding, `FitRadius.md`. Logo container 40×40, `radius-sm`, `theme.surfaceHigher` bg, 18pt 600 letter. Title 15pt 500. Trailing button is the secondary `FitButton` style at `.sm` size; on Compose it's inlined as a pill (FitButton always fills width in Compose, so the row uses an inline pill that matches the secondary style).

**Status:** ✅ Swift, ✅ Compose, ✅ CSS.

---

### FitAvailabilityDay
**Purpose:** Coach availability row for one day-of-week — header (`FitCheckbox` + day name) + collapsible time-interval list + add/remove buttons + optional validation error. Used in coach onboarding (availability step) and Settings → Availability (22 prototype callsites).

**Required props:**
- `day: enum DayOfWeek` (`.monday` … `.sunday`)
- `isActive: Binding<Bool>`
- `intervals: Binding<[Interval]>` — array of `(start: String, end: String)`

**Optional props:**
- `onAddInterval: (() -> Void)?`
- `onRemoveInterval: ((Int) -> Void)?` — index into the intervals array
- `onIntervalEdit: ((Int) -> Void)?` — index into the intervals array (caller opens the time picker)
- `validationError: String?` — appended below the intervals as `text-error`, 13pt

**Sub-elements:** header row (`FitCheckbox` 28×28 + day label 16pt 500 — text-primary when active, text-tertiary when inactive); intervals list (visible only when `isActive`); each interval row: `time-input` 56×56 box + "to" + 56×56 box + remove ⊗; "Add interval" button (text + plus icon, brand-primary); error text 13pt `Red.r400`.

**States:** inactive (header only, day name dimmed), active (intervals visible), with-error (red text below intervals).

**Used:** coach onboarding availability step, Settings → Availability.

**iOS/Android notes:** Time inputs are 56×56 buttons (not free-form text fields) — caller opens the platform-native time picker on tap (UIDatePicker on iOS, Material3 TimePicker on Compose) per `feedback_native_pickers`. Intervals padded by `checkboxSize + sp-3` from the left, aligning with the day label baseline.

**Status:** ✅ Swift, ✅ Compose, ✅ CSS.

---

### FitProgressBar
**Purpose:** The bare determinate bar — track + proportional fill, no label. Anywhere a row already carries its own counter line ("6 of 20 used · 14 left") and only needs the bar underneath it: package buyers, pack cards, pack detail summaries.

**Required props:**
- `progress: Double/Float` — fraction, clamped to `[0, 1]` rather than drawn out of range

**Optional props:**
- `tone: FitProgressTone = .brand` — `brand` (teal, ordinary progress) · `warning` (yellow — the measured thing is running out and someone should act) · `neutral` (`theme.textTertiary`)
- `track: FitProgressTrack = .surface` — `surface` (`theme.surfaceHigh`) for a bar on the screen background; **`divider`** for one inside a plain list row, where `surfaceHigh` sits so close to the background that an empty bar disappears and "0 of 5 used" loses its picture
- `height = 4` — 4px is the in-row default; capacity bars use 8/12, the packages buyer row uses 6

**Sub-elements:** track (`theme.surfaceHigh`, `radius-md`); fill (tone color, `radius-md`, width = `progress`).

**States:** empty (**nothing** is drawn — a zero-width rounded fill still paints its caps and reads as "a little bit done"), partial, full.

**Used:** `FitSpotCounter` is built on it; package Buyers rows and pack cards (session packages).

**iOS/Android notes:** Tone → color mapping is identical on both platforms; `warning` is `FitColors.warning`, not a red. Swift measures with `GeometryReader`, Compose with `fillMaxWidth(ratio)`.

**Status:** ✅ Swift (`Sources/FitUI/Components/FitProgressBar.swift`), ✅ Compose (`components/FitProgressBar.kt`), ⬜ CSS (prototype uses page-local `.pkg-bar` / `.pk-prog` — promote when a third web consumer appears).

---

### FitSpotCounter
**Purpose:** Group-training capacity bar — 12px tall fill bar with proportional teal fill and centered "X of Y spots" label. Used on group session cards / event sheets (33 prototype callsites). **Draws via [FitProgressBar]** — it adds the capacity semantics and the label, and deliberately holds no second copy of the bar.

**Required props:**
- `available: Int`
- `total: Int`

**Optional props:**
- `showLabel: Bool = true` — when false, render just the bar (compact filter rows)
- `compact: Bool = false` — switches the bar to 8px tall (fits inside compact cards)

**Sub-elements:** track (`theme.surfaceHigh`, `radius-md`); fill (`Teal.t500`, proportional width = `available / total`); label (11pt 500 text-primary, centered overlay).

**States:** default, full (fill = 100%), empty (fill = 0%, label still shown), compact (8px tall).

**Used:** group session ticket / event sheet capacity, Discover screen group cards.

**iOS/Android notes:** Fill width clamps to `[0, 1]` of `available / total` with `total` defaulted to 1 if zero (avoids division-by-zero). Label is overlaid via `ZStack` (Swift) / `Box(contentAlignment = Center)` (Compose).

**Status:** ✅ Swift, ✅ Compose, ✅ CSS.

---

## AUTH

### FitPasswordRule
**Purpose:** Vertical list of password validation rules — 12×12 check icon (text-tertiary if unmet, Teal.t500 if met) + 14pt rule text. Used under password input on signup / reset / change-password.

**Required props:**
- `rules: [(label: String, isMet: Bool)]`

**Sub-elements:** each rule = 12×12 icon (checkmark when met / circle when unmet) + 14pt label.

**States:** all-unmet (all gray), partial-met (teal checks for met rules), all-met (all teal).

**Used:** signup, password reset, change-password screens, settings → security.

**iOS/Android notes:** 6px gap between rules; icon-to-label gap `sp-2`. Met rules use `text-secondary` for the label (more contrast than tertiary, signals "yes you got this"); unmet rules use `text-tertiary`.

**Status:** ✅ Swift, ✅ Compose, ✅ CSS.

---

## SUMMARY

| Category | Components | Swift ✅ | Build ❌ |
|---|---|---|---|
| Primitives | 8 | 4 | 4 + Button refactor |
| Layout | 5 | 1 | 4 |
| Overlays | 6 | 1 | 5 |
| Calendar | 4 | 0 | 4 |
| Loading | 1 family | 0 | 1 |
| Lists | 4 | 2 | 2 |
| Social | 2 | 0 | 2 |
| Dashboard / Stats | 6 | 6 | 0 |
| Auth | 1 | 1 | 0 |
| **Total native** | **37** | **14** | **22 + refactor** |

Compose: **37 new** (no existing Android components).

**Gap-batch additions (2026-04-28):**
- Extensions: FitParticipant (leading/trailing/state), FitChip (size), FitSettingsCard (context/isDefault/addressOrSubtitle), FitDayStrip (mode + standalone FitDayButton)
- New: FitStatTile, FitTransactionRow, FitEarningsHero, FitPaymentMethodCard, FitAvailabilityDay, FitSpotCounter, FitProfileHeader, FitPasswordRule

**Coach-profile additions (2026-05-11):**
- New: FitStatStrip (4-column readout), FitMaturityProgress (new-coach progress block), FitReviewCard + FitReviewCarousel — all surfaced during `flows/coach/profile.html` build. CSS + Swift + Compose landed together.

**Coach-profile + Invite-coach additions (2026-05-12):**
- New: FitProfileHero (16:9 hero with 3-variant media fallback + camera overlay), FitInviteRow (referral list row with optional trailing slot), FitSectionTitle--md warm variant (16pt medium normal-case section header). CSS + Swift + Compose landed together; prototype-side migration from page-local `.cp-hero*` / `.ic-invite-row*` is TODO follow-up.

## Meta notes

- All sizes, radii, colors, durations reference generated tokens from `design-tokens/tokens/*.json`
- 4 documented exceptions for font size per `feedback_spacing_typography` memory
- Destructive button tier system per `feedback_destructive_actions` memory
- Navbar visibility per `feedback_navbar_visibility` memory
- Sheet layout rules per `feedback_sheet_rules` memory

## How to update this doc

1. When adding/renaming a component in prototype — update this file in the same PR
2. When changing a prop contract — log the change in commit message and update consumers
3. When this doc and a platform impl disagree — doc is canonical unless the commit specifically updates this file to reflect a new decision
