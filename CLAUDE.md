# Design Tokens

## Purpose
Single source of truth for 321Fit design system. Tokens are maintained in JSON
and compiled to platform-specific code via Style Dictionary. Native component
libraries (SwiftUI + Compose) live alongside the tokens in this same repo.

## GitHub
- **Account:** `whywolfy`
- **Org:** `321-fit`
- **Repo:** https://github.com/321-fit/design-tokens

## Structure

```
design-tokens/
├── Package.swift                 ← iOS SPM entry (root)
├── Sources/FitUI/                ← SwiftUI library
│   ├── Components/               (28 SwiftUI components)
│   ├── Modifiers/                (FitTheme environment)
│   └── Tokens/
│       ├── FitColors.swift       (hand-written SwiftUI wrappers)
│       ├── FitSpacing.swift      (spacing + radius + size)
│       ├── FitTypography.swift   (font styles)
│       └── Generated/            (Style Dictionary output — 6 files)
├── android/                      ← Gradle library module
│   ├── build.gradle.kts
│   ├── consumer-rules.pro
│   └── src/main/
│       ├── AndroidManifest.xml
│       ├── kotlin/com/fit321/fitui/
│       │   ├── components/       (8 files × multiple components each)
│       │   ├── theme/FitTheme.kt
│       │   └── tokens/           (FitColors, FitSpacing, FitTypography)
│       └── res/values/           (Style Dictionary XML output)
├── tokens/                       ← source of truth (JSON)
│   ├── color-palette.json
│   ├── color-semantic.json
│   ├── gradient.json
│   ├── typography.json
│   ├── spacing.json              (includes radius + avatar + height)
│   ├── animation.json
│   ├── elevation.json
│   └── components.json
├── build/                        ← CSS output for prototype (gitignored)
│   ├── css/tokens.css
│   └── tokens.json               (flat, for any consumer)
├── docs/
│   └── components.md             ← canonical component spec
├── package.json
└── style-dictionary.config.js
```

## Pipeline

```
prototype/*.css + memory rules  (canonical source of UX decisions)
      ↓
tokens/*.json                   (hand-edited)
      ↓ npm run build
      ├── Sources/FitUI/Tokens/Generated/*.swift (committed)
      ├── android/src/main/res/values/*.xml       (committed)
      └── build/css/tokens.css                    (copied into prototype)
```

## Consumption

### iOS (SwiftUI)
```swift
// Package.swift of consumer app:
dependencies: [
    .package(url: "https://github.com/321-fit/design-tokens.git",
             from: "1.0.0")
]

// Usage:
import FitUI

FitTheme(.dark) {
    FitButton("Continue", style: .primary) { }
}
```

### Android (Compose)
```kotlin
// settings.gradle.kts:
includeBuild("../design-tokens") {
    dependencySubstitution {
        substitute(module("com.fit321:fitui"))
            .using(project(":android"))
    }
}

// build.gradle.kts (app):
dependencies {
    implementation("com.fit321:fitui")
}

// Usage:
import com.fit321.fitui.components.*
import com.fit321.fitui.theme.FitTheme

FitTheme(isDark = true) {
    FitButton("Continue", style = FitButtonStyle.Primary) { }
}
```

### Web (prototype)
```bash
# In project-spec repo, prototypes/lib/ copies from build/css/tokens.css
cp ../design-tokens/build/css/tokens.css prototypes/lib/fit-ui-tokens.css
# fit-ui.css @imports it at the top
```

## Updating tokens

1. Edit `tokens/*.json` files
2. Run `npm run build`
3. Commit:
   - `tokens/*.json`
   - `Sources/FitUI/Tokens/Generated/*.swift`
   - `android/src/main/res/values/*.xml`
4. If changing token names/structure, update components that reference them
5. Update memory `feedback_spacing_typography` or `feedback_*` if new rule

## Commands
```bash
npm install        # Install style-dictionary (run once)
npm run build      # Generate Swift + Kotlin XML + CSS outputs
swift build        # (requires iOS SDK) verify Swift package compiles
# ./gradlew :android:build   # (when Gradle wrapper added) verify Android
```

## Components

See `docs/components.md` for the canonical component spec — every component
has purpose, props, variants, states, sub-elements, and iOS/Android notes
listed there. When adding a new component:

1. Spec it in `docs/components.md` first
2. Build SwiftUI version in `Sources/FitUI/Components/<Name>.swift`
3. Build Compose mirror in `android/src/main/kotlin/.../components/<Category>.kt`
4. Reference tokens (never hardcode px, colors)
5. Test in a simple preview / prototype screen

## Architecture principles

- **Prototype is canonical** — `feedback_pipeline_principle` memory. Figma is
  out of the pipeline (exploration reference only).
- **Components reference tokens, never hardcode.** If a value needs to change,
  it goes in JSON → regenerates everywhere.
- **API parity iOS ↔ Android.** Same enum names, same prop names, same
  variants. Swift has slight CamelCase differences but semantic match 1:1.
- **Theme via CompositionLocal / Environment.** Never pass theme as prop.
- **Theme-aware tokens must split.** A token with both `dark` and `light` keys
  in `tokens/color-semantic.json` MUST have physically different resolved values.
  If the value is identical across themes (brand mark, accent), it belongs as
  a single `value` field — not as a `dark`/`light` pair with the same RGB.
  Examples of the correct shape: `brand.primary` (single value), `border.selection`
  (single value), `text.primary` (split — dark white / light gray.900).
  - **Why:** identical-but-split tokens ("dynamic but actually static") are silent
    landmines. They look theme-aware to readers but never switch — bugs surface
    only via visual review of light mode.
  - **Aliases are allowed via Style Dictionary refs** when a name is kept for
    historical reasons (e.g. `surface.high → surface.default`). The ref makes
    intent explicit and grep-able.
- **Opacity-based tints belong in tokens, not at the call site.** Anywhere a
  component would otherwise write `.opacity(0.12)`, `rgba(.., 0.18)`, or
  `Color(...).copy(alpha = 0.2f)` over a base color, add (or reuse) a
  `bg.<status>-subtle` / `bg.<status>-tinted` token in `color-semantic.json`
  with **separate opacities for light and dark** (alpha that reads on a tinted
  surface differs from alpha that reads on white).
  - **Why:** `0.18` of red on white ≠ `0.18` of red on gray.800 — opacity
    constants are theme-dependent; baking them into call sites guarantees one
    of the two themes will look wrong.
- **Exceptions allowed with documentation.** See `feedback_spacing_typography`
  meta-rule.

## Related repos

- `../321fit_ios/` — iOS app (consumes FitUI via SPM)
- `../321fit_android/` — Android app (future — consumes FitUI via Gradle)
- `../project-spec/` — prototype + specs + flows (consumes tokens.css)

## GitHub Projects
- Project #2 Task Board: https://github.com/orgs/321-fit/projects/2
- Project #3 Roadmap: https://github.com/orgs/321-fit/projects/3
