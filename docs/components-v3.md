# FitUI v3 — the rework grammar (Compose only)

> **Status:** all 17 pieces of issue #54 built in Compose; screens next
> **Scope:** Android / Compose only. SwiftUI is deliberately not built here — the iOS side is the iOS team's call.
> **Canonical source:** `project-spec/prototypes/flows/coach/dashboard-drafts.html` (the `.k-alpha` block) for the tinted look, `flows/athlete/athlete-drafts.html` (the `.fit-light` rules) for the light one, and `specs/rework-grammar.md` §1–§4 for measurements. Where the spec table and the prototype disagree, the prototype wins.

## Why v3 is a separate package

The rework's table in issue #54 lists ten existing components as **CHANGE**. Changing them in place would repaint every shipped v2 screen the moment the library is rebuilt, and we are not migrating the app wholesale yet. So:

- everything in the rework is built as a **new** component under `com.fit321.fitui.v3`;
- nothing under `com.fit321.fitui.components` / `.theme` / `.tokens` is touched;
- a v3 screen imports `com.fit321.fitui.v3.*` and nothing from the v2 packages. Mixing the two in one file is the only way to hit a name clash, and that mixing is exactly what the split exists to prevent.

The v3 names follow the spec (`FitPanel`, `FitRow`, `FitIdentity`, …) rather than carrying a `V3` prefix, so the package path is what disambiguates. If the app ever retires v2, the package can be flattened without renaming call sites.

## Two looks, one grammar

`FitV3Look.Tinted` is the coach canvas; `FitV3Look.Light` is the athlete one. The shapes, measurements and behaviour are identical — only the palette differs. The light look is **not a new colour system**: it reuses the shipped light theme (`FitColors.Gray.*`, `elevation.2.light`), because the athlete prototype draws the new grammar on the existing light canvas.

| Piece | Tinted | Light |
|---|---|---|
| Canvas | teal gradient top→bottom + a radial glow whose centre sits above the top edge | `#F2F2F7` + the same brand breath at roughly half the alpha |
| Surface (panel, card, chip, input, segmented, stepper, select row) | `rgba(0,0,0,0.28)` + inset 1px `rgba(255,255,255,0.06)` | white + the existing card lift (`FitElevation.fitCardElevation`), no hairline |
| Raised (avatar, plate, face) | `rgba(255,255,255,0.14)` | `gray.100` |
| Face in a stack | opaque `#2b5f6a` — translucent faces show each other through | `gray.200` |
| Text | `#fff` · 74% · 52% | `gray.900` · `gray.600` · `gray.500` |
| Divider | `rgba(255,255,255,0.10)` | `gray.200` |
| Primary CTA | white with depth (`#fff → #f3f7f8 → #e2ebee`, inset highlight, drop shadow), label `#12161a` | the brand gradient, unchanged |
| Secondary button | `rgba(255,255,255,0.10)` + 1px `rgba(255,255,255,0.18)` | white + 1px `gray.200` |
| Sheets / menus / snackbars | material `rgba(5,24,31,0.80)` + blur 28 | `#F2F2F7` |
| Bars over scrolling content | `rgba(4,22,29,0.88)` | `#F2F2F7` |

Three rules that fall out of the table and are easy to break:

1. **Nothing opaque-dark on the tinted canvas.** Every surface is an alpha darkening of the canvas. Inputs, textareas, wheels, select lists, bars and bottom cards included. The only exceptions are media: video, map, photo.
2. **Blur belongs to overlays, never to cards.** A sheet is a layer above the screen and the screen showing through is what says so; a card that blurs is just noise.
3. **The edge is drawn differently per look.** Tinted draws it with the inset hairline, light draws it with the shadow. A component reads `surfaceHairline` / `surfaceLifted` from the palette and never branches on the look itself.

## Tokens

Source of truth is `tokens/v3.json`; the Compose mirror is `android/src/main/kotlin/com/fit321/fitui/v3/tokens/FitV3Colors.kt`.

The group sits at the top level (category `v3`) on purpose: every Style Dictionary platform in this repo filters on `color` / `font` / `spacing` / `radius` / `avatar` / `height`, so a `v3` token reaches **no** generated output — not `Sources/FitUI/Tokens/Generated/*.swift`, not `android/src/main/res/values/*.xml`. The CSS and flat-JSON platforms are unfiltered but write into `build/`, which is gitignored. In other words `npm run build` produces no committed diff from these tokens, which is what keeps the iOS side untouched while the JSON stays the single source.

`FitV3Geometry` carries the measurements the rework adds (panel radius 22, row radius 16, action circle 52, identity avatar 84 …). Values that already exist in `spacing.json` are referenced there rather than redeclared.

## Type scale

`FitV3Type` is the rework's own scale, built on `FitFont.family`. It exists because the rework names weights the v2 scale does not carry (row title 16/500, section title 15/600, field label 13/600, identity 28/700, money hero 48/700); reusing `FitFont` would have meant changing shared styles.

## API

```kotlin
FitV3Theme(look = FitV3Look.Tinted) {
    FitV3Canvas {
        // screen content
    }
}

@Composable
fun Something() {
    val palette = FitV3.palette      // or LocalFitV3Palette.current
}
```

`FitV3Canvas` paints the base gradient and the glow behind its content and takes the whole size. `Modifier.fitV3Canvas(spec)` is the same paint for a surface that cannot be a `Box` (a bar, a sheet body).

The glow is a radial gradient in Compose where the prototype uses a CSS ellipse (`120% 48% at 50% -8%`). The centre offset and the radius are expressed as fractions of the drawn size in `FitV3CanvasSpec`, so the fall-off matches at phone proportions; it is an approximation by construction.

## Where the previews live

The gallery is in the app (`321fit_android_new`, `ui/preview/v3/FitV3Gallery.kt`) — one `@Preview` per look over the whole set, rather than a tooling dependency in this module. Nothing in this repo carries previews today and the app is where they are reviewed.

## Component inventory

| Component | Replaces (v2) | Stage | Status |
|---|---|---|---|
| `FitV3Theme` · `FitV3Colors` · `FitV3Geometry` · `FitV3Canvas` | — | 0 | ✅ built |
| `FitPanel` · `FitRow` (+ `FitRowValue`, `FitRowChevron`, `FitRowPlate`) | `FitCard`, `FitSettingsCard`, `FitSelectRow` | 1 | ✅ built |
| `FitSectionTitle` (15/600 secondary, padding 20/20/8) | `FitSectionTitle` | 1 | ✅ built |
| `FitAddRow` (dashed panel, plate + title + sub) | — | 1 | ✅ built |
| `FitInput` (56 / radius 14 / label above / hint + counter) | `FitInput` | 1 | ✅ built |
| `FitIdentity` (centred, avatar 84, name 28/700) · `FitIdentityCompact` (22/700 + stats 15/600) | `FitProfileHero` | 2 | ✅ built |
| `FitProfileHeader` (cover → row → stats) · `FitProfileCover` · `FitProfileStats` · `FitProfileCard` | `FitProfileHeader` | 2 | ✅ built |
| `FitV3Avatar` (plate on canvas / on surface / brand) | `FitAvatar` | 2 | ✅ built |
| `FitActionCircle` + `FitActionCircleRow` (52, badge, filled = the expected answer) | `FitActionCircle` | 3 | ✅ built |
| `FitNeedsChip` + `FitNeedsRow` (red money · blue question · yellow review · grey waiting) | `FitChip` | 3 | ✅ built |
| `FitNextSessionCard` (planned / request / awaiting perimeters, no buttons) | — | 3 | ✅ built |
| `FitMoneyWidget` · `FitMoneyHero` | `FitEarningsHero`, `FitStatTile` | 3 | ✅ built |
| `FitSessionCard` (radius 18, type in the plate, location strip) | — (app code today) | 4 | ✅ built |
| `FitSportChip` (selection pill, icon slot) | `FitChip` | 4 | ✅ built |
| `FitPickRow` + `FitCheckCircle` (22, muted with the reason) | `FitCheckbox`, `FitSelectionGroup` | 4 | ✅ built |
| `FitSegmented` (selected = light alpha) | `FitSegmented` | 5 | ✅ built |
| `FitStepper` (40px circles on the field surface) | `FitStepper` | 5 | ✅ built |
| `FitSelectListRow` + `FitSelectCheck` (brand wash + teal hairline when selected) | — | 5 | ✅ built |
| `FitV3Snackbar` (action slot, `FIT_V3_SNACKBAR_UNDO_MS` = 5s) | `FitSnackbar` | 5 | ✅ built |
| `FitButton` (white-with-depth on tinted, brand gradient on light) + `FitV3ButtonStyle` | `FitButton` | 6 | ✅ built |
| `FitV3Screen` · `FitV3Header` · `FitV3HeaderCircle` · `FitV3Footer` · `FitV3Bar` | `FitScreen`, `FitHeader`, `FitFooter` | 6 | ✅ built |
| `FitDayRing` · `FitDayWidget` · `FitDayWidgetAnchor` · `FitDayBar` | — | 6 | ✅ built |
| `FitStatusBanner` (attention / error / neutral, optional action) | `StatusBanner`, `InfoBanner` | 6 | ✅ built |
| `FitEmptyPanel` (one card, one door — §4.14) | `FitEmptyState` | 6 | ✅ built |
| `FitTile` · `FitTileGrid` · `FitFaceStack` (the Activity grammar) | — | 6 | ✅ built |
| `FitTipCard` (outlined, dismissable) | `FitTipCard` | 6 | ✅ built |
| `FitAccentBadge` | `FitBadge` | 6 | ✅ built |
| `FitDetailHero` · `FitTxnGroup` · `FitTxnRow` · `FitTxnPlate` · `FitTxnDivider` + `FitV3Tone` | `LedgerRow` kit | 7 | ✅ built |
| `FitV3Sheet` · `FitSheetTitle` · `FitSheetActionRow` · `FitSheetCloseButton` | `FitSheet`, `FitSheetActionItem` | 7 | ✅ built |
| `FitClipStrip` · `FitClipCard` (poster slot, duration, no-clip face) | — | 8 | ✅ built |
| `FitThinProgress` · `FitStatusBadge` · `FitRowGo` · `FitPanel(attention)` | `FitProgressBar`, `FitBadge` | 8 | ✅ built |
| `FitInput(leading)` — the search field's glyph | `FitSearchField` | 8 | ✅ built |

### One tone set for rows and sheet actions

`FitV3Tone` is the single vocabulary for a tinted circle and the ink beside it — `Income` (teal), `Danger` (red), `Info` (blue), `Muted` (grey), `Neutral` (the raised plate). The ledger row, its amount, the detail hero's figure and the sheet's action icons all read from it, so a settle sheet cannot drift from the row that opened it. The prototype paints the sheet's destructive icon at red 14 % and the ledger's danger icon at 12 %; v3 keeps the single 12 % and treats the 2 % as a prototype slip, not a variant.

### A ledger row is not a `FitRow`

`FitRow` is the panel row — 16 px title, a 40 × 12 plate, rounded on its own. `FitTxnRow` is the money row from `.fit-txn`: a 36 px **circle** carrying a tone, a 15 px title, up to two sub-lines (the second is the age, in its own tone) and an amount that takes its colour from the same tone. They stack inside `FitTxnGroup` — one radius-14 surface, hairlines between the rows, the light look's card lift on the group rather than on each row.

### A queue of work has one attention panel, not many badges

The self-paced hub stacks three panels and only the first — *To set up*, the work the coach owes someone who already paid — wears `FitPanel(attention = true)`: the yellow tint plus a yellow-600 hairline. The other two are plain. A tint on every section would say everything is urgent, which is the same as saying nothing is; and a per-row badge inside a tinted panel double-counts the signal. The exception is a row whose own state contradicts its panel — an overdue workout inside the plain *Sent* panel — and that is what `FitStatusBadge` beside the name is for.

### The sheet carries the blur Android cannot draw

`material` is the sheet's colour and the one place the spec asks for blur. Compose blurs a composable's own content, never what sits behind it, so the Android sheet paints the material **opaque** where the token says 80 % + a 28 px blur: with nothing blurred behind it, a translucent sheet reads as see-through rather than as a layer above the screen, and even at 96 % the rows underneath ghost through. The token keeps the blur for the platforms that can draw it.

### The day widget has two shapes

`FitDayWidget` is the full form — ring, three bars, a sentence, a link — and it is deliberately generic: the coach's ring is *sessions today* with money either side, the athlete's is *sessions this week* with balance / self-paced / streak in the bars. `FitDayWidgetAnchor` is the same widget in Home's anchor slot, where the prototype hides the bars, the sentence and the link: the anchor is ring + earned / planned only, and the money widget stays below it as its own block (decided in the prototype's annotation, 2026-09-24). A day with nothing in it drops the anchor entirely rather than drawing a ring of 0/0 — that call belongs to the screen, not the component.

### Status colour splits by look too

`textError`, the next-session perimeters and the attention tint are palette entries, not `FitColors` constants, for the same reason the canon theme splits `text.error`: red-400 reads on a dark canvas and shouts on white (light takes red-700), and a yellow-400 perimeter vanishes on `#F2F2F7` (light takes yellow-600). The one deliberate constant is the review badge's ink — a yellow badge carries a dark number in both looks, because white on yellow is the unreadable pair.

### Only tinted drops the selection gradient

`FitSegmented` marks the selected tab with the brand selection wash in both looks — **except** on tinted, where that wash is teal over a teal canvas and does not separate, so it becomes white at 16% instead. Carrying the tinted recipe into light is what produced a white tab on a white track. The label follows the fill: white on tinted, `text.on-brand` (blue-700) on light.

### Chrome is not a bar

A screen's own header and footer are **transparent** on the tinted canvas — the gradient runs under them (`.fit-phone.fit-dark.k-alpha > .fit-phone-header/.fit-phone-footer`), and that is the `chrome` token (light keeps the canvas colour, which is opaque there). `bar` at 88% is for something else: a bar that floats **over scrolling content**. Painting the footer with `bar` on tinted puts a dark slab across the gradient.

### The snackbar does not follow the canvas

It floats over content instead of replacing a surface, so it stays dark in **both** looks: near-black on light (the canon snackbar, unchanged), the bar colour at 88% on tinted. Its text is white and its action teal-400 in both — reading them from the palette is what made the light one vanish into `#F2F2F7`. `material` is the sheet's colour and is not interchangeable with it.

### Two marks, not one

A picker and a select list do not share a tick, and swapping them is easy to do by accident:

- **`FitCheckCircle`** (`FitPickRow`) — a 22 circle, teal-**500** fill, and a **dark** tick `#06251f`. The dark tick is the same in both looks on purpose: teal-500 is a bright mint and white on it is the weaker pair.
- **`FitSelectCheck`** (`FitSelectListRow`) — a 22 rounded square (radius 6), teal-**600** fill, **white** tick, and it exists only while the row is selected.

### A selection tint is not a surface

`fitV3Surface(brush = …)` paints a translucent tint instead of the surface fill, and when it does it skips the light look's card lift: a shadow under a 10% wash turns the row into floating glass. The selected select-list row is the case — tinted fills it with teal at 14% and a teal hairline, light with the brand wash at 10% (blue → teal, left to right). `bg.brand-subtle` was the wrong token for it: teal-600 at 16% on a white row reads as a solid mint block, not as a selection.

### Icon slots carry no colour

`FitActionCircle` and `FitRowPlate` take the glyph as a slot and provide `LocalContentColor` around it, so a Material `Icon` with its default tint comes out right in both looks — white-on-teal inside a filled circle, `#12161a` inside the white one. A call site that hardcodes a tint defeats it; pass the painter and let the component colour it.

### What v3 reuses from v2

Three things, each deliberate:

- `FitColors.Gray.*` / `Teal` / `Blue` — the palette the light look is built from.
- `FitElevation.fitCardElevation` — the card lift, so the light surface has one shadow in the app, not two.
- `FitAvatar(size: Dp, bg, textColor)` — the photo-or-initials logic, including the guard that keeps initials from showing through a 1×1 alpha stub. `FitV3Avatar` passes v3 colours into it, so none of v2's theming leaks in; rebuilding it would have meant re-deriving that fix.

### Open points carried from the spec

- **Sport chip icons.** The 33-sport set lives in the app (`ic_sport_v2_*.xml` + `SportV2Icons.kt`), not in this repo. The chip takes the icon as a slot rather than owning it — copying 35 vectors across repos guarantees two sets that drift. Revisit if the icons ever move here.
- **Canvas hue per role** (teal for both, or indigo for the athlete) is still open in the spec; the tinted palette is written as one hue and the stacked-face fill is the only value that would have to follow it.
