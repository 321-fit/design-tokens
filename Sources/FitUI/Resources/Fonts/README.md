# Rubik fonts

The four static TTF cuts shipped here are the latin-only normal-italic-style
weights used by the 321Fit design system:

| File | Weight | PostScript name |
|---|---|---|
| Rubik-Regular.ttf | 400 | Rubik-Regular |
| Rubik-Medium.ttf | 500 | Rubik-Medium |
| Rubik-SemiBold.ttf | 600 | Rubik-SemiBold |
| Rubik-Bold.ttf | 700 | Rubik-Bold |

## Source

Fetched from Fontsource's jsDelivr mirror, which republishes the upstream
Google Fonts artifacts:

```
https://cdn.jsdelivr.net/fontsource/fonts/rubik@latest/latin-400-normal.ttf
https://cdn.jsdelivr.net/fontsource/fonts/rubik@latest/latin-500-normal.ttf
https://cdn.jsdelivr.net/fontsource/fonts/rubik@latest/latin-600-normal.ttf
https://cdn.jsdelivr.net/fontsource/fonts/rubik@latest/latin-700-normal.ttf
```

Upstream repo: https://github.com/googlefonts/rubik

## License

Rubik is distributed under the **SIL Open Font License 1.1** (OFL-1.1).
Full license text: https://github.com/googlefonts/rubik/blob/main/OFL.txt

You may bundle these files in commercial and non-commercial products with
no fee or attribution requirement, provided the OFL terms are honored
(no resale of the font itself as a standalone product, etc.).

## Loading

The fonts are auto-registered on first use of `FitTheme(...)` via
`FitFontRegister.ensure()` (see `Sources/FitUI/Tokens/FitFontRegister.swift`).
After registration, `Font.custom("Rubik-Medium", size: …)` etc. resolve
correctly without any host-app `Info.plist` changes.
