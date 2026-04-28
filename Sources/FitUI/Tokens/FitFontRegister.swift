import CoreText
import SwiftUI

// MARK: - Rubik font registration
//
// Bundles the four Rubik TTF cuts (Regular / Medium / SemiBold / Bold) inside
// the FitUI Swift package so consumer apps don't have to add the files to
// `Info.plist > UIAppFonts` themselves. Auto-runs on first use of `FitTheme`.
//
// `Font.custom("Rubik-Medium", size: …)` resolves to the bundled cut once
// CoreText has registered the URL via `CTFontManagerRegisterFontsForURL`.

public enum FitFontRegister {
    /// `let`-init guarantees the side effect runs exactly once across all
    /// callers, threads, and theme switches.
    private static let didRegister: Bool = {
        registerAll()
        return true
    }()

    /// Call this once before constructing custom fonts. Idempotent.
    public static func ensure() {
        _ = didRegister
    }

    private static func registerAll() {
        let names = ["Rubik-Regular", "Rubik-Medium", "Rubik-SemiBold", "Rubik-Bold"]
        for name in names {
            guard let url = Bundle.module.url(forResource: name, withExtension: "ttf") else {
                continue
            }
            // .process scope keeps the registration confined to the current
            // process — no system-wide pollution. Errors are intentionally
            // swallowed: a duplicate registration (host app already shipped
            // the same TTF) is harmless and not actionable.
            CTFontManagerRegisterFontsForURL(url as CFURL, .process, nil)
        }
    }
}
