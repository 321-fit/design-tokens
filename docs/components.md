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
**Purpose:** Circular icon-only button (32px) for header, toolbar, inline actions.

**Required props:**
- `icon: Icon`
- `action: () -> Void`

**Optional props:**
- `color: enum { primary (default), brand, error, success }`
- `tintedBg: Bool = false` — adds background tint matching color

**Variants:**
- default — surface-high bg, text-secondary icon
- `tintedBg: true` with `color: error` — used for header trash icons (10% red bg)

**States:** default, pressed (scale 0.95)

**Used:** headers (menu, bell, +, trash), balance account card actions.

**iOS/Android notes:** 32×32 container, 16×16 SVG, min 44pt tap target (iOS) / 48dp (Android).

**Status:** ❌ Swift missing. Compose: to build.

---

### FitBadge
**Purpose:** Tag/status pill — 12px font, pill shape, color variants.

**Required props:**
- `text: String`
- `style: enum { group, personal, full, joined, pending, special, neutral, success, danger, info, accent, cash }`

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
- `image: URL?`
- `shape: enum { circle (default), rect10 }` — rect10 for session/template icons

**States:** default; `paid` variant (opacity 0.5) when participant has paid cash.

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
- `showBack: Bool = false`
- `onBack: (() -> Void)?`
- `rightActions: [HeaderAction] = []`

**Sub-elements:**
- `FitHeaderBack` — 32×32 circular back button, 16×16 chevron-left SVG
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

**Status:** ❌ Swift missing. Compose: to build.

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

**iOS/Android notes:** 20px padding, 16px radius. Dark: surface-high + 1px gray-600 border. Light: white + `0 0 12px rgba(0,0,0,0.07)` shadow.

**Status:** ❌ Swift missing. Compose: to build.

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
- `statusHeader: SheetStatusHeader?` — (descriptor: String, pill: Badge?)
- `variant: enum { standard (pad-bottom 40), compact (pad-bottom 28) }`
- `footerVariant: String?` — for event sheets, selects footer from `ev-planned/ev-request/ev-awaiting/ev-review/ev-missed/ev-finished`

**Sub-elements:**
- `SheetHandle` — 36×4 rounded bar
- `SheetStatusHeader` — descriptor (18px 500) + optional FitCalEventPill
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
**Purpose:** Top notification banner (success / error / info).

**Required props:**
- `message: String`
- `isVisible: Binding<Bool>`
- `type: enum { success, error, info }`

**States:** hidden (opacity 0, translateY -10), visible (3-second auto-dismiss).

**Used:** "Signed out", "Google Calendar connected", "Password changed".

**iOS/Android notes:** Top-anchored, 60pt from top. Radius 12, 3px left border (matching type color).

**Status:** ✅ Swift exists. Compose: to build.

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

**Sub-elements:**
- `DayChip` — 50×62 rounded card, 16px radius
- `DayChipName` — 10px uppercase tertiary (Mon, Tue…)
- `DayChipNum` — 16px 500 (or 600 selected)
- `DayChipDots` — up to 3 colored dots (4×4 each)

**States:** default, today (brand-primary number), selected (selection-gradient bg + teal-600 border).

**Used:** calendar.html main view, invite.html time selection.

**iOS/Android notes:** Horizontal `ScrollView` with snap-to-center. Smooth scroll 300ms on tap.

**Status:** ❌ Swift missing (JS data-attr pattern exists). Compose: to build (LazyRow).

---

### FitCalEvent
**Purpose:** Colored block representing an event on a timeline.

**Required props:**
- `title: String`
- `time: String`
- `type: enum { personal, group, external }`
- `status: enum { planned, request, awaiting, review, missed, finished }`

**Optional props:**
- `isTiny: Bool = false` (<15min events)
- `badge: String?`
- `rescheduled: Bool = false`

**Variants (status × type combos):**
- personal.planned — surface-high bg + 3px teal-500 left border
- personal.request — yellow tint bg + yellow-600 borders
- personal.awaiting — gray borders, subtle bg
- personal.review — yellow-600 borders (same as request)
- personal.missed — red-400 borders + red tint bg
- personal.finished — opacity 0.5 + teal stripe
- group.* — same states with cyan accents
- external — gray-700 bg, 0.7 opacity, 12px title, no meta

**Sub-elements:**
- CalEventTitleRow — title + optional pill
- CalEventTitle — 12px 500 (10px tiny)
- CalEventMeta — 12px text-secondary (hidden in tiny)
- CalEventPill — inline state badge

**States:** per status above; hover for desktop (prototype-only).

**Used:** calendar timeline main content.

**iOS/Android notes:** Positioned absolutely on timeline with `top` = minutes from 00:00 * pixels/min, `height` = duration * pixels/min.

**Status:** ❌ Swift missing. Compose: to build.

---

### FitCalEventPill
**Purpose:** Inline badge next to event title or in sheet status header (4 status colors).

**Required props:**
- `status: enum { request, review, awaiting, missed }`

**States:**
- request/review — yellow-600 bg, white text
- awaiting — gray-500 bg, white text
- missed — red-400 bg, white text

**Used:** inside FitCalEvent title row, inside FitSheet status header.

**iOS/Android notes:** 11px 500 font (documented exception — Apple Caption 2), 2/6 padding, 99px radius.

**Status:** ❌ Swift missing. Compose: to build.

---

### FitTimeline
**Purpose:** 24-hour vertical grid container for calendar view.

**Required props:**
- `events: [CalEvent]`
- `onEventTap: (CalEvent) -> Void`

**Optional props:**
- `currentTime: Time?` (draws "now" line)
- `unavailableHours: [Int]`

**Sub-elements:**
- Hour — 96px tall, border-top divider, hour label 10px tertiary
- NowLine — 2px brand-primary with 8×8 dot at left edge
- UnavailableBlock — red-tinted diagonal stripe pattern

**States:** default. Events re-layout on selected day change.

**Used:** calendar main view.

**iOS/Android notes:** Each event positioned absolutely; hour height 96px (matches prototype tokens.json update).

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

### FitSettingsCard
**Purpose:** Settings row with icon + title + subtitle + chevron/toggle.

**Required props:**
- `icon: Icon`
- `title: String`

**Optional props:**
- `subtitle: String?`
- `trailing: enum { chevron, toggle(Binding<Bool>), value(String), none }`
- `destructive: Bool = false`
- `onTap: (() -> Void)?`

**States:** default, pressed.

**Used:** settings screens (profile, notifications, language, accounts, etc.).

**iOS/Android notes:** 12px padding, 16px radius, 24×24 icon. Light theme has box-shadow for elevation.

**Status:** ✅ Swift exists. Compose: to build.

---

### FitParticipant
**Purpose:** User row with avatar + name + sport/detail + optional swipe actions or remove button.

**Required props:**
- `name: String`
- `subtitle: String`

**Optional props:**
- `avatar: String` (initials)
- `isRemovable: Bool = false`
- `onRemove: (() -> Void)?`
- `isPaid: Bool = false`
- `paymentType: enum { cash, card, none }`
- `isYou: Bool = false` (highlight)

**States:** default, paid (dimmed name + avatar), you (selection-gradient bg), swipe-left (remove action), swipe-right (mark paid).

**Used:** event sheet participants, clients list (swipe remove), balance cash toggle.

**iOS/Android notes:**
- iOS: SwiftUI `.swipeActions` for native feel
- Android: `SwipeToDismissBox` from Material 3

**Status:** ❌ Swift missing. Compose: to build.

---

### FitChip
**Purpose:** Single, standalone tag-like selectable button (toggleable on/off). For grouped exclusive or multi-select choices use `FitSelectionGroup` instead.

**Required props:**
- `label: String`
- `isSelected: Binding<Bool>` (iOS) / `isSelected: Boolean` + `onClick` (Android)

**Optional props:**
- `icon: Icon?` (iOS `systemImage`) / `icon: ImageVector?` (Android)

**States:** unselected (surface-high bg, no border), selected (selection-gradient bg, selection-border).

**Used:** standalone toggleable tags. Most form-style chip groups should use `FitSelectionGroup` instead.

**iOS/Android notes:** 48px height, `radius-md`, `body1` font, `sp-2` gap inside.

**Status:** ✅ Swift `FitChip.swift`. Compose `FitLists.kt`.

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

## SUMMARY

| Category | Components | Swift ✅ | Build ❌ |
|---|---|---|---|
| Primitives | 8 | 4 | 4 + Button refactor |
| Layout | 5 | 1 | 4 |
| Overlays | 6 | 1 | 5 |
| Calendar | 4 | 0 | 4 |
| Loading | 1 family | 0 | 1 |
| Lists | 4 | 2 | 2 |
| Social | 1 | 0 | 1 |
| **Total native** | **29** | **7** | **22 + refactor** |

Compose: **29 new** (no existing Android components).

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
