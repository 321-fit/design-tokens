# Design Tokens

## Purpose
Single source of truth for 321Fit design system. Tokens are extracted from Figma via MCP and compiled to platform-specific code using Style Dictionary.

## GitHub
- **Account:** `whywolfy`
- **Org:** `321-fit`

## Pipeline
```
Figma (UI Kit) → MCP get_variable_defs → tokens/*.json → Style Dictionary → build/
```

## Structure
```
tokens/
├── color-palette.json    # Raw color scales (neutral, blue, teal, red, yellow, green)
├── color-semantic.json   # Semantic tokens (screen, surface, text, border, brand) with dark/light
├── typography.json       # Font family, text styles (H1, H2, Button, Body, Caption)
└── spacing.json          # Spacing scale, border radius, effects

build/                    # Generated (gitignored)
├── ios/                  # Swift extensions
├── android/              # Kotlin objects
├── css/                  # CSS custom properties
└── tokens.json           # Flat JSON for any consumer
```

## Commands
```bash
npm install          # Install style-dictionary
npm run build        # Generate all platform outputs
```

## Updating tokens
1. Open Figma UI Kit
2. Use MCP `get_variable_defs` to extract current variables
3. Update `tokens/*.json` files
4. Run `npm run build`
5. Copy generated files to respective repos
